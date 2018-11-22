//
//  ViewController.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 19.11.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import UIKit
import WebKit
class ViewController: UIViewController,WKScriptMessageHandler {
    var ipAdress = "";
    var webView: WKWebView?
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
    func jsonEncoding() -> String {
        do {
            let test:ColorCustomized = ColorCustomized(hexColor: "#FF0000");
            let jsonEncoder  = JSONEncoder();
            let jsonData = try jsonEncoder.encode(test);
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
    
    
    

}

