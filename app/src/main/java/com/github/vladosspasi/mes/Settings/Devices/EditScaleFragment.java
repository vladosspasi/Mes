package com.github.vladosspasi.mes.Settings.Devices;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentEditscaleBinding;
import java.util.ArrayList;

/**
 * Фрагмент редактирования шкалы прибора
 */
public class EditScaleFragment extends Fragment {

    private FragmentEditscaleBinding binding; //объект связка

    //Инициализация фрагмента
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentEditscaleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Инициализация объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Получение номера шкалы в списке
        Bundle bundle = getArguments();
        int scalePosition = bundle.getInt("ScalePos");
        ContentValues scaleInfo = GlobalDeviceInfo.getScaleAt(scalePosition);

        //Ввод текущих значений в поля ввода
        binding.edittextEditScaleName.setText(scaleInfo.getAsString("scaleName"));
        binding.edittextEditScaleError.setText(scaleInfo.getAsString("scaleError"));
        binding.edittextEditScaleUnit.setText(scaleInfo.getAsString("scaleUnit"));
        binding.edittextEditScaleMin.setText(scaleInfo.getAsString("scaleMin"));
        binding.edittextEditScaleMax.setText(scaleInfo.getAsString("scaleMax"));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEditScaleType.setAdapter(adapter);
        binding.spinnerEditScaleType.setSelection(scaleInfo.getAsInteger("scaleTypeId") - 1);

        //Действие на кнопку "Сохранить"
        binding.buttonEditScaleSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges(scalePosition); //Сохранить изменения
                Toast.makeText(getContext(), "Шкала обновлена.", Toast.LENGTH_SHORT); //сообщение
                NavHostFragment.findNavController(EditScaleFragment.this).navigateUp(); //вернуться назад
            }
        });

        binding.spinnerEditScaleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        binding.edittextEditScaleUnit.setEnabled(true);
                        binding.edittextEditScaleError.setEnabled(true);
                        binding.edittextEditScaleMin.setEnabled(true);
                        binding.edittextEditScaleMax.setEnabled(true);
                        binding.edittextEditScaleUnit.setText("");
                        binding.edittextEditScaleError.setText("");
                        binding.edittextEditScaleMin.setText("");
                        binding.edittextEditScaleMax.setText("");
                        break;
                    case 1:
                    case 3:
                        binding.edittextEditScaleUnit.setEnabled(false);
                        binding.edittextEditScaleError.setEnabled(false);
                        binding.edittextEditScaleMin.setEnabled(false);
                        binding.edittextEditScaleMax.setEnabled(false);
                        binding.edittextEditScaleUnit.setText("-");
                        binding.edittextEditScaleError.setText("-");
                        binding.edittextEditScaleMin.setText("-");
                        binding.edittextEditScaleMax.setText("-");
                        break;
                    case 2:
                        binding.edittextEditScaleUnit.setEnabled(true);
                        binding.edittextEditScaleError.setEnabled(true);
                        binding.edittextEditScaleMin.setEnabled(false);
                        binding.edittextEditScaleMax.setEnabled(false);
                        binding.edittextEditScaleUnit.setText("");
                        binding.edittextEditScaleError.setText("");
                        binding.edittextEditScaleMin.setText("-");
                        binding.edittextEditScaleMax.setText("-");
                        break;
                    default:
                        Toast.makeText(getContext(),"Ошибка типа данных!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    //Процедура уничтожения фрагмента при закрытии
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Процедура сохранения изменений шкалы
    private void saveChanges(int pos) {

        //Получить шкалу до ее изменения
        ArrayList<ContentValues> scalesList = GlobalDeviceInfo.getScales();
        String scaleID = scalesList.get(pos).getAsString("scaleId");
        ContentValues newScale = new ContentValues();

        //Считать новые данные
        int selectedType = binding.spinnerEditScaleType.getSelectedItemPosition();
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        String type = dataBaseHelper.getValueTypeById(selectedType + 1);
        dataBaseHelper.close();
        newScale.put("scaleId", scaleID);
        newScale.put("scaleName", binding.edittextEditScaleName.getText().toString());
        newScale.put("scaleUnit", binding.edittextEditScaleUnit.getText().toString());
        newScale.put("scaleMax", binding.edittextEditScaleMax.getText().toString());
        newScale.put("scaleMin", binding.edittextEditScaleMin.getText().toString());
        newScale.put("scaleError", binding.edittextEditScaleError.getText().toString());
        newScale.put("scaleTypeId", selectedType + 1);
        newScale.put("valuetypeName", type);
        GlobalDeviceInfo.setScaleAt(pos, newScale);//Обновить информацию
    }

    //Процедура проверки введенных значений
    public boolean validateForm(){
        boolean result = true;
        String title = "Неверно заполнены поля!";
        String message = ""; //Сообщение для вывода

        if(binding.edittextEditScaleName.getText().toString().trim().equals("") ||
                binding.edittextEditScaleMax.getText().toString().trim().equals("")||
                binding.edittextEditScaleMin.getText().toString().trim().equals("")||
                binding.edittextEditScaleError.getText().toString().trim().equals("")||
                binding.edittextEditScaleUnit.getText().toString().trim().equals("")
        ){
            result = false;
            message = message.concat("Все поля должны быть заполнены.\n");
        }else{

            if(binding.edittextEditScaleName.getText().toString().length()<3||
                    binding.edittextEditScaleName.getText().toString().length()>30){
                result = false;
                message = message.concat("Название должно быть от 3 до 30 символов в длину.\n");
            }

            int typeN = binding.spinnerEditScaleType.getSelectedItemPosition();
            switch (typeN){
                case 0:

                    try{
                        float max = Float.parseFloat(binding.edittextEditScaleMax.getText().toString().trim());
                        float min = Float.parseFloat(binding.edittextEditScaleMin.getText().toString().trim());
                        float error = Float.parseFloat(binding.edittextEditScaleError.getText().toString().trim());

                        if(max<min){
                            result = false;
                            message = message.concat("Максимальное значение не может быть меньше чем минимальное.\n");
                        }
                        if(max==min){
                            result = false;
                            message = message.concat("Максимальное значение не может быть равно минимальному.\n");
                        }
                        if(error>=max){
                            result = false;
                            message = message.concat("Погрешность не может равняться максимальному значению шкалы.\n");
                        }
                    }catch (Exception exception){
                        result = false;
                        message = message.concat("В числовые поля нельзя вводить строковые значения.\n");
                    }

                    if(binding.edittextEditScaleUnit.getText().toString().trim().length()==0||
                            binding.edittextEditScaleUnit.getText().toString().trim().length()>10){
                        result = false;
                        message = message.concat("Длина названия единицы измерения должна быть от 1 до 10 сиволов.\n");
                    }

                    break;
                case 2:
                    try{
                        float error = Float.parseFloat(binding.edittextEditScaleError.getText().toString().trim());
                    }catch (Exception exception){
                        result = false;
                        message = message.concat("В числовые поля нельзя вводить строковые значения.\n");
                    }

                    if(binding.edittextEditScaleUnit.getText().toString().trim().length()==0||
                            binding.edittextEditScaleUnit.getText().toString().trim().length()>10){
                        result = false;
                        message = message.concat("Длина названия единицы измерения должна быть от 1 до 10 сиволов.\n");
                    }
                    break;
            }
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
