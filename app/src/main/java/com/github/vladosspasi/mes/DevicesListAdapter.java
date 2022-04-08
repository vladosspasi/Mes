package com.github.vladosspasi.mes;

import android.content.ContentValues;
import android.support.v7.widget.RecyclerView;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;

public class DevicesListAdapter extends RecyclerView.Adapter<DevicesListAdapter.DevicesListElementHolder>{

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
    public DevicesListAdapter.DevicesListElementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_device_list_element, viewGroup, false);
        return new DevicesListAdapter.DevicesListElementHolder(view);
    }

    //что-то нужное
    @Override
    public void onBindViewHolder(@NonNull DevicesListAdapter.DevicesListElementHolder devicesListElementHolder, int i) {
        devicesListElementHolder.bind(elementsList.get(i));
    }

    //Количество элементов в списке
    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    //Холдер для элемента списка измерений
    class DevicesListElementHolder extends RecyclerView.ViewHolder {

        //Все поля элемента списка с которыми происходит взаимодействие
        private TextView nameTextView;
        private TextView commentTextView;
        private TextView typeTextView;

        //Конструктор для поиска элементов лэйаута
        public DevicesListElementHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView_DeviceListElementView_name);
            commentTextView = itemView.findViewById(R.id.textView_DeviceListElementView_comment);
            typeTextView = itemView.findViewById(R.id.textView_DeviceListElementView_type);
        }

        //Заполнение элементов вью данными
        public void bind(ContentValues element) {
            nameTextView.setText("Название прибора: " + element.getAsString("name"));
            commentTextView.setText("Комментарий: " + element.getAsString("comment"));
            typeTextView.setText("Тип прибора: "+ element.getAsString("type"));
        }

    }
}
