package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentAddingnewmesInfoBinding;

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
                MeasurementGlobalInfo.setMesName(String.valueOf(binding.editboxAddNewMesInfoName.getText()));
                MeasurementGlobalInfo.setMesComment(String.valueOf(binding.editboxAddNewMesInfoComment.getText()));
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
        //TODO
        return true;
    }
}