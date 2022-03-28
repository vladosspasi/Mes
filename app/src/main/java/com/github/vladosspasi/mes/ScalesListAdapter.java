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

public class ScalesListAdapter extends RecyclerView.Adapter<ScalesListAdapter.ScalesListElementHolder> {


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
    public ScalesListAdapter.ScalesListElementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_scales_list_element, viewGroup, false);
        return new ScalesListAdapter.ScalesListElementHolder(view);
    }

    //что-то нужное
    @Override
    public void onBindViewHolder(@NonNull ScalesListAdapter.ScalesListElementHolder scalesListElementHolder, int i) {
        scalesListElementHolder.bind(elementsList.get(i));
    }

    //Количество элементов в списке
    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    //Холдер для элемента списка измерений
    class ScalesListElementHolder extends RecyclerView.ViewHolder {

        //Все поля элемента списка с которыми происходит взаимодействие
        private TextView nameTextView;
        private TextView commentTextView;
        private TextView typeTextView;
        private TextView minvalueTextView;
        private TextView maxvalueTextView;
        private TextView errorTextView;

        //Конструктор для поиска элементов лэйаута
        public ScalesListElementHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView_scalesListElementView_name);
            commentTextView = itemView.findViewById(R.id.textView_scalesListElementView_comment);
            typeTextView = itemView.findViewById(R.id.textView_scalesListElementView_type);
            minvalueTextView = itemView.findViewById(R.id.textView_scalesListElementView_minvalue);
            maxvalueTextView = itemView.findViewById(R.id.textView_scalesListElementView_maxvalue);
            errorTextView = itemView.findViewById(R.id.textView_scalesListElementView_error);
        }

        //Заполнение элементов вью данными
        public void bind(ContentValues element) {

            nameTextView.setText("Название: " + element.getAsString("name"));
            commentTextView.setText("Комментарий: " + element.getAsString("comment"));
            typeTextView.setText("Тип данных: "+ element.getAsString("type"));
            minvalueTextView.setText("Минимальное значение: " + element.getAsString("minvalue"));
            maxvalueTextView.setText("Минимальное значение: " + element.getAsString("maxvalue"));
            errorTextView.setText("Минимальное значение: " + element.getAsString("error"));
        }

    }

}
