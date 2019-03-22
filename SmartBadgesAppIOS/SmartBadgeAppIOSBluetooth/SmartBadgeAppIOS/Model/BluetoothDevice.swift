//
//  BluetoothDevice.swift
//  SmartBadgeAppIOSBluetooth
//
//  Created by Rebecca Johnson on 28.02.19.
//  Copyright © 2019 Rebecca Johnson. All rights reserved.
//

import Foundation
import CoreBluetooth
import UIKit
struct BLEParameters {
    static let ledServiceUUID = CBUUID(string: "00001200-0000-1000-8000-00805F9B34FB")
    static let redCharacteristicUUID = CBUUID(string: "00001201-0000-1000-8000-00805F9B34FB")
    static let greenCharacteristicUUID = CBUUID(string: "00001202-0000-1000-8000-00805F9B34FB")
    static let blueCharacteristicUUID = CBUUID(string: "00001203-0000-1000-8000-00805F9B34FB")
    static let brightnessCharacteristicUUID = CBUUID(string: "00001204-0000-1000-8000-00805F9B34FB")
}
// all the code is based in the content of the following video
// https://www.youtube.com/watch?v=FHD-MelqHW4
class BluetoothDevice: NSObject,CBCentralManagerDelegate,CBPeripheralDelegate  {
    var centralManager: CBCentralManager!;
    private var badgeDevice : CBPeripheral?;
    private var ledService: CBService!;
    private var redCharactetistic: CBCharacteristic!;
    private var greenCharactetistic: CBCharacteristic!;
    private var blueCharactetistic: CBCharacteristic!;
    private var brightnessCharactetistic: CBCharacteristic!;
    private var colorSettingUsed: ColorSetting = ColorSetting(color:ColorCustomized(r: 0, g: 0, b: 0));
    func startUpCentralManager(){
        centralManager = CBCentralManager(delegate: self, queue: nil);
    }
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        switch central.state {
        case .poweredOn:
            NotificationCenter.default.post(name: RCNotifications.SearchingDevice, object: nil);
            discoverSmartBadge();
            break;
        default:
            break;
        }
    }
    func discoverSmartBadge(){
        centralManager.scanForPeripherals(withServices: [BLEParameters.ledServiceUUID], options: [CBCentralManagerScanOptionAllowDuplicatesKey:false])
        let timeOfScaning:Double = 3;
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + timeOfScaning ) {
            if (self.centralManager.isScanning){
                self.centralManager.stopScan();
                 NotificationCenter.default.post(name: RCNotifications.DeviceNotFound, object: nil)
            }
        }
    }

    func centralManager(_ central: CBCentralManager,
                        didDiscover peripheral: CBPeripheral,
                        advertisementData: [String : Any],
                        rssi RSSI: NSNumber){
         if badgeDevice == nil {
            badgeDevice = peripheral;
            centralManager.stopScan();
            NotificationCenter.default.post(name: RCNotifications.DeviceFound, object: nil)
            connectToBadge();
         } 
    }
    func connectToBadge() {
        guard let badge = badgeDevice else {
            return;
        }
        centralManager.connect(badge, options: nil);
    }
    func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
        NotificationCenter.default.post(name: RCNotifications.DeviceNotFound, object: nil)
    }
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        badgeDevice!.delegate = self;
       // NotificationCenter.default.post(name: RCNotifications.ReadyToConnect, object: nil)
        //discover services
        badgeDevice!.discoverServices(nil);
    }
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        for service in peripheral.services!{
            if service.uuid == BLEParameters.ledServiceUUID{
                ledService = service as! CBService;
            }
        }
        // get the caracteristics
        if(ledService != nil){
            badgeDevice!.discoverCharacteristics(nil, for: ledService)
            
        }
    }
    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        for characteristics in service.characteristics! {
            if let characteristic = characteristics as? CBCharacteristic {
                switch characteristic.uuid{
                case BLEParameters.redCharacteristicUUID: redCharactetistic = characteristic
                case BLEParameters.greenCharacteristicUUID: greenCharactetistic = characteristic
                case BLEParameters.blueCharacteristicUUID: blueCharactetistic = characteristic
                case BLEParameters.brightnessCharacteristicUUID: brightnessCharactetistic = characteristic
                default:break;
                }
            }
        }
        //After the characteristics were discovered, just send the specific colors
        ConfigureLed.initializeTheDevice(bluetoothDevice: self);
        NotificationCenter.default.post(name: RCNotifications.connected, object: nil)
    }
    func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
         NotificationCenter.default.post(name: RCNotifications.LostConnection, object: nil)
    }
    func writeColorToLed(colorSetting : ColorSetting){
        colorSettingUsed = colorSetting;
        let redByteArray =  intToByteArray(number: colorSettingUsed.color.r);
        let redData = NSData(bytes: redByteArray, length: MemoryLayout<UInt8>.size) as Data;
        badgeDevice!.writeValue(redData, for: redCharactetistic, type: CBCharacteristicWriteType.withResponse)
    }
    func peripheral(_ peripheral: CBPeripheral, didWriteValueFor characteristic: CBCharacteristic, error: Error?) {
        switch characteristic.uuid {
        case BLEParameters.redCharacteristicUUID:
            let greenByteArray =  intToByteArray(number: colorSettingUsed.color.g);
            let greenData = NSData(bytes: greenByteArray, length: MemoryLayout<UInt8>.size) as Data;
            badgeDevice!.writeValue(greenData, for: greenCharactetistic, type: CBCharacteristicWriteType.withResponse)
            break;
        case BLEParameters.greenCharacteristicUUID:
            let blueByteArray =  intToByteArray(number: colorSettingUsed.color.b);
            let blueData = NSData(bytes: blueByteArray, length: MemoryLayout<UInt8>.size) as Data;
            badgeDevice!.writeValue(blueData, for: blueCharactetistic, type: CBCharacteristicWriteType.withResponse)
            break;
        case BLEParameters.blueCharacteristicUUID:
            let correctBrightness  = colorSettingUsed.brightness*255/100;
            let brightnessByteArray =  intToByteArray(number: correctBrightness);
            let brightnessData = NSData(bytes: brightnessByteArray, length: MemoryLayout<UInt8>.size) as Data;
            badgeDevice!.writeValue(brightnessData, for: brightnessCharactetistic, type: CBCharacteristicWriteType.withResponse)
            break;
        case BLEParameters.brightnessCharacteristicUUID:
            //send a message that the values were added
            
            // if it is the initial case create the flow of colors
            if(colorSettingUsed.brightness == 96) {
                if(colorSettingUsed.color.r == 255){
                    colorSettingUsed.color.r = 0;
                    colorSettingUsed.color.g = 255;
                    writeColorToLed(colorSetting: colorSettingUsed);
                    break;
                }
                if(colorSettingUsed.color.g == 255){
                    colorSettingUsed.color.g = 0;
                    colorSettingUsed.color.b = 255;
                    writeColorToLed(colorSetting: colorSettingUsed);
                    break;
                }
                if(colorSettingUsed.color.b == 255){
                    colorSettingUsed.color.b = 0;
                    writeColorToLed(colorSetting: colorSettingUsed);
                    break;
                }
                
            }
            break;
        default:
            break;
        }
    }
    func disconnect() {
        if badgeDevice != nil {
            centralManager.cancelPeripheralConnection(badgeDevice!);
            badgeDevice = nil;
        }
    }
    func intToByteArray(number:Int) -> [UInt8] {
        let string = NSString(format:"%2X", number) as String;
        let length = string.count
        if length & 1 != 0 {
            return [UInt8.init(0)];
        }
        var bytes = [UInt8]()
        bytes.reserveCapacity(length/2)
        var index = string.startIndex
        for _ in 0..<length/2 {
            let nextIndex = string.index(index, offsetBy: 2)
            if let b = UInt8(string[index..<nextIndex], radix: 16) {
                bytes.append(b)
            } else {
                return [UInt8.init(0)];
            }
            index = nextIndex
        }
        return bytes
    }

    
}
