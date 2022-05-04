package com.github.vladosspasi.mes.Adapters;

import android.content.ContentValues;
import android.support.v7.widget.RecyclerView;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.vladosspasi.mes.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-адаптер списка приборов.
 */
public class DevicesListAdapter extends RecyclerView.Adapter<DevicesListAdapter.DevicesListElementHolder>{
    //Массив объектов списка
    private List<ContentValues> elementsList = new ArrayList<>();
    //Получение списка извне
    public void setItems(List<ContentValues> elems) {
        elementsList.addAll(elems);
        notifyDataSetChanged();
    }

    //Очистка списка
    public void clearItems() {
        elementsList.clear();
        notifyDataSetChanged();
    }

    //Установка xml-разметки отдельного элемента списка
    @NonNull
    @Override
    public DevicesListAdapter.DevicesListElementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_device_list_element, viewGroup, false);
        return new DevicesListAdapter.DevicesListElementHolder(view);
    }

    //Привязывание элементов к разметке
    @Override
    public void onBindViewHolder(@NonNull DevicesListAdapter.DevicesListElementHolder devicesListElementHolder, int i) {
        devicesListElementHolder.bind(elementsList.get(i));
    }

    //Получение числа элементов списка
    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    //Класс-холдер для элементов списка
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
            nameTextView.setText("Название прибора: " + element.getAsString("deviceName"));
            commentTextView.setText("Комментарий: " + element.getAsString("deviceComment"));
            typeTextView.setText("Тип прибора: "+ element.getAsString("deviceType"));
        }
    }
}
