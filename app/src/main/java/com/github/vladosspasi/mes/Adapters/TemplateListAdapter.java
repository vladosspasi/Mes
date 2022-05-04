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
 * Класс-адаптер списка шаблонов.
 */
public class TemplateListAdapter extends RecyclerView.Adapter<TemplateListAdapter.TemplateListElementHolder>{
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
    public TemplateListAdapter.TemplateListElementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_templates_list_element, viewGroup, false);
        return new TemplateListElementHolder(view);
    }

    //Привязывание элементов к разметке
    @Override
    public void onBindViewHolder(@NonNull TemplateListAdapter.TemplateListElementHolder templateListElementHolder, int i) {
        templateListElementHolder.bind(elementsList.get(i));
    }

    //Получение числа элементов списка
    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    //Класс-холдер для элементов списка
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
            nameTextView.setText("Название: " + element.getAsString("tempName"));
            commentTextView.setText("Комментарий: "+ element.getAsString("tempComment"));
        }
    }
}

