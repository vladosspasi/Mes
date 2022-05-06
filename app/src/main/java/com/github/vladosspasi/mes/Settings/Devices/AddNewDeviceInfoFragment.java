package com.github.vladosspasi.mes.Settings.Devices;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentCreatedeviceInfoBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Фрагмент создания нового шаблона - заполнение данных: имя, комментарий, тип прибора
 */
public class AddNewDeviceInfoFragment extends Fragment {

    private FragmentCreatedeviceInfoBinding binding;//объект связка

    //Процедура инициализации фрагмента
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentCreatedeviceInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Процедура инициализации объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Установка действия на кнопку "Далее"
        binding.buttonAddNewDeviceToNextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    Bundle arg = new Bundle();
                    //Прочесть введенные данные, ...
                    arg.putString("deviceName", String.valueOf(binding.editboxAddNewDeviceName.getText()));
                    arg.putString("deviceComment", String.valueOf(binding.editboxAddNewDeviceComment.getText()));
                    arg.putString("deviceType", String.valueOf(binding.editboxAddNewDeviceType.getText()));
                    //... и передать их на следующий экран с переходом на него
                    NavHostFragment.findNavController(AddNewDeviceInfoFragment.this)
                            .navigate(R.id.action_AddNewDeviceInfoFragment_to_AddNewDeviceScalesFragment, arg);
                }
            }
        });
    }

    //Процедура уничтожения фрагмента при его закрытии
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Процедура валидации введенных данных
    private boolean validateForm() {

        //получение данных из полей ввода
        String name = binding.editboxAddNewDeviceName.getText().toString();
        String comment = binding.editboxAddNewDeviceComment.getText().toString();
        String type = binding.editboxAddNewDeviceComment.getText().toString();
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
