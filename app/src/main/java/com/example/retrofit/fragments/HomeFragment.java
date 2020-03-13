package com.example.retrofit.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.retrofit.R;
import com.example.retrofit.storage.SharedPrefManager;



public class HomeFragment extends Fragment {




    private TextView textViewEmail, textViewName, textViewSchool;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewName = view.findViewById(R.id.textViewName);
        textViewSchool = view.findViewById(R.id.textViewSchool);

        textViewEmail.setText(SharedPrefManager.getInstance(getActivity()).getUser().getEmail());
        textViewName.setText(SharedPrefManager.getInstance(getActivity()).getUser().getName());
        textViewSchool.setText(SharedPrefManager.getInstance(getActivity()).getUser().getSchool());
    }
}
