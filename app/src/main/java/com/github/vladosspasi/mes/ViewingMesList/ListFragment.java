package com.github.vladosspasi.mes.ViewingMesList;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.Adapters.MeasurementsListAdapter;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.databinding.FragmentScreenListBinding;

import java.util.ArrayList;

/**
 * Фрагмент со списком всех измерений
 */
public class ListFragment extends Fragment {

    private FragmentScreenListBinding binding; //объект связка
    private RecyclerView recView; //объект списка
    ArrayList<ContentValues> mes; //список всех измерений

    //Процедура инициализации фрагмента
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentScreenListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Процедура инициализации объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DataBaseHelper dbHelper = DataBaseHelper.getInstance(getContext());
        mes = dbHelper.getMeasurementsList(); //Получение списка измерений из БД
        dbHelper.close();


        if (mes.get(0).containsKey("empty") || mes.get(0).getAsString("mesName") == null) {
            //Если список пуст
            binding.recyclerViewListScreenTitle.setText("В базе данных нет измерений.");
            binding.recyclerViewListScreenAllMeasurements.setVisibility(View.INVISIBLE);
        } else {
            //Иначе инициализация списка
            binding.recyclerViewListScreenAllMeasurements.setVisibility(View.VISIBLE);
            binding.recyclerViewListScreenTitle.setText("Ваши измерения:");
            recView = getActivity().findViewById(R.id.recyclerView_ListScreen_allMeasurements);
            recView.setLayoutManager(new LinearLayoutManager(getActivity()));
            MeasurementsListAdapter measurementsListAdapter = new MeasurementsListAdapter();
            measurementsListAdapter.setItems(mes);
            recView.setAdapter(measurementsListAdapter);

            //Установка действия на нажатие на элемент списка
            binding.recyclerViewListScreenAllMeasurements.addOnItemTouchListener(
                    new RecyclerItemClickListener(this.getContext(), recView, new RecyclerItemClickListener.OnItemClickListener() {
                        //Короткое нажатие
                        @Override
                        public void onItemClick(View view, int position) {
                            ContentValues selectedMes = mes.get(position);   //Получение выбранного элемента из списка
                            Bundle arg = new Bundle();
                            arg.putInt("MesId", selectedMes.getAsInteger("mesId"));
                            //Переход на выбранное измерение
                            NavHostFragment.findNavController(ListFragment.this)
                                    .navigate(R.id.action_ListFragment_to_ViewMesFragment, arg);
                        }

                        //Длинное нажатие
                        @Override
                        public void onLongItemClick(View view, int position) {
                            //Создание диалога выбора действия
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                            dialog.setMessage("Выберите действие");
                            dialog.setTitle("Измерение " + mes.get(position).getAsString("mesName"));
                            dialog.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Создание диалога подтверждения удаления
                                    AlertDialog.Builder areYouSureDialog = new AlertDialog.Builder(getContext());
                                    areYouSureDialog.setMessage("Вы уверены?");
                                    areYouSureDialog.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            deleteMeasurementFromList(position);
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
                            dialog.show();
                        }
                    })
            );
        }
    }

    //Процедура уничтожения фрагмента при его закрытии
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Процедура удаления измерения
    private void deleteMeasurementFromList(int i) {
        int mesId = mes.get(i).getAsInteger("mesId");
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        dataBaseHelper.deleteMesById(mesId); //удаление из бд
        mes.clear();
        mes = dataBaseHelper.getMeasurementsList(); //Получение нового списка измерений
        dataBaseHelper.close();

        if (mes.get(0).containsKey("empty") || mes.get(0).getAsString("mesName") == null) {
            //Если список пуст - обновить
            binding.recyclerViewListScreenTitle.setText("В базе данных нет измерений. Вернитесь назад и добавьте новое измерение.");
            binding.recyclerViewListScreenAllMeasurements.setVisibility(View.INVISIBLE);
        } else {
            //Иначе обновить список
            MeasurementsListAdapter measurementsListAdapter = new MeasurementsListAdapter();    //Установка адаптера для списка
            measurementsListAdapter.setItems(mes);
            recView.setAdapter(measurementsListAdapter);
        }
    }
}