package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentAddingnewmesInfoBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Фрагмент ввода данных о новом измерении: имя, комментарий.
 */
public class AddNewMesInfoFragment extends Fragment {
    //Объект для связки xml-разметки и фрагмента
    private FragmentAddingnewmesInfoBinding binding;

    //Процедура инициализации фрагмента, связывание с разметкой
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAddingnewmesInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Процедура инициализации объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Установка действия на кнопку "Далее"
        //Сохранение введенных данных и переход к следующему фрагменту
        binding.buttonAddNewMesInfoToNextScreen.setOnClickListener(view1 -> {
            if (validateForm()) {
                MeasurementGlobalInfo.setMesName(String.valueOf(binding.editboxAddNewMesInfoName.getText()).trim());
                MeasurementGlobalInfo.setMesComment(String.valueOf(binding.editboxAddNewMesInfoComment.getText()).trim());
                NavHostFragment.findNavController(AddNewMesInfoFragment.this)
                        .navigate(R.id.action_AddNewMesInfoFragment_to_AddNewMesValuesFragment);
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
        String name = binding.editboxAddNewMesInfoName.getText().toString();
        String comment = binding.editboxAddNewMesInfoComment.getText().toString();
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