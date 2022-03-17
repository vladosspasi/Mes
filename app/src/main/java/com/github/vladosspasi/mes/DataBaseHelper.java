package com.github.vladosspasi.mes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {
    //экземпляр объекта (синглтон)
    private static DataBaseHelper sInstance;
    //название дб
    private static final String DATABASE_NAME = "mesdb";
    //версия дб
    private static final int DATABASE_VERSION = 1;

    //Константы-названия таблиц и полей
    //Таблица Измерений
    public static final String TABLE_MES_NAME = "measurements";
    public static final String FIELD_MES_ID = "_id";
    public static final String FIELD_MES_NAME = "name";
    public static final String FIELD_MES_COMMENT = "comment";
    public static final String FIELD_MES_DEVICEID = "deviceId";
    public static final String FIELD_MES_VALUEID = "valueId";
    public static final String FIELD_MES_DATE = "date";
    //Таблица приборов
    public static final String TABLE_DEVICES_NAME = "devices";
    public static final String FIELD_DEVICES_NAME = "name";
    public static final String FIELD_DEVICES_COMMENT = "comment";
    public static final String FIELD_DEVICES_ID = "_id";
    public static final String FIELD_DEVICES_SCALEID = "scaleId";
    public static final String FIELD_DEVICES_TYPEID = "typeId";
    //Таблица типов приборов
    public static final String TABLE_DEVICETYPES_NAME = "deviceTypes";
    public static final String FIELD_DEVICETYPES_ID = "_id";
    public static final String FIELD_DEVICETYPES_NAME = "name";
    public static final String FIELD_DEVICETYPES_DEFAULTSCALEID = "defScaleId";
    //Таблица величин
    public static final String TABLE_VALUES_NAME = "vals";
    public static final String FIELD_VALUES_ID = "_id";
    public static final String FIELD_VALUES_NAME = "name";
    public static final String FIELD_VALUES_VALUE = "value";
    public static final String FIELD_VALUES_SCALEID = "scaleId";
    //Таблица типов величин
    public static final String TABLE_VALUETYPES_NAME = "valueType";
    public static final String FIELD_VALUETYPES_NAME = "name";
    public static final String FIELD_VALUETYPES_ID = "_id";
    //Таблица шкал
    public static final String TABLE_SCALES_NAME = "scales";
    public static final String FIELD_SCALES_ID = "_id";
    public static final String FIELD_SCALES_NAME = "name";
    public static final String FIELD_SCALES_UNIT = "unit";
    public static final String FIELD_SCALES_VALUETYPEID = "valueTypeId";
    public static final String FIELD_SCALES_MINVALUE = "minvalue";
    public static final String FIELD_SCALES_MAXVALUE = "maxvalue";
    public static final String FIELD_SCALES_ERROR = "error";

    //Конструктор класса
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Действие при первом открытии (создании) базы данных
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Создание таблицы типов величин
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_VALUETYPES_NAME+"(" +
                FIELD_VALUETYPES_ID +" INTEGER PRIMARY KEY, " +
                FIELD_VALUETYPES_NAME +" TEXT" +
                ");");

        //Создание таблицы шкал
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_SCALES_NAME+"(" +
                FIELD_SCALES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_SCALES_NAME+" TEXT, " +
                FIELD_SCALES_UNIT+" TEXT, " +
                FIELD_SCALES_VALUETYPEID+" INTEGER, " +
                FIELD_SCALES_MINVALUE+" DOUBLE, " +
                FIELD_SCALES_MAXVALUE+" DOUBLE, " +
                FIELD_SCALES_ERROR+" DOUBLE, " +
                "FOREIGN KEY("+FIELD_SCALES_VALUETYPEID+") REFERENCES "+TABLE_VALUETYPES_NAME +"("+FIELD_VALUETYPES_ID+")" +
                ");");

        //Создание таблицы типов приборов
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_DEVICETYPES_NAME+"(" +
                FIELD_DEVICETYPES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_DEVICETYPES_NAME+" TEXT, " +
                FIELD_DEVICETYPES_DEFAULTSCALEID+" INTEGER, " +
                "FOREIGN KEY("+FIELD_DEVICETYPES_DEFAULTSCALEID+") REFERENCES "+TABLE_SCALES_NAME +"("+FIELD_SCALES_ID+")" +
                ");");

        //Создание таблицы величин
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_VALUES_NAME+"(" +
                FIELD_VALUES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_VALUES_SCALEID+" INTEGER, " +
                FIELD_VALUES_VALUE +" DOUBLE, " +
                "FOREIGN KEY("+FIELD_VALUES_SCALEID+") REFERENCES "+TABLE_SCALES_NAME +"("+FIELD_SCALES_ID+")" +
                ");");

        //Создание таблицы приборов
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_DEVICES_NAME+"(" +
                FIELD_DEVICES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_DEVICES_NAME+" TEXT, " +
                FIELD_DEVICES_COMMENT+" TEXT, " +
                FIELD_DEVICES_SCALEID +" INTEGER, " +
                FIELD_DEVICES_TYPEID +" INTEGER, " +
                "FOREIGN KEY("+FIELD_DEVICES_TYPEID+") REFERENCES "+TABLE_DEVICETYPES_NAME +"("+FIELD_DEVICETYPES_ID+"), " +
                "FOREIGN KEY("+FIELD_DEVICES_SCALEID+") REFERENCES "+TABLE_SCALES_NAME +"("+FIELD_SCALES_ID+") " +
                ");");

        //Создание таблицы измерений
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_MES_NAME+"(" +
                FIELD_MES_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_MES_NAME +" TEXT, " +
                FIELD_MES_COMMENT +" TEXT, " +
                FIELD_MES_DATE +" INTEGER, " +
                FIELD_MES_DEVICEID +" INTEGER, " +
                FIELD_MES_VALUEID +" INTEGER, " +
                "FOREIGN KEY("+FIELD_MES_VALUEID+") REFERENCES "+TABLE_VALUES_NAME +"("+FIELD_VALUES_ID+"), " +
                "FOREIGN KEY("+FIELD_MES_DEVICEID+") REFERENCES "+TABLE_DEVICES_NAME +"("+FIELD_DEVICES_ID+") " +
                ");");

        //заполнение таблицы бд начальными значениями
        InsertInitialValues();
    }

    //Действие при обновлении версии базы данных
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_MES_NAME+";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_VALUES_NAME+";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_DEVICES_NAME+";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_DEVICETYPES_NAME+";");;
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_SCALES_NAME+";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_VALUETYPES_NAME+";");
        onCreate(sqLiteDatabase);
    }

    //получение экземпляра - необходимо для безопасного обращения к бд только 1 экземпляром хелпера
    public static synchronized DataBaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    //Начальное (тестовое) заполнение бд
    public void InsertInitialValues(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        //Типы величин
        ContentValues valueType = new ContentValues();
        valueType.put(FIELD_VALUETYPES_ID,"1");
        valueType.put(FIELD_VALUETYPES_NAME,"Численный");
        sqLiteDatabase.insert(TABLE_VALUETYPES_NAME,null,valueType);

        valueType.clear();
        valueType.put(FIELD_VALUETYPES_ID,"2");
        valueType.put(FIELD_VALUETYPES_NAME,"Строковый");
        sqLiteDatabase.insert(TABLE_VALUETYPES_NAME,null, valueType);

        valueType.clear();
        valueType.put(FIELD_VALUETYPES_ID,"3");
        valueType.put(FIELD_VALUETYPES_NAME,"Диапазон");
        sqLiteDatabase.insert(TABLE_VALUETYPES_NAME,null, valueType);

        valueType.clear();
        valueType.put(FIELD_VALUETYPES_ID,"4");
        valueType.put(FIELD_VALUETYPES_NAME,"Бинарный");
        sqLiteDatabase.insert(TABLE_VALUETYPES_NAME,null, valueType);

        //шкала напряжения
        ContentValues scale = new ContentValues();
        scale.put(FIELD_SCALES_NAME,"Напряжение");
        scale.put(FIELD_SCALES_UNIT, "Вольт");
        scale.put(FIELD_SCALES_MAXVALUE, 10);
        scale.put(FIELD_SCALES_MINVALUE, 0);
        scale.put(FIELD_SCALES_ERROR, 0.1);
        scale.put(FIELD_SCALES_VALUETYPEID, 1);
        long defScaleId = sqLiteDatabase.insert(TABLE_SCALES_NAME,null, scale);

        //тип вольтметр
        ContentValues deviceType = new ContentValues();
        deviceType.put(FIELD_DEVICETYPES_NAME,"Вольтметр");
        deviceType.put(FIELD_DEVICETYPES_DEFAULTSCALEID, defScaleId);
        long deviceTypeId = sqLiteDatabase.insert(TABLE_DEVICETYPES_NAME,null, deviceType);

        //вольтметр прибор лабораторный
        ContentValues device = new ContentValues();
        device.put(FIELD_DEVICES_NAME, "Лабораторный вольтметр");
        device.put(FIELD_DEVICES_COMMENT, "Аудитория Б300");
        device.put(FIELD_DEVICES_SCALEID, defScaleId);
        device.put(FIELD_DEVICES_TYPEID, deviceTypeId);
        long deviceId = sqLiteDatabase.insert(TABLE_DEVICES_NAME,null, device);

        //снятая величина
        ContentValues value = new ContentValues();
        value.put(FIELD_VALUES_VALUE, 9);
        value.put(FIELD_VALUES_SCALEID, defScaleId);
        long valueId = sqLiteDatabase.insert(TABLE_VALUES_NAME,null, value);

        Date date = new Date();
        //измерение
        ContentValues mes = new ContentValues();
        mes.put(FIELD_MES_NAME, "тест 1");
        mes.put(FIELD_MES_COMMENT, "тестовое измерение для проверки");
        mes.put(FIELD_MES_DATE, date.toString());
        mes.put(FIELD_MES_DEVICEID, deviceId);
        mes.put(FIELD_MES_VALUEID, valueId);
        sqLiteDatabase.insert(TABLE_MES_NAME,null, mes);
    }

    //Процедура тестирования - получение тестовых данных из базы данных
    public ContentValues testDatabase() {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues output = new ContentValues();

        Cursor cursor = db.query(TABLE_MES_NAME,
                new String[]{FIELD_MES_ID, FIELD_MES_NAME, FIELD_MES_COMMENT, FIELD_MES_DATE, FIELD_MES_DEVICEID, FIELD_MES_VALUEID},
                null, null, null, null, null);
        cursor.moveToFirst();

        output.put(FIELD_MES_ID, cursor.getInt(0));
        output.put(FIELD_MES_NAME, cursor.getString(1));
        output.put(FIELD_MES_COMMENT, cursor.getString(2));
        output.put(FIELD_MES_DATE, cursor.getString(3));
        output.put(FIELD_MES_DEVICEID, cursor.getInt(4));
        output.put(FIELD_MES_VALUEID, cursor.getInt(5));

        cursor.close();
        return output;
    }

    public List<ContentValues> getMeasurementsList(){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues mes = new ContentValues();
        List<ContentValues> mesList = new ArrayList<ContentValues>();

        //считывание базовой информации
        Cursor mesCursor = db.query(TABLE_MES_NAME,
                new String[]{FIELD_MES_ID, FIELD_MES_NAME, FIELD_MES_COMMENT, FIELD_MES_DATE, FIELD_MES_DEVICEID},
                null, null, null, null, null);

        //вынужденная инициализация
        Cursor deviceCursor = mesCursor;

        mesCursor.moveToFirst();

        while(!mesCursor.isAfterLast()){
            mes.clear();
            mes.put("id", mesCursor.getInt(0));
            mes.put("name", mesCursor.getString(1));
            mes.put("comment", mesCursor.getInt(2));
            mes.put("date", mesCursor.getInt(3));

            //получение названия прибора из другой таблицы
            deviceCursor = db.rawQuery("SELECT "+FIELD_DEVICES_ID+", "+FIELD_DEVICES_NAME+" " +
                    "FROM "+TABLE_DEVICES_NAME+" " +
                    "WHERE "+FIELD_DEVICES_ID+"="+mesCursor.getInt(4)+";",null);

            mes.put("device",deviceCursor.getString(1));
            mesList.add(mes);
        }
        mesCursor.close();
        deviceCursor.close();

        return mesList;
    }





}
