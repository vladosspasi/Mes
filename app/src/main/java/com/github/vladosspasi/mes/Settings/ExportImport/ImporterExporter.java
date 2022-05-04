package com.github.vladosspasi.mes.Settings.ExportImport;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import com.github.vladosspasi.mes.DataBaseHelper;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Класс имортер-экспортер. Содержить процедуры для импорта и экспорта базы данных приложения в форматах DB и JSON.
 */

public class ImporterExporter {
    public static final String fileName = "mesdb"; //Название файла базы данныъ
    public static String savedFileName = null; //Название сохраненного файла
    private static String PATH = "/data/data/com.github.vladosspasi.mes/databases/mesdb"; //Путь к файлу базы данных приложения

    //Процедура экспорта базы данных
    //Возвращает true, если файл успешно экспортирован
    public static boolean exportFile(boolean InJson, Context context) {
        boolean result = false; //Успешен ли экспорт
        //Получение пути к папке загрузок
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        if (InJson) { //Если экспорт в JSON
            //Получение базы данных в формате JSON
            DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(context);
            JSONObject jsonResult = dataBaseHelper.getDbInJSON();
            dataBaseHelper.close();

            //Создание файла с уникальным именем
            String newFileName = fileName + ".json";
            File dst = new File(downloadsFolder, newFileName);
            int i = 1;
            try {
                while (!dst.createNewFile()) {
                    newFileName = fileName + i + ".json";
                    dst = new File(downloadsFolder, newFileName);
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Запись в файл
            byte[] jsonBytes = jsonResult.toString().getBytes(StandardCharsets.UTF_8);
            try (OutputStream out = new FileOutputStream(dst)) {
                byte[] buf = new byte[1024];
                out.write(jsonBytes, 0, jsonBytes.length);
                result = true;
                savedFileName = newFileName;
            } catch (IOException e) {
                Log.e("ОШИБКА ЗАПИСИ ФАЙЛА в JSON:", e.toString());
            }

        } else { //Если в формате DB
            String newFileName = fileName; //Имя созданного файла
            File src = new File(PATH); //Файл базы данных
            try (InputStream in = new FileInputStream(src)) {
                //Создание файла с уникальным именем
                File dst = new File(downloadsFolder, newFileName);
                int i = 1;
                while (!dst.createNewFile()) {
                    newFileName = fileName + i;
                    dst = new File(downloadsFolder, newFileName);
                    i++;
                }
                //Копирование данных из одного файла в другой
                try (OutputStream out = new FileOutputStream(dst)) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    result = true;
                    savedFileName = newFileName;
                } catch (IOException e) {
                    Log.e("ОШИБКА ЗАПИСИ ФАЙЛА:", e.toString());
                }
            } catch (IOException e) {
                Log.e("ОШИБКА ЧТЕНИЯ ФАЙЛА:", e.toString());
            }
        }
        return result;
    }

    //Процедура импорта файла
    //Возвращает true, если файл успешно импортирован
    public static boolean importFile(Boolean InJson, Intent data, Context context) {
        boolean result = false;
        //Считывание файла в поток бит
        InputStream dataStream = null;
        try {
            dataStream = context.getContentResolver().openInputStream(data.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (InJson) { //Если в JSON
            JSONObject jsonObject = new JSONObject();
            StringBuilder jsonStringBuilder = new StringBuilder();
            StringBuffer inputString = new StringBuffer();
            //Считывание содержимого файла в строку
            BufferedReader d = new BufferedReader(new InputStreamReader(dataStream));
            try {
                String tmp;
                while ((tmp = d.readLine()) != null) {
                    inputString.append(tmp);
                }
                result = true;
            } catch (IOException e) {
                Log.e("ОШИБКА ЧТЕНИЯ ФАЙЛА JSON:", e.toString());
            }

            //Преобразование считанной строки в объект
            try {
                jsonObject= new JSONObject(inputString.toString());
            } catch (JSONException e) {
                Log.e("Ошибка преобразования строки в JSON", e.toString());
            }

            //Получение данных БД из строки
            DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(context);
            dataBaseHelper.importDbFromJson(jsonObject);
            dataBaseHelper.close();

        } else { //Если файл DB

            //Удалить старый файл и заменить новым
            File dst = new File(PATH);
            try {
                dst.delete();
                dst.createNewFile();
                try (OutputStream out = new FileOutputStream(dst)) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = dataStream.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    result = true;
                } catch (IOException e) {
                    Log.e("ОШИБКА ЗАПИСИ ФАЙЛА:", e.toString());
                }
            } catch (IOException e) {
                Log.e("ОШИБКА ЧТЕНИЯ ФАЙЛА:", e.toString());
            }
        }
        return result;
    }
}
