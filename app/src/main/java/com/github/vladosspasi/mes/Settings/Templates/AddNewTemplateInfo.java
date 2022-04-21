package com.github.vladosspasi.mes.Settings.Templates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentAddnewtemplateInfoBinding;

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
                goNextScreen();
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

}
