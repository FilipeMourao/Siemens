//
//  ViewController.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 19.11.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import UIKit
import WebKit
import EventKit
import UserNotifications
import CallKit
import PushKit
import ContactsUI
import CoreData
struct RCNotifications {
    static let SearchingDevice = Notification.Name("org.siemens.blingmebluetooth.searchingDevice")
    static let DeviceNotFound = Notification.Name("org.siemens.blingmebluetooth.deviceNotFound")
    static let DeviceFound = Notification.Name("org.siemens.blingmebluetooth.deviceFound")
    static let LostConnection = Notification.Name("org.siemens.blingmebluetooth.LostConnection")
    static let connected = Notification.Name("org.siemens.blingmebluetooth.connected")
}
//struct BackEndFrontEndEndpoint
struct BackEndFrontEndEndpoint {// unique names for the connections between frontend and backend
    static let connectTheDevice = "org.siemens.blingmebluetooth.connectTheDevice"
    static let showUserEvents = "org.siemens.blingmebluetooth.showUserEvent"
    static let createNotifications = "org.siemens.blingmebluetooth.createNotifications"
    static let showUserContacts = "org.siemens.blingmebluetooth.showUserContacts"
    static let saveContacts = "org.siemens.blingmebluetooth.saveContacts"
    static let saveConfiguration = "org.siemens.blingmebluetooth.saveConfiguration"
}

class ViewController: UIViewController,WKScriptMessageHandler,UITableViewDelegate {
    public var ipAdress = "";
    var webView: WKWebView?;
    var events:[Event] = [];
    var defaults = UserDefaults.standard;
    let eventStore = EKEventStore();
    let callObserver = CXCallObserver();
    var contactList:[Contact] = [];
    var phoneContacts:[ContactEntity] = [];
    var provider = CXProvider(configuration: CXProviderConfiguration(localizedName: "My App"));
    var notificationCounter = 0;
   
    let persistenceManager:PersistenceManager = PersistenceManager();
       override func viewDidLoad() {
        super.viewDidLoad()
            let configuration = WKWebViewConfiguration()
            let controller = WKUserContentController()
            controller.add(self, name: "JSInterface")
            configuration.userContentController = controller
            webView = WKWebView(frame: self.view.frame, configuration: configuration)
            let url = Bundle.main.url(forResource: "newIndex", withExtension: "html")
            let request = URLRequest(url: url!)
            self.view = webView
            webView!.load(request)
        // this notifications are sended in the bluetooth class and handle in the view controller
        NotificationCenter.default.addObserver(self, selector: #selector(showSearchingDeviceMessage(_:)),
                                               name: RCNotifications.SearchingDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(showFoundDeviceMessage(_:)),
                                               name: RCNotifications.DeviceFound, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(showNotFoundDeviceMessage(_:)),
                                               name: RCNotifications.DeviceNotFound, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(showLostConnectionMessage(_:)),
                                               name: RCNotifications.LostConnection, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(deviceConnected(_:)),
                                               name: RCNotifications.connected, object: nil)
//        test user calling
//        callObserver.setDelegate(self, queue: nil) // nil queue means main thread
//        provider.setDelegate(self, queue: nil)
//        let update = CXCallUpdate()
//        update.remoteHandle = CXHandle(type: .generic, value: "Pete Za")
//        provider.reportNewIncomingCall(with: UUID(), update: update, completion: { error in })
//
        
    }
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        // handling the different calls of the front end, depending of the content received a different function is called
        print("Message from beyond: \(message.body)")
        let endPoint = "\(message.body)"
        if (endPoint == BackEndFrontEndEndpoint.connectTheDevice) {createPopUp()}
        if (endPoint == BackEndFrontEndEndpoint.showUserEvents) {showUserEvents()}
        if (endPoint == BackEndFrontEndEndpoint.createNotifications) {createNotifications()}
        if (endPoint == BackEndFrontEndEndpoint.showUserContacts) {showUserContacts()}
        if (endPoint.range(of: BackEndFrontEndEndpoint.saveConfiguration) != nil) {saveConfigurations(endPoint: endPoint);}
        if (endPoint.range(of: BackEndFrontEndEndpoint.saveContacts) != nil) {saveContacts(endPoint: endPoint);}
    }
    func showUserContacts(){
        // this function gets all the contatcs from the user phone and present in the contact screen,
        //the contact screen is commented in the ios application since it is not possible to retrieve the incoming number of a call
        getDatabaseContacts();
        var partialContactList:[Contact] = [];
        let contactStore = CNContactStore()
        let keysToFetch = [
            CNContactFormatter.descriptorForRequiredKeys(for: .fullName),
            CNContactPhoneNumbersKey] as [Any]
        
        var allContainers: [CNContainer] = []
        do {
            allContainers = try contactStore.containers(matching: nil)
        } catch {
            print( "Error fetching containers")
        }
        
        var results: [CNContact] = []
        
        for container in allContainers {
            let fetchPredicate = CNContact.predicateForContactsInContainer(withIdentifier: container.identifier)
            
            do {
                let containerResults = try contactStore.unifiedContacts(matching: fetchPredicate, keysToFetch: keysToFetch as! [CNKeyDescriptor])
                results.append(contentsOf: containerResults)
            } catch {
                print( "Error fetching containers")
            }
            var contact:Contact;
            for cnContact in results {
                contact = Contact(name: cnContact.givenName, number: (cnContact.phoneNumbers[0].value ).value(forKey: "digits") as! String );
                partialContactList.append(contact);
            }
            
        }
        var contact1:Contact;
        for contact in partialContactList {
            if(!self.contactList.filter({$0.number == contact.number}).isEmpty){
                // if the contact list does not have the currently analyzed contact add it in the list
                contact1 = self.contactList.filter({$0.number == contact.number})[0];
                contact.colorBrihgtness = contact1.colorBrihgtness;
                contact.color = contact1.color;
            }
        }
        self.contactList =  partialContactList.sorted(by: { $0.name < $1.name });
        let jsonEncoder  = JSONEncoder();
        let jsonData = try! jsonEncoder.encode(self.contactList);
        let json =  String(data: jsonData, encoding: .utf8)!;
        let javaScriptString = "app.ListContacts('\(json)');"
        // send the list in an acceptable format to the front end
        self.webView?.evaluateJavaScript(javaScriptString, completionHandler: nil);
        
    }
    func saveContacts(endPoint:String) {
        let contactString = endPoint.replacingOccurrences(of: "saveContacts()", with: "");
        let jsonData2: Data = contactString.data(using: String.Encoding.utf8)!
        let jsonDecoder = JSONDecoder();
        contactList = try! jsonDecoder.decode([Contact].self, from: jsonData2);
        for contact in contactList {
            if(!phoneContacts.filter({$0.number == contact.number}).isEmpty){
                 // if the contact list does not have the currently analyzed contact add it in the list
                let phoneContact = phoneContacts.filter({$0.number == contact.number}).first!
                phoneContact.name = contact.name
                phoneContact.color = contact.color
                phoneContact.colorBrightness = Int16(contact.colorBrihgtness)
                
            } else {
                 // save the contacts in the CoreData database
                let phoneContact = ContactEntity(context: persistenceManager.context)
                phoneContact.name = contact.name
                phoneContact.number = contact.number
                phoneContact.color = contact.color
                phoneContact.colorBrightness = Int16(contact.colorBrihgtness)
            }
            persistenceManager.save();
        }
    }
    func getDatabaseContacts()  {
        // get the contacts from the database
        phoneContacts  = persistenceManager.fetch(ContactEntity.self)
        self.contactList = [];
        for phoneContact in phoneContacts {
            contactList.append(Contact(name: phoneContact.name,
                                       number: phoneContact.number,
                                       color: phoneContact.color,
                                       colorBrihgtness: Int(phoneContact.colorBrightness)))
        }
        
    }

    func showUserEvents(){
        // send the current events in the calendar to the front end
        self.events = getUserEvents();
        let jsonEncoder  = JSONEncoder();
        let jsonData = try! jsonEncoder.encode(self.events);
        let json =  String(data: jsonData, encoding: .utf8)!;
        let javaScriptString = "app.ListEvents('\(json)');"
        self.webView?.evaluateJavaScript(javaScriptString, completionHandler: nil);
        
    }
    func getUserEvents() -> [Event] {
        // get the events with the specific location format
        var events:[Event] = [];
        let ekEvents = get();
        var location:String;
        var color:String;
        for ekEvent in ekEvents{
            location = ekEvent.location!;
            for i in 0...location.count - 1{
                if(Array(location)[i] == "#" ){
                    if(Array(location)[i + 1] == "#" ){
                        color = String(Array(location)[i+1...i+7]);
                        if (ColorCustomized.checkIfColorIsValid(hexColor: color)) {
                            events.append(Event(title: ekEvent.title,
                                                location: ekEvent.location!,
                                                color: color,
                                                calendar: ekEvent.startDate))
                            break;
                        } else {
                            print("Error Converting color: \(color)" );
                            break;
                        }
                    }
                    
                }
            }
            
        }
        return events;
    }
    private func get()->[EKEvent] {
        // get all events in the EK from of the phone
        let calendars = eventStore.calendars(for: .event)
        var eventsTotal:[EKEvent]  = []
        for calendar in calendars {
            // This checking will remove Birthdays and Hollidays callendars
            guard calendar.allowsContentModifications else {
                continue
            }
            let cal = Calendar.current
            let start = Date();
            let end = createDate(year: cal.component(.year, from: Date()) + 4)// limit of it is 4 years
            
            //print("start: \(start)")
            //print("  end: \(end)")
            
            let predicate = eventStore.predicateForEvents(withStart: start, end: end, calendars: [calendar])
            
            print("predicate: \(predicate)")
            
            let events = eventStore.events(matching: predicate)
            for event in events {
              eventsTotal.append(event);
            }
        }
        return eventsTotal;
    }
    private func createNotifications(){
        // here the phone notifications are created
        for event in self.events {
            let configureLed = ConfigureLed(ipAdress: ipAdress,colorSetting: ColorSetting(color: ColorCustomized(hexColor:event.color )));
            var time = event.calendar.timeIntervalSinceNow;
            let content = UNMutableNotificationContent()
            content.title = "Reminder!";
            content.subtitle =  event.title + " is starting..."
            content.badge = 1
            ///Code for prototyping showing
            time = 8;
            DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(NSInteger(time))) {
                configureLed.colorSetting.brightness = 0;
                configureLed.configureColors();
                print("second time: \( time)");
            }
            let trigger = UNTimeIntervalNotificationTrigger(timeInterval: time, repeats: false)
            let request = UNNotificationRequest(identifier: "meeting reminder \(self.notificationCounter)", content: content, trigger: trigger)
            self.notificationCounter = self.notificationCounter + 1;
            UNUserNotificationCenter.current().add(request, withCompletionHandler: nil)
            
            time = 3;
            let content2 = UNMutableNotificationContent()
            content2.title = "Reminder!";
            content2.subtitle =  event.title + " will start in 2 minutes";
            content2.badge = 2
            DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(NSInteger(time))) {
                configureLed.colorSetting.brightness = 75;
                print("second time: \( time)");
                configureLed.configureColors();
            }
            let trigger2 = UNTimeIntervalNotificationTrigger(timeInterval: time, repeats: false)
            let request2 = UNNotificationRequest(identifier: "meeting reminder \(self.notificationCounter)", content: content2, trigger: trigger2);
            self.notificationCounter = self.notificationCounter + 1;
            UNUserNotificationCenter.current().add(request2, withCompletionHandler: nil)
            
            
            ///this is the code to the use of the app
            //            configureLed.colorSetting.brightness = 0;
            //            DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(NSInteger(time))) {
            //                configureLed.configureColors();
            //            }
            //            let trigger = UNTimeIntervalNotificationTrigger(timeInterval: time, repeats: false)
            //            let request = UNNotificationRequest(identifier: "meeting reminder 1", content: content, trigger: trigger)
            //
            //            UNUserNotificationCenter.current().add(request, withCompletionHandler: nil)
            //            if(time - 2*60 > 0){
            //                time = time - 2*60;
            //                let content2 = UNMutableNotificationContent()
            //                content2.title = "Reminder!";
            //                content2.subtitle =  event.title + " will start in 2 minutes";
            //                content2.badge = 1
            //                configureLed.colorSetting.brightness = 75;
            //                DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(NSInteger(time))) {
            //                    configureLed.configureColors();
            //                }
            //                let trigger2 = UNTimeIntervalNotificationTrigger(timeInterval: time, repeats: false)
            //                let request2 = UNNotificationRequest(identifier: "meeting reminder 2", content: content2, trigger: trigger2)
            //                UNUserNotificationCenter.current().add(request2, withCompletionHandler: nil)
            //            }
        }
    }

    private func createDate(year: Int) -> Date {
        // this method is used to create the date used to get all the events inside 2 dates interval in the get() function
        var components = DateComponents()
        components.year = year
        components.timeZone = TimeZone(secondsFromGMT: 0)
        
        return Calendar.current.date(from: components)!
    }

    func createPopUp(){
        // create a popup to let the person fill in his card number
        
        let alertController = UIAlertController(title: "Add your card number!",
                                                message: nil,
                                                preferredStyle: .alert)
        alertController.addTextField(configurationHandler: takeCardNumber)
          let okAction = UIAlertAction (title: "OK", style: .default, handler: {[weak alertController](_) in
            let textField = alertController?.textFields![0];
            if(textField?.text?.count == 3) {
                self.ipAdress = "192.168.1." + (textField?.text!)!;
                NotificationCenter.default.post(name: RCNotifications.SearchingDevice, object: nil)
                let configureLed = ConfigureLed(ipAdress: self.ipAdress,
                                                colorSetting: ColorSetting(
                                                    color: ColorCustomized(hexColor:"#000000"
                                                )));
                configureLed.colorSetting.brightness = -50;
                configureLed.configureColors();
                
            }
          })
        let cancelAction = UIAlertAction (title: "Cancel", style: .cancel, handler: nil)
        alertController.addAction(okAction)
        alertController.addAction(cancelAction)
        self.present(alertController,animated: true)
    }
        func takeCardNumber(textField: UITextField) {
        textField.keyboardType = .numberPad;
    }
    
    func saveConfigurations(endPoint:String) {
        let (appNames,colorStrings) = getNotificationsNamesAndColors(functionString: endPoint);
        // save the configurations names and colors in the database
        for i in 0...appNames.count - 1 {
            UserDefaults.standard.set( colorStrings[i], forKey: appNames[i].lowercased());
        }
    }
    func getNotificationsNamesAndColors(functionString:String) -> ([String], [String]) {
        // get the notifications colors and name from the database
        var notificantionsNames = "";
       var notificantionsColors = "";
       var addChars = 0;
        for char in functionString {
            if char == "[" || char == "]" {addChars = addChars + 1 ;}
            if addChars == 1 {
                notificantionsNames = notificantionsNames  + String(char);
            }
            if addChars == 3 {
                notificantionsColors = notificantionsColors  + String(char);
            }
            
        }
        notificantionsNames = notificantionsNames  + "]";
        notificantionsColors = notificantionsColors  + "]";
        return(convertStringToStringArray(string: notificantionsNames),
        convertStringToStringArray(string: notificantionsColors));
    }
    func convertStringToStringArray(string:String) -> [String] {
        let separators = CharacterSet(charactersIn: "[,]");
        var stringArray:[String] = string.components(separatedBy: separators);
        return Array(stringArray[1...stringArray.count - 2]);

    }
    func jsonEncoding(object:[Event]) -> String {
        do {
            let jsonEncoder  = JSONEncoder();
            let jsonData = try jsonEncoder.encode(object);
            let json =  String(data: jsonData, encoding: .utf8)!;
            return json;
        } catch {
            return "Error";
        }
    }
    func jsonDencoding(json:String) -> ColorCustomized {
        let jsonData2: Data = json.data(using: String.Encoding.utf8)!
        let jsonDecoder = JSONDecoder();
        let test2 = try! jsonDecoder.decode(ColorCustomized.self, from: jsonData2);
        return test2;
    }
     public func callJSFunction(javascriptString:String){
        DispatchQueue.main.async {
            self.webView?.evaluateJavaScript(javascriptString, completionHandler: nil);
        }
    }
    // functions for the notifications alert
    @objc func showSearchingDeviceMessage(_ notification:Notification){
        let message = "Searching for device..."
        let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        self.present(alert, animated: true)
        
        // duration in seconds
        let duration: Double = 1
        
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.8*duration) {
            alert.dismiss(animated: true)
        }
        
    }
    @objc func showFoundDeviceMessage(_ notification:Notification){
        let message = "Device was found!"
        let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        // duration in seconds
        let duration: Double = 1
        
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.8*duration) {
            alert.dismiss(animated: true)
        }
    }
    @objc func showNotFoundDeviceMessage(_ notification:Notification){
        let message = "Device was not found!"
        let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        // duration in seconds
        let duration: Double = 1
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + duration) {
            self.present(alert, animated: true)
        }
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 2.0*duration) {
            alert.dismiss(animated: true)
        }
        callJSFunction(javascriptString: "app.bluetoothBageLostConnection();")
    }
    @objc func showLostConnectionMessage(_ notification:Notification){
        let message = "Lost connection with device..."
        let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        // duration in seconds
        let duration: Double = 1
        self.present(alert, animated: true)
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 1.5*duration) {
            alert.dismiss(animated: true)
        }
        callJSFunction(javascriptString: "app.bluetoothBageLostConnection();")
        
    }
    @objc func deviceConnected(_ notification:Notification){
        let javascriptString = "app.connectDevice();"
        callJSFunction(javascriptString: javascriptString)
    }

}


