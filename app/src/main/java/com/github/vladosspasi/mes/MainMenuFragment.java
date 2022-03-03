package com.github.vladosspasi.mes;

import android.os.Bundle;
import android.view.*;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
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

        binding.buttonMainmenuToAddScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainMenuFragment.this)
                        .navigate(R.id.action_MainmenuFragment_to_AddDataFragment);
            }
        });

        binding.buttonMainmenuToListScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainMenuFragment.this)
                        .navigate(R.id.action_MainmenuFragment_to_ListFragment);
            }
        });

        binding.buttonMainmenuToSettingScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainMenuFragment.this)
                        .navigate(R.id.action_MainmenuFragment_to_SettingsFragment);
            }
        });

        binding.buttonMainmenuToExpImpScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainMenuFragment.this)
                        .navigate(R.id.action_MainmenuFragment_to_ImpExpFragment);
            }
        });

        binding.buttonMainmenuToExpImpScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainMenuFragment.this)
                        .navigate(R.id.action_MainmenuFragment_to_ImpExpFragment);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
