//
//  PersistenceManager.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 10.12.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import Foundation
import CoreData
final class PersistenceManager{
   init(){}
    static let shared = PersistenceManager()
    lazy var persistentContainer: NSPersistentContainer = {
        
        let container = NSPersistentContainer(name: "ContactDatabase")
        container.loadPersistentStores(completionHandler: { (storeDescription, error) in
            if let error = error {
                
                fatalError("Unresolved error, \((error as NSError).userInfo)")
            }
        })
        return container
    }()
    lazy var context = persistentContainer.viewContext
    func save() {
        let context = persistentContainer.viewContext
        if context.hasChanges{
            do {
                try context.save()
                print("Saved")
            } catch {
                print("Error saving")
            }
        }
        
    }
    func fetch<T: NSManagedObject>(_ objectType: T.Type) -> [T] {
        
        let entityName = String(describing: objectType)
        
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: entityName)
        
        do {
            let fetchedObjects = try context.fetch(fetchRequest) as? [T]
            return fetchedObjects ?? [T]()
            
        } catch {
            print(error)
            return [T]()
        }
        
    }


}
