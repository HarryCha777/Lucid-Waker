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
    
    var body: some View {
        VStack {
            Spacer()
                .frame(height: 50)
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
        }
        .onAppear {
            if !self.settings.isAlarmScheduled {
                self.schedule()
            }
            if !self.settings.timeLeftUpdating {
                self.settings.startUpdatingTimeLeft()
            }
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
}

struct AlarmSetView_Previews: PreviewProvider {
    static var previews: some View {
        AlarmSetView()
            .environmentObject(Settings())
    }
}


