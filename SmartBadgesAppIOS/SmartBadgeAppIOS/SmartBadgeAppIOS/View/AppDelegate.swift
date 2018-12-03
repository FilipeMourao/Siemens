//
//  AppDelegate.swift
//  SmartBadgeAppIOS
//
//  Created by Rebecca Johnson on 19.11.18.
//  Copyright © 2018 Rebecca Johnson. All rights reserved.
//

import UIKit
import UserNotifications
import EventKit
@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?
    let center = UNUserNotificationCenter.current();

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        center.delegate = self;
        //permissions
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge], completionHandler: {didAllow, error in
        })
        EKEventStore().requestAccess(to: EKEntityType.event, completion: {
            (granted, error) in})
        
        return true
    }

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }


}

extension AppDelegate : UNUserNotificationCenterDelegate {
    // while your app is active in forground
    
    // Handle Notifications While Your App Runs in the Foreground
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        completionHandler([.alert,.sound])
        let notifiticationIdentifier = notification.request.identifier
        print(notifiticationIdentifier);
        let viewController = (self.window?.rootViewController as? ViewController)!
        if (notifiticationIdentifier.lowercased().range(of: "facebook") != nil) {
            if let color  = UserDefaults.standard.string(forKey:"facebook"){
                let configureLed = ConfigureLed(ipAdress: viewController.ipAdress,
                                                colorSetting: ColorSetting(
                                                    color: ColorCustomized(
                                                        hexColor: color)
                ));
                configureLed.configureColors();
            }
        }
        if (notifiticationIdentifier.lowercased().range(of: "twitter") != nil) {
            if let color  = UserDefaults.standard.string(forKey:"twitter"){
                let configureLed = ConfigureLed(ipAdress: viewController.ipAdress,
                                                colorSetting: ColorSetting(
                                                    color: ColorCustomized(
                                                        hexColor: color)
                ));
                configureLed.configureColors();
            }
        }
        if (notifiticationIdentifier.lowercased().range(of: "instagram") != nil) {
            if let color  = UserDefaults.standard.string(forKey:"instagram"){
                let configureLed = ConfigureLed(ipAdress: viewController.ipAdress,
                                                colorSetting: ColorSetting(
                                                    color: ColorCustomized(
                                                        hexColor: color)
                ));
                configureLed.configureColors();
            }
        }
        if (notifiticationIdentifier.lowercased().range(of: "whatsapp") != nil) {
            if let color  = UserDefaults.standard.string(forKey:"whatsapp"){
                let configureLed = ConfigureLed(ipAdress: viewController.ipAdress,
                                                colorSetting: ColorSetting(
                                                    color: ColorCustomized(
                                                        hexColor: color)
                ));
                configureLed.configureColors();
            }
        }

        // Change this to your preferred presentation option
        // Play a sound.
        //  completionHandler(UNNotificationPresentationOptions.sound)
    }
    
    // While App is inactive  in background
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completionHandler: @escaping () -> Void) {
        let userInfo = response.notification.request.identifier
        print(userInfo)

        // While App is inactive  in background
        
        
        print(userInfo)
        
        
        completionHandler()
    }
}

