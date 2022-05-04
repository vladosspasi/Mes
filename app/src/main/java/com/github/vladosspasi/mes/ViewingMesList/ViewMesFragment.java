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
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.Adapters.ValuesListAdapter;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.databinding.FragmentScreenViewmesBinding;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Фрагмент просмотра информации о измерении
 */
public class ViewMesFragment extends Fragment {

    private FragmentScreenViewmesBinding binding; //объект связка
    private RecyclerView recView; //объект списка
    private ArrayList<ContentValues> mesData; //данные о измерении
    Integer mesId; // ID измерения

    //Процедура инициализации фрагмента
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentScreenViewmesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Процедура инициализации объектов фрагмента
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        //Получение id измерения от предыдущего фрагмента
        Bundle arg = this.getArguments();
        assert arg != null;
        mesId = arg.getInt("MesId");

        //Получение данных о измерении из бд
        DataBaseHelper dbHelper = DataBaseHelper.getInstance(getContext());
        mesData = dbHelper.getMeasurmentData(mesId);
        dbHelper.close();
        ContentValues mesInfo = mesData.get(0);

        //Заполнение полей
        binding.textViewViewMesName.setText("Измерение \"" + mesInfo.getAsString("mesName") + "\"");
        binding.textViewViewMesComment.setText("Комментарий: " + mesInfo.getAsString("mesComment"));
        binding.textViewViewMesDate.setText("Дата снятия: " + mesInfo.getAsString("mesDate"));
        binding.textViewViewMesMestitle.setText("Снятые значения");

        //Инициализация списка снятых значений
        mesData.remove(0);
        recView = getActivity().findViewById(R.id.resView_ViewMes_mesurements);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ValuesListAdapter valuesListAdapter = new ValuesListAdapter();
        valuesListAdapter.setItems(mesData);
        recView.setAdapter(valuesListAdapter);

        //Установка действия на нажатие на элемент списка
        binding.resViewViewMesMesurements.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), recView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }
                    //Долгое нажатие
                    @Override
                    public void onLongItemClick(View view, int position) {
                        //Создание диалога
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Значение №" + Integer.toString(position + 1));
                        alertDialog.setMessage("Выберите действие");
                        alertDialog.setPositiveButton("Просмотреть прибор", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Переход к прибору, на котором было снято значение
                                ContentValues value = mesData.get(0);
                                Bundle arg = new Bundle();
                                arg.putInt("DeviceId", value.getAsInteger("deviceId"));
                                NavHostFragment.findNavController(ViewMesFragment.this)
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
                                        deleteValueFromList(position); //Удаление величины из списка
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

    //Процедура уничтожения фрагмента при закрытии
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Процедура удаления величины
    private void deleteValueFromList(int i) {
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        ContentValues value = mesData.get(i); //получение удаляемой величины
        int id = value.getAsInteger("valueId");

        if (dataBaseHelper.deleteValueById(id)) {
            //Если удалено - получит новый список величин
            mesData = dataBaseHelper.getMeasurmentData(mesId);
            mesData.remove(0);
            AlertDialog.Builder successDialog = new AlertDialog.Builder(getContext());
            if (mesData.size()==0){
                //Если величин больше нет
                //Сообщение + вернуть на предыдущий экран
                dataBaseHelper.deleteMesById(mesId);
                Toast.makeText(getContext(),"Больше нет значений, измерение удалено.", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(ViewMesFragment.this).navigateUp();
                dataBaseHelper.close();
            }else{
                //Иначе обновить список
                recView.setLayoutManager(new LinearLayoutManager(getActivity()));
                ValuesListAdapter valuesListAdapter = new ValuesListAdapter();
                valuesListAdapter.setItems(mesData);
                recView.setAdapter(valuesListAdapter);
                Toast.makeText(getContext(),"Значение удалено", Toast.LENGTH_SHORT).show();
                dataBaseHelper.close();
            }

        } else {
            //Если не получилось удалить
            Toast.makeText(getContext(),"Ошибка!", Toast.LENGTH_SHORT).show();
            dataBaseHelper.close();
        }
    }
}