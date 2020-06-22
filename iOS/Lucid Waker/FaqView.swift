//
//  FaqView.swift
//  Lucid Waker
//
//  Created by Harry Cha on 3/21/20.
//  Copyright © 2020 Harry Cha. All rights reserved.
//

import SwiftUI

struct FaqView: View {
    @State private var sectionHeaders = [
        "Alarm Issues",
        "Mode Questions",
        "About the Developer"
    ]
    @State private var questions = [
        "Why didn't my alarms ring?",
        "Why are my alarms cut short?",
        "What is FILD?",
        "What is Chaining?",
        "What is Rausis?",
        "How do I use this app for FILD?",
        "How do I use this app for Chaining?",
        "How do I use this app for Rausis?",
        "Did this app help you lucid dream personally?",
        "Will you continue updating this alarm app?",
        "What tools did you use to make this app?",
        "Does this app have a privacy policy?",
        "How can I contact you in private?"
    ]

    var body: some View {
        NavigationView {
            Form {
                Section (header: Text(sectionHeaders[0])) {
                    ForEach(0 ..< 2) { index in
                        NavigationLink(destination: FaqAnswerView(sectionHeader: self.sectionHeaders[0], question: self.questions[index], answerIndex: index)) {
                            Text(self.questions[index])
                                .foregroundColor(Color.blue)
                        }
                    }
                }
                
                Section (header: Text(sectionHeaders[1])) {
                    ForEach(2 ..< 8) { index in
                        NavigationLink(destination: FaqAnswerView(sectionHeader: self.sectionHeaders[1], question: self.questions[index], answerIndex: index)) {
                            Text(self.questions[index])
                                .foregroundColor(Color.blue)
                        }
                    }
                }
                
                Section (header: Text(sectionHeaders[2])) {
                    ForEach(8 ..< 13) { index in
                        NavigationLink(destination: FaqAnswerView(sectionHeader: self.sectionHeaders[2], question: self.questions[index], answerIndex: index)) {
                            Text(self.questions[index])
                                .foregroundColor(Color.blue)
                        }
                    }
                }
            }
            .navigationBarTitle("FAQ")
            .onAppear {
                UITableView.appearance().separatorStyle = .singleLine // show lines inbetween list items
            }
        }
        .navigationViewStyle(StackNavigationViewStyle()) // needed so the screen works on iPad
    }
}

struct FaqAnswerView: View {
    @State var sectionHeader : String
    @State var question : String
    @State var answerIndex : Int
    @State private var answers = [
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
        "Of course! As I take all of your feedback into account, I am definitely planning to continuously update this app by improving and adding more features in the near future!",
        "The answer is a button, not this text.",
        "The answer is a button, not this text.",
        "The answer is a button, not this text."
    ]
    @State private var interactiveSwiftUIButton : Button =
        Button(action: {
            guard let url = URL(string: "https://apps.apple.com/app/id1511793071") else { return }
            UIApplication.shared.open(url)
        }) {
            Text("I utilized Swift on Xcode to program this app, and I designed the app with SwiftUI.\n" +
                "Actually, I have recently published an app where you can learn SwiftUI interactively, and it contains many source code of this app.\n" +
                "So if you are interested in making an app similar to this, please check out the app by clicking ") +
                Text("here")
                    .foregroundColor(Color.blue) +
                Text(".")
    }
    @State private var privacyPolicyButton : Button =
        Button(action: {
            guard let url = URL(string: "https://lucidwaker.pythonanywhere.com") else { return }
            UIApplication.shared.open(url)
        }) {
            Text("I am required to have a privacy policy for this app, which you can read ") +
                Text("here")
                    .foregroundColor(Color.blue) +
                Text(".\n") +
                Text("But all it says is that I don't collect any kind of data at all.")
    }
    @State private var contactButton : Button =
        Button(action: {
            guard let url = URL(string: "mailto:lucidwakerapp@gmail.com") else { return }
            UIApplication.shared.open(url)
        }) {
            Text("For any questions or suggestions, please don't hesitate to reach out to me at ") +
                Text("lucidwakerapp@gmail.com")
                    .foregroundColor(Color.blue) +
                Text(".\n") +
                Text("And I would appreciate it if you clarify in your email that you use iOS, not Android.")
    }

    var body: some View {
            Form {
                Text(question)
                    .fontWeight(.heavy)
                if answerIndex < 10 {
                    Text(answers[answerIndex])
                }
                if answerIndex == 10 {
                    interactiveSwiftUIButton
                        .buttonStyle(PlainButtonStyle())
                }
                if answerIndex == 11 {
                    privacyPolicyButton
                        .buttonStyle(PlainButtonStyle())
                }
                if answerIndex == 12 {
                    contactButton
                        .buttonStyle(PlainButtonStyle())
                }
            }
            .navigationBarTitle("\(sectionHeader)", displayMode: .inline)
    }
}

struct FaqView_Previews: PreviewProvider {
    static var previews: some View {
        FaqView()
    }
}
