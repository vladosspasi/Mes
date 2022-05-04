package com.github.vladosspasi.mes.Settings.Devices;

import android.content.ContentValues;
import java.util.ArrayList;

/**
 * Класс для хранения информации о приборе, который в данный момент редактируется
 */

public class GlobalDeviceInfo {

    private static ContentValues deviceData; //Контейнер с данными о приборе
    private static ArrayList<ContentValues> scales; //Список шкал прибора

    //Процедуры получения и установки значений полей выше
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

    //Очистка данных
    public static void clear(){
        deviceData = new ContentValues();
        scales = new ArrayList<>();
    }
}
