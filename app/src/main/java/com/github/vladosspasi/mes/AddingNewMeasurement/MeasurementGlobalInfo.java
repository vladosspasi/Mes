package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.content.ContentValues;

import java.util.ArrayList;

public class MeasurementGlobalInfo {

    private static String mesName;
    private static String mesComment;
    private static ArrayList<Integer> scalesIds;
    private static ArrayList<String> values;

    public static String getMesName() {
        return mesName;
    }

    public static void setMesName(String mesName) {
        MeasurementGlobalInfo.mesName = mesName;
    }

    public static String getMesComment() {
        return mesComment;
    }

    public static void setMesComment(String mesComment) {
        MeasurementGlobalInfo.mesComment = mesComment;
    }

    public static ArrayList<Integer> getScalesIds() {
        if(scalesIds==null) scalesIds = new ArrayList<>();
        return scalesIds;
    }

    public static void setScalesIds(ArrayList<Integer> scalesIds) {
        if(scalesIds==null) scalesIds = new ArrayList<>();
        MeasurementGlobalInfo.scalesIds = scalesIds;
    }

    public static ArrayList<String> getValues() {
        if(values==null) values = new ArrayList<>();
        return values;
    }

    public static void setValues(ArrayList<String> values) {
        if(values==null) values = new ArrayList<>();
        MeasurementGlobalInfo.values = values;
    }

    public static void clearAll(){
        mesName = new String();
        mesComment = new String();
        scalesIds = new ArrayList<>();
        values = new ArrayList<>();
    }
}
