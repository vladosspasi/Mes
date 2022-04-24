package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.Adapters.TemplateListAdapter;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.databinding.FragmentSelecttemplateBinding;

import java.util.ArrayList;

public class SelectTemplateFragment extends Fragment {

    private FragmentSelecttemplateBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSelecttemplateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TemplateListAdapter adapter = new TemplateListAdapter();
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        ArrayList<ContentValues> templatesList = dataBaseHelper.getTemplates();
        dataBaseHelper.close();
        adapter.setItems(templatesList);

        binding.recviewSelectTemplateList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recviewSelectTemplateList.setAdapter(adapter);

        binding.recviewSelectTemplateList.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), binding.recviewSelectTemplateList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MeasurementGlobalInfo.setTemplateId(templatesList.get(position).getAsInteger("tempId"));
                        NavHostFragment.findNavController(SelectTemplateFragment.this)
                                .navigateUp();
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }){
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
