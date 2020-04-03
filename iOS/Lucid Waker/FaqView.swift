//
//  FaqView.swift
//  Lucid Waker
//
//  Created by Harry Cha on 3/21/20.
//  Copyright © 2020 Harry Cha. All rights reserved.
//

import SwiftUI

struct FaqView: View {
    @State private var showAnswer = [Bool]()
    @State private var question = [
        "Why didn't my alarms ring?",
        "Why are my alarms cut short?",
        "What is FILD?",
        "What is Chaining?",
        "What is Rausis?",
        "How do I use this app for FILD?",
        "How do I use this app for Chaining?",
        "How do I use this app for Rausis?",
        "Did this app help you lucid dream personally?",
        "What tools did you use to make this app?",
        "Will you continue updating this app?",
        "Does this app have a privacy policy?",
        "How can I contact you in private?"
    ]
    @State private var answer = [
        "If your alarms didn't ring at all, make sure your device is not in silent mode.\n" +
            "If the alarms still don't ring, please try reinstalling the app and give it the permission to ring when you set your first alarm.\n" +
            "In addition, the alarms might have been terminated by another app or low battery or memory.\n" +
            "In this case, the most reliable course of action to keep the alarms alive over the night is to restart and fully charge your phone and terminate all other unused apps.",
        "If you set long Auto-Off duration and your device's screen was left on, your alarms might have been cut short.\n" +
        "For the alarms to work properly, please turn off your device's screen after you set the alarms.",
        "FILD is an acronym for Finger Induced Lucid Dreaming.\n" +
        "It is a simple lucid dream technique where you wake up about 5 hours into sleep, stay still on bed, and as you slightly and alternately move your right hand's index and middle fingers, go back to sleep while staying conscious.",
        "Chaining mode simply repeats FILD mode.\n" +
            "While FILD mode plays an alarm only once, Chaining mode plays infinite number of alarms with Wait time in-between the consecutive alarms.\n" +
        "What makes Chaining mode useful is that it can be used to attempt FILD mode multiple times in one night, which allows you to have multiple lucid dreams and dream recalls.",
        "Rausis is a lucid dreaming technique developed by a French-speaking researcher named Jean Rausis.\n" +
            "In this technique, you wake up after about 5 hours into sleep, stay on bed, and simply fall asleep again right away.\n" +
            "But a few minutes after, a soft music must be played by your alarm, which you'll hear in your dream to become lucid.\n" +
        "This soft music must be disruptive enough for you to notice it in your dream yet calm enough not to wake yourself up.",
        "To execute the FILD technique using Lucid Waker, select the FILD mode. Set an alarm to ring after 5-6 hours of sleep.\n" +
            "When the alarm rings, wake up but stay still on bed as the alarm turns itself off automatically.\n" +
            "Then slightly but continuously move your index and middle fingers alternately.\n" +
        "This will help you drift back to sleep consciously.",
        "If you select the Chaining mode, Lucid Waker automatically loops the FILD mode with specified Wait minutes in-between each alarm.\n" +
            "This mode gives you multiple chances to attempt FILD and remember dreams in only one night.\n" +
        "This mode will repeat forever until you quit the alarm.",
        "First, set an alarm to wake you up after 5-6 hours of sleep.\n" +
            "After the alarm turns itself off, simply go back to sleep.\n" +
            "After specified Wait minutes, second alarm will ring, which serves as a signal.\n" +
            "As you hear this in your dream, stay asleep and become lucid as the second alarm turns itself off.\n" +
        "It might take a couple trial and error to find an appropriate alarm, Auto-off duration, and Wait Time that will be just right for you personally to have the greatest success rate of lucid dreaming.",
        "This app is simply a mobile application adaptation of a basic program I wrote a few years ago to help myself lucid dream.\n" +
        "So yes, this app is absolutely effective for me personally to lucid dream.",
        "I utilized Swift on Xcode to program this app, and I designed the app with SwiftUI.",
        "Of course! As I take all of your feedback into account, I am definitely planning to continuously update this app by improving and adding more features in the near future!",
        "The answer is a button, not this text.",
        "The answer is a button, not this text."
    ]
    @State private var privacyPolicyButton : Button =
        Button(action: {
            guard let url = URL(string: "https://lucidwaker.pythonanywhere.com") else { return }
            UIApplication.shared.open(url)
        }) {
            Text("I am required to have a privacy policy for this app, which you can read ")
                .foregroundColor(Color.white) +
                Text("here") +
                Text(".\n")
                    .foregroundColor(Color.white) +
                Text("But all it says is that I don't collect any kind of data at all.")
                    .foregroundColor(Color.white)
        }
    @State private var contactButton : Button =
        Button(action: {
            guard let url = URL(string: "mailto:lucidwakerapp@gmail.com") else { return }
            UIApplication.shared.open(url)
        }) {
            Text("For any questions or suggestions, please don't hesitate to reach out to me at ")
                .foregroundColor(Color.white) +
                Text("lucidwakerapp@gmail.com") +
                Text(".\n")
                    .foregroundColor(Color.white) +
                Text("And I would appreciate it if you clarify in your email that you use iOS, not Android.")
                    .foregroundColor(Color.white)
        }

    var body: some View {
        NavigationView {
            Form {
                Section (header: Text("Alarm Issues")) {
                    ForEach(0 ..< 2) { index in
//                        Button(action: {
//                            self.showAnswer[index].toggle()
//                        }) {
                            Text(self.question[index])
                                .foregroundColor(Color.blue)
                                .font(.headline)
//                        }
                        
//                        if index < self.showAnswer.count && self.showAnswer[index] {
                            Text(self.answer[index])
//                        }
                    }
                }
                
                Section (header: Text("Mode Questions")) {
                    ForEach(2 ..< 8) { index in
//                        Button(action: {
//                            self.showAnswer[index].toggle()
//                        }) {
                            Text(self.question[index])
                                .foregroundColor(Color.blue)
                                .font(.headline)
//                        }
                        
//                        if index < self.showAnswer.count && self.showAnswer[index] {
                            Text(self.answer[index])
//                        }
                    }
                }
                
                Section (header: Text("About the Developer")) {
                    ForEach(8 ..< 11) { index in
//                        Button(action: {
//                            self.showAnswer[index].toggle()
//                        }) {
                            Text(self.question[index])
                                .foregroundColor(Color.blue)
                                .font(.headline)
//                        }
                        
//                        if index < self.showAnswer.count && self.showAnswer[index] {
                            Text(self.answer[index])
//                        }
                    }
                    
                    ForEach(11 ..< 12) { index in
//                        Button(action: {
//                            self.showAnswer[index].toggle()
//                        }) {
                            Text(self.question[index])
                                .foregroundColor(Color.blue)
                                .font(.headline)
//                        }
                        
//                        if index < self.showAnswer.count && self.showAnswer[index] {
                            self.privacyPolicyButton
//                        }
                    }
                    
                    ForEach(12 ..< 13) { index in
//                        Button(action: {
//                            self.showAnswer[index].toggle()
//                        }) {
                            Text(self.question[index])
                                .foregroundColor(Color.blue)
                                .font(.headline)
//                        }
                        
//                        if index < self.showAnswer.count && self.showAnswer[index] {
                            self.contactButton
//                        }
                    }
                }
            }
            .navigationBarTitle("FAQ")
            .onAppear {
                UITableView.appearance().separatorStyle = .singleLine // show lines inbetween list items
                
                self.showAnswer = [Bool](repeating: true, count: self.question.count)
                /*
                self.showAnswer = [Bool](repeating: false, count: self.question.count)
                
                if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.pad) { // if it's iPad
                    self.showAnswer = [Bool](repeating: true, count: self.question.count)
                }
                */
            }
        }
        .navigationViewStyle(StackNavigationViewStyle()) // needed so the screen works on iPad
    }
}

struct FaqView_Previews: PreviewProvider {
    static var previews: some View {
        FaqView()
    }
}

