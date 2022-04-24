package com.github.vladosspasi.mes.Settings.Devices;

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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.Adapters.ScalesListAdapter;
import com.github.vladosspasi.mes.databinding.FragmentCreatedeviceScalesBinding;
import static com.github.vladosspasi.mes.DataBaseHelper.*;

import java.util.ArrayList;

public class AddNewDeviceScalesFragment extends Fragment {

    private FragmentCreatedeviceScalesBinding binding;
    private ContentValues deviceInfo;
    private RecyclerView recView;
    ArrayList<ContentValues> scales;
    ScalesListAdapter scalesListAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCreatedeviceScalesBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arg = getArguments();
        deviceInfo = new ContentValues();
        deviceInfo.put(FIELD_DEVICES_NAME, arg.getString("deviceName"));
        deviceInfo.put(FIELD_DEVICES_COMMENT, arg.getString("deviceComment"));
        deviceInfo.put(FIELD_DEVICES_TYPE, arg.getString("deviceType"));

        scales = new ArrayList<>();
        recView = getActivity().findViewById(R.id.rec_addNewDeviceScales_scalesList);
        recView.setVisibility(View.INVISIBLE);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        scalesListAdapter = new ScalesListAdapter();

        binding.textboxAddNewDeviceScalesListtitle.setText("Список добавленных шкал пуст. Добавьте шкалы ниже.");

        binding.buttonAddNewDeviceScalesAddScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    addScaleToList();
                }
            }
        });

        binding.buttonAddNewDeviceScalesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDeviceToDB();
            }
        });

        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner_addNewDeviceScales_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void saveDeviceToDB(){

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());

        dataBaseHelper.addDevice(deviceInfo, scales);

        AlertDialog.Builder success = new AlertDialog.Builder(getContext());
        success.setMessage("Прибор успешно добавлен в базу данных. Теперь его можно использовать при добавлении измерений.");
        success.setTitle("Прибор добавлен!");
        success.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NavHostFragment.findNavController(AddNewDeviceScalesFragment.this).
                        navigate(R.id.action_AddNewDeviceScalesFragment_to_DevicesFragment);
            }
        });
        success.show();

        dataBaseHelper.close();
    }

    //TODO тип прибора

    public void addScaleToList(){
        if (validateForm()){

            ContentValues scale = new ContentValues();

            scale.put("scaleName", String.valueOf(binding.editboxAddNewDeviceScalesName.getText()));
            scale.put("scaleUnit", String.valueOf(binding.editboxAddNewDeviceScalesUnit.getText()));

            DataBaseHelper dataBaseHelper = getInstance(getContext());

            int typeId = (int) binding.spinnerAddNewDeviceScalesType.getSelectedItemPosition()+1;
            String type = dataBaseHelper.getValueTypeById(typeId);

            dataBaseHelper.close();

            scale.put("valuetypeId", typeId);
            scale.put("valuetypeName", type);
            scale.put("valueTypeId", typeId);
            scale.put("scaleMin", String.valueOf(binding.editboxAddNewDeviceScalesFromvalue.getText()));
            scale.put("scaleMax", String.valueOf(binding.editboxAddNewDeviceScalesTovalue.getText()));
            scale.put("scaleError", String.valueOf(binding.editboxAddNewDeviceScalesError.getText()));

            binding.textboxAddNewDeviceScalesListtitle.setText("Шкалы прибора:");

            scales.add(scale);
            scalesListAdapter = new ScalesListAdapter();
            scalesListAdapter.setItems(scales);
            recView.setAdapter(scalesListAdapter);
            recView.setVisibility(View.VISIBLE);

        }
    }

    public boolean validateForm(){
        return true;
    }

}
