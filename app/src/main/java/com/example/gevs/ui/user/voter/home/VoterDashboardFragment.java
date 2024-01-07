package com.example.gevs.ui.user.voter.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.gevs.R;
import com.example.gevs.databinding.FragmentVoterDashboardBinding;

public class VoterDashboardFragment extends Fragment {

    FragmentVoterDashboardBinding binding;

    public VoterDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_dashboard, container, false);


        return binding.getRoot();
    }

}
