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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.Adapters.ScalesListAdapter;
import com.github.vladosspasi.mes.databinding.FragmentCreatedeviceScalesBinding;
import static com.github.vladosspasi.mes.DataBaseHelper.*;
import java.util.ArrayList;

/**
 * Фрагмет добавления нового прибора - добавление шкал в прибор
 */
public class AddNewDeviceScalesFragment extends Fragment {

    private FragmentCreatedeviceScalesBinding binding;//объект связка
    private ContentValues deviceInfo;//данные о приборе
    private RecyclerView recView;//объект списка
    ArrayList<ContentValues> scales;//список шкал
    ScalesListAdapter scalesListAdapter;//адаптер списка шкал

    //Процедура инициализации фрагмента
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentCreatedeviceScalesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Процедура инициализации объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Получение данных, введенных в прошлом фрагменте
        Bundle arg = getArguments();
        deviceInfo = new ContentValues();
        deviceInfo.put(FIELD_DEVICES_NAME, arg.getString("deviceName"));
        deviceInfo.put(FIELD_DEVICES_COMMENT, arg.getString("deviceComment"));
        deviceInfo.put(FIELD_DEVICES_TYPE, arg.getString("deviceType"));

        //Инициализация списка
        scales = new ArrayList<>();
        recView = getActivity().findViewById(R.id.rec_addNewDeviceScales_scalesList);
        recView.setVisibility(View.INVISIBLE);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        scalesListAdapter = new ScalesListAdapter();
        binding.textboxAddNewDeviceScalesListtitle.setText("Список добавленных шкал пуст. Добавьте шкалы ниже.");

        //Установка действия на кнопку "Добавить шкалу"
        binding.buttonAddNewDeviceScalesAddScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    addScaleToList();
                }
            }
        });

        //Установка действия на кнопку "Сохранить"
        binding.buttonAddNewDeviceScalesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDeviceToDB();
            }
        });

        //Инициализация выпадающего списка
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner_addNewDeviceScales_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    //Процедура уничтожения фрагмента при закрытии
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Процедура сохранения прибора в базу данных
    public void saveDeviceToDB(){

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());//открытие класса для работы с бд
        dataBaseHelper.addDevice(deviceInfo, scales);//добавление прибора

        //Вывод сообщения
        AlertDialog.Builder success = new AlertDialog.Builder(getContext());
        success.setMessage("Прибор успешно добавлен в базу данных. Теперь его можно использовать при добавлении измерений.");
        success.setTitle("Прибор добавлен!");
        success.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //возврат к списку приборов
                NavHostFragment.findNavController(AddNewDeviceScalesFragment.this).
                        navigate(R.id.action_AddNewDeviceScalesFragment_to_DevicesFragment);
            }
        });
        success.show();
        dataBaseHelper.close();
    }

    //Процедура добавления шкалы в прибор
    public void addScaleToList(){
        if (validateForm()){ //Если введенные значения верны
            ContentValues scale = new ContentValues();

            //Получение id типа данных
            int typeId = (int) binding.spinnerAddNewDeviceScalesType.getSelectedItemPosition()+1;
            DataBaseHelper dataBaseHelper = getInstance(getContext());
            String type = dataBaseHelper.getValueTypeById(typeId);
            dataBaseHelper.close();

            //Заполнение контейнера введенными данными
            scale.put("scaleName", String.valueOf(binding.editboxAddNewDeviceScalesName.getText()));
            scale.put("scaleUnit", String.valueOf(binding.editboxAddNewDeviceScalesUnit.getText()));
            scale.put("valuetypeId", typeId);
            scale.put("valuetypeName", type);
            scale.put("valueTypeId", typeId);
            scale.put("scaleMin", String.valueOf(binding.editboxAddNewDeviceScalesFromvalue.getText()));
            scale.put("scaleMax", String.valueOf(binding.editboxAddNewDeviceScalesTovalue.getText()));
            scale.put("scaleError", String.valueOf(binding.editboxAddNewDeviceScalesError.getText()));

            //Обновление списка
            binding.textboxAddNewDeviceScalesListtitle.setText("Шкалы прибора:");
            scales.add(scale);
            scalesListAdapter = new ScalesListAdapter();
            scalesListAdapter.setItems(scales);
            recView.setAdapter(scalesListAdapter);
            recView.setVisibility(View.VISIBLE);
        }
    }

    //Процедура проверки введенных значений
    public boolean validateForm(){
        return true;
    }

}
