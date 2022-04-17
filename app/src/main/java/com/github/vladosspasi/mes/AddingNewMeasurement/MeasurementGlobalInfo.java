package com.github.vladosspasi.mes.AddingNewMeasurement;

import java.util.ArrayList;

public class MeasurementGlobalInfo {

    private static String mesName;
    private static String mesComment;
    private static ArrayList<Integer> scalesIds;

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

    public static void clearAll(){
        mesName = "";
        mesComment = "";
        scalesIds = new ArrayList<>();
    }
}
