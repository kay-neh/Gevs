package com.example.gevs.ui.user.admin.voters;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.databinding.FragmentAdminVotersBinding;
import com.example.gevs.ui.user.adapters.VoterAdapter;

import java.util.List;


public class AdminVotersFragment extends Fragment {

    FragmentAdminVotersBinding binding;
    BaseRepository baseRepository;
    VoterAdapter voterAdapter;

    public AdminVotersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_voters, container, false);

        baseRepository = new BaseRepository();
        initAdapter();

        baseRepository.getAllVoters().observe(getViewLifecycleOwner(), new Observer<List<Voter>>() {
            @Override
            public void onChanged(List<Voter> voters) {
                if (voters != null) {
                    voterAdapter.setList(voters);
                    if (voters.size() == 0) {
                        binding.adminVotersEmptyState.setVisibility(View.VISIBLE);
                    } else {
                        binding.adminVotersEmptyState.setVisibility(View.GONE);
                    }
                }
            }
        });

        return binding.getRoot();

    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.adminVotersRecyclerview.setLayoutManager(llm);
        binding.adminVotersRecyclerview.setHasFixedSize(true);
        voterAdapter = new VoterAdapter();
        binding.adminVotersRecyclerview.setAdapter(voterAdapter);
    }

}