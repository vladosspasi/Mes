package com.github.vladosspasi.mes.Settings.Devices;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentCreatedeviceInfoBinding;

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
        //TODO
        return true;
    }


}
