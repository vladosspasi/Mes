package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.content.ContentValues;
import java.util.ArrayList;

/**
 * Класс для хранения информации о измерении, информация о котором в данный момент заполняется
 */
public class MeasurementGlobalInfo {

    private static String mesName; //название
    private static String mesComment; //комментарий
    private static ArrayList<ContentValues> scalesList; //список шкал
    private static int templateId = -1; //айди примененного шаблона

    //Процедуры получения и установки данных, указанных выше
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

    //Процедура очистки данных
    public static void clearAll(){
        mesName = "";
        mesComment = "";
        scalesList = new ArrayList<>();
        templateId = -1;
    }
}
