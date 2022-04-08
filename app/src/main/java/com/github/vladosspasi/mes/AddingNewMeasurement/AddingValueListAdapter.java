package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.support.v7.widget.RecyclerView;
import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.github.vladosspasi.mes.R;

import java.util.ArrayList;
import java.util.List;

public class AddingValueListAdapter extends RecyclerView.Adapter<AddingValueListAdapter.AddingValueListElementHolder> {

    private List<ContentValues> elementsList = new ArrayList<>();

    public void setItems(List<ContentValues> elems) {
        elementsList.addAll(elems);
        notifyDataSetChanged();
    }

    public void clearItems() {
        elementsList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddingValueListElementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_addingvaluetomes_list_element, viewGroup, false);
        return new AddingValueListElementHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddingValueListElementHolder addingValueListElementHolder, int i) {
        addingValueListElementHolder.bind(elementsList.get(i));
    }

    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    class AddingValueListElementHolder extends RecyclerView.ViewHolder {

        //Все поля элемента списка с которыми происходит взаимодействие
        private TextView deviceNameTextView;
        private TextView scaleNameTextView;
        private TextView unitTextView;
        private TextView errorTextView;
        private EditText valueEditText;

        //Конструктор для поиска элементов лэйаута
        public AddingValueListElementHolder(View itemView) {
            super(itemView);
            deviceNameTextView = itemView.findViewById(R.id.textView_AddingValueListElementView_deviceName);
            scaleNameTextView = itemView.findViewById(R.id.textView_AddingValueListElementView_scaleName);
            unitTextView = itemView.findViewById(R.id.textView_AddingValueListElementView_unit);
            errorTextView = itemView.findViewById(R.id.textView_AddingValueListElementView_error);
            valueEditText = itemView.findViewById(R.id.editText_AddingValueListElementView_valueEnter);
        }

        //Заполнение элементов вью данными
        public void bind(ContentValues element) {
            deviceNameTextView.setText(element.getAsString("deviceName"));
            scaleNameTextView.setText(element.getAsString("scaleName"));
            unitTextView.setText(element.getAsString("scaleUnit"));
            errorTextView.setText(element.getAsString("scaleError"));
        }

    }

}