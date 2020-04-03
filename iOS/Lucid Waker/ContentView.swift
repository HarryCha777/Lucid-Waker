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
    @FetchRequest(entity: SettingsEntity.entity(), sortDescriptors: []) var settingsEntityList: FetchedResults<SettingsEntity>
    @State private var fetchedCoreData = false

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
    
    var body: some View {
        NavigationView {
            if settings.isAlarmSet {
                AlarmSetView().environmentObject(settings)
            }
            
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
                        self.settings.isAlarmSet = true
                        self.settings.alarmSetCounter += 1
                    }) {
                        Text("Set the Alarm !")
                    }
                }
            }
            .navigationBarTitle("Lucid Waker")
            .onAppear {
                UITableView.appearance().separatorStyle = .singleLine // show lines inbetween list items
                
                if !self.fetchedCoreData {
                    self.fetchCoreData()
                }

                if !self.settings.timeLeftUpdating {
                    self.settings.startUpdatingTimeLeft()
                }
                
                let soundView = SoundView(settings: self._settings, isSound1: true)
                soundView.stopSound()
            }
        }
        .navigationViewStyle(StackNavigationViewStyle()) // needed so pickers don't jump in position right after nagivation
    }

    func fetchCoreData() {
        if self.settingsEntityList.count == 0 {
            return
        }
        
        let settingsEntity = settingsEntityList[0]
        self.settings.alarmDate = settingsEntity.alarmDate ?? Date()
        self.settings.alarmSetCounter = Int(settingsEntity.alarmSetCounter)
        self.settings.autoOffIndex1 = Int(settingsEntity.autoOffIndex1)
        self.settings.autoOffIndex2 = Int(settingsEntity.autoOffIndex2)
        self.settings.firstLaunchDate = settingsEntity.firstLaunchDate ?? Date()
        self.settings.isAlarmScheduled = settingsEntity.isAlarmScheduled
        self.settings.isAlarmSet = settingsEntity.isAlarmSet
        self.settings.modeIndex = Int(settingsEntity.modeIndex)
        self.settings.requestedReview = settingsEntity.requestedReview
        self.settings.soundIndex1 = Int(settingsEntity.soundIndex1)
        self.settings.soundIndex2 = Int(settingsEntity.soundIndex2)
        self.settings.waitIndexForChaining = Int(settingsEntity.waitIndexForChaining)
        self.settings.waitIndexForRausis = Int(settingsEntity.waitIndexForRausis)
        self.fetchedCoreData = true
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
            .environmentObject(Settings())
    }
}


