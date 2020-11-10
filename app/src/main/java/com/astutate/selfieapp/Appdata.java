package com.astutate.selfieapp;

public class Appdata {


    //since the api requires api level 19 or plus for data interference between classes, we use this class so that it can work for any level
    private static String encodedData;
    public Appdata() {}
    public static String getEncodedData() {
        return encodedData;
    }
    public static void setEncodedData(String encodedData_) {
        encodedData = encodedData_;
    }
}
