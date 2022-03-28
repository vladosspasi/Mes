package com.github.vladosspasi.mes;

import android.content.ContentValues;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.vladosspasi.mes.databinding.FragmentScreenViewdeviceBinding;

import java.util.ArrayList;


public class ViewDeviceFragment extends Fragment {
    private FragmentScreenViewdeviceBinding binding;
    private RecyclerView recView;
    int deviceId;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScreenViewdeviceBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    //Действия после создания
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arg = this.getArguments();
        assert arg != null;
        deviceId = arg.getInt("DeviceId");
        ArrayList<ContentValues> deviceData = new ArrayList<>();

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        deviceData = dataBaseHelper.getDeviceInfo(deviceId);
        dataBaseHelper.close();

        ContentValues deviceInfo = deviceData.get(0);
        deviceData.remove(0);

        binding.textViewViewDeviceTitle.setText("Информация о приборе");
        binding.textViewViewDeviceName.setText("Название: " + deviceInfo.getAsString("name"));
        binding.textViewViewDeviceType.setText("Тип: " + deviceInfo.getAsString("type"));
        binding.textViewViewDeviceComment.setText("Комментарий: " + deviceInfo.getAsString("comment"));
        binding.textViewViewDeviceScalesTitle.setText("Шкалы прибора:");

        recView = getActivity().findViewById(R.id.resView_ViewDevice_scalesList);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ScalesListAdapter scalesListAdapter = new ScalesListAdapter();
        scalesListAdapter.setItems(deviceData);
        recView.setAdapter(scalesListAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
