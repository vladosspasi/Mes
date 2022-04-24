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
import com.github.vladosspasi.mes.Adapters.ValuesListAdapter;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.databinding.FragmentScreenViewmesBinding;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ViewMesFragment extends Fragment {

    private FragmentScreenViewmesBinding binding;
    private RecyclerView recView;
    private ArrayList<ContentValues> mesData;
    Integer mesId;

    private static Logger log = Logger.getLogger(ViewMesFragment.class.getName());

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScreenViewmesBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Bundle arg = this.getArguments();
        assert arg != null;
        mesId = arg.getInt("MesId");


        DataBaseHelper dbHelper = DataBaseHelper.getInstance(getContext()); //Подключение дб
        mesData = dbHelper.getMeasurmentData(mesId);

        dbHelper.close();
        ContentValues mesInfo = mesData.get(0);

        binding.textViewViewMesName.setText("Измерение \"" + mesInfo.getAsString("mesName") + "\"");
        binding.textViewViewMesComment.setText("Комментарий: " + mesInfo.getAsString("mesComment"));
        binding.textViewViewMesDate.setText("Дата снятия: " + mesInfo.getAsString("mesDate"));
        binding.textViewViewMesMestitle.setText("Снятые значения");

        mesData.remove(0);

        recView = getActivity().findViewById(R.id.resView_ViewMes_mesurements);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ValuesListAdapter valuesListAdapter = new ValuesListAdapter();
        valuesListAdapter.setItems(mesData);
        recView.setAdapter(valuesListAdapter);

        binding.resViewViewMesMesurements.addOnItemTouchListener(      //Действие при нажатии элемента списка
                new RecyclerItemClickListener(this.getContext(), recView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Значение №" + Integer.toString(position + 1));
                        alertDialog.setMessage("Выберите действие");

                        alertDialog.setPositiveButton("Просмотреть прибор", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ContentValues value = mesData.get(0);

                                Bundle arg = new Bundle();                                     //Оболочка для передачи данных в другой фрагмент
                                arg.putInt("DeviceId", value.getAsInteger("deviceId"));          //Помещаем в оболочку id прибора
                                NavHostFragment.findNavController(ViewMesFragment.this)    //Переход на нажатое измерение (с аргументом)
                                        .navigate(R.id.action_ViewMesFragment_to_ViewDeviceFragment, arg);

                                dialogInterface.cancel();
                            }
                        });

                        alertDialog.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                AlertDialog.Builder areYouSureDialog = new AlertDialog.Builder(getContext());
                                areYouSureDialog.setMessage("Вы уверены?");
                                areYouSureDialog.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        deleteValueFromList(position);
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

                        alertDialog.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog.show();
                    }
                })
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Удалить величину из списка
    private void deleteValueFromList(int i) {
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        log.info("КАКАЯ ИШКА: "+i);
        ContentValues value = mesData.get(i);
        int id = value.getAsInteger("valueId");
        if (dataBaseHelper.deleteValueById(id)) {

            mesData = dataBaseHelper.getMeasurmentData(mesId);

            mesData.remove(0);

            AlertDialog.Builder successDialog = new AlertDialog.Builder(getContext());
            if (mesData.size()==0){
                dataBaseHelper.deleteMesById(mesId);
                successDialog.setMessage("Значение удалено. В данном измерении нет других значений. Вы будете возвращены на предыдущий экран.");
                successDialog.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        NavHostFragment.findNavController(ViewMesFragment.this).navigateUp();
                        dataBaseHelper.close();
                    }
                });
            }else{
                recView.setLayoutManager(new LinearLayoutManager(getActivity()));
                ValuesListAdapter valuesListAdapter = new ValuesListAdapter();
                valuesListAdapter.setItems(mesData);
                recView.setAdapter(valuesListAdapter);
                successDialog.setMessage("Значение удалено");
                successDialog.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }
            successDialog.show();

        } else {
            AlertDialog.Builder failDialog = new AlertDialog.Builder(getContext());
            failDialog.setMessage("Не удалось удалить значение");
            failDialog.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
        dataBaseHelper.close();
    }

}