package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.*;
import com.github.vladosspasi.mes.databinding.FragmentSelectdeviceforaddingBinding;
import com.github.vladosspasi.mes.databinding.FragmentSelectscaleforaddingBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectScaleForAddingFragment extends Fragment {

    private FragmentSelectscaleforaddingBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<ContentValues> scalesList;
    private int deviceId;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSelectscaleforaddingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arg = this.getArguments();
        assert arg != null;
        deviceId = arg.getInt("DeviceId");

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        scalesList = dataBaseHelper.getScalesByDeviceId(deviceId);
        dataBaseHelper.close();
        ScalesListAdapter scalesListAdapter = new ScalesListAdapter();
        scalesListAdapter.setItems(scalesList);

        recyclerView = getActivity().findViewById(R.id.recView_SelectScale_scales);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(scalesListAdapter);

        binding.recViewSelectScaleScales.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int id = scalesList.get(position).getAsInteger("id");
                        Bundle arg = getArguments();

                        ArrayList<ParsInteger> scalesIds = arg.getParcelableArrayList("ScalesIds");
                        scalesIds.add(new ParsInteger(id));
                        arg.putParcelableArrayList("ScalesIds", scalesIds);
                        NavHostFragment.findNavController(SelectScaleForAddingFragment.this)
                                .navigate(R.id.action_AddNewMesSelectScaleFragment_to_AddNewValuesFragment, arg);
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
