//
//  ContentView.swift
//  Lucid Waker
//
//  Created by Harry Cha on 3/21/20.
//  Copyright © 2020 Harry Cha. All rights reserved.
//

import SwiftUI

struct ContentView: View {
    @EnvironmentObject var settings: Settings

    @State private var showSoundInfo2 = false
    @State private var showAutoOffInfo1 = false
    @State private var showAutoOffInfo2 = false
    @State private var showModeInfo = false
    @State private var showWaitInfoForChaining = false
    @State private var showWaitInfoForRausis = false
    
    @State private var soundInfo2 = "This is the second alarm used in Rausis.\n\n" +
        "This alarm will ring after specified Wait time following the first alarm.\n\n" +
        "This alarm should be disruptive enough for you to notice it in your dream " +
    "yet calm enough not to wake yourself up."
    @State private var autoOffInfo1 = "This is the number of seconds the alarm will ring.\n\n" +
        "The alarm will then turn itself off automatically.\n\n" +
        "Since you don't need to move after waking up, " +
    "your relaxed body will be in the ideal condition for lucid dreaming."
    @State private var autoOffInfo2 = "This is the number of seconds the second alarm will ring.\n\n" +
        "The alarm will then turn itself off automatically.\n\n" +
        "You need to set it to ring for a time period long enough to cue yourself in your dream " +
    "yet short enough not to wake yourself up."
    @State private var modeInfo = "The app will present additional options depending on the selected mode.\n\n" +
    "To read about how to best use this app to perform FILD, Chaining, and Rausis, please visit the FAQ tab on the bottom."
    @State private var waitInfoForChaining = "This is the number of minutes the app will stay silent " +
        "between the end of previous alarm and start of next alarm.\n\n" +
    "In Chaining mode, it is recommended to give yourself enough time to experience a dream each interval."
    @State private var waitInfoForRausis = "This is the number of minutes the app will stay silent " +
        "between the end of first alarm and start of second alarm.\n\n" +
        "In Rausis mode, you need to set it to wait for a time period " +
    "long enough for you to be asleep again yet short enough not to lose your consciousness."
    
    @State private var firstAlarmTitle = "Congrats, you set your very first alarm!"
    @State private var firstAlarmMessage = "Please make sure both silent mode and this device's screen are turned off for alarms to work properly!"
    
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
    
    @State private var alertId: AlertId?
    private struct AlertId: Identifiable {
        enum Id {
            case
            firstAlarm,
            requestOnOpening,
            requestOnYes,
            requestOnNo
        }
        var id: Id
    }
    
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text(settings.timeLeft)) {
                    DatePicker("Please enter a time", selection: $settings.alarmDate, displayedComponents: .hourAndMinute)
                        .labelsHidden()
                    
                }
                
                Section(header: Text("Sound")) {
                    HStack {
                        Image(systemName: "bell")
                            .imageScale(.large)
                        NavigationLink(destination: SoundView(isSound1: true).environmentObject(settings)) {
                            Text("Play")
                            Spacer()
                            Text(settings.soundList[settings.soundIndex1])
                                .foregroundColor(Color.gray)
                        }
                        .padding(.horizontal)
                    }
                    
                    /*
                    HStack {
                        Image(systemName: "speaker.3")
                            .imageScale(.large)
                        Slider(value: $settings.volume, in: 0...100, step: 1)
                            .padding(.horizontal)
                    }
                    */
                }
                
                Section(header: HStack {
                    Text("Auto-Off")
                    Button(action: {
                        self.showAutoOffInfo1 = true
                    }) {
                        Image(systemName: "questionmark.circle.fill")
                            .imageScale(.large)
                    }
                    .alert(isPresented: $showAutoOffInfo1) {
                        Alert(title: Text("Auto-Off Info"), message: Text(autoOffInfo1), dismissButton: .default(Text("Got it!")))
                    }
                }) {
                    HStack {
                        Image(systemName: "speaker.slash")
                            .imageScale(.large)
                        Picker("Turn silent in", selection: $settings.autoOffIndex1) {
                            ForEach(0 ..< settings.autoOffList.count, id: \.self) {
                                Text(self.settings.autoOffList[$0])
                            }
                        }
                        .padding(.horizontal)
                    }
                }
                
                Section(header: HStack {
                    Text("Mode")
                    Button(action: {
                        self.showModeInfo = true
                    }) {
                        Image(systemName: "questionmark.circle.fill")
                            .imageScale(.large)
                    }
                    .alert(isPresented: $showModeInfo) {
                        Alert(title: Text("Mode Info"), message: Text(modeInfo), dismissButton: .default(Text("Got it!")))
                    }
                }) {
                    Picker("", selection: $settings.modeIndex) {
                        ForEach(0 ..< settings.modeList.count, id: \.self) {
                            Text(self.settings.modeList[$0])
                        }
                    }
                    .pickerStyle(SegmentedPickerStyle())
                }
                
                if settings.modeIndex == 1 {
                    Section(header: HStack {
                        Text("Chaining Wait")
                        Button(action: {
                            self.showWaitInfoForChaining = true
                        }) {
                            Image(systemName: "questionmark.circle.fill")
                                .imageScale(.large)
                        }
                        .alert(isPresented: $showWaitInfoForChaining) {
                            Alert(title: Text("Chaining Wait Info"), message: Text(waitInfoForChaining), dismissButton: .default(Text("Got it!")))
                        }
                    }) {
                        HStack {
                            Image(systemName: "stopwatch")
                                .imageScale(.large)
                            Picker("Stay silent for", selection: $settings.waitIndexForChaining) {
                                ForEach(0 ..< settings.waitList.count, id: \.self) {
                                    Text(self.settings.waitList[$0])
                                }
                            }
                            .padding(.horizontal)
                        }
                    }
                }
                
                if settings.modeIndex == 2 {
                    Section(header: HStack {
                        Text("Rausis Wait")
                        Button(action: {
                            self.showWaitInfoForRausis = true
                        }) {
                            Image(systemName: "questionmark.circle.fill")
                                .imageScale(.large)
                        }
                        .alert(isPresented: $showWaitInfoForRausis) {
                            Alert(title: Text("Rausis Wait Info"), message: Text(waitInfoForRausis), dismissButton: .default(Text("Got it!")))
                        }
                    }) {
                        HStack {
                            Image(systemName: "stopwatch")
                                .imageScale(.large)
                            Picker("Stay silent for", selection: $settings.waitIndexForRausis) {
                                ForEach(0 ..< settings.waitList.count, id: \.self) {
                                    Text(self.settings.waitList[$0])
                                }
                            }
                            .padding(.horizontal)
                        }
                    }
                    
                    Section(header: HStack {
                        Text("Second Sound")
                        Button(action: {
                            self.showSoundInfo2 = true
                        }) {
                            Image(systemName: "questionmark.circle.fill")
                                .imageScale(.large)
                        }
                        .alert(isPresented: $showSoundInfo2) {
                            Alert(title: Text("Second Sound Info"), message: Text(soundInfo2), dismissButton: .default(Text("Got it!")))
                        }
                    }) {
                        HStack {
                            Image(systemName: "bell")
                                .imageScale(.large)
                            NavigationLink(destination: SoundView(isSound1: false).environmentObject(settings)) {
                                Text("Then play")
                                Spacer()
                                Text(settings.soundList[settings.soundIndex2])
                                    .foregroundColor(Color.gray)
                            }
                            .padding(.horizontal)
                        }
                    }
                    
                    Section(header: HStack {
                        Text("Second Auto-Off")
                        Button(action: {
                            self.showAutoOffInfo2 = true
                        }) {
                            Image(systemName: "questionmark.circle.fill")
                                .imageScale(.large)
                        }
                        .alert(isPresented: $showAutoOffInfo2) {
                            Alert(title: Text("Second Auto-Off Info"), message: Text(autoOffInfo2), dismissButton: .default(Text("Got it!")))
                        }
                    }) {
                        HStack {
                            Image(systemName: "speaker.slash")
                                .imageScale(.large)
                            Picker("Turn silent in", selection: $settings.autoOffIndex2) {
                                ForEach(0 ..< settings.autoOffList.count, id: \.self) {
                                    Text(self.settings.autoOffList[$0])
                                }
                            }
                            .padding(.horizontal)
                        }
                    }
                }
                
                Section {
                    Button(action: {
                        if self.settings.alarmSetCounter == 0 {
                            self.alertId = AlertId(id: .firstAlarm)
                        } else {
                            self.settings.isAlarmSet = true
                        }
                        self.settings.alarmSetCounter += 1
                    }) {
                        Text("Set the Alarm !")
                    }
                    // show alerts on content view instead of alarm set view
                    // because app crashes for unknown reason when a pop-up shows up while set alarm time is over approximately 12 hours from now
                    .alert(item: $alertId) { alert in
                        switch alert.id {
                        case .firstAlarm:
                            return Alert(title: Text(firstAlarmTitle), message: Text(firstAlarmMessage), dismissButton: .default(Text("OK"), action: {
                                self.settings.isAlarmSet = true
                            }))
                        case .requestOnOpening:
                            return Alert(title: Text(requestOnOpeningTitle), message: Text(requestOnOpeningMessage), primaryButton: .destructive(Text(requestOnOpeningSayNo), action: {
                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) { // very short delay is needed
                                    self.alertId = AlertId(id: .requestOnNo)
                                }
                            }), secondaryButton: .default(Text(requestOnOpeningSayYes), action: {
                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) { // very short delay is needed
                                    self.alertId = AlertId(id: .requestOnYes)
                                }
                            }))
                        case .requestOnYes:
                            return Alert(title: Text(requestOnYesTitle), message: Text(requestOnYesMessage), primaryButton: .default(Text(requestAfterOpeningSayYes), action: {
                                self.linkToReview()
                            }), secondaryButton: .destructive(Text(requestAfterOpeningSayNo)))
                        case .requestOnNo:
                            return Alert(title: Text(requestOnNoTitle), message: Text(requestOnNoMessage), primaryButton: .default(Text(requestAfterOpeningSayYes), action: {
                                self.linkToReview()
                            }), secondaryButton: .destructive(Text(requestAfterOpeningSayNo)))
                        }
                    }
                }
            }
            .navigationBarTitle("Lucid Waker")
            .onAppear {
                UITableView.appearance().separatorStyle = .singleLine // show lines inbetween list items

                if !self.settings.timeLeftUpdating {
                    self.settings.startUpdatingTimeLeft()
                }
                
                let soundView = SoundView(settings: self._settings, isSound1: true)
                soundView.stopSound()
                
                self.checkRatePopUp()
            }
        }
        .navigationViewStyle(StackNavigationViewStyle()) // needed so pickers don't jump in position right after nagivation
    }

    func checkRatePopUp() {
        // if it's first time asking for review, alarm has been set 4 or more times, and it's been 3 or more days since first launch
        if !self.settings.requestedReview
            && self.settings.alarmSetCounter >= 4
            && Date().timeIntervalSince(self.settings.firstLaunchDate) > 60 * 60 * 24 * 3 {
            self.settings.requestedReview = true
            self.alertId = AlertId(id: .requestOnOpening)
        }
    }
    
    func linkToReview() {
        guard let productURL = URL(string: "https://apps.apple.com/app/id1505570242") else { return }
        
        var components = URLComponents(url: productURL, resolvingAgainstBaseURL: false)
        components?.queryItems = [
            URLQueryItem(name: "action", value: "write-review")
        ]
        
        guard let writeReviewURL = components?.url else { return }
        UIApplication.shared.open(writeReviewURL)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
            .environmentObject(Settings())
    }
}


