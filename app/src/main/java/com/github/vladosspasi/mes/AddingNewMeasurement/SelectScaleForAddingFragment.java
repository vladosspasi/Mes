package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.*;
import com.github.vladosspasi.mes.Adapters.ScalesListAdapter;
import com.github.vladosspasi.mes.databinding.FragmentSelectscaleforaddingBinding;
import java.util.ArrayList;
import java.util.Objects;

import static com.github.vladosspasi.mes.DataBaseHelper.*;

public class SelectScaleForAddingFragment extends Fragment {

    private FragmentSelectscaleforaddingBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<ContentValues> scalesList;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSelectscaleforaddingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arg = this.getArguments();
        assert arg != null;
        int deviceId = arg.getInt("DeviceId");

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        scalesList = dataBaseHelper.getScalesByDeviceId(deviceId);
        dataBaseHelper.close();
        ScalesListAdapter scalesListAdapter = new ScalesListAdapter();
        scalesListAdapter.setItems(scalesList);

        recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.recView_SelectScale_scales);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(scalesListAdapter);

        binding.recViewSelectScaleScales.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        MeasurementGlobalInfo.addToScalesList(scalesList.get(position));
                        NavHostFragment.findNavController(SelectScaleForAddingFragment.this)
                                .navigate(R.id.action_AddNewMesSelectScaleFragment_to_AddNewValuesFragment);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }){
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
