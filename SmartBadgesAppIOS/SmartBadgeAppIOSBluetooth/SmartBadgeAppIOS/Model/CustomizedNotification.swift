//
//  CustomizedNotification.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 01.12.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import Foundation
// notification class
class CustomizedNotification: Codable {
    var appName:String;
    var colorString:String;
    init(appName:String, colorString:String) {
        self.appName = appName;
        self.colorString = colorString;
    }
    
}
