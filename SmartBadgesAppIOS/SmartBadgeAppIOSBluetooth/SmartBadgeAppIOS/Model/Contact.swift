//
//  Contact.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 06.12.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import Foundation
class Contact: NSObject, NSCoding,Codable {
    var name:String;
    var number:String;
    var color:String;
    var colorBrihgtness:Int;
    init(name:String, number:String) {
        self.name = name;
        self.number = number;
        self.colorBrihgtness = 0;
        self.color = "#123456"
    }
    init(name:String, number:String, color:String,colorBrihgtness:Int) {
        self.name = name;
        self.number = number;
        self.color = color;
        self.colorBrihgtness = colorBrihgtness;
    }
    func encode(with aCoder: NSCoder) {
        aCoder.encode(name, forKey: "name")
        aCoder.encode(number, forKey: "number")
        aCoder.encode(color, forKey: "color")
        aCoder.encode(colorBrihgtness, forKey: "colorBrihgtness")
    }
    
    required init?(coder aDecoder: NSCoder) {
        guard let name = aDecoder.decodeObject(forKey: "name") as? String,
            let number = aDecoder.decodeObject(forKey: "number") as? String else {
                return nil
        }
        self.name = name;
        self.number = number;
        guard let color = aDecoder.decodeObject(forKey: "color") as? String,
            let colorBrihgtness = aDecoder.decodeObject(forKey: "colorBrihgtness") as? Int else {
                self.color = "#123456";
                self.colorBrihgtness = 0;
                return
        }
        self.color = color;
        self.colorBrihgtness = colorBrihgtness;
    }
    
}
