package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentAddingnewmesScalesanddevicesBinding;

import java.sql.Array;
import java.util.ArrayList;

import static com.github.vladosspasi.mes.DataBaseHelper.*;

public class AddNewMesValuesFragment extends Fragment {

    private FragmentAddingnewmesScalesanddevicesBinding binding;
    private ContentValues mesInfo;
    private ArrayList<ContentValues> scalesInfo;
    private RecyclerView recyclerView;
    private ArrayList<ParsInteger> scalesIds;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddingnewmesScalesanddevicesBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mesInfo = new ContentValues();
        mesInfo.put(FIELD_MES_NAME, args.getString("mesName"));
        mesInfo.put(FIELD_MES_COMMENT, args.getString("mesComment"));

        scalesIds = args.getParcelableArrayList("ScalesIds");

        AddingValueListAdapter adapter = new AddingValueListAdapter();
        recyclerView = getActivity().findViewById(R.id.res_addNewMesDevice_values);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (scalesIds == null || scalesIds.size()==0) {
            recyclerView.setVisibility(RecyclerView.INVISIBLE);
            binding.textboxAddNewMesDeviceScalesTitle.setText("" +
                    "Вы пока что не добавили данные. Нажмите кнопку \"Добавить шкалу\" ниже."
            );
            scalesIds = new ArrayList<>();
        } else {

            binding.textboxAddNewMesDeviceScalesTitle.setText("Список величин:");
            recyclerView.setVisibility(RecyclerView.VISIBLE);

            scalesInfo = new ArrayList<>();
            DataBaseHelper dataBaseHelper = getInstance(getContext());
            ContentValues scaleInfo;

            //for (int id : scalesIds) {
            for (ParsInteger id : scalesIds) {
                scaleInfo = new ContentValues();
                scaleInfo = dataBaseHelper.getScaleById(id.getValue());
                scalesInfo.add(scaleInfo);
            }
            dataBaseHelper.close();

            adapter.setItems(scalesInfo);
            recyclerView.setAdapter(adapter);
        }


        binding.buttonAddNewMesDeviceAddvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewScaleToList();
            }
        });

        binding.buttonAddNewMesDeviceSaveMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    saveMesToDataBase();
                }
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

        Bundle arg = new Bundle();
        arg.putParcelableArrayList("ScalesIds", scalesIds);

        NavHostFragment.findNavController(AddNewMesValuesFragment.this)
                .navigate(R.id.action_AddNewMesValuesFragment_to_AddNewMesSelectDeviceFragment, arg);

    }

    private boolean validate() {
        //TODO
        return true;
    }

    private boolean saveMesToDataBase() {

        ArrayList<ContentValues> valuesList = new ArrayList<>();

        ContentValues value;

        Log.println(Log.DEBUG, "КОНТРОЛЬ" , "Размер массива с информацией о шкалах: "+ scalesInfo.size() );
        Log.println(Log.DEBUG, "КОНТРОЛЬ" , "Число дочерних элементов списка: "+ recyclerView.getChildCount() );

        //Считывание введенных значений
        for(int i =0; i<scalesInfo.size(); i++){
            value = new ContentValues();
            View view = recyclerView.getChildAt(i);
            EditText editValue = (EditText) view.findViewById(R.id.editText_AddingValueListElementView_valueEnter);
            value.put("scaleI",i);
            value.put("value",editValue.getText().toString());
            Log.println(Log.DEBUG, "КОНТРОЛЬ" , "Значение: "+ editValue.getText().toString());
        }

        DataBaseHelper dataBaseHelper = getInstance(getContext());
        boolean operationSuccess = dataBaseHelper.addMeasurement(mesInfo,scalesInfo,valuesList);
        dataBaseHelper.close();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        if(operationSuccess){
            alertDialog.setTitle("Готово!");
            alertDialog.setMessage("Измерение успешно добавлено в базу данных");
            //TODO возврат назад в меню
        }else{
            alertDialog.setTitle("Ошибка!");
            alertDialog.setMessage("Возникла непредвиденная проблема.");
            //TODO возврат назад в меню
        }

        alertDialog.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();

        return true;
    }
}
