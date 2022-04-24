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
import com.github.vladosspasi.mes.Adapters.DevicesListAdapter;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.databinding.FragmentDevicesBinding;

import java.util.ArrayList;

public class DevicesListFragment extends Fragment {

    private FragmentDevicesBinding binding;
    private ArrayList<ContentValues> devicesList;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentDevicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.floatingActionButtonDevicesAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewDevice();
            }
        });

        recyclerView = getActivity().findViewById(R.id.recyclerView_devices_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        devicesList = dataBaseHelper.getDevices();
        dataBaseHelper.close();

        if(devicesList.get(0).containsKey("no values")){
            binding.textViewDevicesNoDevicesTitle.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }else{
            binding.textViewDevicesNoDevicesTitle.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            DevicesListAdapter adapter = new DevicesListAdapter();
            adapter.setItems(devicesList);
            recyclerView.setAdapter(adapter);
        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        goToThisDevice(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setMessage("Выберите действие");
                        dialog.setTitle("Прибор " + devicesList.get(position).getAsString("deviceName"));
                        dialog.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                AlertDialog.Builder areYouSureDialog = new AlertDialog.Builder(getContext());
                                areYouSureDialog.setMessage("Вы уверены? Все измерения, в которых используются шкалы данного прибора, будут удалены.");
                                areYouSureDialog.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        deleteDeviceFromList(position);
                                        dialogInterface.cancel();
                                    }
                                });

                                areYouSureDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                areYouSureDialog.show();
                                dialogInterface.cancel();
                            }
                        });

                        dialog.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        dialog.setPositiveButton("Редактировать", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editDevice(position);
                                dialogInterface.cancel();
                            }
                        });
                        dialog.show();
                    }
                }));
    }

    private void deleteDeviceFromList(int i){

        int deviceId = devicesList.get(i).getAsInteger("deviceId");

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        dataBaseHelper.deleteDeviceById(deviceId);
        devicesList.clear();
        devicesList = dataBaseHelper.getDevices();
        dataBaseHelper.close();

        if(devicesList.get(0).containsKey("no values")){
            binding.textViewDevicesNoDevicesTitle.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }else{
            binding.textViewDevicesNoDevicesTitle.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            DevicesListAdapter adapter = new DevicesListAdapter();
            adapter.setItems(devicesList);
            recyclerView.setAdapter(adapter);
        }
    }

    public void editDevice(int pos){
        int deviceId = devicesList.get(pos).getAsInteger("deviceId");
        Bundle arg = new Bundle();
        arg.putInt("DeviceId", deviceId);
        NavHostFragment.findNavController(DevicesListFragment.this)
                .navigate(R.id.action_DevicesFragment_to_EditDeviceFragment, arg);
    }

    public void goToThisDevice(int pos){
        int deviceId = devicesList.get(pos).getAsInteger("deviceId");
        Log.println(Log.DEBUG, "ID ПРИБОРА", ""+deviceId);
        Bundle arg = new Bundle();
        arg.putInt("DeviceId", deviceId);
        NavHostFragment.findNavController(DevicesListFragment.this)
                .navigate(R.id.action_DevicesFragment_to_ViewDeviceFragment, arg);

    }

    public void addNewDevice(){
        NavHostFragment.findNavController(DevicesListFragment.this)
                .navigate(R.id.action_DevicesFragment_to_AddNewDeviceInfoFragment);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
