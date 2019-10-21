package com.harry.lucidwaker;

import com.unity3d.player.UnityPlayer;

// This is an extention of UnitySendMessage so that UnitySendMessage can return a string as a response.
public final class MyUnityPlugin {
    //Make class static variable so that the callback function is sent to one instance of this class.
    public static MyUnityPlugin testInstance;

    public static MyUnityPlugin instance() {
        if(testInstance == null)
            testInstance = new MyUnityPlugin();
        return testInstance;
    }

    static String result = "";
    public static String UnitySendMessageExtension(String gameObject, String functionName, String funcParam) {
        UnityPlayer.UnitySendMessage(gameObject, functionName, funcParam);
        String tempResult = result;
        return tempResult;
    }

    //Receives result from C# and saves it to result variable
    void receiveResult(String value) {
        result = ""; //Clear old data
        result = value; //Get new one
    }
}