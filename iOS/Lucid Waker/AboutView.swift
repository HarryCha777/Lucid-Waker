//
//  AboutView.swift
//  Lucid Waker
//
//  Created by Harry Cha on 3/21/20.
//  Copyright © 2020 Harry Cha. All rights reserved.
//

import SwiftUI

struct AboutView: View {
    @State private var appVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String
    
    var body: some View {
        NavigationView {
            List {
                Section(header: Text("About the App")) {
                    Text("Lucid Waker is an alarm app designed to assist lucid dreamers who exercise techniques that utilize alarms, such as FILD and Rausis.")
                    Text("App version: \(appVersion!)")
                }
                
                Section(header: Text("About Me")) {
                    Text("Hello, my name is Harry!")
                    Text("I am a college student and have been a lucid dreamer for a couple years.")
                }
                
                Section(header: Text("Why I Made the App")) {
                    Text("Since I wanted to apply my programming skills to my lucid dreaming lifestyle, I developed a simple program that schedules alarms and tones automatically.")
                    Text("And after realizing that I was able to lucid dream more easily using my personalized alarm program, I then decided to turn it into a mobile app in hopes of supporting more lucid dreamers like myself.")
                }
                
                Section(header: Text("Thank You !")) {
                    Text("While this is only my first iOS app, I sincerely wish this alarm would serve its functions and assist its users to lucid dream more conveniently!")
                    Text("I am definitely planning to update it by continuously improving and adding more features in the near future as I take into account all of your feedback!")
                    
                    Button(action: {
                        guard let url = URL(string: "mailto:lucidwakerapp@gmail.com") else { return }
                        UIApplication.shared.open(url)
                    }) {
                        Text("So for any questions or suggestions, please reach out to me at ") +
                            Text("lucidwakerapp@gmail.com")
                                .foregroundColor(Color.blue) +
                            Text(". (Please clarify that you use iOS, not Android.)")
                    }
                    
                    Text("I truly thank you for your interest and taking your valuable time reading about me and my app!")
                }
            }
            .navigationBarTitle("About")
            .onAppear {
                UITableView.appearance().separatorStyle = .none
            }
            .onDisappear {
                UITableView.appearance().separatorStyle = .singleLine
            }
        }
        .navigationViewStyle(StackNavigationViewStyle()) // needed so the screen works on iPad
    }
}

struct AboutView_Previews: PreviewProvider {
    static var previews: some View {
        AboutView()
    }
}

