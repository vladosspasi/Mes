package com.github.vladosspasi.mes.Settings.Devices;

import android.content.ContentValues;

import java.util.ArrayList;

public class GlobalDeviceInfo {

    private static ContentValues deviceData;
    private static ArrayList<ContentValues> scales;

    public static ContentValues getDeviceData() {
        return deviceData;
    }

    public static void setDeviceData(ContentValues newDeviceData) {
        deviceData = newDeviceData;
    }

    public static ArrayList<ContentValues> getScales() {
        return scales;
    }

    public static void setScales(ArrayList<ContentValues> newScales) {
        scales = newScales;
    }

    public static void setScaleAt(int pos, ContentValues scale){
        scales.set(pos, scale);
    }

    public static ContentValues getScaleAt(int pos){
        return scales.get(pos);
    }

    public static void clear(){
        deviceData = new ContentValues();
        scales = new ArrayList<>();
    }
}
