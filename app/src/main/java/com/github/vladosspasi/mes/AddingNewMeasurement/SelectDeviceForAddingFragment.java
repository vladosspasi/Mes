package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.Adapters.DevicesListAdapter;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.databinding.FragmentSelectdeviceforaddingBinding;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Фрагмент выбора устройства, шкалу которого нужно добавить в измерение
 */
public class SelectDeviceForAddingFragment extends Fragment {

    private FragmentSelectdeviceforaddingBinding binding;//связка разметки и фрагмента
    private RecyclerView recyclerView;//объект списка
    private ArrayList<ContentValues> deviceList;//список устройств

    //Процедура инициализации фрагмента
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSelectdeviceforaddingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Процедура инициализации объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());//открытие класса для работы с бд
        deviceList = dataBaseHelper.getDevices(); //получение списка приборов
        dataBaseHelper.close();

        //Инициализация списка на экране
        recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.recView_SelectDevice_devices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DevicesListAdapter devicesListAdapter = new DevicesListAdapter();
        devicesListAdapter.setItems(deviceList);
        recyclerView.setAdapter(devicesListAdapter);

        //Установка действия на нажатие на элемент списка
        binding.recViewSelectDeviceDevices.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int id = deviceList.get(position).getAsInteger("deviceId"); //получить айди выбранного прибора
                        Bundle arg = new Bundle();
                        arg.putInt("DeviceId", id); //передать его в следующий фрагмент
                        NavHostFragment.findNavController(SelectDeviceForAddingFragment.this)
                                .navigate(R.id.action_AddNewMesSelectDeviceFragment_to_AddNewMesSelectScaleFragment, arg);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }){
        });
    }

    //Процедура уничтожения фрагмента при его закрытии
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
