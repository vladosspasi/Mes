package com.github.vladosspasi.mes.Settings.Templates;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.Adapters.TemplateListAdapter;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.RecyclerItemClickListener;
import com.github.vladosspasi.mes.Settings.Devices.DevicesListFragment;
import com.github.vladosspasi.mes.databinding.FragmentTemplatesBinding;

import java.util.ArrayList;

import static com.github.vladosspasi.mes.DataBaseHelper.FIELD_TEMPLATES_NAME;
import static com.github.vladosspasi.mes.DataBaseHelper.getInstance;

public class TemplatesListFragment extends Fragment {
    FragmentTemplatesBinding binding;
    ArrayList<ContentValues> templates;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentTemplatesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Получение данных
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        templates = dataBaseHelper.getTemplates();
        dataBaseHelper.close();

        //Инициализация списка
        if (templates.size() == 0 || templates.get(0).containsKey("no values")) {
            binding.textViewTemplatesTitle.setText("Сейчас у вас нет шаблонов.");
            binding.recviewTemplatesList.setVisibility(View.INVISIBLE);
        } else {
            binding.recviewTemplatesList.setLayoutManager(new LinearLayoutManager(getContext()));
            TemplateListAdapter adapter = new TemplateListAdapter();

            adapter.setItems(templates);
            binding.recviewTemplatesList.setAdapter(adapter);
        }

        //Действие по нажатию на список
        binding.recviewTemplatesList.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), binding.recviewTemplatesList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        goToTemplate(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Шаблон " + templates.get(position).getAsString(FIELD_TEMPLATES_NAME));
                        alert.setMessage("Выберите действие:");
                        alert.setPositiveButton("Редактировать", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editTemplate(position);
                                dialogInterface.cancel();
                            }
                        });
                        alert.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                AlertDialog.Builder AreYouSure = new AlertDialog.Builder(getContext());
                                AreYouSure.setMessage("Вы уверены, что хотите удалить данный шаблон?");
                                AreYouSure.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        deleteTemplate(position);
                                        dialogInterface.cancel();
                                    }
                                });
                                AreYouSure.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                AreYouSure.show();

                                dialogInterface.cancel();
                            }
                        });

                        alert.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alert.show();

                    }
                }));

        //Действие на плавающую кнопку
        binding.floatingActionButtonTemplatesAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTemplate();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void addNewTemplate() {
        NavHostFragment.findNavController(TemplatesListFragment.this)
                .navigate(R.id.action_templatesListFragment_to_addNewTemplateInfo);
    }

    public void goToTemplate(int pos) {
        Bundle arg = new Bundle();
        arg.putInt("TemplateId", templates.get(pos).getAsInteger("id"));
        NavHostFragment.findNavController(TemplatesListFragment.this)
                .navigate(R.id.action_templatesListFragment_to_viewTemplateFragment, arg);
    }

    public void editTemplate(int pos) {

    }

    public void deleteTemplate(int pos) {

        int tempId = templates.get(pos).getAsInteger("id");

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        dataBaseHelper.deleteTemplateById(tempId);
        templates.remove(pos);

        if (templates.size() == 0) {
            binding.textViewTemplatesTitle.setText("Сейчас у вас нет шаблонов.");
        }

        TemplateListAdapter adapter = new TemplateListAdapter();
        adapter.setItems(templates);
        binding.recviewTemplatesList.setAdapter(adapter);

        dataBaseHelper.close();
    }

}
