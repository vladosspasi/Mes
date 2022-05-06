package com.github.vladosspasi.mes.Settings.Templates;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentAddnewtemplateInfoBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewTemplateInfo extends Fragment {

    FragmentAddnewtemplateInfoBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddnewtemplateInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonAddNewTemplateInfoGoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()) goNextScreen();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void goNextScreen(){
        GlobalTemplateInfo.setTemplate(new Template());
        Template template = new Template();
        template.setName(binding.editTextAddNewTemplateInfoName.getText().toString());
        template.setComment(binding.editTextAddNewTemplateInfoComment.getText().toString());
        GlobalTemplateInfo.setTemplate(template);

        NavHostFragment.findNavController(AddNewTemplateInfo.this)
                .navigate(R.id.action_addNewTemplateInfo_to_addNewTemplateScales);

    }

    //Процедура валидации введенных данных
    private boolean validateForm() {

        //получение данных из полей ввода
        String name = binding.editTextAddNewTemplateInfoName.getText().toString();
        String comment = binding.editTextAddNewTemplateInfoComment.getText().toString();
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
