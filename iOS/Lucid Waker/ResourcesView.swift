//
//  ResourcesView.swift
//  Lucid Waker
//
//  Created by Harry Cha on 3/21/20.
//  Copyright © 2020 Harry Cha. All rights reserved.
//

import SwiftUI

struct ResourcesView: View {
    var body: some View {
        NavigationView {
            List {
                Section {
                    Text("The following is a list of lucid dreaming resources that I personally used and recommend.")
                        .font(.headline)
                }
                
                Section(header: Text("Exploring the World of Lucid Dreaming")) {
                    Text("Exploring the World of Lucid Dreaming is a book written by Stephen LaBerge, a scientist and researcher with PhD from Stanford University, in 1990.\n" +
                        "As many consider this to be the Bible of lucid dreaming, simply reading this book everyday can get its readers into the mindset of lucid dreaming.\n" +
                        "I strongly recommend this book to all lucid dreamers!")
                    
                    Button(action: {
                        guard let url = URL(string: "https://amazon.com/Exploring-World-Dreaming-Stephen-LaBerge/dp/034537410X") else { return }
                        UIApplication.shared.open(url)
                    }) {
                        Text("Click here to check out the book.")
                            .foregroundColor(Color.blue)
                    }
                }
                
                Section(header: Text("How to Lucid")) {
                    Text("As a website managed by Stefan, How to Lucid is a website is very beginner-friendly and personal.\n" +
                        "I highly recommend checking out his website as it contains many informative articles and useful tips and suggestions on lucid dreaming.")
                    
                    Button(action: {
                        guard let url = URL(string: "https://howtolucid.com") else { return }
                        UIApplication.shared.open(url)
                    }) {
                        Text("Click here to visit How to Lucid.")
                            .foregroundColor(Color.blue)
                    }
                }
                
                Section(header: Text("Lucid Dream Society")) {
                    Text("Lucid Dream Society is an online community run by Merilin, a lucid dream enthusiast.\n" +
                        "It is a home of numerous beneficial suggestions and free educational materials to learn from.\n" +
                        "Therefore, I personally recommend reading its articles, especially for the lucid dreaming novices.")
                    
                    Button(action: {
                        guard let url = URL(string: "https://luciddreamsociety.com") else { return }
                        UIApplication.shared.open(url)
                    }) {
                        Text("Click here to visit Lucid Dream Society.")
                            .foregroundColor(Color.blue)
                    }
                }
                
                Section {
                    Spacer()
                }
                
                Section {
                    Text("The following is a list of credit for all sounds used in this app.")
                        .font(.headline)
                }
                
                Section(header: Text("Music")) {
                    Text("Bensound\n" +
                        "License: Free License with Attribution")
                    
                    Button(action: {
                        guard let url = URL(string: "https://www.bensound.com/royalty-free-music") else { return }
                        UIApplication.shared.open(url)
                    }) {
                        Text("https://www.bensound.com/royalty-free-music")
                            .foregroundColor(Color.blue)
                    }
                }
                
                Section(header: Text("Binaural Beats")) {
                    Text("12000 Hz Etheric Force⎪10000 Hz Full Restore⎪Whole Being Regeneration⎪DNA Stimulation⎪Slow Trance\n" +
                        "By Lovemotives Meditation Music\n" +
                        "License: Creative Commons Attribution license (reuse allowed)")
                    
                    Button(action: {
                        guard let url = URL(string: "https://www.youtube.com/watch?v=jKo_Xhoxdak") else { return }
                        UIApplication.shared.open(url)
                    }) {
                        Text("https://www.youtube.com/watch?v=jKo_Xhoxdak")
                            .foregroundColor(Color.blue)
                    }
                }
            }
            .navigationBarTitle("Resources")
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

struct ResourcesView_Previews: PreviewProvider {
    static var previews: some View {
        ResourcesView()
    }
}

