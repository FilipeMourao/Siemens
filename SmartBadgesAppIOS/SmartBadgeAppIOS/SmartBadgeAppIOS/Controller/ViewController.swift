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
class ViewController: UIViewController,WKScriptMessageHandler,UITableViewDelegate {
    var ipAdress = "";
    var webView: WKWebView?;
    var events:[Event] = [];
    let eventStore = EKEventStore();
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
//        if let path = Bundle.main.path(forResource: "newIndex", ofType: "html"){
//            let url = URL(string: path)
//            myWebView.loadFileURL(url!, allowingReadAccessTo: url!.deletingPathExtension())
//        }else{
            let configuration = WKWebViewConfiguration()
            let controller = WKUserContentController()
            controller.add(self, name: "JSInterface")
            configuration.userContentController = controller
            webView = WKWebView(frame: self.view.frame, configuration: configuration)
            let url = Bundle.main.url(forResource: "newIndex", withExtension: "html")
            let request = URLRequest(url: url!)
            self.view = webView
            webView!.load(request)
//            myWebView.configuration = configuration
//            myWebView.loadFileURL(url!, allowingReadAccessTo: url!.deletingPathExtension())
//
//        if let url = Bundle.main.url(forResource: "newIndex", withExtension: "html"){
//        } else{
//            print("Error Loadging page")
//        }
//
        
    }
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        print("Message from beyond: \(message.body)")
        if ("\(message.body)" == "connectDevice()") {createPopUp()}
        if ("\(message.body)" == "showEvents()") {showUserEvents()}
        if ("\(message.body)" == "createAlarms()") {createNotifications()}
        
        
    }
    func showUserEvents(){
        eventStore.requestAccess(to: EKEntityType.event, completion: {
            (granted, error) in
            if granted && error == nil {
                print("Permisson granted!");
            
            } else {
                print("No permisson!");
            }
        })
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
        var event:Event;
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
            let end = createDate(year: cal.component(.year, from: Date()) + 4)
            
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
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge], completionHandler: {didAllow, error in
        })
//        UNUserNotificationCenter.current().requestAuthorization(options: [.alert], completionHandler: {didAllow, error in
//        })
        
        for event in self.events {
            
//            let content = UNMutableNotificationContent()
//            content.title = "How many days are there in one year"
//            content.subtitle = "Do you know?"
//            content.body = "Do you really know?"
//            content.badge = 1
//
//            let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 5, repeats: false)
//            let request = UNNotificationRequest(identifier: "timerDone", content: content, trigger: trigger)
//
//            UNUserNotificationCenter.current().add(request, withCompletionHandler: nil)
//
//
            let configureLed = ConfigureLed(ipAdress: ipAdress, colorSetting: ColorSetting(color: ColorCustomized(hexColor:event.color )));
            configureLed.colorSetting.brightness = 0;
            var time = event.calendar.timeIntervalSinceNow;
            let descriptionFirstAlarm = "Reminder! " +  event.title + " will start in 2 minutes";
            let descriptionLastAlarm = "Reminder! " +  event.title + " is starting...";
            let content = UNMutableNotificationContent()
            time = 5;
            //$(PRODUCT_NAME)
            //Siemens.SmartBadgeAppIOS
            content.title = "Reminder!";
            content.subtitle =  event.title + " is starting..."
            content.badge = 1
            DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(NSInteger(time))) {
                configureLed.configureColors();
            }
            let trigger = UNTimeIntervalNotificationTrigger(timeInterval: time, repeats: false)
            let request = UNNotificationRequest(identifier: "timerDone", content: content, trigger: trigger)

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
                let request2 = UNNotificationRequest(identifier: "timerDone", content: content2, trigger: trigger2)
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

    func createPopUp(){
        
        let alertController = UIAlertController(title: "Add your card number!",
                                                message: nil,
                                                preferredStyle: .alert)
        alertController.addTextField(configurationHandler: takeCardNumber)
//        let okAction = UIAlertAction (title: "OK", style: .default, handler: cardNumberWasAdded)
          let okAction = UIAlertAction (title: "OK", style: .default, handler: {[weak alertController](_) in
            let textField = alertController?.textFields![0];
            if(textField?.text?.count == 3) {
                self.ipAdress = "192.168.1." + (textField?.text!)!;
                let javaScriptString = "app.connectDevice();"
                self.webView?.evaluateJavaScript(javaScriptString, completionHandler: nil);
                
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
    
    func delay(delay: Double, closure: @escaping () -> ()) {
        
        DispatchQueue.main.asyncAfter(deadline: .now() + delay) {
            closure()
        }
    }
    

}

