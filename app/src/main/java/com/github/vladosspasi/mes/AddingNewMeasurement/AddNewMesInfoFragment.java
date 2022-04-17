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

public class AddNewMesInfoFragment extends Fragment {

    private FragmentAddingnewmesInfoBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddingnewmesInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonAddNewMesInfoToNextScreen.setOnClickListener(view1 -> {

            //TODO скрывать клавиатуру по нажатию на пустое место экрана
            if (validateForm()) {
                MeasurementGlobalInfo.setMesName(String.valueOf(binding.editboxAddNewMesInfoName.getText()));
                MeasurementGlobalInfo.setMesComment(String.valueOf(binding.editboxAddNewMesInfoComment.getText()));
                NavHostFragment.findNavController(AddNewMesInfoFragment.this)
                        .navigate(R.id.action_AddNewMesInfoFragment_to_AddNewMesValuesFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean validateForm() {


        return true;
    }
}