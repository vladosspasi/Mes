package com.github.vladosspasi.mes.AddingNewMeasurement;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentCreatedeviceInfoBinding;

import static com.github.vladosspasi.mes.DataBaseHelper.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewDeviceInfoFragment extends Fragment {

    private FragmentCreatedeviceInfoBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCreatedeviceInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonAddNewDeviceToNextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    Bundle arg = new Bundle();
                    arg.putString("deviceName", String.valueOf(binding.editboxAddNewDeviceName.getText()));
                    arg.putString("deviceComment", String.valueOf(binding.editboxAddNewDeviceComment.getText()));
                    arg.putString("deviceType", String.valueOf(binding.editboxAddNewDeviceType.getText()));
                    NavHostFragment.findNavController(AddNewDeviceInfoFragment.this)
                            .navigate(R.id.action_AddNewDeviceInfoFragment_to_AddNewDeviceScalesFragment, arg);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean validateForm() {
        //Получение строк
//        String name = String.valueOf(binding.editboxAddNewDeviceName.getText());
//        String comment = String.valueOf(binding.editboxAddNewDeviceComment.getText());
//        String type = String.valueOf(binding.editboxAddNewDeviceType.getText());
//
//        String regex = "[^a-z|а-я|0-9| |_]";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher;
//
//        String errorMessage = "Неверно заполненные поля.\n";
//        boolean error = false;
//
//        comment = comment.trim();
//        type = type.trim();
//
//        //Проверка названия
//        String nameRedacted = name.trim();
//        if (nameRedacted.length() == 0) {
//            errorMessage = errorMessage.concat("Поле \"Название\" не может быть пустым.\n");
//            error = true;
//        } else {
//            if (name.length() > 50) {
//                errorMessage = errorMessage.concat("Название не может быть длинее 50 символов.\n");
//                error = true;
//            } else if (name.length() < 5) {
//                errorMessage = errorMessage.concat("Название не может быть короче 5 символов.\n");
//                error = true;
//            }
//
//            nameRedacted = nameRedacted.replace(" ", "");    //обрезать пробелы в начале и конце
//
//            matcher = pattern.matcher(nameRedacted);
//
//            if (matcher.find()) {
//                errorMessage = errorMessage.concat("Название может содержать только буквы латиницы и кириллицы, цифры, пробелы и \"_\".\n");
//                error = true;
//            }
//
//            //TODO добавить проверку на существование имени в базе
//            //TODO добавить проверку на то что пользователь ввел _____________________
//        }
//
//        //Проверка комментария
//        if (comment.length() > 500) {
//            errorMessage = errorMessage.concat("Комментарий не может быть длинее 500 символов.\n");
//            error = true;
//        }
//
//        String commentRedacted = comment.trim().replace(" ", "");    //обрезать пробелы в начале и конце
//        matcher = pattern.matcher(commentRedacted);
//
//        if (matcher.find()) {
//            errorMessage = errorMessage.concat("Комментарий может содержать только буквы латиницы и кириллицы, цифры, пробелы и \"_\".\n");
//            error = true;
//        }
//
//        //Проверка типа
//        if (type.length() == 0) {
//            errorMessage = errorMessage.concat("Поле \"Тип\" не может быть пустым.\n");
//            error = true;
//        } else if (type.length() > 500) {
//            errorMessage = errorMessage.concat("Тип прибора не может быть длинее 500 символов.\n");
//            error = true;
//        }
//
//        if (error) {
//            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//            alert.setTitle("Ошибка!");
//            alert.setMessage(errorMessage);
//            alert.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.cancel();
//                }
//            });
//            alert.show();
//            return false;
//        }

        return true;
    }


}
