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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if (validateForm()) {
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

    //Процедура сохранения данных в базу данных
    private void saveMesToDataBase() {
        ArrayList<String> valuesList = new ArrayList<>();
        //Считывание введенных значений
        for (int i = 0; i < scalesList.size(); i++) {
            View view = recyclerView.getChildAt(i);
            EditText editValue = view.findViewById(R.id.editText_AddingValueListElementView_valueEnter);
            valuesList.add(editValue.getText().toString().trim());
        }
        DataBaseHelper dataBaseHelper = getInstance(getContext()); //инициализация класса для работы с бд
        dataBaseHelper.addMeasurement(mesInfo, scalesList, valuesList); //передача данных
        dataBaseHelper.close();
    }

    //Процедура валидации данных
    private boolean validateForm() {

        String dreg = "[^\\d(?:.)]";
        String binreg = "[^10]";
        String diareg = "^([0-9]{1,16}(.){0,1}[0-9]{0,16}-[0-9]{1,16}(.){0,1}[0-9]{0,16})$";

        Pattern dptrn = Pattern.compile(dreg);
        Pattern binptrn = Pattern.compile(binreg);
        Pattern diaptrn = Pattern.compile(diareg);

        Matcher mtchr;

        String title = "Неверно заполненные значения!";
        String message = "";
        boolean result = true;

        //Получение введенных данных
        for (int i = 0; i < scalesList.size(); i++) {
            View view = recyclerView.getChildAt(i);
            EditText editValue = null;

            try {
                editValue = view.findViewById(R.id.editText_AddingValueListElementView_valueEnter);
            } catch (NullPointerException e) {
                result = false;
                message = message.concat("Все добавленные значения должны быть заполнены.\n");
            }
            if (result) {

                String value = editValue.getText().toString().trim();
                String type = scalesList.get(i).getAsString("valuetypeName");

                //Введено ли значение
                if (value.length() == 0) {
                    result = false;
                    message = message.concat("Все добавленные значения должны быть заполнены.\n");
                } else if (value.length() > 50) {
                    result = false;
                    message = message.concat("Слишком длинное введенное значение - не может длиной до 50 символов.\n");
                } else {

                    //Проверки для каждого типа данных отдельно
                    if (type.equals("Численный")) {
                        mtchr = dptrn.matcher(value);
                        if (mtchr.lookingAt()) {
                            result = false;
                            message = message.concat("Значение шкалы \""+ scalesList.get(i).getAsString("scaleName") +"\" может содержать только цифры и \".\".\n");
                        }else{
                            if(Float.parseFloat(value)>scalesList.get(i).getAsFloat("scaleMax")){
                                result = false;
                                message = message.concat("Значение шкалы \""+ scalesList.get(i).getAsString("scaleName") +"\" " +
                                        "не может быть больше максимального значения ("+ scalesList.get(i).getAsString("scaleMax") +").\n");
                            }else if (Float.parseFloat(value)<scalesList.get(i).getAsFloat("scaleMin")){
                                result = false;
                                message = message.concat("Значение шкалы \""+ scalesList.get(i).getAsString("scaleName") +"\" " +
                                        "не может быть меньше минимального значения ("+ scalesList.get(i).getAsString("scaleMin") +").\n");
                            }
                        }

                    } else if (type.equals("Диапазон")) {
                        mtchr = diaptrn.matcher(value);
                        if (mtchr.lookingAt()) {
                            result = false;
                            message = message.concat(
                                    "Значение \""+ scalesList.get(i).getAsString("scaleName") +"\" может содержать только цифры, \".\" и обязано иметь \"-\" в середине.\n");
                        }

                    } else if (type.equals("Бинарный")) {
                        mtchr = binptrn.matcher(value);
                        if (mtchr.lookingAt()) {
                            result = false;
                            message = message.concat(
                                    "Значение \""+ scalesList.get(i).getAsString("scaleName") +"\" может содержать только 1 и 0.\n");
                            //TODO поменять текст на экране - булевый на бинарный при создании прибора
                            //TODO На экране бинарный и диапазон не на тех местах при создании прибора!!
                            //TODO Клавиатура не сразу выпадает при нажатии????
                        }

                    } else if (!type.equals("Строковый")){
                        result = false;
                        message = message.concat(
                                "ОШИБКА! "+ scalesList.get(i).getAsString("scaleName")+".\n");
                    }

                }
            }
        }

        if (!result) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle(title);
            alert.setMessage(message);
            alert.setPositiveButton("Ок", (dialogInterface, i) -> dialogInterface.cancel());
            alert.show();
        }
        return result;
    }
}
