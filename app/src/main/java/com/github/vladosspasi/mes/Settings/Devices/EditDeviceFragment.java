package com.github.vladosspasi.mes.Settings.Devices;

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
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.Adapters.ScalesListAdapter;
import com.github.vladosspasi.mes.databinding.FragmentEditdeviceBinding;
import static com.github.vladosspasi.mes.DataBaseHelper.*;
import java.util.ArrayList;
import java.util.Objects;

public class EditDeviceFragment extends Fragment {

    private FragmentEditdeviceBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<ContentValues> scalesList;
    private ContentValues deviceData;
    private ScalesListAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentEditdeviceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        assert bundle != null;
        int deviceId = bundle.getInt("DeviceId");

        //Получение данных прибора
        deviceData = GlobalDeviceInfo.getDeviceData();
        if (deviceData==null||deviceData.size()==0){
            DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
            scalesList = dataBaseHelper.getDeviceInfo(deviceId);
            dataBaseHelper.close();
            deviceData = scalesList.get(0);
            scalesList.remove(0);
            GlobalDeviceInfo.setDeviceData(deviceData);
            GlobalDeviceInfo.setScales(scalesList);
        }else{
            scalesList = GlobalDeviceInfo.getScales();
        }

        //Инициализация полей информации прибора
        binding.editTextEditdeviceName.setText(deviceData.getAsString("deviceName"));
        binding.editTextEditdeviceComment.setText(deviceData.getAsString("deviceComment"));
        binding.editTextEditdeviceType.setText(deviceData.getAsString("deviceType"));

        //Инициализация списка шкал
        recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.recview_editdevice_scales);
        adapter = new ScalesListAdapter();
        adapter.setItems(scalesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        //Добавление новой шкалы в список
        binding.buttonEditdeviceAddScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues newScale = new ContentValues();
                newScale.put("scaleId", "no");
                newScale.put("scaleName", "");
                newScale.put("scaleUnit", "");
                newScale.put("scaleMax", "");
                newScale.put("scaleMin", "");
                newScale.put("scaleError", "");
                newScale.put("valuetypeName", 1);

                scalesList.add(newScale);
                GlobalDeviceInfo.setScales(scalesList);
                adapter.clearItems();
                adapter.setItems(scalesList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                recyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount()-1);
            }
        });

        //Выход
        binding.buttonEditdeviceCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EditDeviceFragment.this).
                        navigateUp();
            }
        });

        //Сохранить изменения
        binding.buttonEditdeviceSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChangesToDataBase(deviceId);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Изменения были сохранены.");
                alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                NavHostFragment.findNavController(EditDeviceFragment.this).
                       navigateUp();
            }
        });

        //Нажатие на элемент списка
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle arg = new Bundle();
                        arg.putInt("ScalePos", position);
                        NavHostFragment.findNavController(EditDeviceFragment.this)
                                .navigate(R.id.action_EditDeviceFragment_to_editScaleFragment, arg);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("Убрать шкалу из прибора?");
                        alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeScaleFromDevice(position);
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
                }));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void saveChangesToDataBase(int dId) {

        Log.e("АЙДИ ПРИБОРА", ""+dId);
        Log.e("НАЗВАНИЕ ПРИБОРА", ""+binding.editTextEditdeviceName.getText().toString());
        Log.e("КОМЕНТ ПРИБОРА", ""+binding.editTextEditdeviceComment.getText().toString());
        Log.e("ТИП ПРИБОРА", ""+binding.editTextEditdeviceType.getText().toString());
        Log.e("-", "-------------------");

        ContentValues scale = scalesList.get(0);
        Log.e("АЙДИ ШКАЛЫ", ""+scale.getAsString("scaleId"));
        Log.e("ИМЯ ШКАЛЫ", ""+scale.getAsString("scaleName"));
        Log.e("АЙДИ ТИПА ШКАЛЫ", ""+scale.getAsString("scaleTypeId"));
        Log.e("ПОГР ШКАЛЫ", ""+scale.getAsString("scaleError"));
        Log.e("МАКС ШКАЛЫ", ""+scale.getAsString("scaleMax"));
        Log.e("МИН ШКАЛЫ", ""+scale.getAsString("scaleMin"));
        Log.e("АЙДИ ПРИБОРА ШКАЛЫ", ""+scale.getAsString("scaleDeviceId"));

        ContentValues newDeviceData = new ContentValues();
        newDeviceData.put(FIELD_DEVICES_NAME,binding.editTextEditdeviceName.getText().toString());
        newDeviceData.put(FIELD_DEVICES_COMMENT,binding.editTextEditdeviceComment.getText().toString());
        newDeviceData.put(FIELD_DEVICES_TYPE,binding.editTextEditdeviceType.getText().toString());

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        dataBaseHelper.modifyDevice(dId, newDeviceData, scalesList);
        dataBaseHelper.close();
        GlobalDeviceInfo.clear();
    }

    public void removeScaleFromDevice(int pos){
        scalesList.remove(pos);
        GlobalDeviceInfo.setScales(scalesList);
        adapter.clearItems();
        adapter.setItems(scalesList);
        recyclerView.setAdapter(adapter);
    }
}
