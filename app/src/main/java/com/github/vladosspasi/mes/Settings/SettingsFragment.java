package com.github.vladosspasi.mes.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.AddingNewMeasurement.AddNewMesValuesFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.Settings.ExportImport.ImporterExporter;
import com.github.vladosspasi.mes.databinding.FragmentScreenSettingsBinding;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SettingsFragment extends Fragment {

    private FragmentScreenSettingsBinding binding;

    private static final int PICKFILE_RESULT_CODE = 1;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScreenSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSettingsGoToDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingsFragment.this)
                        .navigate(R.id.action_SettingsFragment_to_DevicesFragment);
            }
        });

        binding.buttonSettingsGoToTemplates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingsFragment.this)
                        .navigate(R.id.action_SettingsFragment_to_templatesListFragment);
            }
        });

        binding.buttonSettingsClearDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearDatabase();
            }
        });

        binding.buttonSettingsExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDatabase();
            }
        });

        binding.buttonSettingsImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBrowser();
            }
        });

    }

    private void clearDatabase() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Очистка данных");
        alert.setMessage("Вы уверены что хотите удалить сохраненные данные? Восстановить их будет невозможно.");
        alert.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
                dataBaseHelper.eraseAllData();
                dataBaseHelper.close();
            }
        });
        alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alert.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void exportDatabase() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Экспорт");
        alert.setMessage("В каком формате экспортировать базу данных?");
        alert.setPositiveButton(".JSON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ImporterExporter.exportFile(true)) {
                    Toast.makeText(getContext(),
                            "Файл сохранен в папке \"Загрузки\" с именем " + ImporterExporter.savedFileName,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton(".DB", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ImporterExporter.exportFile(false)) {
                    Toast.makeText(getContext(),
                            "Файл сохранен в папке \"Загрузки\" с именем " + ImporterExporter.savedFileName,
                            Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getContext(),"Ошибка!", Toast.LENGTH_LONG);
                }
            }
        });

        alert.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    private void showBrowser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent, "Выберите файл базы данных.");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }

    //Процедура, которая запускается после выбора файла
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri fileUri = data.getData();

                    Log.i("ПОЛУЧЕННЫЙ URI", fileUri.toString());
                    String filePath = fileUri.getPath();
                    Log.i("ПОЛУЧЕННЫЙ ПУТЬ", filePath);


                    switch (ImporterExporter.importFile(fileUri)) {
                        case 0:
                            Toast.makeText(getContext(), "База данных импортирована.", Toast.LENGTH_LONG).show();
                            break;
                    }

                } else {
                    Toast.makeText(getContext(), "Произошла ошибка!", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}