//
//  PhoneContact+CoreDataProperties.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 10.12.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//
//

import Foundation
import CoreData


extension ContactEntity {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<ContactEntity> {
        return NSFetchRequest<ContactEntity>(entityName: "ContactEntity")
    }

    @NSManaged public var number: String
    @NSManaged public var color: String
    @NSManaged public var name: String
    @NSManaged public var colorBrightness: Int16

}
