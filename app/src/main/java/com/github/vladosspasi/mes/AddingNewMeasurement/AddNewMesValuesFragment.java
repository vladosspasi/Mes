package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.Adapters.AddingValueListAdapter;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.Settings.Templates.Template;
import com.github.vladosspasi.mes.databinding.FragmentAddingnewmesScalesanddevicesBinding;

import java.util.ArrayList;
import java.util.Objects;

import static com.github.vladosspasi.mes.DataBaseHelper.*;

/**
 * Фрагмент ввода данных о новом измерении: снятые значения измерительных приборов
 */
public class AddNewMesValuesFragment extends Fragment {

    private FragmentAddingnewmesScalesanddevicesBinding binding; //Связка разметки и фрагмента
    private ContentValues mesInfo; //Данные о измерении
    private ArrayList<ContentValues> scalesList; //Список шкал
    private RecyclerView recyclerView; //Объект списка на экране

    //Процедура инициализации фрагмента, связывание с разметкой
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAddingnewmesScalesanddevicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Процедура инициализации объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Получение данных о измерении, введенных во фрагменте AddNewMesInfoFragment
        mesInfo = new ContentValues();
        mesInfo.put(FIELD_MES_NAME, MeasurementGlobalInfo.getMesName());
        mesInfo.put(FIELD_MES_COMMENT, MeasurementGlobalInfo.getMesComment());

        //Инициализация списка
        AddingValueListAdapter adapter = new AddingValueListAdapter();
        recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.res_addNewMesDevice_values);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Если был использован шаблон, то применить шаблон
        //Вернуть шкалы, которые уже были, и добавить к ним шкалы шаблона
        int tempId = MeasurementGlobalInfo.getTemplateId();
        if (tempId != -1) {
            DataBaseHelper dataBaseHelper = getInstance(getContext());
            Template temp = dataBaseHelper.getTemplateById(tempId);
            dataBaseHelper.close();
            ArrayList<ContentValues> sList = temp.getScalesList();
            for (ContentValues contentValues : sList) {
                MeasurementGlobalInfo.addToScalesList(contentValues);
            }
            MeasurementGlobalInfo.setTemplateId(-1);
        }

        //Получение списка шкал
        scalesList = MeasurementGlobalInfo.getScalesList();

        //Если список шкал пуст, то сообщение
        if (scalesList.size() == 0) {
            recyclerView.setVisibility(RecyclerView.INVISIBLE);
            binding.textboxAddNewMesDeviceScalesTitle.setText("" +
                    "Вы пока что не добавили данные. Нажмите кнопку \"Добавить шкалу\" ниже.");
        }
        //иначе вывести
        else {
            binding.textboxAddNewMesDeviceScalesTitle.setText("Список величин:");
            recyclerView.setVisibility(RecyclerView.VISIBLE);
            adapter.setItems(scalesList);
            recyclerView.setAdapter(adapter);
        }

        //Установка действия на кнопку "Добавить шкалу"
        binding.buttonAddNewMesDeviceAddvalue.setOnClickListener(view1 -> addNewScaleToList());

        //Установка действия на кнопку "Сохранить"
        binding.buttonAddNewMesDeviceSaveMes.setOnClickListener(view12 -> {
            if (validate()) {
                saveMesToDataBase();
                Toast.makeText(getContext(), "Измерение успешно добавлено в базу данных!", Toast.LENGTH_SHORT).show();
                MeasurementGlobalInfo.clearAll();
                NavHostFragment.findNavController(AddNewMesValuesFragment.this)
                        .navigate(R.id.action_AddNewMesValuesFragment_to_MainMenuFragment);
            }
        });

        //Установка действия на кнопку "Выбрать шаблон"
        binding.buttonAddNewMesDeviceSelectTemplate.setOnClickListener(view13 -> selectTemplate());

        //Установка действия на нажатие на элемент списка
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }

                    //Долгое нажатие - удаление шкалы из списка
                    @Override
                    public void onLongItemClick(View view, int position) {
                        //создание диалогового окна
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("Убрать шкалу из измерения??");
                        alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeScale(position);  //Удаление
                                dialogInterface.cancel();
                            }
                        });
                        alert.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alert.show();
                    }
                }) {
                });
    }

    //Процедура уничтожения фрагмента при его закрытии
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Процедура добавления шкалы в список
    private void addNewScaleToList() {
        MeasurementGlobalInfo.setScalesList(scalesList);//сохранение текущего списка шкал
        //Переход к экрану со списком приборов
        NavHostFragment.findNavController(AddNewMesValuesFragment.this)
                .navigate(R.id.action_AddNewMesValuesFragment_to_AddNewMesSelectDeviceFragment);
    }

    //Процедура удаления шкалы из списка
    private void removeScale(int position) {
        scalesList.remove(position); //удалить из списка элементов
        MeasurementGlobalInfo.setScalesList(scalesList);// сохранить новый список
        AddingValueListAdapter adapter = new AddingValueListAdapter();
        adapter.setItems(scalesList);
        recyclerView.setAdapter(adapter);//вывести новый список

        if (scalesList.size() == 0) {//если список пуст
            binding.textboxAddNewMesDeviceScalesTitle.setText("" +
                    "Вы пока что не добавили данные. Нажмите кнопку \"Добавить шкалу\" ниже.");
        } else {
            binding.textboxAddNewMesDeviceScalesTitle.setText("Список величин:");
        }
    }

    //Процедура выбора шаблона
    public void selectTemplate() {
        //переход к фрагменту выбора шаблона
        NavHostFragment.findNavController(AddNewMesValuesFragment.this)
                .navigate(R.id.action_AddNewMesValuesFragment_to_selectTemplateFragment);
    }

    //Процедура валидации данных
    private boolean validate() {
        //TODO
        return true;
    }

    //Процедура сохранения данных в базу данных
    private void saveMesToDataBase() {
        ArrayList<String> valuesList = new ArrayList<>();
        //Считывание введенных значений
        for (int i = 0; i < scalesList.size(); i++) {
            View view = recyclerView.getChildAt(i);
            EditText editValue = view.findViewById(R.id.editText_AddingValueListElementView_valueEnter);
            valuesList.add(editValue.getText().toString());
        }
        DataBaseHelper dataBaseHelper = getInstance(getContext()); //инициализация класса для работы с бд
        dataBaseHelper.addMeasurement(mesInfo, scalesList, valuesList); //передача данных
        dataBaseHelper.close();
    }
}
