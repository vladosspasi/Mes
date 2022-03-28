package com.github.vladosspasi.mes;

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
import com.github.vladosspasi.mes.databinding.FragmentScreenListBinding;

import java.util.ArrayList;

//Фрагмент со списком всех измерений
public class ListFragment extends Fragment {

    private FragmentScreenListBinding binding;
    private RecyclerView recView;
    ArrayList<ContentValues> mes;

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
        mes = dbHelper.getMeasurementsList();      //Получение списка измерений
        dbHelper.close();

        if (mes.get(0).containsKey("empty") || mes.get(0).getAsString("name") == null) {
            binding.recyclerViewListScreenTitle.setText("В базе данных нет измерений. Вернитесь назад и добавьте новое измерение.");
            binding.recyclerViewListScreenAllMeasurements.setVisibility(View.INVISIBLE);
        } else {
            binding.recyclerViewListScreenAllMeasurements.setVisibility(View.VISIBLE);
            binding.recyclerViewListScreenTitle.setText("Ваши измерения:");
            recView = getActivity().findViewById(R.id.recyclerView_ListScreen_allMeasurements);
            recView.setLayoutManager(new LinearLayoutManager(getActivity()));

            MeasurementsListAdapter measurementsListAdapter = new MeasurementsListAdapter();    //Установка адаптера для списка
            measurementsListAdapter.setItems(mes);
            recView.setAdapter(measurementsListAdapter);

            binding.recyclerViewListScreenAllMeasurements.addOnItemTouchListener(       //Действие при нажатии элемента списка
                    new RecyclerItemClickListener(this.getContext(), recView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ContentValues selectedMes = mes.get(position);                  //Получение выбранного элемента из списка
                            Bundle arg = new Bundle();                                     //Оболочка для передачи данных в другой фрагмент
                            arg.putInt("MesId", selectedMes.getAsInteger("id"));          //Помещаем в оболочку id измерения
                            NavHostFragment.findNavController(ListFragment.this)    //Переход на нажатое измерение (с аргументом)
                                    .navigate(R.id.action_ListFragment_to_ViewMesFragment, arg); //Для просмотра полной информации
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                            dialog.setMessage("Выберите действие");
                            dialog.setTitle("Измерение" + mes.get(position).getAsString("name"));
                            dialog.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void deleteMeasurementFromList(int i) {
        int mesId = mes.get(i).getAsInteger("id");

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        dataBaseHelper.deleteMesById(mesId);
        mes.clear();
        mes = dataBaseHelper.getMeasurementsList();
        dataBaseHelper.close();

        if (mes.get(0).containsKey("empty") || mes.get(0).getAsString("name") == null) {
            binding.recyclerViewListScreenTitle.setText("В базе данных нет измерений. Вернитесь назад и добавьте новое измерение.");
            binding.recyclerViewListScreenAllMeasurements.setVisibility(View.INVISIBLE);
        } else {
            MeasurementsListAdapter measurementsListAdapter = new MeasurementsListAdapter();    //Установка адаптера для списка
            measurementsListAdapter.setItems(mes);
            recView.setAdapter(measurementsListAdapter);
        }
    }
}