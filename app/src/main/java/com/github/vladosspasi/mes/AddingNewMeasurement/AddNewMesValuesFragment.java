package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentAddingnewmesScalesanddevicesBinding;
import java.util.ArrayList;
import java.util.Objects;

import static com.github.vladosspasi.mes.DataBaseHelper.*;

public class AddNewMesValuesFragment extends Fragment {

    private FragmentAddingnewmesScalesanddevicesBinding binding;
    private ContentValues mesInfo;
    private ArrayList<ContentValues> scalesInfo;
    private RecyclerView recyclerView;
    private ArrayList<Integer> scalesIds;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddingnewmesScalesanddevicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mesInfo = new ContentValues();
        mesInfo.put(FIELD_MES_NAME, MeasurementGlobalInfo.getMesName());
        mesInfo.put(FIELD_MES_COMMENT, MeasurementGlobalInfo.getMesComment());

        scalesIds = MeasurementGlobalInfo.getScalesIds();

        AddingValueListAdapter adapter = new AddingValueListAdapter();
        recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.res_addNewMesDevice_values);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (scalesIds.size()==0) {
            recyclerView.setVisibility(RecyclerView.INVISIBLE);
            binding.textboxAddNewMesDeviceScalesTitle.setText("" +
                    "Вы пока что не добавили данные. Нажмите кнопку \"Добавить шкалу\" ниже."
            );
        } else {
            binding.textboxAddNewMesDeviceScalesTitle.setText("Список величин:");
            recyclerView.setVisibility(RecyclerView.VISIBLE);

            scalesInfo = new ArrayList<>();
            DataBaseHelper dataBaseHelper = getInstance(getContext());
            ContentValues scaleInfo;

            for (Integer id : scalesIds) {
                scaleInfo = dataBaseHelper.getScaleById(id);
                scalesInfo.add(scaleInfo);
            }
            dataBaseHelper.close();
            adapter.setItems(scalesInfo);
            recyclerView.setAdapter(adapter);
        }

        binding.buttonAddNewMesDeviceAddvalue.setOnClickListener(view1 -> addNewScaleToList());

        binding.buttonAddNewMesDeviceSaveMes.setOnClickListener(view12 -> {
            if (validate()) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                if(saveMesToDataBase()){
                    alertDialog.setTitle("Готово!");
                    alertDialog.setMessage("Измерение успешно добавлено в базу данных");
                }else{
                    alertDialog.setTitle("Ошибка!");
                    alertDialog.setMessage("Возникла непредвиденная проблема.");
                }

                MeasurementGlobalInfo.clearAll();
                NavHostFragment.findNavController(AddNewMesValuesFragment.this)
                        .navigate(R.id.action_AddNewMesValuesFragment_to_MainMenuFragment);

                alertDialog.setPositiveButton("Ок", (dialogInterface, i) -> dialogInterface.cancel());
                alertDialog.show();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addNewScaleToList() {
        //Переход к экрану со списком приборов
        MeasurementGlobalInfo.setScalesIds(scalesIds);
        NavHostFragment.findNavController(AddNewMesValuesFragment.this)
                .navigate(R.id.action_AddNewMesValuesFragment_to_AddNewMesSelectDeviceFragment);

    }

    private boolean validate() {
        //TODO
        return true;
    }

    private boolean saveMesToDataBase() {

        ArrayList<String> valuesList = new ArrayList<>();

        //Считывание введенных значений
        for(int i =0; i<scalesInfo.size(); i++){
            View view = recyclerView.getChildAt(i);
            EditText editValue = view.findViewById(R.id.editText_AddingValueListElementView_valueEnter);
            valuesList.add(editValue.getText().toString());
        }

        DataBaseHelper dataBaseHelper = getInstance(getContext());
        boolean operationSuccess = dataBaseHelper.addMeasurement(mesInfo,scalesInfo,valuesList);
        dataBaseHelper.close();

        return operationSuccess;
    }
}
