package com.github.vladosspasi.mes.Settings.ExportImport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.URI;

public class ImporterExporter {

    public static final String fileName = "mesdb";
    public static String savedFileName = null;
    private static String PATH = "/data/data/com.github.vladosspasi.mes/databases/mesdb";

    public static boolean exportFile(boolean InJson) {
        boolean result = false;
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        if (InJson) {

            //TODO экспорт в JSON

        } else {

            String newFileName = fileName;
            File src = new File(PATH);

            try (InputStream in = new FileInputStream(src)) {

                File dst = new File(downloadsFolder, newFileName);
                int i = 1;
                while (!dst.createNewFile()) {
                    newFileName = fileName + i;
                    dst = new File(downloadsFolder, newFileName);
                    i++;
                }
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

    public static boolean importFile(Intent data, Context context){
        boolean result = false;

        InputStream dataStream = null;
        try {
            dataStream = context.getContentResolver().openInputStream(data.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //TODO сделать обработку для JSON

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
        return result;
    }

}
