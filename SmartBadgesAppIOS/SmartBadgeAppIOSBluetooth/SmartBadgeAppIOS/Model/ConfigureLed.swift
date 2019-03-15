//
//  ConfigureLed.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 22.11.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import Foundation
import UIKit
class ConfigureLed {
    //class used to send the values to the led
    var colorSetting:ColorSetting;
    var bluetoothDevice:BluetoothDevice
    init(bluetoothDevice:BluetoothDevice, colorSetting:ColorSetting) {
        self.colorSetting = colorSetting;
        self.bluetoothDevice = bluetoothDevice ;
    }
    func configureColors() {
        bluetoothDevice.writeColorToLed(colorSetting: colorSetting);
    }
    static func initializeTheDevice(bluetoothDevice:BluetoothDevice){
        // cant create a delay of the led occurence, so for a specific value of brightness (96) the occurence will considered the initials
        let colorSetting = ColorSetting(color: ColorCustomized(r: 255, g: 0, b: 0));
        colorSetting.brightness = 96;
        bluetoothDevice.writeColorToLed(colorSetting: colorSetting);
    }
    func jsonEncoding(data:ColorSetting) -> String {
        do {
            let jsonEncoder  = JSONEncoder();
            let jsonData = try jsonEncoder.encode(data);
            let json =  String(data: jsonData, encoding: .utf8)!;
            return json;
        } catch {
            return "Error";
        }
    }
}


