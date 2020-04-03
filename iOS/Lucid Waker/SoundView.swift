//
//  SoundView.swift
//  Lucid Waker
//
//  Created by Harry Cha on 3/23/20.
//  Copyright © 2020 Harry Cha. All rights reserved.
//

import SwiftUI
import AVFoundation

struct SoundView: View {
    @EnvironmentObject var settings: Settings
    @State var isSound1: Bool
    
    @State private var isOn = [Bool]()
    
    var body: some View {
        Form {
            Group {
                Section (header: Text("Exciting Music")) {
                    ForEach(0 ..< settings.soundList.count / 2) { index in
                        self.displayButtons(index: index)
                    }
                }
                
                Section (header: Text("Soft Tunes")) {
                    ForEach(settings.soundList.count / 2 ..< settings.soundList.count) { index in
                        self.displayButtons(index: index)
                    }
                }
            }
            .onAppear {
                UITableView.appearance().separatorStyle = .singleLine // show lines inbetween list items
                
                self.isOn = [Bool](repeating: false, count: self.settings.soundList.count)
                if self.isSound1 {
                    self.isOn[self.settings.soundIndex1] = true
                } else {
                    self.isOn[self.settings.soundIndex2] = true
                }
            }
        }
        .navigationBarTitle("Sound", displayMode: .inline)
    }
    
    func displayButtons(index: Int) -> some View {
        return Button(action: {
            self.isOn = [Bool](repeating: false, count: self.settings.soundList.count)
            self.isOn[index] = true//.toggle()
            
            if self.isSound1 {
                self.settings.soundIndex1 = index
            } else {
                self.settings.soundIndex2 = index
            }
            
            let alarmSetView = AlarmSetView(settings: self._settings)
            let soundFileName = alarmSetView.getSoundFileName(soundIndex: index, autoOffIndex: self.settings.autoOffList.count - 1)
            let path = Bundle.main.path(forResource: soundFileName, ofType:nil)!
            let url = URL(fileURLWithPath: path)
            
            do {
                self.settings.avAudioPlayer = try AVAudioPlayer(contentsOf: url)
                self.settings.avAudioPlayer?.play()
                self.settings.avAudioPlayer?.numberOfLoops = -1 // loop forever
            } catch {}
        }) {
            HStack {
                Text(self.settings.soundList[index])
                    .foregroundColor(Color.white)
                Spacer()
                if index < self.isOn.count && self.isOn[index] {
                    ZStack {
                        Circle()
                            .fill(Color.blue)
                            .frame(width: 20, height: 20)
                        Circle()
                            .fill(Color.white)
                            .frame(width: 8, height: 8)
                    }
                } else {
                    Circle()
                        .fill(Color.white)
                        .frame(width: 20, height: 20)
                        .overlay(Circle().stroke(Color.gray, lineWidth: 1))
                }
            }
        }
    }
    
    func stopSound() {
        settings.avAudioPlayer?.stop()
    }
}

struct SoundView_Previews: PreviewProvider {
    static var previews: some View {
        SoundView(isSound1: true)
            .environmentObject(Settings())
    }
}

