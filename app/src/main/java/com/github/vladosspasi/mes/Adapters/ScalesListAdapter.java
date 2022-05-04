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
 * Класс-адаптер списка шкал.
 */
public class ScalesListAdapter extends RecyclerView.Adapter<ScalesListAdapter.ScalesListElementHolder> {
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
    public ScalesListAdapter.ScalesListElementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_scales_list_element, viewGroup, false);
        return new ScalesListAdapter.ScalesListElementHolder(view);
    }

    //Привязывание элементов к разметке
    @Override
    public void onBindViewHolder(@NonNull ScalesListAdapter.ScalesListElementHolder scalesListElementHolder, int i) {
        scalesListElementHolder.bind(elementsList.get(i));
    }

    //Получение числа элементов списка
    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    //Класс-холдер для элементов списка
    class ScalesListElementHolder extends RecyclerView.ViewHolder {

        //Все объекты элемента списка с которыми происходит взаимодействие
        private TextView nameTextView;
        private TextView typeTextView;
        private TextView minvalueTextView;
        private TextView maxvalueTextView;
        private TextView errorTextView;
        private TextView unitTextView;

        //Конструктор для поиска элементов лэйаута
        public ScalesListElementHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView_scalesListElementView_name);
            typeTextView = itemView.findViewById(R.id.textView_scalesListElementView_type);
            minvalueTextView = itemView.findViewById(R.id.textView_scalesListElementView_minvalue);
            maxvalueTextView = itemView.findViewById(R.id.textView_scalesListElementView_maxvalue);
            errorTextView = itemView.findViewById(R.id.textView_scalesListElementView_error);
            unitTextView = itemView.findViewById(R.id.textView_scalesListElementView_unit);
        }

        //Заполнение элементов вью данными
        public void bind(ContentValues element) {
            nameTextView.setText("Название: " + element.getAsString("scaleName"));
            typeTextView.setText("Тип данных: "+ element.getAsString("valuetypeName"));
            minvalueTextView.setText("Минимальное значение: " + element.getAsString("scaleMin"));
            maxvalueTextView.setText("Максимальное значение: " + element.getAsString("scaleMax"));
            errorTextView.setText("Погрешность: " + element.getAsString("scaleError"));
            unitTextView.setText("Единица измерения: " + element.getAsString("scaleUnit"));
        }


    }



}
