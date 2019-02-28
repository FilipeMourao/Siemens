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
    static let ReadyToConnect = Notification.Name("org.siemens.blingmebluetooth.readyToConnect")
        static let connected = Notification.Name("org.siemens.blingmebluetooth.connected")
}

class ViewController: UIViewController,WKScriptMessageHandler,UITableViewDelegate {
    //public var ipAdress = "";
    public var bluetoohDevice:BluetoothDevice!;
    var webView: WKWebView?;
    var events:[Event] = [];
    var defaults = UserDefaults.standard;
    let eventStore = EKEventStore();
    let callObserver = CXCallObserver();
    var contactList:[Contact] = [];
    var phoneContacts:[ContactEntity] = [];
    var provider = CXProvider(configuration: CXProviderConfiguration(localizedName: "My App"));
   
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
         bluetoohDevice = BluetoothDevice();
        NotificationCenter.default.addObserver(self, selector: #selector(showSearchingDeviceMessage(_:)),
                                               name: RCNotifications.SearchingDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(showFoundDeviceMessage(_:)),
                                               name: RCNotifications.SearchingDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(showNotFoundDeviceMessage(_:)),
                                               name: RCNotifications.SearchingDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(showReadyToConnectMessage(_:)),
                                               name: RCNotifications.SearchingDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(deviceConnected(_:)),
                                               name: RCNotifications.connected, object: nil)
    }
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        print("Message from beyond: \(message.body)")
        if ("\(message.body)" == "connectDevice()") {bluetoohDevice.startUpCentralManager()}
        if ("\(message.body)" == "showEvents()") {showUserEvents()}
        if ("\(message.body)" == "createAlarms()") {createNotifications()}
        if (("\(message.body)").range(of: "saveConfiguration") != nil) {
            let (appNames,colorStrings) = getNotificationsNamesAndColors(functionString:"\(message.body)");
            saveConfigurations(appNames: appNames, colorStrings: colorStrings);
         }
        if ("\(message.body)" == "showContacts()") {showUserContacts()}
        if (("\(message.body)").range(of: "saveContacts()") != nil) {
            var contactsCompleted = "\(message.body)";
            let test = contactsCompleted.replacingOccurrences(of: "saveContacts()", with: "");
           // print(test);
            let jsonData2: Data = test.data(using: String.Encoding.utf8)!
            let jsonDecoder = JSONDecoder();
            contactList = try! jsonDecoder.decode([Contact].self, from: jsonData2);
            saveContacts(contacts: contactList);
        }
    }
    func showUserContacts(){
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
            print( "Error fetching containers") // you can use print()
        }
        
        var results: [CNContact] = []
        
        for container in allContainers {
            let fetchPredicate = CNContact.predicateForContactsInContainer(withIdentifier: container.identifier)
            
            do {
                let containerResults = try contactStore.unifiedContacts(matching: fetchPredicate, keysToFetch: keysToFetch as! [CNKeyDescriptor])
                results.append(contentsOf: containerResults)
            } catch {
                print( "Error fetching containers") // you can use print()            }
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
        self.webView?.evaluateJavaScript(javaScriptString, completionHandler: nil);
        
    }
    func saveContacts(contacts:[Contact]) {
        for contact in contacts {
            if(!phoneContacts.filter({$0.number == contact.number}).isEmpty){
                let phoneContact = phoneContacts.filter({$0.number == contact.number}).first!
                phoneContact.name = contact.name
                phoneContact.color = contact.color
                phoneContact.colorBrightness = Int16(contact.colorBrihgtness)
                
            } else {
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
        phoneContacts  = persistenceManager.fetch(ContactEntity.self)
//       guard let phoneContactsList = try! persistenceManager.context.fetch(PhoneContact.fetchRequest()) as? [PhoneContact] else { return }
       // phoneContacts = phoneContactsList
        self.contactList = [];
        for phoneContact in phoneContacts {
            contactList.append(Contact(name: phoneContact.name,
                                       number: phoneContact.number,
                                       color: phoneContact.color,
                                       colorBrihgtness: Int(phoneContact.colorBrightness)))
        }
        
    }

    func showUserEvents(){
        self.events = getUserEvents();
        let jsonEncoder  = JSONEncoder();
        let jsonData = try! jsonEncoder.encode(self.events);
        let json =  String(data: jsonData, encoding: .utf8)!;
        let javaScriptString = "app.ListEvents('\(json)');"
        //let javaScriptString = "app.ListEvents('\(jsonEncoding(object: events))');"
        self.webView?.evaluateJavaScript(javaScriptString, completionHandler: nil);
        
    }
    func getUserEvents() -> [Event] {
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
            //return events;
            for event in events {
              eventsTotal.append(event);
            }
        }
        return eventsTotal;
    }
    private func createNotifications(){
        for event in self.events {
            let configureLed = ConfigureLed(bluetoothDevice: bluetoohDevice,colorSetting: ColorSetting(color: ColorCustomized(hexColor:event.color )));
            var time = event.calendar.timeIntervalSinceNow;
             let content = UNMutableNotificationContent()
            //let descriptionFirstAlarm = "Reminder! " +  event.title + " will start in 2 minutes";
            //let descriptionLastAlarm = "Reminder! " +  event.title + " is starting...";
            //$(PRODUCT_NAME)
            //$(TARGET_NAME)
            content.title = "Reminder!";
            content.subtitle =  event.title + " is starting..."
            content.badge = 1
            //time = 5;
            configureLed.colorSetting.brightness = 0;
            DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(NSInteger(time))) {
                configureLed.configureColors();
            }
            let trigger = UNTimeIntervalNotificationTrigger(timeInterval: time, repeats: false)
            let request = UNNotificationRequest(identifier: "meeting reminder", content: content, trigger: trigger)

            UNUserNotificationCenter.current().add(request, withCompletionHandler: nil)
            if(time - 2*60 > 0){
                time = time - 2*60;
                let content2 = UNMutableNotificationContent()
                content2.title = "Reminder!";
                content.subtitle =  event.title + " will start in 2 minutes";
                content.badge = 1
                configureLed.colorSetting.brightness = 75;
                DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(NSInteger(time))) {
                    configureLed.configureColors();
                }
                let trigger2 = UNTimeIntervalNotificationTrigger(timeInterval: time, repeats: false)
                let request2 = UNNotificationRequest(identifier: "meeting reminder", content: content2, trigger: trigger2)
                UNUserNotificationCenter.current().add(request2, withCompletionHandler: nil)

            }
            
        }
    }

    private func createDate(year: Int) -> Date {
        var components = DateComponents()
        components.year = year
        components.timeZone = TimeZone(secondsFromGMT: 0)
        
        return Calendar.current.date(from: components)!
    }
    
    func saveConfigurations(appNames: [String],colorStrings: [String] ) {
        for i in 0...appNames.count - 1 {
            UserDefaults.standard.set( colorStrings[i], forKey: appNames[i].lowercased());
            //print("\(UserDefaults.standard.string(forKey: appNames[i].lowercased())!)")
        }
    }
    func getNotificationsNamesAndColors(functionString:String) -> ([String], [String]) {
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
    //    print(notificantionsNames);
    //    print(notificantionsColors);
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
    @objc func showSearchingDeviceMessage(_ notification:Notification){
        let message = "Searching for device..."
        let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        self.present(alert, animated: true)
        
        // duration in seconds
        let duration: Double = 1
        
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + duration) {
            alert.dismiss(animated: true)
        }
        
    }
    @objc func showFoundDeviceMessage(_ notification:Notification){
        let message = "Device was found!"
        let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        // duration in seconds
        let duration: Double = 1
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + duration) {
            self.present(alert, animated: true)
            }
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 2*duration) {
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
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 2*duration) {
            alert.dismiss(animated: true)
        }
    }
    @objc func showReadyToConnectMessage(_ notification:Notification){
        let message = "Connecting..."
        let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        // duration in seconds
        let duration: Double = 1
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 2*duration) {
            self.present(alert, animated: true)
        }
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 3*duration) {
            alert.dismiss(animated: true)
        }
       // ConfigureLed.initializeTheDevice(bluetoothDevice: bluetoohDevice);
        let javaScriptString = "app.connectDevice();"
        self.webView?.evaluateJavaScript(javaScriptString, completionHandler: nil);
    }
    @objc func deviceConnected(_ notification:Notification){
        let javaScriptString = "app.connectDevice();"
        self.webView?.evaluateJavaScript(javaScriptString, completionHandler: nil);
    }
}


extension ViewController: CXCallObserverDelegate,CXProviderDelegate,PKPushRegistryDelegate{
    func callObserver(_ callObserver: CXCallObserver, callChanged call: CXCall) {
        
    }
    
    func pushRegistry(_ registry: PKPushRegistry, didUpdate pushCredentials: PKPushCredentials, for type: PKPushType) {
        print("passed from pushRegistery")
    }
    
    func providerDidReset(_ provider: CXProvider) {
    }

    func provider(_ provider: CXProvider, perform action: CXAnswerCallAction) {
            let configureLed = ConfigureLed(bluetoothDevice: bluetoohDevice,
                                            colorSetting: ColorSetting(
                                                color: ColorCustomized(hexColor:"#123456"
                                            )));
            configureLed.colorSetting.brightness = 0;
            configureLed.configureColors();
        action.fulfill()
    }

    func provider(_ provider: CXProvider, perform action: CXEndCallAction) {
            let configureLed = ConfigureLed(bluetoothDevice: bluetoohDevice,
                                            colorSetting: ColorSetting(
                                                color: ColorCustomized(hexColor:"#123456"
                                            )));
            configureLed.colorSetting.brightness = 0;
            configureLed.configureColors();
        
        action.fulfill()
    }
    func pushRegistry(_ registry: PKPushRegistry, didReceiveIncomingPushWith payload: PKPushPayload, for type: PKPushType) {
        // report new incoming call
        if let uuidString = payload.dictionaryPayload["UUID"] as? String,
            let identifier = payload.dictionaryPayload["identifier"] as? String,
            let uuid = UUID(uuidString: uuidString) {
            let update = CXCallUpdate()
            update.remoteHandle = CXHandle(type: .generic, value: identifier)
            provider.reportNewIncomingCall(with: uuid, update: update, completion: { error in })
//            //Change this to income number
//            let phoneNumber:String = "test";
//            if(!phoneContacts.filter({$0.number == phoneNumber}).isEmpty){
//                let phoneContact = phoneContacts.filter({$0.number == phoneNumber}).first!
//                let configureLed = ConfigureLed(ipAdress: ipAdress,
//                                                colorSetting: ColorSetting(color: ColorCustomized(hexColor: phoneContact.color)));
//                configureLed.colorSetting.brightness = Int(phoneContact.colorBrightness);
//                configureLed.configureColors();
//           }
            if let color = UserDefaults.standard.string(forKey: "calls") {
                let configureLed = ConfigureLed(bluetoothDevice: bluetoohDevice,
                                                colorSetting: ColorSetting(
                                                    color: ColorCustomized(hexColor:color
                                                )));
                configureLed.colorSetting.brightness = 75;
                configureLed.configureColors();
                
            }
            
        }
    }
}
