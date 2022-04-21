package com.github.vladosspasi.mes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.vladosspasi.mes.Adapters.ScalesListAdapter;
import com.github.vladosspasi.mes.Adapters.TemplateListAdapter;
import com.github.vladosspasi.mes.Settings.Templates.Template;
import com.github.vladosspasi.mes.databinding.FragmentViewtemplateBinding;

import static com.github.vladosspasi.mes.DataBaseHelper.FIELD_TEMPLATES_NAME;

public class ViewTemplateFragment extends Fragment {

    FragmentViewtemplateBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentViewtemplateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        int tempId = bundle.getInt("TemplateId");
        DataBaseHelper db = DataBaseHelper.getInstance(getContext());
        Template template = db.getTemplateById(tempId);
        db.close();

        binding.textViewViewTemplateName.setText("Название шаблона: "+template.getName());
        binding.textViewViewTemplateComment.setText("Комментарий: "+template.getComment());

        ScalesListAdapter scalesListAdapter = new ScalesListAdapter();
        scalesListAdapter.setItems(template.getScalesList());
        binding.recViewViewTemplateList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recViewViewTemplateList.setAdapter(scalesListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
