//
//  ViewController.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 19.11.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import UIKit
import WebKit
class ViewController: UIViewController {

    @IBOutlet weak var myWebView: WKWebView!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        if let path = Bundle.main.path{
            myWebView.loadFileURL(url, allowingReadAccessTo: url.deletingPathExtension())
        }else{
//        if let url = Bundle.main.url(forResource: "FrontEnd/newIndex", withExtension: "html"){
//            myWebView.loadFileURL(url, allowingReadAccessTo: url.deletingPathExtension())
//        }else{
            print("Error Loadging page")
        }
        
        
    }

}

