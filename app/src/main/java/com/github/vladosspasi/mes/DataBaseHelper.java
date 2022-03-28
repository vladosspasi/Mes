package com.github.vladosspasi.mes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static Logger log = Logger.getLogger(DataBaseHelper.class.getName());


    //экземпляр объекта (синглтон)
    private static DataBaseHelper sInstance;
    //название дб
    private static final String DATABASE_NAME = "mesdb";
    //версия дб
    private static final int DATABASE_VERSION = 3;

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
    public static final String FIELD_DEVICES_TYPEID = "typeId";
    //Таблица типов приборов
    public static final String TABLE_DEVICETYPES_NAME = "deviceTypes";
    public static final String FIELD_DEVICETYPES_ID = "_id";
    public static final String FIELD_DEVICETYPES_NAME = "name";
    //Таблица величин
    public static final String TABLE_VALUES_NAME = "vals";
    public static final String FIELD_VALUES_ID = "_id";
    public static final String FIELD_VALUES_VALUE = "value";
    public static final String FIELD_VALUES_SCALEID = "scaleId";
    public static final String FIELD_VALUES_MESID = "mesId";
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
    public static final String FIELD_SCALES_DEVICEID = "deviceId";

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
                FIELD_SCALES_DEVICEID+" INTEGER, "+
                "FOREIGN KEY("+FIELD_SCALES_VALUETYPEID+") REFERENCES "+TABLE_VALUETYPES_NAME +"("+FIELD_VALUETYPES_ID+")," +
                "FOREIGN KEY("+FIELD_SCALES_DEVICEID+") REFERENCES "+TABLE_DEVICES_NAME +"("+FIELD_DEVICES_ID+")" +
                ");");

        //Создание таблицы типов приборов
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_DEVICETYPES_NAME+"(" +
                FIELD_DEVICETYPES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_DEVICETYPES_NAME+" TEXT" +
                ");");

        //Создание таблицы величин
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_VALUES_NAME+"(" +
                FIELD_VALUES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_VALUES_MESID+" INTEGER, " +
                FIELD_VALUES_SCALEID+" INTEGER, " +
                FIELD_VALUES_VALUE +" TEXT, " +
                "FOREIGN KEY("+FIELD_VALUES_SCALEID+") REFERENCES "+TABLE_SCALES_NAME +"("+FIELD_SCALES_ID+"), " +
                "FOREIGN KEY("+FIELD_VALUES_MESID+") REFERENCES "+TABLE_MES_NAME +"("+FIELD_MES_ID+")" +
                ");");

        //Создание таблицы приборов
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_DEVICES_NAME+"(" +
                FIELD_DEVICES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_DEVICES_NAME+" TEXT, " +
                FIELD_DEVICES_COMMENT+" TEXT, " +
                FIELD_DEVICES_TYPEID +" INTEGER, " +
                "FOREIGN KEY("+FIELD_DEVICES_TYPEID+") REFERENCES "+TABLE_DEVICETYPES_NAME +"("+FIELD_DEVICETYPES_ID+")" +
                ");");

        //Создание таблицы измерений
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_MES_NAME+"(" +
                FIELD_MES_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_MES_NAME +" TEXT, " +
                FIELD_MES_COMMENT +" TEXT, " +
                FIELD_MES_DATE +" TEXT " +
                ");");

        //заполнение таблицы бд начальными значениями
        InsertInitialValues(sqLiteDatabase);
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
    public void InsertInitialValues(SQLiteDatabase sqLiteDatabase){
        //SQLiteDatabase sqLiteDatabase = getWritableDatabase();
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

        //тип вольтметр
        ContentValues deviceType = new ContentValues();
        deviceType.put(FIELD_DEVICETYPES_NAME,"Вольтметр M12");
        long deviceTypeId = sqLiteDatabase.insert(TABLE_DEVICETYPES_NAME,null, deviceType);

        //вольтметр прибор лабораторный
        ContentValues device = new ContentValues();
        device.put(FIELD_DEVICES_NAME, "Лабораторный вольтметр");
        device.put(FIELD_DEVICES_COMMENT, "Аудитория Б300");
        device.put(FIELD_DEVICES_TYPEID, deviceTypeId);
        long deviceId = sqLiteDatabase.insert(TABLE_DEVICES_NAME,null, device);

        //шкала напряжения
        ContentValues scale = new ContentValues();
        scale.put(FIELD_SCALES_NAME,"Напряжение");
        scale.put(FIELD_SCALES_UNIT, "Вольт");
        scale.put(FIELD_SCALES_MAXVALUE, 10);
        scale.put(FIELD_SCALES_MINVALUE, 0);
        scale.put(FIELD_SCALES_ERROR, 0.1);
        scale.put(FIELD_SCALES_VALUETYPEID, 1);
        scale.put(FIELD_SCALES_DEVICEID, deviceId);
        long scaleId = sqLiteDatabase.insert(TABLE_SCALES_NAME,null, scale);

        //TODO дату надо поменять в нормальный формат
        Date date = new Date();

        //измерение
        ContentValues mes = new ContentValues();
        mes.put(FIELD_MES_NAME, "тест 1");
        mes.put(FIELD_MES_COMMENT, "тестовое измерение для проверки");
        mes.put(FIELD_MES_DATE, date.toString());
        long mesId = sqLiteDatabase.insert(TABLE_MES_NAME,null, mes);

        //снятыe величины
        ContentValues value1 = new ContentValues();
        value1.put(FIELD_VALUES_VALUE, "9");
        value1.put(FIELD_VALUES_SCALEID, scaleId);
        value1.put(FIELD_VALUES_MESID, mesId);
        sqLiteDatabase.insert(TABLE_VALUES_NAME,null, value1);

        ContentValues value2 = new ContentValues();
        value2.put(FIELD_VALUES_VALUE, "13");
        value2.put(FIELD_VALUES_SCALEID, scaleId);
        value2.put(FIELD_VALUES_MESID, mesId);
        sqLiteDatabase.insert(TABLE_VALUES_NAME,null, value2);

        ContentValues value3 = new ContentValues();
        value3.put(FIELD_VALUES_VALUE, "15");
        value3.put(FIELD_VALUES_SCALEID, scaleId);
        value3.put(FIELD_VALUES_MESID, mesId);
        sqLiteDatabase.insert(TABLE_VALUES_NAME,null, value3);

        ContentValues value4 = new ContentValues();
        value4.put(FIELD_VALUES_VALUE, "18");
        value4.put(FIELD_VALUES_SCALEID, scaleId);
        value4.put(FIELD_VALUES_MESID, mesId);
        sqLiteDatabase.insert(TABLE_VALUES_NAME,null, value4);
    }

    //Процедура тестирования - получение тестовых данных из базы данных
    public ContentValues testDatabase() {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues output = new ContentValues();

        Cursor cursor = db.query(TABLE_MES_NAME,
                new String[]{FIELD_MES_ID, FIELD_MES_NAME, FIELD_MES_COMMENT, FIELD_MES_DATE, FIELD_MES_VALUEID},
                null, null, null, null, null);
        cursor.moveToFirst();

        output.put(FIELD_MES_ID, cursor.getInt(0));
        output.put(FIELD_MES_NAME, cursor.getString(1));
        output.put(FIELD_MES_COMMENT, cursor.getString(2));
        output.put(FIELD_MES_DATE, cursor.getString(3));
        output.put(FIELD_MES_VALUEID, cursor.getInt(4));

        cursor.close();
        return output;
    }

    public ArrayList<ContentValues> getMeasurementsList(){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues mes = new ContentValues();
        ArrayList<ContentValues> mesList = new ArrayList<ContentValues>();

        //считывание базовой информации
        Cursor mesCursor = db.query(TABLE_MES_NAME,
                new String[]{FIELD_MES_ID, FIELD_MES_NAME, FIELD_MES_COMMENT, FIELD_MES_DATE},
                null, null, null, null, null);

        if (mesCursor.getCount()==0){ //Если нет никаких значений, то соответственное сообщение.
            ContentValues empty = new ContentValues();
            empty.put("no values", 0);
            ArrayList<ContentValues> emptyList = new ArrayList<>();
            emptyList.add(empty);
            return emptyList;
        }

        mesCursor.moveToFirst();
        do{
            mes = new ContentValues();
            mes.put("id", mesCursor.getInt(0));
            mes.put("name", mesCursor.getString(1));
            mes.put("comment", mesCursor.getString(2));
            mes.put("date", mesCursor.getString(3));
            mesList.add(mes);
            mesCursor.moveToNext();

        }while(!mesCursor.isAfterLast());

        mesCursor.close();
        return mesList;
    }

    public ArrayList<ContentValues> getMeasurmentData(int mesId){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ContentValues> mesData = new ArrayList<>();
        ContentValues mesInfo = new ContentValues();
        ContentValues val;

        Cursor mesCursor = db.query(TABLE_MES_NAME,
                new String[]{FIELD_MES_ID, FIELD_MES_NAME, FIELD_MES_COMMENT, FIELD_MES_DATE},
                FIELD_MES_ID + "=" + mesId, null, null, null, null);

        mesCursor.moveToFirst();
        mesInfo.clear();
        mesInfo.put("name", mesCursor.getString(1)); //нет данных
        mesInfo.put("comment", mesCursor.getString(2));
        mesInfo.put("date", mesCursor.getString(3));
        mesCursor.close();
        mesData.add(mesInfo);

        Cursor valsCursor = db.rawQuery("SELECT " +
                TABLE_VALUES_NAME +"."+FIELD_VALUES_ID +", " +
                TABLE_VALUES_NAME +"."+FIELD_VALUES_VALUE +", " +
                TABLE_SCALES_NAME +"."+FIELD_SCALES_ERROR + "," +
                TABLE_SCALES_NAME +"."+ FIELD_SCALES_NAME + "," +
                TABLE_SCALES_NAME +"."+ FIELD_SCALES_UNIT + ", " +
                TABLE_DEVICES_NAME+ "." + FIELD_DEVICES_ID+ ", " +
                TABLE_DEVICES_NAME+ "." + FIELD_DEVICES_NAME+ " " +
                "FROM " + TABLE_VALUES_NAME+ " "+
                "JOIN " + TABLE_SCALES_NAME + " "+
                "ON " + TABLE_SCALES_NAME + "."+ FIELD_SCALES_ID +" = "+TABLE_VALUES_NAME + "."+ FIELD_VALUES_SCALEID+" "+
                "JOIN " + TABLE_DEVICES_NAME + " "+
                "ON " + TABLE_SCALES_NAME + "."+ FIELD_SCALES_DEVICEID +" = "+TABLE_DEVICES_NAME + "."+ FIELD_DEVICES_ID+" "+
                "WHERE " + TABLE_VALUES_NAME +"."+ FIELD_VALUES_MESID +" = "+mesId+
                ";",null);

        valsCursor.moveToFirst();

        if (valsCursor.getCount()==0){ //Если нет никаких значений, то соответственное сообщение.
            ContentValues empty = new ContentValues();
            empty.put("no values", 0);
            ArrayList<ContentValues> emptyList = new ArrayList<>();
            emptyList.add(empty);
            return emptyList;
        }

        do{
            val=new ContentValues();

            val.put("id",valsCursor.getInt(0));
            val.put("value",valsCursor.getString(1));
            val.put("error",valsCursor.getDouble(2));
            val.put("scaleName",valsCursor.getString(3));
            val.put("unit",valsCursor.getString(4));
            val.put("device_id",valsCursor.getString(5));
            val.put("deviceName",valsCursor.getString(6));
            mesData.add(val);
            valsCursor.moveToNext();
        }while(!valsCursor.isAfterLast());

        valsCursor.close();

        return mesData;
    }

    public boolean deleteValueById(int valId){
        SQLiteDatabase db = getReadableDatabase();
        return db.delete(TABLE_VALUES_NAME, FIELD_VALUES_ID + "=" + valId, null) > 0;
    }

    public boolean deleteMesById(int mesId){
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_VALUES_NAME, FIELD_VALUES_MESID+"="+mesId,null);
        return db.delete(TABLE_MES_NAME, FIELD_MES_ID + "=" + mesId, null) > 0;
    }

    public ArrayList<ContentValues> getDeviceInfo(int deviceId){
        ArrayList<ContentValues> deviceData = new ArrayList<>();
        ContentValues deviceInfo = new ContentValues();
        ContentValues scaleInfo;

        SQLiteDatabase db = getReadableDatabase();

        Cursor deviceCursor = db.rawQuery("SELECT " +
                TABLE_DEVICES_NAME +"."+ FIELD_DEVICES_NAME + " , " +
                TABLE_DEVICES_NAME +"."+ FIELD_DEVICES_COMMENT + " , " +
                TABLE_DEVICETYPES_NAME+"."+ FIELD_DEVICETYPES_NAME + "  " +
                "FROM " + TABLE_DEVICES_NAME +" "+
                "JOIN " + TABLE_DEVICETYPES_NAME+" "+
                "ON " + TABLE_DEVICES_NAME+"."+FIELD_DEVICES_TYPEID+" = "+TABLE_DEVICETYPES_NAME+"."+FIELD_DEVICETYPES_ID+" "+
                ";", null);

        deviceCursor.moveToFirst();
        deviceInfo.put("name", deviceCursor.getString(0));
        deviceInfo.put("comment", deviceCursor.getString(1));
        deviceInfo.put("type", deviceCursor.getString(2));
        deviceData.add(deviceInfo);
        deviceCursor.close();

        Cursor scalesCursor = db.rawQuery("SELECT " +
                TABLE_SCALES_NAME+"."+ FIELD_SCALES_NAME + " , " +
                TABLE_SCALES_NAME+"."+ FIELD_SCALES_UNIT + " , " +
                TABLE_SCALES_NAME+"."+  FIELD_SCALES_ERROR + " , " +
                TABLE_SCALES_NAME+"."+  FIELD_SCALES_MINVALUE + " , " +
                TABLE_SCALES_NAME+"."+  FIELD_SCALES_MAXVALUE + " , " +
                TABLE_VALUETYPES_NAME+"."+ FIELD_VALUETYPES_NAME + " " +
                "FROM " + TABLE_SCALES_NAME +" "+
                "JOIN " + TABLE_VALUETYPES_NAME+" "+
                "ON " + TABLE_VALUETYPES_NAME+"."+FIELD_VALUETYPES_ID+" = "+TABLE_SCALES_NAME+"."+FIELD_SCALES_VALUETYPEID+" " +
                "WHERE "+ TABLE_SCALES_NAME+"."+FIELD_SCALES_DEVICEID+" = "+deviceId+
                ";", null);

        scalesCursor.moveToFirst();
        do{
            scaleInfo=new ContentValues();

            scaleInfo.put("name",scalesCursor.getString(0));
            scaleInfo.put("unit",scalesCursor.getString(1));
            scaleInfo.put("error",scalesCursor.getString(2));
            scaleInfo.put("minvalue",scalesCursor.getString(3));
            scaleInfo.put("maxvalue",scalesCursor.getString(4));
            scaleInfo.put("type",scalesCursor.getString(5));

            deviceData.add(scaleInfo);
            scalesCursor.moveToNext();
        }while(!scalesCursor.isAfterLast());

        log.info("КОЛИЧЕСТВО СТРОК ШКАЛ:" + scalesCursor.getCount());
        log.info("НАЗВАНИЕ ШКАЛЫ:" + scaleInfo.getAsString("name"));

        scalesCursor.close();




        db.close();

        return deviceData;
    }


}
