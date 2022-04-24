package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.Adapters.AddingValueListAdapter;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
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
            for (ContentValues contentValues : sList) {
                MeasurementGlobalInfo.addToScalesList(contentValues);
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
                saveMesToDataBase();
                Toast.makeText(getContext(),"Измерение успешно добавлено в базу данных!",Toast.LENGTH_SHORT).show();
                MeasurementGlobalInfo.clearAll();
                NavHostFragment.findNavController(AddNewMesValuesFragment.this)
                        .navigate(R.id.action_AddNewMesValuesFragment_to_MainMenuFragment);
            }
        });

        binding.buttonAddNewMesDeviceSelectTemplate.setOnClickListener(view13 -> selectTemplate());

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("Убрать шкалу из измерения??");
                        alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeScale(position);
                                dialogInterface.cancel();
                            }
                        });
                        alert.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alert.show();
                    }
                }){
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

    private void removeScale(int position){
        scalesList.remove(position);
        MeasurementGlobalInfo.setScalesList(scalesList);
        AddingValueListAdapter adapter = new AddingValueListAdapter();
        adapter.setItems(scalesList);
        recyclerView.setAdapter(adapter);

        if(scalesList.size()==0){
            binding.textboxAddNewMesDeviceScalesTitle.setText("" +
                    "Вы пока что не добавили данные. Нажмите кнопку \"Добавить шкалу\" ниже.");
        }else{
            binding.textboxAddNewMesDeviceScalesTitle.setText("Список величин:");
        }
    }

    public void selectTemplate(){
        NavHostFragment.findNavController(AddNewMesValuesFragment.this)
                .navigate(R.id.action_AddNewMesValuesFragment_to_selectTemplateFragment);
    }

    private boolean validate() {
        //TODO
        return true;
    }

    private void saveMesToDataBase() {
        ArrayList<String> valuesList = new ArrayList<>();
        //Считывание введенных значений
        for(int i =0; i<scalesList.size(); i++){
            View view = recyclerView.getChildAt(i);
            EditText editValue = view.findViewById(R.id.editText_AddingValueListElementView_valueEnter);
            valuesList.add(editValue.getText().toString());
        }
        DataBaseHelper dataBaseHelper = getInstance(getContext());
        dataBaseHelper.addMeasurement(mesInfo,scalesList,valuesList);
        dataBaseHelper.close();
    }
}
