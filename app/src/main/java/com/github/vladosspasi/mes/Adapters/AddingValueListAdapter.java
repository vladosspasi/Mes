package com.github.vladosspasi.mes.Adapters;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.vladosspasi.mes.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-адаптер списка ввода значений показаний при создании нового измерения.
 */
public class AddingValueListAdapter extends RecyclerView.Adapter<AddingValueListAdapter.AddingValueListElementHolder> {
    //Массив объектов списка
    private List<ContentValues> elementsList = new ArrayList<>();

    //Получение списка извне
    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<ContentValues> elems) {
        elementsList.addAll(elems);
        notifyDataSetChanged();
    }

    //Установка xml-разметки отдельного элемента списка
    @NonNull
    @Override
    public AddingValueListElementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_addingvaluetomes_list_element, viewGroup, false);
        return new AddingValueListElementHolder(view);
    }

    //Привязывание элементов к разметке
    @Override
    public void onBindViewHolder(@NonNull AddingValueListElementHolder addingValueListElementHolder, int i) {
        addingValueListElementHolder.bind(elementsList.get(i));
    }

    //Получение числа элементов списка
    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    //Класс-холдер для элементов списка
    static class AddingValueListElementHolder extends RecyclerView.ViewHolder {
        //Все объекты элемента списка с которыми происходит взаимодействие
        private final TextView deviceNameTextView;
        private final TextView scaleNameTextView;
        private final TextView unitTextView;
        private final TextView errorTextView;

        //Конструктор для поиска элементов лэйаута
        public AddingValueListElementHolder(View itemView) {
            super(itemView);
            deviceNameTextView = itemView.findViewById(R.id.textView_AddingValueListElementView_deviceName);
            scaleNameTextView = itemView.findViewById(R.id.textView_AddingValueListElementView_scaleName);
            unitTextView = itemView.findViewById(R.id.textView_AddingValueListElementView_unit);
            errorTextView = itemView.findViewById(R.id.textView_AddingValueListElementView_error);
        }

        //Заполнение объектов данными
        public void bind(ContentValues element) {
            deviceNameTextView.setText(element.getAsString("deviceName"));
            scaleNameTextView.setText(element.getAsString("scaleName"));
            unitTextView.setText(element.getAsString("scaleUnit"));
            errorTextView.setText(element.getAsString("scaleError"));
        }
    }
}