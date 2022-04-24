package com.github.vladosspasi.mes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.github.vladosspasi.mes.Settings.Templates.Template;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataBaseHelper extends SQLiteOpenHelper {

    //экземпляр объекта (синглтон)
    private static DataBaseHelper sInstance;
    //название дб
    private static final String DATABASE_NAME = "mesdb";
    //версия дб
    private static final int DATABASE_VERSION = 5;

    //Константы-названия таблиц и полей
    //Таблица Измерений
    public static final String TABLE_MES_NAME = "measurements";
    public static final String FIELD_MES_ID = "_id";
    public static final String FIELD_MES_NAME = "name";
    public static final String FIELD_MES_COMMENT = "comment";
    //public static final String FIELD_MES_DEVICEID = "deviceId";
    public static final String FIELD_MES_VALUEID = "valueId";
    public static final String FIELD_MES_DATE = "date";
    //Таблица приборов
    public static final String TABLE_DEVICES_NAME = "devices";
    public static final String FIELD_DEVICES_NAME = "name";
    public static final String FIELD_DEVICES_COMMENT = "comment";
    public static final String FIELD_DEVICES_ID = "_id";
    public static final String FIELD_DEVICES_TYPE = "type";

    //Таблица величин
    public static final String TABLE_VALUES_NAME = "vals";
    public static final String FIELD_VALUES_ID = "_id";
    public static final String FIELD_VALUES_VALUE = "value";
    public static final String FIELD_VALUES_SCALEID = "scaleId";
    public static final String FIELD_VALUES_MESID = "mesId";
    //Таблица типов величин
    public static final String TABLE_VALUETYPES_NAME = "valueType";
    public static final String FIELD_VALUETYPES_NAME = "typename";
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
    public static final String FIELD_SCALES_DEVICEID = "deviceId";

    //Таблица шаблонов
    public static final String TABLE_TEMPLATES_NAME = "templates";
    public static final String FIELD_TEMPLATES_NAME = "tempname";
    public static final String FIELD_TEMPLATES_COMMENT = "tempcomment";
    public static final String FIELD_TEMPLATES_ID = "_id";

    //Таблица шаблонов-шкал
    public static final String TABLE_TEMPSCALES_NAME = "tempscales";
    public static final String FIELD_TEMPSCALES_SCALEID = "scaleId";
    public static final String FIELD_TEMPSCALES_TEMPID = "tempId";
    public static final String FIELD_TEMPSCALES_ID = "_id";

    //Конструктор класса
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Действие при первом открытии (создании) базы данных
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Создание таблицы типов величин
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_VALUETYPES_NAME + "(" +
                FIELD_VALUETYPES_ID + " INTEGER PRIMARY KEY, " +
                FIELD_VALUETYPES_NAME + " TEXT" +
                ");");

        //Создание таблицы шкал
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_SCALES_NAME + "(" +
                FIELD_SCALES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_SCALES_NAME + " TEXT, " +
                FIELD_SCALES_UNIT + " TEXT, " +
                FIELD_SCALES_VALUETYPEID + " INTEGER, " +
                FIELD_SCALES_MINVALUE + " DOUBLE, " +
                FIELD_SCALES_MAXVALUE + " DOUBLE, " +
                FIELD_SCALES_ERROR + " DOUBLE, " +
                FIELD_SCALES_DEVICEID + " INTEGER, " +
                "FOREIGN KEY(" + FIELD_SCALES_VALUETYPEID + ") REFERENCES " + TABLE_VALUETYPES_NAME + "(" + FIELD_VALUETYPES_ID + ")," +
                "FOREIGN KEY(" + FIELD_SCALES_DEVICEID + ") REFERENCES " + TABLE_DEVICES_NAME + "(" + FIELD_DEVICES_ID + ")" +
                ");");

        //Создание таблицы величин
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_VALUES_NAME + "(" +
                FIELD_VALUES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_VALUES_MESID + " INTEGER, " +
                FIELD_VALUES_SCALEID + " INTEGER, " +
                FIELD_VALUES_VALUE + " TEXT, " +
                "FOREIGN KEY(" + FIELD_VALUES_SCALEID + ") REFERENCES " + TABLE_SCALES_NAME + "(" + FIELD_SCALES_ID + "), " +
                "FOREIGN KEY(" + FIELD_VALUES_MESID + ") REFERENCES " + TABLE_MES_NAME + "(" + FIELD_MES_ID + ")" +
                ");");

        //Создание таблицы приборов
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_DEVICES_NAME + "(" +
                FIELD_DEVICES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_DEVICES_NAME + " TEXT, " +
                FIELD_DEVICES_COMMENT + " TEXT, " +
                FIELD_DEVICES_TYPE + " TEXT " +
                ");");

        //Создание таблицы измерений
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_MES_NAME + "(" +
                FIELD_MES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_MES_NAME + " TEXT, " +
                FIELD_MES_COMMENT + " TEXT, " +
                FIELD_MES_DATE + " TEXT " +
                ");");

        //Создание таблицы шаблонов
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TEMPLATES_NAME + "(" +
                FIELD_TEMPLATES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_TEMPLATES_NAME + " TEXT, " +
                FIELD_TEMPLATES_COMMENT + " TEXT " +
                ");");

        //Создание таблицы шаблонов-шкал
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TEMPSCALES_NAME + "(" +
                FIELD_TEMPSCALES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_TEMPSCALES_SCALEID + " INTEGER, " +
                FIELD_TEMPSCALES_TEMPID + " INTEGER, " +
                "FOREIGN KEY(" + FIELD_TEMPSCALES_SCALEID + ") REFERENCES " + TABLE_SCALES_NAME + "(" + FIELD_SCALES_ID + "), " +
                "FOREIGN KEY(" + FIELD_TEMPSCALES_TEMPID + ") REFERENCES " + TABLE_TEMPLATES_NAME + "(" + FIELD_TEMPLATES_ID + ") " +
                ");");

        //заполнение таблицы бд начальными значениями
        InsertInitialValues(sqLiteDatabase);
    }

    //Действие при обновлении версии базы данных
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MES_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VALUES_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SCALES_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VALUETYPES_NAME + ";");
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
    public void InsertInitialValues(SQLiteDatabase sqLiteDatabase) {
        //SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        //Типы величин
        ContentValues valueType = new ContentValues();
        valueType.put(FIELD_VALUETYPES_ID, "1");
        valueType.put(FIELD_VALUETYPES_NAME, "Численный");
        sqLiteDatabase.insert(TABLE_VALUETYPES_NAME, null, valueType);

        valueType.clear();
        valueType.put(FIELD_VALUETYPES_ID, "2");
        valueType.put(FIELD_VALUETYPES_NAME, "Строковый");
        sqLiteDatabase.insert(TABLE_VALUETYPES_NAME, null, valueType);

        valueType.clear();
        valueType.put(FIELD_VALUETYPES_ID, "3");
        valueType.put(FIELD_VALUETYPES_NAME, "Диапазон");
        sqLiteDatabase.insert(TABLE_VALUETYPES_NAME, null, valueType);

        valueType.clear();
        valueType.put(FIELD_VALUETYPES_ID, "4");
        valueType.put(FIELD_VALUETYPES_NAME, "Бинарный");
        sqLiteDatabase.insert(TABLE_VALUETYPES_NAME, null, valueType);

        //вольтметр прибор лабораторный
        ContentValues device = new ContentValues();
        device.put(FIELD_DEVICES_NAME, "Лабораторный вольтметр");
        device.put(FIELD_DEVICES_COMMENT, "Аудитория Б300");
        device.put(FIELD_DEVICES_TYPE, "Вольтметр М12");
        long deviceId = sqLiteDatabase.insert(TABLE_DEVICES_NAME, null, device);

        //шкала напряжения
        ContentValues scale = new ContentValues();
        scale.put(FIELD_SCALES_NAME, "Напряжение");
        scale.put(FIELD_SCALES_UNIT, "Вольт");
        scale.put(FIELD_SCALES_MAXVALUE, 10);
        scale.put(FIELD_SCALES_MINVALUE, 0);
        scale.put(FIELD_SCALES_ERROR, 0.1);
        scale.put(FIELD_SCALES_VALUETYPEID, 1);
        scale.put(FIELD_SCALES_DEVICEID, deviceId);
        long scaleId = sqLiteDatabase.insert(TABLE_SCALES_NAME, null, scale);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E dd.MM.yyyy ',' HH:mm:ss");

        //измерение
        ContentValues mes = new ContentValues();
        mes.put(FIELD_MES_NAME, "тест 1");
        mes.put(FIELD_MES_COMMENT, "тестовое измерение для проверки");
        mes.put(FIELD_MES_DATE, simpleDateFormat.format(date));
        long mesId = sqLiteDatabase.insert(TABLE_MES_NAME, null, mes);

        //снятыe величины
        ContentValues value1 = new ContentValues();
        value1.put(FIELD_VALUES_VALUE, "9");
        value1.put(FIELD_VALUES_SCALEID, scaleId);
        value1.put(FIELD_VALUES_MESID, mesId);
        sqLiteDatabase.insert(TABLE_VALUES_NAME, null, value1);

        ContentValues value2 = new ContentValues();
        value2.put(FIELD_VALUES_VALUE, "13");
        value2.put(FIELD_VALUES_SCALEID, scaleId);
        value2.put(FIELD_VALUES_MESID, mesId);
        sqLiteDatabase.insert(TABLE_VALUES_NAME, null, value2);

        ContentValues value3 = new ContentValues();
        value3.put(FIELD_VALUES_VALUE, "15");
        value3.put(FIELD_VALUES_SCALEID, scaleId);
        value3.put(FIELD_VALUES_MESID, mesId);
        sqLiteDatabase.insert(TABLE_VALUES_NAME, null, value3);

        ContentValues value4 = new ContentValues();
        value4.put(FIELD_VALUES_VALUE, "18");
        value4.put(FIELD_VALUES_SCALEID, scaleId);
        value4.put(FIELD_VALUES_MESID, mesId);
        sqLiteDatabase.insert(TABLE_VALUES_NAME, null, value4);
    }

    public ArrayList<ContentValues> getMeasurementsList() {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues mes = new ContentValues();
        ArrayList<ContentValues> mesList = new ArrayList<ContentValues>();

        //считывание базовой информации
        Cursor mesCursor = db.query(TABLE_MES_NAME,
                new String[]{FIELD_MES_ID, FIELD_MES_NAME, FIELD_MES_COMMENT, FIELD_MES_DATE},
                null, null, null, null, null);

        if (mesCursor.getCount() == 0) { //Если нет никаких значений, то соответственное сообщение.
            ContentValues empty = new ContentValues();
            empty.put("no values", 0);
            ArrayList<ContentValues> emptyList = new ArrayList<>();
            emptyList.add(empty);
            mesCursor.close();
            db.close();
            return emptyList;
        }

        mesCursor.moveToFirst();
        do {
            mes = new ContentValues();
            mes.put("mesId", mesCursor.getInt(0));
            mes.put("mesName", mesCursor.getString(1));
            mes.put("mesComment", mesCursor.getString(2));
            mes.put("mesDate", mesCursor.getString(3));
            mesList.add(mes);
            mesCursor.moveToNext();

        } while (!mesCursor.isAfterLast());

        mesCursor.close();
        return mesList;
    }

    public ArrayList<ContentValues> getMeasurmentData(int mesId) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ContentValues> mesData = new ArrayList<>();
        ContentValues mesInfo = new ContentValues();
        ContentValues val;

        Cursor mesCursor = db.query(TABLE_MES_NAME,
                new String[]{FIELD_MES_ID, FIELD_MES_NAME, FIELD_MES_COMMENT, FIELD_MES_DATE},
                FIELD_MES_ID + "=" + mesId, null, null, null, null);

        mesCursor.moveToFirst();
        mesInfo.clear();
        mesInfo.put("mesName", mesCursor.getString(1)); //нет данных
        mesInfo.put("mesComment", mesCursor.getString(2));
        mesInfo.put("mesDate", mesCursor.getString(3));
        mesCursor.close();
        mesData.add(mesInfo);

        Cursor valsCursor = db.rawQuery("SELECT " +
                TABLE_VALUES_NAME + "." + FIELD_VALUES_ID + ", " +
                TABLE_VALUES_NAME + "." + FIELD_VALUES_VALUE + ", " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_ERROR + "," +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_NAME + "," +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_UNIT + ", " +
                TABLE_DEVICES_NAME + "." + FIELD_DEVICES_ID + ", " +
                TABLE_DEVICES_NAME + "." + FIELD_DEVICES_NAME + " " +
                "FROM " + TABLE_VALUES_NAME + " " +
                "JOIN " + TABLE_SCALES_NAME + " " +
                "ON " + TABLE_SCALES_NAME + "." + FIELD_SCALES_ID + " = " + TABLE_VALUES_NAME + "." + FIELD_VALUES_SCALEID + " " +
                "JOIN " + TABLE_DEVICES_NAME + " " +
                "ON " + TABLE_SCALES_NAME + "." + FIELD_SCALES_DEVICEID + " = " + TABLE_DEVICES_NAME + "." + FIELD_DEVICES_ID + " " +
                "WHERE " + TABLE_VALUES_NAME + "." + FIELD_VALUES_MESID + " = " + mesId +
                ";", null);

        valsCursor.moveToFirst();

        if (valsCursor.getCount() == 0) { //Если нет никаких значений, то соответственное сообщение.
            ContentValues empty = new ContentValues();
            empty.put("no values", 0);
            ArrayList<ContentValues> emptyList = new ArrayList<>();
            emptyList.add(empty);
            valsCursor.close();
            db.close();
            return emptyList;
        }

        do {
            val = new ContentValues();

            val.put("valueId", valsCursor.getInt(0));
            val.put("value", valsCursor.getString(1));
            val.put("scaleError", valsCursor.getDouble(2));
            val.put("scaleName", valsCursor.getString(3));
            val.put("scaleUnit", valsCursor.getString(4));
            val.put("deviceId", valsCursor.getString(5));
            val.put("deviceName", valsCursor.getString(6));
            mesData.add(val);
            valsCursor.moveToNext();
        } while (!valsCursor.isAfterLast());

        valsCursor.close();

        return mesData;
    }

    public boolean deleteValueById(int valId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.delete(TABLE_VALUES_NAME, FIELD_VALUES_ID + "=" + valId, null) > 0;
    }

    public boolean deleteMesById(int mesId) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_VALUES_NAME, FIELD_VALUES_MESID + "=" + mesId, null);
        return db.delete(TABLE_MES_NAME, FIELD_MES_ID + "=" + mesId, null) > 0;
    }

    public ArrayList<ContentValues> getDeviceInfo(int deviceId) {
        ArrayList<ContentValues> deviceData = new ArrayList<>();
        ContentValues deviceInfo = new ContentValues();
        ContentValues scaleInfo;

        SQLiteDatabase db = getReadableDatabase();

        Cursor deviceCursor = db.rawQuery("SELECT " +
                TABLE_DEVICES_NAME + "." + FIELD_DEVICES_NAME + " , " +
                TABLE_DEVICES_NAME + "." + FIELD_DEVICES_COMMENT + " , " +
                TABLE_DEVICES_NAME + "." + FIELD_DEVICES_TYPE + "  " +
                "FROM " + TABLE_DEVICES_NAME + " " +
                "WHERE " + FIELD_DEVICES_ID + "=" + deviceId + "" +
                ";", null);

        deviceCursor.moveToFirst();
        deviceInfo.put("deviceName", deviceCursor.getString(0));
        deviceInfo.put("deviceComment", deviceCursor.getString(1));
        deviceInfo.put("deviceType", deviceCursor.getString(2));
        deviceData.add(deviceInfo);
        deviceCursor.close();

        Cursor scalesCursor = db.rawQuery("SELECT " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_NAME + " , " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_UNIT + " , " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_ERROR + " , " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_MINVALUE + " , " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_MAXVALUE + " , " +
                TABLE_VALUETYPES_NAME + "." + FIELD_VALUETYPES_NAME + ", " +
                TABLE_VALUETYPES_NAME + "." + FIELD_VALUETYPES_ID + ", " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_ID + " " +
                "FROM " + TABLE_SCALES_NAME + " " +
                "JOIN " + TABLE_VALUETYPES_NAME + " " +
                "ON " + TABLE_VALUETYPES_NAME + "." + FIELD_VALUETYPES_ID + " = " + TABLE_SCALES_NAME + "." + FIELD_SCALES_VALUETYPEID + " " +
                "WHERE " + TABLE_SCALES_NAME + "." + FIELD_SCALES_DEVICEID + " = " + deviceId +
                ";", null);

        scalesCursor.moveToFirst();
        do {
            scaleInfo = new ContentValues();

            scaleInfo.put("scaleName", scalesCursor.getString(0));
            scaleInfo.put("scaleUnit", scalesCursor.getString(1));
            scaleInfo.put("scaleError", scalesCursor.getString(2));
            scaleInfo.put("scaleMin", scalesCursor.getString(3));
            scaleInfo.put("scaleMax", scalesCursor.getString(4));
            scaleInfo.put("valuetypeName", scalesCursor.getString(5));
            scaleInfo.put("scaleTypeId", scalesCursor.getString(6));
            scaleInfo.put("scaleId", scalesCursor.getString(7));

            deviceData.add(scaleInfo);
            scalesCursor.moveToNext();
        } while (!scalesCursor.isAfterLast());

        scalesCursor.close();

        db.close();

        return deviceData;
    }

    public ArrayList<ContentValues> getDevices() {
        ArrayList<ContentValues> deviceList = new ArrayList<>();
        ContentValues deviceInfo;

        SQLiteDatabase db = getReadableDatabase();

        Cursor devicesCursor = db.query(TABLE_DEVICES_NAME,
                new String[]{FIELD_DEVICES_ID, FIELD_DEVICES_NAME, FIELD_DEVICES_COMMENT, FIELD_DEVICES_TYPE},
                null, null, null, null, null);

        //Если нет приборов
        if (devicesCursor.getCount() == 0) {
            deviceInfo = new ContentValues();
            deviceInfo.put("no values", 0);
            deviceList.add(deviceInfo);
            devicesCursor.close();
            db.close();
            return deviceList;
        }

        //Если есть
        devicesCursor.moveToFirst();

        do {
            deviceInfo = new ContentValues();
            deviceInfo.put("deviceId", devicesCursor.getString(0));
            deviceInfo.put("deviceName", devicesCursor.getString(1));
            deviceInfo.put("deviceComment", devicesCursor.getString(2));
            deviceInfo.put("deviceType", devicesCursor.getString(3));

            deviceList.add(deviceInfo);
            devicesCursor.moveToNext();
        } while (!devicesCursor.isAfterLast());

        devicesCursor.close();
        db.close();

        return deviceList;
    }


    public void addDevice(ContentValues deviceInfo, ArrayList<ContentValues> scales) {
        SQLiteDatabase db = getReadableDatabase();
        Long deviceId = db.insert(TABLE_DEVICES_NAME, null, deviceInfo);

        ContentValues scale;

        for (int i = 0; i < scales.size(); i++) {
            scale = scales.get(i);
            scale.remove("type");
            scale.put(FIELD_SCALES_DEVICEID, deviceId);
            db.insert(TABLE_SCALES_NAME, null, scale);
        }
    }

    //Тут процедура аналогичная как для получения данных о приборе
    public ArrayList<ContentValues> getScalesByDeviceId(int deviceId) {
        ArrayList<ContentValues> scalesList = new ArrayList<>();
        ContentValues scale;
        SQLiteDatabase db = getReadableDatabase();

        Cursor scalesCursor = db.rawQuery("SELECT " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_ID + " , " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_NAME + " , " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_UNIT + " , " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_ERROR + " , " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_MINVALUE + " , " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_MAXVALUE + " , " +
                TABLE_VALUETYPES_NAME + "." + FIELD_VALUETYPES_NAME + ", " +
                TABLE_DEVICES_NAME+ "." + FIELD_DEVICES_NAME + " " +
                "FROM " + TABLE_SCALES_NAME + " " +
                "JOIN " + TABLE_VALUETYPES_NAME + " " +
                "ON " + TABLE_VALUETYPES_NAME + "." + FIELD_VALUETYPES_ID + " = " + TABLE_SCALES_NAME + "." + FIELD_SCALES_VALUETYPEID + " " +
                "JOIN " + TABLE_DEVICES_NAME + " " +
                "ON " + TABLE_DEVICES_NAME + "." + FIELD_DEVICES_ID + " = " + TABLE_SCALES_NAME + "." + FIELD_SCALES_DEVICEID + " " +
                "WHERE " + TABLE_SCALES_NAME + "." + FIELD_SCALES_DEVICEID + " = " + deviceId +
                ";", null);

        scalesCursor.moveToFirst();
        do {
            scale = new ContentValues();
            scale.put("scaleId", scalesCursor.getInt(0));
            scale.put("scaleName", scalesCursor.getString(1));
            scale.put("scaleUnit", scalesCursor.getString(2));
            scale.put("scaleError", scalesCursor.getString(3));
            scale.put("scaleMin", scalesCursor.getString(4));
            scale.put("scaleMax", scalesCursor.getString(5));
            scale.put("valuetypeName", scalesCursor.getString(6));
            scale.put("deviceId", deviceId);
            scale.put("deviceName", scalesCursor.getString(7));
            scalesList.add(scale);
            scalesCursor.moveToNext();
        } while (!scalesCursor.isAfterLast());
        scalesCursor.close();

        return scalesList;
    }

    public String getValueTypeById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor typeCursor = db.query(TABLE_VALUETYPES_NAME,
                new String[]{FIELD_VALUETYPES_NAME}, FIELD_VALUETYPES_ID + " = " + id,
                null, null, null, null, null);
        typeCursor.moveToFirst();
        String typeName = typeCursor.getString(0);
        typeCursor.close();
        return typeName;
    }

    public ContentValues getScaleById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues scaleInfo = new ContentValues();

        Cursor cursor = db.query(TABLE_SCALES_NAME,
                new String[]{FIELD_SCALES_ID, FIELD_SCALES_NAME, FIELD_SCALES_UNIT,
                        FIELD_SCALES_ERROR, FIELD_SCALES_MINVALUE, FIELD_SCALES_MAXVALUE, FIELD_SCALES_DEVICEID},
                FIELD_SCALES_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        scaleInfo.put("scaleId", cursor.getInt(0));
        scaleInfo.put("scaleName", cursor.getString(1));
        scaleInfo.put("scaleUnit", cursor.getString(2));
        scaleInfo.put("scaleError", cursor.getString(3));
        scaleInfo.put("scaleMin", cursor.getString(4));
        scaleInfo.put("scaleMax", cursor.getString(5));

        int deviceId = cursor.getInt(6);
        cursor = db.query(TABLE_DEVICES_NAME,
                new String[]{FIELD_DEVICES_NAME},
                FIELD_DEVICES_ID + " = " + deviceId, null, null, null, null);
        cursor.moveToFirst();
        scaleInfo.put("deviceName", cursor.getString(0));
        scaleInfo.put("deviceId", deviceId);

        cursor.close();
        return scaleInfo;
    }

    public boolean addMeasurement(ContentValues mesInfo, ArrayList<ContentValues> scalesInfo, ArrayList<String> valuesInfo) {

        SQLiteDatabase db = getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E dd.MM.yyyy ',' HH:mm:ss");

        ContentValues measurementInfo = new ContentValues();
        measurementInfo.put(FIELD_MES_DATE, simpleDateFormat.format(date));
        measurementInfo.put(FIELD_MES_NAME, mesInfo.getAsString(FIELD_MES_NAME));
        measurementInfo.put(FIELD_MES_COMMENT, mesInfo.getAsString(FIELD_MES_COMMENT));

        int mesId = (int) db.insert(TABLE_MES_NAME, null, measurementInfo);
        ContentValues record = new ContentValues();

        for (int i = 0; i < valuesInfo.size(); i++) {
            int scaleId = scalesInfo.get(i).getAsInteger("scaleId");
            record = new ContentValues();
            record.put(FIELD_VALUES_MESID, mesId);
            record.put(FIELD_VALUES_SCALEID, scaleId);
            record.put(FIELD_VALUES_VALUE, valuesInfo.get(i));
            db.insert(TABLE_VALUES_NAME, null, record);
        }
        return true;
    }

    public void deleteDeviceById(int id) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                TABLE_MES_NAME + "." + FIELD_MES_ID + ", " +
                TABLE_VALUES_NAME + "." + FIELD_VALUES_ID + ", " +
                TABLE_SCALES_NAME + "." + FIELD_SCALES_ID + ", " +
                TABLE_DEVICES_NAME + "." + FIELD_DEVICES_ID + " " +
                "FROM " + TABLE_MES_NAME + " " +
                "JOIN " + TABLE_VALUES_NAME + " " +
                "ON " + TABLE_VALUES_NAME + "." + FIELD_VALUES_MESID + " = " + TABLE_MES_NAME + "." + FIELD_MES_ID + " " +
                "JOIN " + TABLE_SCALES_NAME + " " +
                "ON " + TABLE_SCALES_NAME + "." + FIELD_SCALES_ID + " = " + TABLE_VALUES_NAME + "." + FIELD_VALUES_SCALEID + " " +
                "JOIN " + TABLE_DEVICES_NAME + " " +
                "ON " + TABLE_DEVICES_NAME + "." + FIELD_DEVICES_ID + " = " + TABLE_SCALES_NAME + "." + FIELD_SCALES_DEVICEID + " " +
                "WHERE " + TABLE_DEVICES_NAME + "." + FIELD_DEVICES_ID + " =" + id +
                ";", null, null);

        cursor.moveToFirst();

        ArrayList<Integer> mesIds = new ArrayList<>();
        ArrayList<Integer> valuesIds = new ArrayList<>();
        if (cursor.getCount() != 0) {
            do {
                mesIds.add(cursor.getInt(0));
                valuesIds.add(cursor.getInt(1));
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

            for (int i : valuesIds) {
                db.delete(TABLE_VALUES_NAME, FIELD_VALUES_ID + "=" + i, null);
            }
            for (int i : mesIds) {
                db.delete(TABLE_MES_NAME, FIELD_MES_ID + "=" + i, null);
            }

        }
        cursor.close();
        db.delete(TABLE_SCALES_NAME, FIELD_SCALES_DEVICEID + "=" + id, null);
        db.delete(TABLE_DEVICES_NAME, FIELD_DEVICES_ID + "=" + id, null);
        db.close();
    }

    public void modifyDevice(int deviceId, ContentValues deviceData, ArrayList<ContentValues> scalesList) {

        SQLiteDatabase db = getReadableDatabase();

        db.update(TABLE_DEVICES_NAME, deviceData, FIELD_DEVICES_ID + " = " + deviceId, null);

        for (ContentValues scale : scalesList) {
            scale.remove(FIELD_VALUETYPES_NAME);
            if (scale.getAsString(FIELD_SCALES_ID).equals("no")) {
                scale.remove(FIELD_SCALES_ID);
                scale.put(FIELD_SCALES_DEVICEID, deviceId);
                db.insert(TABLE_SCALES_NAME, null, scale);

            } else {
                db.update(TABLE_SCALES_NAME, scale, FIELD_SCALES_ID + " = " + scale.getAsInteger(FIELD_SCALES_ID), /*new String[]{FIELD_SCALES_ID}*/null);
            }
        }

        db.close();
    }

    public void createTemplate(Template template) {

        SQLiteDatabase db = getReadableDatabase();

        ContentValues templateInfo = new ContentValues();
        templateInfo.put(FIELD_TEMPLATES_NAME, template.getName());
        templateInfo.put(FIELD_TEMPLATES_COMMENT, template.getComment());

        long templateId = db.insert(TABLE_TEMPLATES_NAME, null, templateInfo);

        ArrayList<Integer> scalesIds = template.getScalesIds();
        ContentValues tempScale;

        for (Integer i : scalesIds) {
            tempScale = new ContentValues();
            tempScale.put(FIELD_TEMPSCALES_TEMPID, templateId);
            tempScale.put(FIELD_TEMPSCALES_SCALEID, i);
            db.insert(TABLE_TEMPSCALES_NAME, null, tempScale);
        }

        db.close();
    }


    public Template getTemplateById(int id) {
        Template template = new Template();
        ContentValues scale;
        SQLiteDatabase db = getReadableDatabase();


        Cursor cursor = db.query(TABLE_TEMPLATES_NAME,
                new String[]{FIELD_TEMPLATES_ID, FIELD_TEMPLATES_NAME, FIELD_TEMPLATES_COMMENT},
                FIELD_TEMPLATES_ID + "=" + id, null, null, null, null);

        cursor.moveToFirst();
        template.setName(cursor.getString(1));
        template.setComment(cursor.getString(2));
        cursor.close();

        cursor = db.rawQuery("SELECT " +
                        TABLE_SCALES_NAME + "." + FIELD_SCALES_NAME + ", " +
                        TABLE_SCALES_NAME + "." + FIELD_SCALES_UNIT + ", " +
                        TABLE_SCALES_NAME + "." + FIELD_SCALES_MINVALUE + ", " +
                        TABLE_SCALES_NAME + "." + FIELD_SCALES_MAXVALUE + ", " +
                        TABLE_SCALES_NAME + "." + FIELD_SCALES_ERROR + ", " +
                        TABLE_SCALES_NAME + "." + FIELD_SCALES_VALUETYPEID + ", " +
                        TABLE_VALUETYPES_NAME + "." + FIELD_VALUETYPES_NAME + ", " +
                        TABLE_DEVICES_NAME+ "." + FIELD_DEVICES_NAME + ", " +
                        TABLE_SCALES_NAME + "." + FIELD_SCALES_ID + " " +
                        "FROM " + TABLE_SCALES_NAME+" "+
                        "JOIN " + TABLE_VALUETYPES_NAME + " " +
                        "ON " + TABLE_SCALES_NAME + "." + FIELD_SCALES_VALUETYPEID + " = " + TABLE_VALUETYPES_NAME + "." + FIELD_VALUETYPES_ID + " " +
                        "JOIN " + TABLE_TEMPSCALES_NAME + " " +
                        "ON " + TABLE_TEMPSCALES_NAME + "." + FIELD_TEMPSCALES_SCALEID + " = " + TABLE_SCALES_NAME + "." + FIELD_SCALES_ID + " " +
                        "JOIN " + TABLE_DEVICES_NAME + " " +
                        "ON " + TABLE_DEVICES_NAME + "." + FIELD_DEVICES_ID + " = " + TABLE_SCALES_NAME + "." + FIELD_SCALES_DEVICEID + " " +
                        "WHERE " + TABLE_TEMPSCALES_NAME + "." + FIELD_TEMPSCALES_TEMPID + " =" + id +
                        "; "
                , null, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            scale = new ContentValues();
            scale.put("scaleName",cursor.getString(0));
            scale.put("scaleUnit",cursor.getString(1));
            scale.put("scaleMin",cursor.getString(2));
            scale.put("scaleMax",cursor.getString(3));
            scale.put("scaleError",cursor.getString(4));
            scale.put("valuetypeName",cursor.getString(6));
            scale.put("deviceName",cursor.getString(7));
            scale.put("scaleId",cursor.getString(8));

            template.addToScalesList(scale);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return template;
    }

    public ArrayList<ContentValues> getTemplates() {
        ArrayList<ContentValues> list = new ArrayList<>();
        ContentValues template;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TEMPLATES_NAME,
                new String[]{FIELD_TEMPLATES_ID, FIELD_TEMPLATES_NAME, FIELD_TEMPLATES_COMMENT},
                null, null, null, null, null);

        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            template = new ContentValues();
            template.put("no values", 0);
        } else {
            while (!cursor.isAfterLast()) {

                template = new ContentValues();
                template.put("tempId", cursor.getInt(0));
                template.put("tempName", cursor.getString(1));
                template.put("tempComment", cursor.getString(2));
                list.add(template);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteTemplateById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_TEMPSCALES_NAME, FIELD_TEMPSCALES_TEMPID + "=" + id, null);
        db.delete(TABLE_TEMPLATES_NAME, FIELD_TEMPLATES_ID + "=" + id, null);
        db.close();
    }


}
