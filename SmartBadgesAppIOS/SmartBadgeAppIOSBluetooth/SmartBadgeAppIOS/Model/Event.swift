//
//  Event.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 22.11.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import Foundation
// event class
class Event: Codable {
    var title:String;
    var location:String;
    var color:String;
    var calendar:Date;
    init(title:String, location:String, color:String,calendar:Date) {
        self.title = title;
        self.location = location;
        self.color = color;
        self.calendar = calendar;
    }
}
