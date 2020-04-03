//
//  AlarmSetView.swift
//  Lucid Waker
//
//  Created by Harry Cha on 3/21/20.
//  Copyright © 2020 Harry Cha. All rights reserved.
//

import SwiftUI
import CoreData
import MediaPlayer
import UserNotifications

struct AlarmSetView: View {
    @EnvironmentObject var settings: Settings
    @Environment(\.managedObjectContext) var moc

    @State private var setHour = 0
    @State private var setMin = 0
    @State private var period = ""
    
    @State private var showRequestOnOpening = false
    @State private var showRequestOnYes = false
    @State private var showRequestOnNo = false
    
    @State private var requestOnOpeningTitle = "Just a moment please!"
    @State private var requestOnOpeningMessage = "Does Lucid Waker help you lucid dream?"
    @State private var requestOnYesTitle = "I am super glad to hear that.   :)"
    @State private var requestOnYesMessage = "Would you mind rating this app on App Store?"
    @State private var requestOnNoTitle = "I am so sorry to hear that.   :("
    @State private var requestOnNoMessage = "Would you mind providing feedback so that I can improve this app for you?"
    
    @State private var requestOnOpeningSayYes = "Yes, it does!"
    @State private var requestOnOpeningSayNo = "Not really."
    @State private var requestAfterOpeningSayYes = "Sure, take me there!"
    @State private var requestAfterOpeningSayNo = "No, don't ask again."

    var body: some View {
        VStack {
            Text(settings.modeList[settings.modeIndex])
                .font(.system(size: 40))
            Spacer()
                .frame(height: 10)
            Text(settings.timeLeft)
                .font(.system(size: 25))
            Spacer()
                .frame(height: 50)
            Text(displayAlarmDate())
                .font(.system(size: 70))
            
            Spacer()
            
            Button(action: {
                UNUserNotificationCenter.current().removeAllDeliveredNotifications()
                UNUserNotificationCenter.current().removeAllPendingNotificationRequests()
                
                self.settings.isAlarmSet = false
                self.settings.isAlarmScheduled = false
                self.saveCoreData()
                ContentView().environmentObject(self.settings)
            }) {
                Text("Cancel")
            }
            .padding(10)
            .background(Color.accentColor)
            .foregroundColor(.white)
            .cornerRadius(20.0)
            .font(.system(size: 50))
            
            Spacer()
                .frame(height: 30)
            
            // request review alerts
            HStack {
                Spacer()
                    .alert(isPresented: $showRequestOnOpening) {
                        Alert(title: Text(requestOnOpeningTitle), message: Text(requestOnOpeningMessage), primaryButton: .destructive(Text(requestOnOpeningSayNo), action: {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) { // very short delay is needed
                                self.showRequestOnNo = true
                            }
                        }), secondaryButton: .default(Text(requestOnOpeningSayYes), action: {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) { // very short delay is needed
                                self.showRequestOnYes = true
                            }
                        }))
                    }
                
                Spacer()
                    .alert(isPresented: $showRequestOnYes) {
                        Alert(title: Text(requestOnYesTitle), message: Text(requestOnYesMessage), primaryButton: .default(Text(requestAfterOpeningSayYes)), secondaryButton: .destructive(Text(requestAfterOpeningSayNo), action: {
                            // link to app
                        }))
                    }
                
                Spacer()
                    .alert(isPresented: $showRequestOnNo) {
                        Alert(title: Text(requestOnNoTitle), message: Text(requestOnNoMessage), primaryButton: .default(Text(requestAfterOpeningSayYes)), secondaryButton: .destructive(Text(requestAfterOpeningSayNo), action: {
                            // link to app
                        }))
                    }
            }
        }
        .onAppear {
            if !self.settings.isAlarmScheduled {
                self.schedule()
            }
            if !self.settings.timeLeftUpdating {
                self.settings.startUpdatingTimeLeft()
            }
            self.saveCoreData()
            self.checkRatePopUp()
        }
    }
    
    func displayAlarmDate() -> String {
        DispatchQueue.main.async { // needed to assign values to vars
            self.setHour = Calendar.current.component(.hour, from: self.settings.alarmDate)
            self.setMin = Calendar.current.component(.minute, from: self.settings.alarmDate)
            self.period = "AM"
            
            if self.setHour == 12 {
                self.period = "PM"
            } else if self.setHour > 12 {
                self.setHour -= 12
                self.period = "PM"
            } else if self.setHour == 0 {
                self.setHour = 12
            }
        }
        
        var setMinString = String(setMin)
        if setMin < 10 {
            setMinString = "0" + setMinString
        }
        
        return "\(setHour):\(setMinString) \(period)"
    }
    
    func schedule() {
        UNUserNotificationCenter.current().getNotificationSettings { notificationSettings in
            switch notificationSettings.authorizationStatus {
            case .notDetermined:
                self.requestPermission()
            case .authorized, .provisional:
                self.scheduleNotifications()
            default:
                break
            }
        }
    }
    
    func requestPermission() {
        UNUserNotificationCenter
            .current()
            .requestAuthorization(options: [.alert, .sound]) { granted, error in
                if granted == true && error == nil { // We have permission!
                    self.scheduleNotifications()
                }
        }
    }
    
    func scheduleNotifications() {
        var soundFileName = getSoundFileName(soundIndex: settings.soundIndex1, autoOffIndex: settings.autoOffIndex1)
        
        var content = UNMutableNotificationContent()
        content.title = "Alarm is ringing!"
        content.body = "Happy lucid dreaming!"
        content.sound = UNNotificationSound(named: UNNotificationSoundName(rawValue: soundFileName))
        
        DispatchQueue.main.sync { // use sync, not async, since the alarm date must be set before continuing
            self.setAlarmDateDay()
        }
        
        var scheduleDateComponents = Calendar.current.dateComponents([.day, .hour, .minute, .second], from: settings.alarmDate)
        var trigger = UNCalendarNotificationTrigger(dateMatching: scheduleDateComponents, repeats: false)
        var request = UNNotificationRequest(identifier: UUID().uuidString, content: content, trigger: trigger)
        
        UNUserNotificationCenter.current().removeAllDeliveredNotifications()
        UNUserNotificationCenter.current().removeAllPendingNotificationRequests()
        UNUserNotificationCenter.current().add(request)
        
        if settings.modeIndex == 1 { // Chaining
            content.title = "New Chaining alarm is ringing!"
            
            var scheduleDate = settings.alarmDate
            
            for _ in 1...10 { // adding too many requests may make iOS reject them
                scheduleDate = Calendar.current.date(byAdding: .minute, value: getWaitTime(waitIndex: settings.waitIndexForChaining), to: scheduleDate)!
                scheduleDate = Calendar.current.date(byAdding: .second, value: getAutoOffTime(autoOffIndex: settings.autoOffIndex1), to: scheduleDate)!
                scheduleDateComponents = Calendar.current.dateComponents([.day, .hour, .minute, .second], from: scheduleDate)
                trigger = UNCalendarNotificationTrigger(dateMatching: scheduleDateComponents, repeats: false)
                request = UNNotificationRequest(identifier: UUID().uuidString, content: content, trigger: trigger)
                
                UNUserNotificationCenter.current().add(request)
            }
            
        } else if settings.modeIndex == 2 { // Rausis
            soundFileName = getSoundFileName(soundIndex: settings.soundIndex2, autoOffIndex: settings.autoOffIndex2)
            
            content = UNMutableNotificationContent()
            content.title = "Second alarm for Rausis is ringing!"
            content.body = "Happy lucid dreaming!"
            content.sound = UNNotificationSound(named: UNNotificationSoundName(rawValue: soundFileName))
            
            var scheduleDate = Calendar.current.date(byAdding: .minute, value: getWaitTime(waitIndex: settings.waitIndexForRausis), to: settings.alarmDate)!
            scheduleDate = Calendar.current.date(byAdding: .second, value: getAutoOffTime(autoOffIndex: settings.autoOffIndex1), to: scheduleDate)!
            scheduleDateComponents = Calendar.current.dateComponents([.day, .hour, .minute, .second], from: scheduleDate)
            trigger = UNCalendarNotificationTrigger(dateMatching: scheduleDateComponents, repeats: false)
            request = UNNotificationRequest(identifier: UUID().uuidString, content: content, trigger: trigger)
            
            UNUserNotificationCenter.current().add(request)
        }
        
        DispatchQueue.main.async {
            self.settings.isAlarmScheduled = true
        }
    }
    
    func setAlarmDateDay() {
        let currDay = Calendar.current.component(.day, from: Date())
        
        var alarmDateComponents = Calendar.current.dateComponents([.day, .hour, .minute, .second], from: settings.alarmDate)
        alarmDateComponents.day = currDay
        settings.alarmDate = Calendar.current.date(from: alarmDateComponents)!
        
        let currHour = Calendar.current.component(.hour, from: Date())
        let currMin = Calendar.current.component(.minute, from: Date())
        
        let setHour = Calendar.current.component(.hour, from: settings.alarmDate)
        let setMin = Calendar.current.component(.minute, from: settings.alarmDate)
        
        if (setHour < currHour) || (setHour == currHour && setMin <= currMin) {
            settings.alarmDate = Calendar.current.date(byAdding: .day, value: 1, to: settings.alarmDate)!
        }
    }
    
    func getSoundFileName(soundIndex: Int, autoOffIndex: Int) -> String {
        let lowercased = settings.soundList[soundIndex].lowercased()
        let noSpaces = lowercased.replacingOccurrences(of: " ", with: "_")
        let autoOffTime = String(getAutoOffTime(autoOffIndex: autoOffIndex))
        
        let soundFileName = noSpaces + "_" + autoOffTime + ".mp3"
        return soundFileName
    }
    
    func getAutoOffTime(autoOffIndex: Int) -> Int {
        let range: Range<String.Index> = settings.autoOffList[autoOffIndex].range(of: " ")!
        let spaceIndex: Int = settings.autoOffList[autoOffIndex].distance(from: settings.autoOffList[autoOffIndex].startIndex, to: range.lowerBound)
        
        let autoOffNumber = settings.autoOffList[autoOffIndex].prefix(spaceIndex)
        return Int(autoOffNumber)!
    }
    
    func getWaitTime(waitIndex: Int) -> Int {
        let range: Range<String.Index> = settings.waitList[waitIndex].range(of: " ")!
        let spaceIndex: Int = settings.waitList[waitIndex].distance(from: settings.waitList[waitIndex].startIndex, to: range.lowerBound)
        
        let waitTime = settings.waitList[waitIndex].prefix(spaceIndex)
        return Int(waitTime)!
    }
    
    func checkRatePopUp() {
        // if it's fisrt time asking for review, alarm has been set 4 or more times, and it's been 3 or more days since first launch
        if !self.settings.requestedReview
            && self.settings.alarmSetCounter >= 4
            && Date().timeIntervalSince(self.settings.firstLaunchDate) > 60 * 60 * 24 * 3 {
            
            self.settings.requestedReview = true
            self.saveCoreData()
            
            self.showRequestOnOpening = true
        }
    }
    
    func saveCoreData() {
        deleteAllCoreData()
        
        let settingsEntity = SettingsEntity(context: self.moc)
        settingsEntity.alarmDate = self.settings.alarmDate
        settingsEntity.alarmSetCounter = Int16(self.settings.alarmSetCounter + 1)
        settingsEntity.autoOffIndex1 = Int16(self.settings.autoOffIndex1)
        settingsEntity.autoOffIndex2 = Int16(self.settings.autoOffIndex2)
        settingsEntity.firstLaunchDate = self.settings.firstLaunchDate
        settingsEntity.isAlarmScheduled = self.settings.isAlarmScheduled
        settingsEntity.isAlarmSet = self.settings.isAlarmSet
        settingsEntity.modeIndex = Int16(self.settings.modeIndex)
        settingsEntity.requestedReview = self.settings.requestedReview
        settingsEntity.soundIndex1 = Int16(self.settings.soundIndex1)
        settingsEntity.soundIndex2 = Int16(self.settings.soundIndex2)
        settingsEntity.waitIndexForChaining = Int16(self.settings.waitIndexForChaining)
        settingsEntity.waitIndexForRausis = Int16(self.settings.waitIndexForRausis)
        try? self.moc.save()
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
        try? self.moc.save()
    }
}

struct AlarmSetView_Previews: PreviewProvider {
    static var previews: some View {
        AlarmSetView()
            .environmentObject(Settings())
    }
}


