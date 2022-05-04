package com.github.vladosspasi.mes;
import android.os.Bundle;
import android.view.*;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.github.vladosspasi.mes.AddingNewMeasurement.MeasurementGlobalInfo;
import com.github.vladosspasi.mes.databinding.FragmentMainmenuBinding;

public class MainMenuFragment extends Fragment{

    private FragmentMainmenuBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMainmenuBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_info:
                NavHostFragment.findNavController(MainMenuFragment.this)
                        .navigate(R.id.action_MainmenuFragment_to_InfoFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonMainmenuToAddScreen.setOnClickListener(view1 -> {

            DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
            if(dataBaseHelper.countDevices()==0){
                Toast.makeText(getContext(),
                        "Нет приборов. Перейдите в Настройки->Сохраненные приборы.",
                        Toast.LENGTH_LONG).show();
            }else{
                NavHostFragment.findNavController(MainMenuFragment.this)
                        .navigate(R.id.action_MainmenuFragment_to_AddNewMesInfoFragment);
            }
            dataBaseHelper.close();
        });

        binding.buttonMainmenuToListScreen.setOnClickListener(view12 -> NavHostFragment.findNavController(MainMenuFragment.this)
                .navigate(R.id.action_MainmenuFragment_to_ListFragment));

        binding.buttonMainmenuToSettingScreen.setOnClickListener(view13 -> NavHostFragment.findNavController(MainMenuFragment.this)
                .navigate(R.id.action_MainmenuFragment_to_SettingsFragment));

        MeasurementGlobalInfo.clearAll();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
