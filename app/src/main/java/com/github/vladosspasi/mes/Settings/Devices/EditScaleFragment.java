package com.github.vladosspasi.mes.Settings.Devices;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.DataBaseHelper;
import com.github.vladosspasi.mes.R;
import com.github.vladosspasi.mes.databinding.FragmentEditscaleBinding;

import java.util.ArrayList;

import static com.github.vladosspasi.mes.DataBaseHelper.*;

public class EditScaleFragment extends Fragment {

    private FragmentEditscaleBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentEditscaleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        int scalePosition = bundle.getInt("ScalePos");

        ContentValues scaleInfo = GlobalDeviceInfo.getScaleAt(scalePosition);

        binding.edittextEditScaleName.setText(scaleInfo.getAsString("scaleName"));
        binding.edittextEditScaleError.setText(scaleInfo.getAsString("scaleError"));
        binding.edittextEditScaleUnit.setText(scaleInfo.getAsString("scaleUnit"));
        binding.edittextEditScaleMin.setText(scaleInfo.getAsString("scaleMin"));
        binding.edittextEditScaleMax.setText(scaleInfo.getAsString("scaleMax"));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEditScaleType.setAdapter(adapter);

        binding.spinnerEditScaleType.setSelection(scaleInfo.getAsInteger("scaleTypeId") - 1);

        binding.buttonEditScaleSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges(scalePosition);
                Toast.makeText(getContext(), "Шкала обновлена.", Toast.LENGTH_SHORT);
                NavHostFragment.findNavController(EditScaleFragment.this).navigateUp();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void saveChanges(int pos) {

        ArrayList<ContentValues> scalesList = GlobalDeviceInfo.getScales();
        String scaleID = scalesList.get(pos).getAsString("scaleId");
        ContentValues newScale = new ContentValues();

        int selectedType = binding.spinnerEditScaleType.getSelectedItemPosition();
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        String type = dataBaseHelper.getValueTypeById(selectedType + 1);
        dataBaseHelper.close();

        newScale.put("scaleId", scaleID);
        newScale.put("scaleName", binding.edittextEditScaleName.getText().toString());
        newScale.put("scaleUnit", binding.edittextEditScaleUnit.getText().toString());
        newScale.put("scaleMax", binding.edittextEditScaleMax.getText().toString());
        newScale.put("scaleMin", binding.edittextEditScaleMin.getText().toString());
        newScale.put("scaleError", binding.edittextEditScaleError.getText().toString());

        newScale.put("scaleTypeId", selectedType + 1);

        newScale.put("valuetypeName", type);

        GlobalDeviceInfo.setScaleAt(pos, newScale);


    }

}
