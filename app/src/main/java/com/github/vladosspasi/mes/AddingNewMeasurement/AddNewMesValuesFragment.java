package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.Adapters.AddingValueListAdapter;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.Settings.Templates.Template;
import com.github.vladosspasi.mes.databinding.FragmentAddingnewmesScalesanddevicesBinding;
import java.util.ArrayList;
import java.util.Objects;

import static com.github.vladosspasi.mes.DataBaseHelper.*;

public class AddNewMesValuesFragment extends Fragment {

    private FragmentAddingnewmesScalesanddevicesBinding binding;
    private ContentValues mesInfo;
    private ArrayList<ContentValues> scalesList;
    private RecyclerView recyclerView;

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

        AddingValueListAdapter adapter = new AddingValueListAdapter();
        recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.res_addNewMesDevice_values);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int tempId = MeasurementGlobalInfo.getTemplateId();
        if(tempId!=-1){
            DataBaseHelper dataBaseHelper = getInstance(getContext());
            Template temp = dataBaseHelper.getTemplateById(tempId);
            dataBaseHelper.close();

            ArrayList<ContentValues> sList = temp.getScalesList();

            for(int i =0; i<sList.size();i++){
                MeasurementGlobalInfo.addToScalesList(sList.get(i));
            }
            MeasurementGlobalInfo.setTemplateId(-1);
        }

        scalesList = MeasurementGlobalInfo.getScalesList();

        if (scalesList.size()==0) {
            recyclerView.setVisibility(RecyclerView.INVISIBLE);
            binding.textboxAddNewMesDeviceScalesTitle.setText("" +
                    "Вы пока что не добавили данные. Нажмите кнопку \"Добавить шкалу\" ниже.");
        }
        else{
            binding.textboxAddNewMesDeviceScalesTitle.setText("Список величин:");
            recyclerView.setVisibility(RecyclerView.VISIBLE);
            adapter.setItems(scalesList);
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

        binding.buttonAddNewMesDeviceSelectTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTemplate();
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
        MeasurementGlobalInfo.setScalesList(scalesList);
        NavHostFragment.findNavController(AddNewMesValuesFragment.this)
                .navigate(R.id.action_AddNewMesValuesFragment_to_AddNewMesSelectDeviceFragment);
    }

    public void selectTemplate(){
        NavHostFragment.findNavController(AddNewMesValuesFragment.this)
                .navigate(R.id.action_AddNewMesValuesFragment_to_selectTemplateFragment);
    }


    private boolean validate() {
        //TODO
        return true;
    }

    private boolean saveMesToDataBase() {

        ArrayList<String> valuesList = new ArrayList<>();

        //Считывание введенных значений
        for(int i =0; i<scalesList.size(); i++){
            View view = recyclerView.getChildAt(i);
            EditText editValue = view.findViewById(R.id.editText_AddingValueListElementView_valueEnter);
            valuesList.add(editValue.getText().toString());
        }

        DataBaseHelper dataBaseHelper = getInstance(getContext());
        boolean operationSuccess = dataBaseHelper.addMeasurement(mesInfo,scalesList,valuesList);
        dataBaseHelper.close();

        return operationSuccess;
    }

}
