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

/**
 * Фрагмент списка имеющихся приборов
 */

public class DevicesListFragment extends Fragment {

    private FragmentDevicesBinding binding;//объект-связка
    private ArrayList<ContentValues> devicesList;//список приборов
    private RecyclerView recyclerView; //объект списка

    //Инициализация фрагмента
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentDevicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Инициализация объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Установка действия на плавающую кнопку "+"
        binding.floatingActionButtonDevicesAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewDevice(); //добавить новый прибор
            }
        });

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext()); //открытие класса для работы с бд
        devicesList = dataBaseHelper.getDevices(); //получение списка приборов
        dataBaseHelper.close();

        //Инициализация списка
        recyclerView = getActivity().findViewById(R.id.recyclerView_devices_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(devicesList.get(0).containsKey("no values")){ //если нет приборов
            binding.textViewDevicesNoDevicesTitle.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }else{ //иначе
            binding.textViewDevicesNoDevicesTitle.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            DevicesListAdapter adapter = new DevicesListAdapter();
            adapter.setItems(devicesList);
            recyclerView.setAdapter(adapter);
        }

        //Установка действия на нажатие на элемент списка
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    //короткое нажатие
                    @Override
                    public void onItemClick(View view, int position) {
                        goToThisDevice(position); //переход к просмотру прибора
                    }
                    //долгое нажатие
                    @Override
                    public void onLongItemClick(View view, int position) {
                        //Инициализация диалога выбора действия
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setMessage("Выберите действие");
                        dialog.setTitle("Прибор " + devicesList.get(position).getAsString("deviceName"));
                        dialog.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Инициализация диалога подтверждения действия
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
                                editDevice(position); //редактирование прибора
                                dialogInterface.cancel();
                            }
                        });
                        dialog.show();
                    }
                }));
    }

    //Процедура удаления прибора
    private void deleteDeviceFromList(int i){
        int deviceId = devicesList.get(i).getAsInteger("deviceId"); //получение id выбранного прибора
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext()); //открытие класса для работы с бд
        dataBaseHelper.deleteDeviceById(deviceId); //Удаление прибора по его id
        devicesList.clear();
        devicesList = dataBaseHelper.getDevices(); //Получение обновленного списка
        dataBaseHelper.close();

        if(devicesList.get(0).containsKey("no values")){ //если список пуст
            binding.textViewDevicesNoDevicesTitle.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }else{ //иначе обновить список
            binding.textViewDevicesNoDevicesTitle.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            DevicesListAdapter adapter = new DevicesListAdapter();
            adapter.setItems(devicesList);
            recyclerView.setAdapter(adapter);
        }
    }

    //Процедура изменения прибора
    public void editDevice(int pos){
        int deviceId = devicesList.get(pos).getAsInteger("deviceId");
        Bundle arg = new Bundle();
        arg.putInt("DeviceId", deviceId);
        //Переход к фрагменту изменения прибора с id прибора
        NavHostFragment.findNavController(DevicesListFragment.this)
                .navigate(R.id.action_DevicesFragment_to_EditDeviceFragment, arg);
    }

    //Процедура перехода к просмотру данных о приборе
    public void goToThisDevice(int pos){
        int deviceId = devicesList.get(pos).getAsInteger("deviceId");
        Bundle arg = new Bundle();
        arg.putInt("DeviceId", deviceId);
        //Переход к фрагменту просмотра с id
        NavHostFragment.findNavController(DevicesListFragment.this)
                .navigate(R.id.action_DevicesFragment_to_ViewDeviceFragment, arg);

    }

    //Процедура добавления нового прибора
    public void addNewDevice(){
        //переход к соответствующему фрагменту
        NavHostFragment.findNavController(DevicesListFragment.this)
                .navigate(R.id.action_DevicesFragment_to_AddNewDeviceInfoFragment);
    }

    //Процедура уничтожения фрагмента при его закрытии
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
