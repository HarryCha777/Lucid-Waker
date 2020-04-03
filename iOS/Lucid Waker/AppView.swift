//
//  AppView.swift
//  Lucid Waker
//
//  Created by Harry Cha on 3/21/20.
//  Copyright © 2020 Harry Cha. All rights reserved.
//

import SwiftUI

struct AppView: View {
    var body: some View {
        TabView {
            ContentView()
                .tabItem {
                    Image(systemName: "alarm")
                        .imageScale(.large)
                    Text("Alarm")
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
    }
}

struct AppView_Previews: PreviewProvider {
    static var previews: some View {
        AppView()
            .environmentObject(Settings())
            .edgesIgnoringSafeArea(.top)
    }
}


