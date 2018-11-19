//
//  ViewController.swift
//  SmartBadgesAppIOS
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
//        let url = URL(string: "https://www.google.com")
//        let request = URLRequest(url: url!)
//        let htmlpath = Bundle.main.path(forResource: "smartBadgesFrontEndLastVersion/newIndex", ofType: "html")
        let htmlpath = Bundle.main.path(forResource: "newIndex", ofType: "html")
        let url = URL(fileURLWithPath: htmlpath!)
        let request = URLRequest(url: url)
        myWebView.load(request)
    }


}

