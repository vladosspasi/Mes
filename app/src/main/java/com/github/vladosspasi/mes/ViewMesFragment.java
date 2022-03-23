package com.github.vladosspasi.mes;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.github.vladosspasi.mes.databinding.FragmentScreenListBinding;
import com.github.vladosspasi.mes.databinding.FragmentScreenViewmesBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ViewMesFragment extends Fragment {

    private FragmentScreenViewmesBinding binding;
    private RecyclerView recView;

    private static Logger log = Logger.getLogger(ViewMesFragment.class.getName());

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScreenViewmesBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Bundle arg = this.getArguments();
        assert arg != null;
        Integer mesId = arg.getInt("MesData");

        DataBaseHelper dbHelper = DataBaseHelper.getInstance(getContext()); //Подключение дб
        ArrayList<ContentValues> mesData = dbHelper.getMeasurmentData(mesId);
        dbHelper.close();
        ContentValues mesInfo = mesData.get(0);

        binding.textViewViewMesName.setText("Измерение \""+mesInfo.getAsString("name")+"\"");
        binding.textViewViewMesComment.setText("Комментарий: "+mesInfo.getAsString("comment"));
        binding.textViewViewMesDate.setText("Дата снятия: "+mesInfo.getAsString("date"));
        binding.textViewViewMesDevicename.setText("Прибор: "+mesInfo.getAsString("deviceName"));
        binding.textViewViewMesDevicecomment.setText("Комментарий к прибору: "+mesInfo.getAsString("deviceComment"));
        binding.textViewViewMesDevicetype.setText("Тип прибора: "+mesInfo.getAsString("deviceType"));
        binding.textViewViewMesMestitle.setText("Снятые значения");

        mesData.remove(0);

        recView = getActivity().findViewById(R.id.resView_ViewMes_mesurements);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ValuesListAdapter valuesListAdapter = new ValuesListAdapter();
        valuesListAdapter.setItems(mesData);
        recView.setAdapter(valuesListAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}