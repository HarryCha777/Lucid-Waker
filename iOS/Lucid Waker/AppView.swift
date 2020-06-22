//
//  AppView.swift
//  Lucid Waker
//
//  Created by Harry Cha on 3/21/20.
//  Copyright © 2020 Harry Cha. All rights reserved.
//

import SwiftUI

struct AppView: View {
    @EnvironmentObject var settings: Settings
    @FetchRequest(entity: SettingsEntity.entity(), sortDescriptors: []) var settingsEntityList: FetchedResults<SettingsEntity>
    
    var body: some View {
        TabView {
            if !settings.isAlarmSet {
                ContentView()
                    .tabItem {
                        Image(systemName: "alarm")
                            .imageScale(.large)
                        Text("Alarm")
                }
            } else {
                AlarmSetView()
                    .tabItem {
                        Image(systemName: "alarm")
                            .imageScale(.large)
                        Text("Alarm")
                }
            }

            FaqView()
                .tabItem {
                    Image(systemName: "questionmark.circle")
                        .imageScale(.large)
                    Text("FAQ")
            }
            
            ResourcesView()
                .tabItem {
                    Image(systemName: "folder")
                        .imageScale(.large)
                    Text("Resources")
            }
            
            AboutView()
                .tabItem {
                    Image(systemName: "person")
                        .imageScale(.large)
                    Text("About")
            }
        }
        .onAppear {
            self.fetchCoreData()
        }
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
    }
}

struct AppView_Previews: PreviewProvider {
    static var previews: some View {
        AppView()
            .environmentObject(Settings())
    }
}


