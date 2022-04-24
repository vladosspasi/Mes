package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.content.ContentValues;

import java.util.ArrayList;

public class MeasurementGlobalInfo {

    private static String mesName;
    private static String mesComment;
    private static ArrayList<ContentValues> scalesList;
    private static int templateId = -1;

    public static int getTemplateId() {
        return templateId;
    }

    public static void setTemplateId(int templateId) {
        MeasurementGlobalInfo.templateId = templateId;
    }

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

    public static ArrayList<ContentValues> getScalesList() {
        return scalesList;
    }

    public static void setScalesList(ArrayList<ContentValues> scalesList) {
        MeasurementGlobalInfo.scalesList = scalesList;
    }

    public static void addToScalesList(ContentValues newScale){
        scalesList.add(newScale);
    }

    public static void clearAll(){
        mesName = "";
        mesComment = "";
        scalesList = new ArrayList<>();
        templateId = -1;
    }
}
