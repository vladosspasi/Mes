package com.github.vladosspasi.mes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import com.github.vladosspasi.mes.databinding.FragmentScreenListBinding;
import java.util.logging.Logger;

public class ListFragment extends Fragment {


    private FragmentScreenListBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScreenListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private static Logger log = Logger.getLogger(MainActivity.class.getName());

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.testbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String directory = getActivity().getBaseContext().getFilesDir().toString();
                Toast.makeText(getContext(), directory, Toast.LENGTH_LONG).show();

                DataBaseHelper dbHelper = DataBaseHelper.getInstance(getContext());
                //ContentValues output = dbHelper.testDatabase();





            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}