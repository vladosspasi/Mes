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
 * Класс-адаптер списка величин измерения.
 */
public class ValuesListAdapter extends RecyclerView.Adapter<ValuesListAdapter.ValuesListElementHolder>{
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
    public ValuesListElementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_val_list_element, viewGroup, false);
        return new ValuesListElementHolder(view);
    }

    //Привязывание элементов к разметке
    @Override
    public void onBindViewHolder(@NonNull ValuesListElementHolder valuesListElementHolder, int i) {
        valuesListElementHolder.bind(elementsList.get(i),i+1);
    }

    //Получение числа элементов списка
    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    //Класс-холдер для элементов списка
    class ValuesListElementHolder extends RecyclerView.ViewHolder {

        //Все поля элемента списка с которыми происходит взаимодействие
        private TextView idTextView;
        private TextView valueTitleTextView;
        private TextView errorTitleTextView;
        private TextView unitTitleTextView;
        private TextView valueTextView;
        private TextView errorTextView;
        private TextView unitTextView;
        private TextView scaleNameTextView;
        private TextView deviceNameTextView;

        //Конструктор для поиска элементов лэйаута
        public ValuesListElementHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.textView_valListElementView_id);
            valueTitleTextView = itemView.findViewById(R.id.textView_valListElementView_valueTitle);
            errorTitleTextView = itemView.findViewById(R.id.textView_valListElementView_errorTitle);
            unitTitleTextView = itemView.findViewById(R.id.textView_valListElementView_unitTitle);
            valueTextView = itemView.findViewById(R.id.textView_valListElementView_value);
            errorTextView = itemView.findViewById(R.id.textView_valListElementView_error);
            unitTextView = itemView.findViewById(R.id.textView_valListElementView_unit);
            scaleNameTextView = itemView.findViewById(R.id.textView_valListElementView_scaleTitle);
            deviceNameTextView = itemView.findViewById(R.id.textView_valListElementView_deviceTitle);
        }

        //Заполнение элементов вью данными
        public void bind(ContentValues element, int i) {
            idTextView.setText("Значение №"+i);
            valueTitleTextView.setText("Значение");
            errorTitleTextView.setText("Погр.");
            unitTitleTextView.setText("Ед. изм.");
            valueTextView.setText(element.getAsString("value"));
            errorTextView.setText("+-"+element.getAsString("scaleError"));
            unitTextView.setText(element.getAsString("scaleUnit"));
            scaleNameTextView.setText("Шкала: "+element.getAsString("scaleName"));
            deviceNameTextView.setText("Прибор: "+element.getAsString("deviceName"));
        }
    }
}
