//
//  SceneDelegate.swift
//  Lucid Waker
//
//  Created by Harry Cha on 3/21/20.
//  Copyright © 2020 Harry Cha. All rights reserved.
//

import UIKit
import SwiftUI
import CoreData

var settingsObject = Settings()

class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    var window: UIWindow?

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        // Use this method to optionally configure and attach the UIWindow `window` to the provided UIWindowScene `scene`.
        // If using a storyboard, the `window` property will automatically be initialized and attached to the scene.
        // This delegate does not imply the connecting scene or session are new (see `application:configurationForConnectingSceneSession` instead).

        // Get the managed object context from the shared persistent container.
        let context = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext

        // Create the SwiftUI view and set the context as the value for the managedObjectContext environment keyPath.
        // Add `@Environment(\.managedObjectContext)` in the views that will need the context.
        let appView = AppView()
            .environmentObject(settingsObject)
            .environment(\.managedObjectContext, context)

        // Use a UIHostingController as window root view controller.
        if let windowScene = scene as? UIWindowScene {
            let window = UIWindow(windowScene: windowScene)
            window.overrideUserInterfaceStyle = .dark // force dark mode
            window.rootViewController = UIHostingController(rootView: appView)
            self.window = window
            window.makeKeyAndVisible()
        }
    }

    func sceneDidDisconnect(_ scene: UIScene) {
        // Called as the scene is being released by the system.
        // This occurs shortly after the scene enters the background, or when its session is discarded.
        // Release any resources associated with this scene that can be re-created the next time the scene connects.
        // The scene may re-connect later, as its session was not neccessarily discarded (see `application:didDiscardSceneSessions` instead).
    }

    func sceneDidBecomeActive(_ scene: UIScene) {
        // Called when the scene has moved from an inactive state to an active state.
        // Use this method to restart any tasks that were paused (or not yet started) when the scene was inactive.
    }

    func sceneWillResignActive(_ scene: UIScene) {
        // Called when the scene will move from an active state to an inactive state.
        // This may occur due to temporary interruptions (ex. an incoming phone call).
        
        deleteAllCoreData()
        saveAllCoreData()
    }
    
    func deleteAllCoreData() {
        let appDel : AppDelegate = (UIApplication.shared.delegate as! AppDelegate)
        let context : NSManagedObjectContext = appDel.persistentContainer.viewContext
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "SettingsEntity")
        fetchRequest.returnsObjectsAsFaults = false
        
        let results = try? context.fetch(fetchRequest)
        for managedObject in results! {
            if let managedObjectData: NSManagedObject = managedObject as? NSManagedObject {
                context.delete(managedObjectData)
            }
        }
        let moc = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
        try? moc.save()
    }
    
    func saveAllCoreData() {
        let moc = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
        let settingsEntity = SettingsEntity(context: moc)
        settingsEntity.alarmDate = settingsObject.alarmDate
        settingsEntity.alarmSetCounter = Int16(settingsObject.alarmSetCounter + 1)
        settingsEntity.autoOffIndex1 = Int16(settingsObject.autoOffIndex1)
        settingsEntity.autoOffIndex2 = Int16(settingsObject.autoOffIndex2)
        settingsEntity.firstLaunchDate = settingsObject.firstLaunchDate
        settingsEntity.isAlarmScheduled = settingsObject.isAlarmScheduled
        settingsEntity.isAlarmSet = settingsObject.isAlarmSet
        settingsEntity.modeIndex = Int16(settingsObject.modeIndex)
        settingsEntity.requestedReview = settingsObject.requestedReview
        settingsEntity.soundIndex1 = Int16(settingsObject.soundIndex1)
        settingsEntity.soundIndex2 = Int16(settingsObject.soundIndex2)
        settingsEntity.waitIndexForChaining = Int16(settingsObject.waitIndexForChaining)
        settingsEntity.waitIndexForRausis = Int16(settingsObject.waitIndexForRausis)
        try? moc.save()
    }
    
    func sceneWillEnterForeground(_ scene: UIScene) {
        // Called as the scene transitions from the background to the foreground.
        // Use this method to undo the changes made on entering the background.
    }

    func sceneDidEnterBackground(_ scene: UIScene) {
        // Called as the scene transitions from the foreground to the background.
        // Use this method to save data, release shared resources, and store enough scene-specific state information
        // to restore the scene back to its current state.

        // Save changes in the application's managed object context when the application transitions to the background.
        (UIApplication.shared.delegate as? AppDelegate)?.saveContext()
    }
}
