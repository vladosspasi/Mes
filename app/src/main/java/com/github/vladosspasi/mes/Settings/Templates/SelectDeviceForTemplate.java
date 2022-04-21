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
import com.github.vladosspasi.mes.Adapters.DevicesListAdapter;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.databinding.FragmentSelectdeviceforaddingBinding;
import java.util.ArrayList;

public class SelectDeviceForTemplate extends Fragment {

    FragmentSelectdeviceforaddingBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSelectdeviceforaddingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<ContentValues> deviceList;

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        deviceList = dataBaseHelper.getDevices();
        dataBaseHelper.close();

        binding.recViewSelectDeviceDevices.setLayoutManager(new LinearLayoutManager(getContext()));
        DevicesListAdapter devicesListAdapter = new DevicesListAdapter();
        devicesListAdapter.setItems(deviceList);
        binding.recViewSelectDeviceDevices.setAdapter(devicesListAdapter);

        binding.recViewSelectDeviceDevices.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), binding.recViewSelectDeviceDevices, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int id = deviceList.get(position).getAsInteger("id");
                        Bundle arg = new Bundle();
                        arg.putInt("DeviceId", id);
                        NavHostFragment.findNavController(SelectDeviceForTemplate.this)
                                .navigate(R.id.action_selectDeviceForTemplate_to_selectScaleForTemplate, arg);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }){
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}







