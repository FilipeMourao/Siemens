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
        bluetoothDevice.writeColorToLed(colorSetting: ColorSetting(color: ColorCustomized(r: 255, g: 0, b: 0)));
        usleep(333);
        bluetoothDevice.writeColorToLed(colorSetting: ColorSetting(color: ColorCustomized(r: 0, g: 255, b: 0)));
        usleep(333);
        bluetoothDevice.writeColorToLed(colorSetting: ColorSetting(color: ColorCustomized(r: 0, g: 0, b: 255)));
        usleep(333);
        bluetoothDevice.writeColorToLed(colorSetting: ColorSetting(color: ColorCustomized(r: 0, g: 0, b: 0)));
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


