package com.github.vladosspasi.mes.Settings.Devices;

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
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.Adapters.ScalesListAdapter;
import com.github.vladosspasi.mes.databinding.FragmentEditdeviceBinding;
import static com.github.vladosspasi.mes.DataBaseHelper.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Фрагмент изменения данных прибора
 */
public class EditDeviceFragment extends Fragment {

    private FragmentEditdeviceBinding binding; //объект связка
    private RecyclerView recyclerView; //объект списка
    private ArrayList<ContentValues> scalesList; //список шкал
    private ScalesListAdapter adapter; //Адаптер списка шкал

    //Инициализация фрагмента
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentEditdeviceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Инициализация объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Получение айди прибора от предыдущего фрагмента
        Bundle bundle = getArguments();
        assert bundle != null;
        int deviceId = bundle.getInt("DeviceId");

        //Получение данных прибора
        ContentValues deviceData = GlobalDeviceInfo.getDeviceData();//получение сохраненных данных
        if (deviceData ==null|| deviceData.size()==0){//Если их нет, то получить из бд и сохранить
            DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
            scalesList = dataBaseHelper.getDeviceInfo(deviceId);
            dataBaseHelper.close();
            deviceData = scalesList.get(0);
            scalesList.remove(0);
            GlobalDeviceInfo.setDeviceData(deviceData);
            GlobalDeviceInfo.setScales(scalesList);
        }else{
            scalesList = GlobalDeviceInfo.getScales();
        }

        //Инициализация полей информации прибора
        binding.editTextEditdeviceName.setText(deviceData.getAsString("deviceName"));
        binding.editTextEditdeviceComment.setText(deviceData.getAsString("deviceComment"));
        binding.editTextEditdeviceType.setText(deviceData.getAsString("deviceType"));

        //Инициализация списка шкал
        recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.recview_editdevice_scales);
        adapter = new ScalesListAdapter();
        adapter.setItems(scalesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        //Дейстиве на кнопку "добавить шкалу" - Добавление новой шкалы в список
        binding.buttonEditdeviceAddScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Установить начальные данные
                ContentValues newScale = new ContentValues();
                newScale.put("scaleId", "no");
                newScale.put("scaleName", "");
                newScale.put("scaleUnit", "");
                newScale.put("scaleMax", "");
                newScale.put("scaleMin", "");
                newScale.put("scaleError", "");
                newScale.put("valuetypeName", 1);
                //Добавить и обновить список
                scalesList.add(newScale);
                GlobalDeviceInfo.setScales(scalesList);
                adapter.clearItems();
                adapter.setItems(scalesList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                recyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount()-1);
            }
        });

        //Действие на нажатие кнопки "Отмена"
        binding.buttonEditdeviceCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalDeviceInfo.clear();
                NavHostFragment.findNavController(EditDeviceFragment.this).
                        navigateUp(); //Вернуться назад
            }

        });

        //Действие на нажатие кнопки "Сохранить"
        binding.buttonEditdeviceSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateForm()){
                    saveChangesToDataBase(deviceId);
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("Изменения были сохранены.");
                    alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    NavHostFragment.findNavController(EditDeviceFragment.this).
                            navigateUp();
                }
            }
        });

        //Действие на нажатие на элемент списка
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    //короткое нажатие
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle arg = new Bundle();
                        arg.putInt("ScalePos", position);
                        NavHostFragment.findNavController(EditDeviceFragment.this)
                                .navigate(R.id.action_EditDeviceFragment_to_editScaleFragment, arg);
                        //переход к выбранной шкале для ее редактирования
                    }

                    //Длинное нажатие
                    @Override
                    public void onLongItemClick(View view, int position) {
                        //Создание диалога
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("Убрать шкалу из прибора?");
                        alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeScaleFromDevice(position); //удаление шкалы из прибора
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
                }));
    }

    //Уничтожение фрагмента при его закрытии
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Процедура сохранения изменений в базу данных
    private void saveChangesToDataBase(int dId) {
        //Считывание полей и помещение в контейнер
        ContentValues newDeviceData = new ContentValues();
        newDeviceData.put(FIELD_DEVICES_NAME,binding.editTextEditdeviceName.getText().toString());
        newDeviceData.put(FIELD_DEVICES_COMMENT,binding.editTextEditdeviceComment.getText().toString());
        newDeviceData.put(FIELD_DEVICES_TYPE,binding.editTextEditdeviceType.getText().toString());

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());//открытие класса для работы с бд
        dataBaseHelper.modifyDevice(dId, newDeviceData, scalesList);//Сохранение изменений в бд
        dataBaseHelper.close();
        GlobalDeviceInfo.clear(); //очистка сохраненных данных о приборе
    }

    //Процедура удаления прибора из устройства
    public void removeScaleFromDevice(int pos){
        //Удалить из списка и обновить
        scalesList.remove(pos);
        GlobalDeviceInfo.setScales(scalesList);
        adapter.clearItems();
        adapter.setItems(scalesList);
        recyclerView.setAdapter(adapter);
    }

    //Процедура валидации введенных данных
    private boolean validateForm() {

        //получение данных из полей ввода
        String name = binding.editTextEditdeviceName.getText().toString();
        String comment = binding.editTextEditdeviceComment.getText().toString();
        String type = binding.editTextEditdeviceType.getText().toString();
        name = name.trim();
        comment = comment.trim();

        String title = "Неверно заполнены поля!";
        String message = ""; //Сообщение для вывода
        boolean result = true; //заполнена ли форма правильно
        String namerg = "[^А-Яа-яA-Za-z\\d\\s(?:_)]"; //регулярное выражение для проверки
        Pattern nameptrn = Pattern.compile(namerg); //шаблон проверки
        Matcher namemtchr = nameptrn.matcher(name); //объект-сравниватель

        //Проверка полей название и комментарий
        if(namemtchr.lookingAt()){
            message = message.concat("Название может содержать только цифры, буквы, символы \" \" и \"_\".\n");
            result = false;
        }
        if(name.length()<3 || name.length()>30){
            message = message.concat("Название должно быть от 3 до 30 символов в длину.\n");
            result = false;
        }
        if(comment.length()>300){
            message = message.concat("Комментарий должен быть не более 300 символов в длину.\n");
            result = false;
        }
        if(type.length()>300){
            message = message.concat("Тип прибора должен быть не более 100 символов в длину.\n");
            result = false;
        }

        //Если была обнаружена ошибка ввода
        if(!result){
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setMessage(message);
            alert.setTitle(title);
            alert.setPositiveButton("Ок", (dialogInterface, i) -> dialogInterface.cancel());
            alert.show();
        }
        return result;
    }



}
