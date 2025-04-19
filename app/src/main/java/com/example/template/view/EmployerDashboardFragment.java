package com.example.template.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.template.R;


public class EmployerDashboardFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_employer, container, false);

        return view;
    }

    public EmployerDashboardFragment() {
        // Required empty public constructor
    }
}
