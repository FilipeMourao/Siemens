//
//  ColorCustomized.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 22.11.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import Foundation
class ColorCustomized: Codable{
    let mode = "rgb";
    var r: Int;
    var g: Int;
    var b :Int;
    init(r:Int, g:Int, b:Int) {
        self.r = r;
        self.g = g;
        self.b = b;
    }
    init(hexColor:String){
        var cString:String = hexColor.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
        
        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }
        
        
        var rgbValue = UInt32()
        Scanner(string: cString).scanHexInt32(&rgbValue)
        self.r = Int(((rgbValue & 0xFF0000) >> 16));
        self.g = Int(((rgbValue & 0x00FF00) >> 8));
        self.b = Int(((rgbValue & 0x0000FF) ));
    }
}
