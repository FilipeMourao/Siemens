//
//  ColorSetting.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 22.11.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import Foundation
class ColorSetting: Codable {
    var state:String;
    var color:ColorCustomized;
    var mode:String;
    var brightness:Int;
    init(state:String, color:ColorCustomized,brightness:Int, mode:String) {
        self.state = state;
        self.color = color;
        self.mode = mode;
        self.brightness = brightness;
    }
    init(color:ColorCustomized) {
        self.state = "ON";
        self.brightness = 75;
        self.color = color;
        self.mode = "SOLID";
    }
}
