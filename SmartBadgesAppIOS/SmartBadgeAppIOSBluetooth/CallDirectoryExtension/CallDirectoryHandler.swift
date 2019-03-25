//
//  CallDirectoryHandler.swift
//  CallDirectoryExtension
//
//  Created by Rebecca Johnson on 22.03.19.
//  Copyright © 2019 Rebecca Johnson. All rights reserved.
//

import Foundation
import CallKit
import CoreData
class CallDirectoryHandler: CXCallDirectoryProvider {
    override func beginRequest(with context: CXCallDirectoryExtensionContext) {
        context.delegate = self
        
        do {
            try addBlockingPhoneNumbers(to: context)
        } catch {
            NSLog("Unable to add blocking phone numbers")
            let error = NSError(domain: "CallDirectoryHandler", code: 1, userInfo: nil)
            context.cancelRequest(withError: error)
            return
        }
        
        do {
            try addIdentificationPhoneNumbers(to: context)
        } catch {
            NSLog("Unable to add identification phone numbers")
            let error = NSError(domain: "CallDirectoryHandler", code: 2, userInfo: nil)
            context.cancelRequest(withError: error)
            return
        }
        
        context.completeRequest()
    }
    
    // 1.
    private func addBlockingPhoneNumbers(to context: CXCallDirectoryExtensionContext) throws {
        let phoneNumbers: [CXCallDirectoryPhoneNumber] = [ 1234 ]
        
        for phoneNumber in phoneNumbers {
            context.addBlockingEntry(withNextSequentialPhoneNumber: phoneNumber)
        }
    }
    
    //add identification numbers from database
    private func addIdentificationPhoneNumbers(to context: CXCallDirectoryExtensionContext) throws {
        // get the contacts from the database
        let persistenceManager:PersistenceManager = PersistenceManager();
       let phoneContacts  = persistenceManager.fetch(ContactEntity.self)
        var contactList:[Contact] = [];
        for phoneContact in phoneContacts {
            contactList.append(
                Contact(name: phoneContact.name,
                        number: phoneContact.number,
                        color: phoneContact.color,
                        colorBrihgtness: Int(phoneContact.colorBrightness)))
        }
        for contact in contactList {
            let phoneNumber = Double(contact.number)
            if phoneNumber != nil {
                let phoneNumberCXCall: CXCallDirectoryPhoneNumber =
                    CXCallDirectoryPhoneNumber(phoneNumber!)
                let labelString =  "\(contact.name), color: \(contact.color), brihtness: \(contact.colorBrihgtness)"
                context.addIdentificationEntry(withNextSequentialPhoneNumber: phoneNumberCXCall, label: labelString)
            }
        }
//        let phoneNumbers: [CXCallDirectoryPhoneNumber] = [ 1111 ]
//        let labels = [ "RW Tutorial Team" ]
//
//        for (phoneNumber, label) in zip(phoneNumbers, labels) {
//            context.addIdentificationEntry(withNextSequentialPhoneNumber: phoneNumber, label: label)
//
//        }
    }
}

extension CallDirectoryHandler: CXCallDirectoryExtensionContextDelegate {
    
    func requestFailed(for extensionContext: CXCallDirectoryExtensionContext, withError error: Error) {
        print("An error occured when completing the request: \(error.localizedDescription)")
    }
    
}
