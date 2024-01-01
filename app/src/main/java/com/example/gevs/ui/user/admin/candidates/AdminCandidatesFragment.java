package com.example.gevs.ui.user.admin.candidates;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.databinding.FragmentAdminCandidatesBinding;
import com.example.gevs.ui.user.adapters.CandidateAdapter;
import com.example.gevs.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminCandidatesFragment extends Fragment {

    FragmentAdminCandidatesBinding binding;
    List<String> expandableListTitle;
    HashMap<String, List<Candidate>> expandableListDetail;
    CandidateAdapter candidateAdapter;
    BaseRepository baseRepository;

    public AdminCandidatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_candidates, container, false);

        baseRepository = new BaseRepository();

        baseRepository.getAllCandidates().observe(getViewLifecycleOwner(), new Observer<List<Candidate>>() {
            @Override
            public void onChanged(List<Candidate> candidates) {
                if (candidates != null) {
                    expandableListDetail = createHashmap(candidates);
                    expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
                    candidateAdapter = new CandidateAdapter(getContext(), expandableListTitle, expandableListDetail);
                    binding.expandableListView.setAdapter(candidateAdapter);
                }
            }
        });

        binding.expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });


        return binding.getRoot();
    }

    public List<Candidate> segmentList(List<Candidate> masterList, String party) {
        List<Candidate> partyList = new ArrayList<>();
        for (Candidate c : masterList) {
            if (c.getParty().equals(party)) {
                // create party list
                partyList.add(c);
            }
        }
        return partyList;
    }

    public HashMap<String, List<Candidate>> createHashmap(List<Candidate> masterList) {
        HashMap<String, List<Candidate>> expandableListDetail = new HashMap<>();

        List<Candidate> blueParty = segmentList(masterList, Constants.PARTY_BLUE);
        List<Candidate> redParty = segmentList(masterList, Constants.PARTY_RED);
        List<Candidate> yellowParty = segmentList(masterList, Constants.PARTY_YELLOW);
        List<Candidate> independentParty = segmentList(masterList, Constants.PARTY_INDEPENDENT);

        expandableListDetail.put(Constants.PARTY_BLUE, blueParty);
        expandableListDetail.put(Constants.PARTY_RED, redParty);
        expandableListDetail.put(Constants.PARTY_YELLOW, yellowParty);
        expandableListDetail.put(Constants.PARTY_INDEPENDENT, independentParty);

        return expandableListDetail;
    }

}