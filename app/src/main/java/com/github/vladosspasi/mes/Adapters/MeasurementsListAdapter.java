package com.github.vladosspasi.mes.Adapters;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.vladosspasi.mes.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-адаптер списка измерений.
 */
public class MeasurementsListAdapter extends RecyclerView.Adapter<MeasurementsListAdapter.MeasurementsListElementHolder> {

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
    public MeasurementsListElementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_mes_list_element, viewGroup, false);
        return new MeasurementsListElementHolder(view);
    }

    //Привязывание элементов к разметке
    @Override
    public void onBindViewHolder(@NonNull MeasurementsListElementHolder measurementsListElementHolder, int i) {
        measurementsListElementHolder.bind(elementsList.get(i));
    }

    //Получение числа элементов списка
    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    //Класс-холдер для элементов списка
    class MeasurementsListElementHolder extends RecyclerView.ViewHolder {

        //Все поля элемента списка с которыми происходит взаимодействие
        private TextView nameTextView;
        private TextView commentTextView;
        private TextView dateTextView;

        //Конструктор для поиска элементов лэйаута
        public MeasurementsListElementHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView_mesListElementView_name);
            commentTextView = itemView.findViewById(R.id.textView_mesListElementView_comment);
            dateTextView = itemView.findViewById(R.id.textView_mesListElementView_date);
        }

        //Заполнение элементов вью данными
        public void bind(ContentValues element) {
            nameTextView.setText(element.getAsString("mesName"));
            commentTextView.setText("Комментарий:\n"+ element.getAsString("mesComment"));
            dateTextView.setText("Дата снятия:\n"+ element.getAsString("mesDate"));
        }

    }
}
