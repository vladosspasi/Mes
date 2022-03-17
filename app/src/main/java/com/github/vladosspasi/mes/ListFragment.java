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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ListFragment extends Fragment {

    private FragmentScreenListBinding binding;
    private RecyclerView recView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScreenListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private static Logger log = Logger.getLogger(MainActivity.class.getName());

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DataBaseHelper dbHelper = DataBaseHelper.getInstance(getContext());
        ArrayList<ContentValues> mes = dbHelper.getMeasurementsList();

        recView = getActivity().findViewById(R.id.recyclerView_ListScreen_allMeasurements);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));


        MeasurementsListAdapter measurementsListAdapter = new MeasurementsListAdapter();
        measurementsListAdapter.setItems(mes);
        recView.setAdapter(measurementsListAdapter);

        /*String[] data = new String[] { "one", "two", "three", "four" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);*/


        /*binding.testbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String directory = getActivity().getBaseContext().getFilesDir().toString();
                //Toast.makeText(getContext(), directory, Toast.LENGTH_LONG).show();

                DataBaseHelper dbHelper = DataBaseHelper.getInstance(getContext());
                //ContentValues output = dbHelper.testDatabase();


            }
        });*/



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}