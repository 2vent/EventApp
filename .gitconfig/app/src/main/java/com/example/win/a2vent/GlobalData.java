package com.example.win.a2vent;

/**
 * Created by win on 2017-07-24.
 */

public class GlobalData {
    private final static String URL = "http://121.151.63.53:8080/EventApp/";
    private static String Login_ID;

    public static String getURL() {
        return URL;
    }

    public static String getLogin_ID () {
        return Login_ID;
    }

    public static void setLogin_ID(String id) {
        Login_ID = id;
    }

}
