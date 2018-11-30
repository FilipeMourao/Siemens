//
//  ConfigureLed.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 22.11.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import Foundation
class ConfigureLed {
    var ipAdress:String;
    var colorSetting:ColorSetting;
    init(ipAdress:String, colorSetting:ColorSetting) {
        self.ipAdress = ipAdress;
        self.colorSetting = colorSetting;
    }
    func configureColors() {
        let url = URL(string: "http://" + self.ipAdress + "/api/v1/state")!
        var request = URLRequest(url: url)
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpMethod = "POST"
        let postString = jsonEncoding(data: self.colorSetting)
        print(postString)
        request.httpBody = postString.data(using: .utf8)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data, error == nil else {                                                 // check for fundamental networking error
              //  print("error=\(error)");
                return
            }
            
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200 {           // check for http errors
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
              //  print("response = \(response)");
            }
            
            let responseString = String(data: data, encoding: .utf8)
           // print("responseString = \(responseString)");
        }
        task.resume();
      
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


