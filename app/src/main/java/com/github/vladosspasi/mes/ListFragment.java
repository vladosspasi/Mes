package com.github.vladosspasi.mes;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.databinding.FragmentScreenListBinding;
import java.util.ArrayList;

//Фрагмент со списком всех измерений
public class ListFragment extends Fragment {

    private FragmentScreenListBinding binding;
    private RecyclerView recView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScreenListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    //Действия после создания
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DataBaseHelper dbHelper = DataBaseHelper.getInstance(getContext()); //Подключение дб
        ArrayList<ContentValues> mes = dbHelper.getMeasurementsList();      //Получение списка измерений
        dbHelper.close();

        recView = getActivity().findViewById(R.id.recyclerView_ListScreen_allMeasurements);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        MeasurementsListAdapter measurementsListAdapter = new MeasurementsListAdapter();    //Установка адаптера для списка
        measurementsListAdapter.setItems(mes);
        recView.setAdapter(measurementsListAdapter);

        binding.recyclerViewListScreenAllMeasurements.addOnItemTouchListener(       //Действие при нажатии элемента списка
                new RecyclerItemClickListener(this.getContext(), recView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        ContentValues selectedMes = mes.get(position);                  //Получение выбранного элемента из списка
                        Bundle arg = new Bundle();                                     //Оболочка для передачи данных в другой фрагмент
                        arg.putInt("MesData", selectedMes.getAsInteger("id"));          //Помещаем в оболочку id измерения
                        NavHostFragment.findNavController(ListFragment.this)    //Переход на нажатое измерение (с аргументом)
                                .navigate(R.id.action_ListFragment_to_ViewMesFragment, arg); //Для просмотра полной информации
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        //Длинное нажатие не обрабатывается
                        //TODO Сделать диалог для удаления измерения через длинное нажатие на нем в списке
                    }
                })
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}