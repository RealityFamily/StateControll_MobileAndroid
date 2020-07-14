package ru.realityfamily.statecontrol.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ru.realityfamily.statecontrol.R;

public class TypeChooseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.type_choose, null);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((Button) view.findViewById(R.id.Launcher)).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.ToLauncher));
        ((Button) view.findViewById(R.id.App)).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.ToApp));
    }
}
