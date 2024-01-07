package com.example.gevs.ui.user.admin.result;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.DistrictVote;
import com.example.gevs.databinding.FragmentAdminResultBinding;
import com.example.gevs.ui.user.adapters.ConstituencyAdapter;
import com.example.gevs.ui.user.admin.result.resultdetails.AdminResultDetailsActivity;
import com.example.gevs.util.NoteDecoration;

import java.util.List;

public class AdminResultFragment extends Fragment {

    FragmentAdminResultBinding binding;
    ConstituencyAdapter constituencyAdapter;
    BaseRepository baseRepository;

    public AdminResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_result, container, false);

        baseRepository = new BaseRepository();
        initAdapter();

        baseRepository.getDistrictVotes().observe(getViewLifecycleOwner(), new Observer<List<DistrictVote>>() {
            @Override
            public void onChanged(List<DistrictVote> districtVotes) {
                if (districtVotes != null) {
                    constituencyAdapter.setList(districtVotes);
                }
            }
        });

        return binding.getRoot();
    }

    public void initAdapter() {
        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        int spacing = 16;
        binding.adminDistrictResultRecyclerview.setPadding(spacing, spacing, spacing, spacing);
        binding.adminDistrictResultRecyclerview.setClipToPadding(false);
        binding.adminDistrictResultRecyclerview.setClipChildren(false);
        binding.adminDistrictResultRecyclerview.addItemDecoration(new NoteDecoration(spacing));
        binding.adminDistrictResultRecyclerview.setLayoutManager(glm);
        binding.adminDistrictResultRecyclerview.setHasFixedSize(true);
        constituencyAdapter = new ConstituencyAdapter(new ConstituencyAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(String key) {
                Intent i = new Intent(getContext(), AdminResultDetailsActivity.class);
                i.putExtra("Constituency", key);
                startActivity(i);
            }
        });
        binding.adminDistrictResultRecyclerview.setAdapter(constituencyAdapter);
    }

}