//
//  Settings.swift
//  Lucid Waker
//
//  Created by Harry Cha on 3/22/20.
//  Copyright © 2020 Harry Cha. All rights reserved.
//

import SwiftUI
import Foundation
import AVFoundation

class Settings: ObservableObject {
    @Published var firstLaunchDate = Date()
    @Published var alarmSetCounter = 0
    @Published var requestedReview = false

    @Published var timeLeft = ""
    @Published var timeLeftUpdating = false
    @Published var alarmDate = defaultAlarmDate {
        didSet {
            updateTimeLeft()
        }
    }
    
    @Published var soundList = ["Punky", "Happy Rock", "Extreme Action", "Dub Step", "Rumble", "New Beginning", "Summer", "Ukulele", "Creative Minds", "Rain", "Meditation", "Binaural Beats"]
    @Published var soundIndex1 = 0
    @Published var soundIndex2 = 5
    
    @Published var avAudioPlayer : AVAudioPlayer?
    //@Published var volume : Double = 90
    
    @Published var autoOffList = ["1 Second", "3 Seconds", "5 Seconds", "10 Seconds", "15 Seconds", "30 Seconds"]
    @Published var autoOffIndex1 = 4
    @Published var autoOffIndex2 = 3
    
    @Published var modeList = ["FILD", "Chaining", "Rausis"]
    @Published var modeIndex = 1
    
    @Published var waitList = ["1 Minute", "5 Minutes", "10 Minutes", "20 Minutes", "30 Minutes", "45 Minutes", "60 Minutes", "90 Minutes"]
    @Published var waitIndexForChaining = 5
    @Published var waitIndexForRausis = 2
    
    @Published var isAlarmSet = false
    @Published var isAlarmScheduled = false
    
    static var defaultAlarmDate: Date {
        var components = DateComponents()
        components.hour = 6
        components.minute = 0
        return Calendar.current.date(from: components) ?? Date()
    }
    
    func startUpdatingTimeLeft() {
        timeLeftUpdating = true
        updateTimeLeft()
        
        Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(updateTimeLeft), userInfo: nil, repeats: true)
    }
    
    @objc func updateTimeLeft() {
        let numberOfPendingRequests = UIApplication.shared.scheduledLocalNotifications?.count
        if isAlarmScheduled { // check if alarm scheduling is finished and the first alarm already rang
            if (modeIndex == 0 && numberOfPendingRequests! <= 1 - 1) || // FILD
                (modeIndex == 1 && numberOfPendingRequests! <= 11 - 1) || // Chaining
                (modeIndex == 2 && numberOfPendingRequests! <= 2 - 1) { // Rausis
                timeLeft = "The Alarm Began !"
                return
            }
        }
        
        let currHour = Calendar.current.component(.hour, from: Date())
        let currMin = Calendar.current.component(.minute, from: Date())
        
        let setHour = Calendar.current.component(.hour, from: alarmDate)
        let setMin = Calendar.current.component(.minute, from: alarmDate)
        
        let currTotalMin = currHour * 60 + currMin
        let setTotalMin = setHour * 60 + setMin
        
        var diffMin = -1
        if currTotalMin <= setTotalMin {
            diffMin = setTotalMin - currTotalMin
        } else {
            diffMin = 24 * 60 - (currTotalMin - setTotalMin)
        }
        
        timeLeft = "In ";
        if (diffMin == 0) {
            timeLeft += "24 Hours"
        }
        if (diffMin == 1) {
            timeLeft += "less than a Minute"
        }
        if (diffMin / 60 == 1) {
            timeLeft += String(diffMin / 60) + " Hour"
        }
        if (diffMin / 60 > 1) {
            timeLeft += String(diffMin / 60) + " Hours"
        }
        if (diffMin != 1 && diffMin / 60 >= 1 && diffMin % 60 >= 1) {
            timeLeft += " and "
        }
        if (diffMin != 1 && diffMin % 60 == 1) {
            timeLeft += String(diffMin % 60) + " Minute"
        }
        if (diffMin % 60 > 1) {
            timeLeft += String(diffMin % 60) + " Minutes"
        }
    }
}

