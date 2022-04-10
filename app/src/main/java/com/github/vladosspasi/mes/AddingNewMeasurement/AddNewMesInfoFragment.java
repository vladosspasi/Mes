package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentAddingnewmesInfoBinding;
import static com.github.vladosspasi.mes.DataBaseHelper.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewMesInfoFragment extends Fragment {

    private FragmentAddingnewmesInfoBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddingnewmesInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonAddNewMesInfoToNextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO скрывать клавиатуру по нажатию на пустое место экрана
                if (validateForm()) {
                    MeasurementGlobalInfo.setMesName(String.valueOf(binding.editboxAddNewMesInfoName.getText()));
                    MeasurementGlobalInfo.setMesComment(String.valueOf(binding.editboxAddNewMesInfoComment.getText()));

                    Log.println(Log.DEBUG, "КОНТРОЛЬ" , "Название измерения: "+ MeasurementGlobalInfo.getMesName());


                    NavHostFragment.findNavController(AddNewMesInfoFragment.this)
                            .navigate(R.id.action_AddNewMesInfoFragment_to_AddNewMesValuesFragment);
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

       /*
        //Получение строк
        String name = String.valueOf(binding.editboxAddNewMesInfoName.getText());
        String comment = String.valueOf(binding.editboxAddNewMesInfoComment.getText());

        String regex = "[^a-z|а-я|0-9| |_]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;

        String errorMessage = "Неверно заполненные поля.\n";
        boolean error = false;

        //Проверка названия
        String nameRedacted = name.trim();
        if (nameRedacted.length() == 0) {
            errorMessage = errorMessage.concat("Поле \"Название\" не может быть пустым.\n");
            error = true;
        } else {
            if (name.length() > 50) {
                errorMessage = errorMessage.concat("Название не может быть длинее 50 символов.\n");
                error = true;
            } else if (name.length() < 5) {
                errorMessage = errorMessage.concat("Название не может быть короче 5 символов.\n");
                error = true;
            }

            nameRedacted = nameRedacted.replace(" ", "");    //обрезать пробелы в начале и конце

            matcher = pattern.matcher(nameRedacted);

            if (matcher.find()) {
                errorMessage = errorMessage.concat("Название может содержать только буквы латиницы и кириллицы, цифры, пробелы и \"_\".\n");
                error = true;
            }

            //TODO добавить проверку на существование имени в базе
            //TODO добавить проверку на то что пользователь ввел _____________________
        }

        //Проверка комментария
        if (comment.length() > 500) {
            errorMessage = errorMessage.concat("Комментарий не может быть длинее 500 символов.\n");
            error = true;
        }

        String commentRedacted = comment.trim().replace(" ", "");    //обрезать пробелы в начале и конце
        matcher = pattern.matcher(commentRedacted);

        if (matcher.find()) {
            errorMessage = errorMessage.concat("Комментарий может содержать только буквы латиницы и кириллицы, цифры, пробелы и \"_\".\n");
            error = true;
        }


        if (error) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Ошибка!");
            alert.setMessage(errorMessage);
            alert.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.show();
            return false;
        }
*/
        return true;
    }
}