package com.github.vladosspasi.mes.Settings.Templates;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.Adapters.ScalesListAdapter;
import com.github.vladosspasi.mes.AddingNewMeasurement.MeasurementGlobalInfo;
import com.github.vladosspasi.mes.AddingNewMeasurement.SelectScaleForAddingFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.databinding.FragmentSelectscaleforaddingBinding;

import java.util.ArrayList;

import static com.github.vladosspasi.mes.DataBaseHelper.FIELD_SCALES_ID;

public class SelectScaleForTemplate extends Fragment {

    FragmentSelectscaleforaddingBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSelectscaleforaddingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<ContentValues> scalesList;

        Bundle arg = this.getArguments();
        assert arg != null;
        int deviceId = arg.getInt("DeviceId");
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        scalesList = dataBaseHelper.getScalesByDeviceId(deviceId);
        dataBaseHelper.close();

        ScalesListAdapter scalesListAdapter = new ScalesListAdapter();
        scalesListAdapter.setItems(scalesList);

        binding.recViewSelectScaleScales.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recViewSelectScaleScales.setAdapter(scalesListAdapter);

        binding.recViewSelectScaleScales.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), binding.recViewSelectScaleScales, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ContentValues scale = scalesList.get(position);

                        Template template = GlobalTemplateInfo.getTemplate();
                        template.addToScalesList(scale);
                        GlobalTemplateInfo.setTemplate(template);

                        NavHostFragment.findNavController(SelectScaleForTemplate.this)
                                .navigate(R.id.action_selectScaleForTemplate_to_addNewTemplateScales);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }) {
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

