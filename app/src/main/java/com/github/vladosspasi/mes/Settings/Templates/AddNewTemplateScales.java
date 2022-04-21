package com.github.vladosspasi.mes.Settings.Templates;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.Adapters.ScalesListAdapter;
import com.github.vladosspasi.mes.Adapters.TemplateListAdapter;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.databinding.FragmentAddnewtemplateScalesBinding;

import java.util.ArrayList;

import static com.github.vladosspasi.mes.DataBaseHelper.FIELD_TEMPLATES_NAME;

public class AddNewTemplateScales extends Fragment {
    private FragmentAddnewtemplateScalesBinding binding;
    private ArrayList<ContentValues> scales;
    private ScalesListAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddnewtemplateScalesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Получение данных
        scales = GlobalTemplateInfo.getTemplate().getScalesList();
        //Инициализация списка

        if (scales.size() == 0) {
            binding.textViewAddNewTemplateScalesTitle.setText("Вы еще не добавили шкалы в шаблон.");
        } else {
            binding.recViewAddNewTemplateScales.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ScalesListAdapter();
            adapter.setItems(scales);
            binding.recViewAddNewTemplateScales.setAdapter(adapter);
        }

        //Обработчики нажатий на элементы списка

        binding.recViewAddNewTemplateScales.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), binding.recViewAddNewTemplateScales,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                alert.setMessage("Удалить шкалу из шаблона?");
                                alert.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        deleteScaleFromList(position);
                                    }
                                });
                                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                alert.show();
                            }
                        }));

        //Обработчики нажатий на кнопки
        binding.buttonAddNewTemplateScalesAddscale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addScale();
            }
        });

        binding.buttonAddNewTemplateScalesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTemplate();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void saveTemplate() {

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        dataBaseHelper.createTemplate(GlobalTemplateInfo.getTemplate());
        dataBaseHelper.close();

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage("Шаблон успешно добавлен.");
        alert.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alert.show();
        NavHostFragment.findNavController(AddNewTemplateScales.this)
                .navigate(R.id.action_addNewTemplateScales_to_templatesListFragment);
    }

    public void addScale() {
        NavHostFragment.findNavController(AddNewTemplateScales.this)
                .navigate(R.id.action_addNewTemplateScales_to_selectDeviceForTemplate);
    }

    public void deleteScaleFromList(int pos) {
        scales.remove(pos);
        adapter.clearItems();
        adapter.setItems(scales);
        binding.recViewAddNewTemplateScales.setAdapter(adapter);
    }

}


