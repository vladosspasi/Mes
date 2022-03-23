package com.github.vladosspasi.mes;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//Адаптер для списка измерений
public class MeasurementsListAdapter extends RecyclerView.Adapter<MeasurementsListAdapter.MeasurementsListElementHolder> {

    //лист, хранящий список элементов
    private List<ContentValues> elementsList = new ArrayList<>();

    //Добавление элементов в список
    public void setItems(List<ContentValues> elems) {
        elementsList.addAll(elems);
        notifyDataSetChanged();
    }

    //очистка списка
    public void clearItems() {
        elementsList.clear();
        notifyDataSetChanged();
    }

    //что то нужное при создании элемента списка
    @NonNull
    @Override
    public MeasurementsListElementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_mes_list_element, viewGroup, false);
        return new MeasurementsListElementHolder(view);
    }

    //что-то нужное
    @Override
    public void onBindViewHolder(@NonNull MeasurementsListElementHolder measurementsListElementHolder, int i) {
        measurementsListElementHolder.bind(elementsList.get(i));
    }

    //Количество элементов в списке
    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    //Холдер для элемента списка измерений
    class MeasurementsListElementHolder extends RecyclerView.ViewHolder {

        //Все поля элемента списка с которыми происходит взаимодействие
        private TextView nameTextView;
        private TextView commentTextView;
        private TextView dateTextView;
        private TextView deviceNameTextView;

        //Конструктор для поиска элементов лэйаута
        public MeasurementsListElementHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView_mesListElementView_name);
            commentTextView = itemView.findViewById(R.id.textView_mesListElementView_comment);
            dateTextView = itemView.findViewById(R.id.textView_mesListElementView_date);
            deviceNameTextView = itemView.findViewById(R.id.textView_mesListElementView_device);
        }

        //Заполнение элементов вью данными
        public void bind(ContentValues element) {
            nameTextView.setText(element.getAsString("name"));
            commentTextView.setText("Комментарий:\n"+ element.getAsString("comment"));
            dateTextView.setText("Дата снятия:\n"+ element.getAsString("date"));
            deviceNameTextView.setText("Прибор: "+ element.getAsString("device"));
        }

    }
}
