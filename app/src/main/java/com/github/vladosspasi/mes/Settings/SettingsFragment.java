package com.github.vladosspasi.mes.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.Settings.ExportImport.ImporterExporter;
import com.github.vladosspasi.mes.databinding.FragmentScreenSettingsBinding;

/**
 * Фрагмент настроек.
 */
public class SettingsFragment extends Fragment {

    private FragmentScreenSettingsBinding binding; //объект связка
    private static final int PICKFILE_RESULT_CODE = 1; //Результат операции выбора файла

    //Инициализация фрагмента
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentScreenSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Инициализация объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Установка действия на кнопку перехода к приборам
        binding.buttonSettingsGoToDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingsFragment.this)
                        .navigate(R.id.action_SettingsFragment_to_DevicesFragment);
            }
        });

        //Установка действия на кнопку перехода к шаблонам
        binding.buttonSettingsGoToTemplates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingsFragment.this)
                        .navigate(R.id.action_SettingsFragment_to_templatesListFragment);
            }
        });

        //Установка действия на кнопку "Очистить бд"
        binding.buttonSettingsClearDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearDatabase();
            }
        });

        //Установка действия на кнопку "Экспорт"
        binding.buttonSettingsExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDatabase();
            }
        });

        //Установка действия на кнопку "Импорт"
        binding.buttonSettingsImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBrowser();
            }
        });

    }

    //Процедура очистки базы данных
    private void clearDatabase() {
        //Создание диалога
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Очистка данных");
        alert.setMessage("Вы уверены что хотите удалить сохраненные данные? Восстановить их будет невозможно.");
        alert.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
                dataBaseHelper.eraseAllData(); //очистка содержимого бд
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

    //Уничтожение фрагмента при его закрытии
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Процедура экспорта базы данных
    private void exportDatabase() {
        //Создание диалога
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Экспорт");
        alert.setMessage("В каком формате экспортировать базу данных?");
        alert.setPositiveButton(".JSON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ImporterExporter.exportFile(true,getContext())) { //Если файл JSON был экспортирован
                    Toast.makeText(getContext(),
                            "Файл сохранен в папке \"Загрузки\" с именем " + ImporterExporter.savedFileName,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton(".DB", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ImporterExporter.exportFile(false, getContext())) { //Если файл DB был экспортирован
                    Toast.makeText(getContext(),
                            "Файл сохранен в папке \"Загрузки\" с именем " + ImporterExporter.savedFileName,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_LONG).show();
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

    //Процедура вывода проводника файловой системы устройства для выбора файла
    private void showBrowser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent, "Выберите файл базы данных.");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }

    //Процедура, которая запускается после выбора файла
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_RESULT_CODE) { //Если код запроса совпадает
            if (resultCode == Activity.RESULT_OK) { //Если код результата совпадает
                if (data != null) { //и если файл выбран
                    //Создать диалог
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("Выберите формат файла:");
                    alert.setPositiveButton("DB", (dialogInterface, i) -> {
                        if (ImporterExporter.importFile(false, data, getContext())) { //Если файл DB импортирован
                            Toast.makeText(getContext(), "База данных импортирована.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Произошла ошибка!", Toast.LENGTH_LONG).show();
                        }
                    });
                    alert.setNegativeButton("JSON", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ImporterExporter.importFile(true, data, getContext())) { //Если файл JSON импортирован
                                Toast.makeText(getContext(), "База данных импортирована.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Произошла ошибка!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    alert.show();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}