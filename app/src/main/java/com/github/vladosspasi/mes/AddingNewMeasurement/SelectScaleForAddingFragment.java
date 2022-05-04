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
import com.github.vladosspasi.mes.*;
import com.github.vladosspasi.mes.Adapters.ScalesListAdapter;
import com.github.vladosspasi.mes.databinding.FragmentSelectscaleforaddingBinding;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Фрагмент выбора шкалы, которую нужно добавить в измерение для дальнейшего ввода значения
 */
public class SelectScaleForAddingFragment extends Fragment {

    private FragmentSelectscaleforaddingBinding binding; //объект связка
    private RecyclerView recyclerView; //объект списка
    private ArrayList<ContentValues> scalesList; //список шкал

    //Инициализация фрагмента
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSelectscaleforaddingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Инициализация объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arg = this.getArguments();
        assert arg != null;
        int deviceId = arg.getInt("DeviceId");//получение айди прибора, у которого нужно выбрать шкалу

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());//открытие класса для работы с бд
        scalesList = dataBaseHelper.getScalesByDeviceId(deviceId); //получение списка шкал
        dataBaseHelper.close();

        //Инициализация списка
        ScalesListAdapter scalesListAdapter = new ScalesListAdapter();
        scalesListAdapter.setItems(scalesList);
        recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.recView_SelectScale_scales);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(scalesListAdapter);

        //Установка действия на нажатие на элемент списка
        binding.recViewSelectScaleScales.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //добавить выбранную шкалу и вернуться к эерану заполнения величин
                        MeasurementGlobalInfo.addToScalesList(scalesList.get(position));
                        NavHostFragment.findNavController(SelectScaleForAddingFragment.this)
                                .navigate(R.id.action_AddNewMesSelectScaleFragment_to_AddNewValuesFragment);
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
