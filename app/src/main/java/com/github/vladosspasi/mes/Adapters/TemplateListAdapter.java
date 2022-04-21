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

public class TemplateListAdapter extends RecyclerView.Adapter<TemplateListAdapter.TemplateListElementHolder>{

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
    public TemplateListAdapter.TemplateListElementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_templates_list_element, viewGroup, false);
        return new TemplateListElementHolder(view);
    }

    //что-то нужное
    @Override
    public void onBindViewHolder(@NonNull TemplateListAdapter.TemplateListElementHolder templateListElementHolder, int i) {
        templateListElementHolder.bind(elementsList.get(i));
    }

    //Количество элементов в списке
    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    //Холдер для элемента списка измерений
    static class TemplateListElementHolder extends RecyclerView.ViewHolder {

        //Все поля элемента списка с которыми происходит взаимодействие
        private TextView nameTextView;
        private TextView commentTextView;

        //Конструктор для поиска элементов лэйаута
        public TemplateListElementHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView_viewTemplateListElement_name);
            commentTextView = itemView.findViewById(R.id.textView_viewTemplateListElement_comment);
        }

        //Заполнение элементов вью данными
        public void bind(ContentValues element) {
            nameTextView.setText("Название: " + element.getAsString("name"));
            commentTextView.setText("Комментарий: "+ element.getAsString("comment"));
        }
    }
}

