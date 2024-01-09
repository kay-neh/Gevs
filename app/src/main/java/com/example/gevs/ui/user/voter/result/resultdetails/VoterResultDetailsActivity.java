package com.example.gevs.ui.user.voter.result.resultdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.DistrictVote;
import com.example.gevs.data.pojo.DistrictVoteCount;
import com.example.gevs.data.pojo.VoteCount;
import com.example.gevs.databinding.ActivityVoterResultDetailsBinding;
import com.example.gevs.ui.user.adapters.VoteCountAdapter;
import com.example.gevs.ui.user.admin.result.resultdetails.AdminResultDetailsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoterResultDetailsActivity extends AppCompatActivity {

    ActivityVoterResultDetailsBinding binding;
    String constituency;
    VoteCountAdapter voteCountAdapter;
    BaseRepository baseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voter_result_details);

        constituency = getIntent().getStringExtra("Constituency");
        baseRepository = new BaseRepository();
        initAdapter();

        binding.adminResultDetailsToolbar.setTitle(constituency);
        binding.adminResultDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        baseRepository.getDistrictVoteById(constituency).observe(this, new Observer<DistrictVote>() {
            @Override
            public void onChanged(DistrictVote districtVote) {
                if (districtVote != null) {
                    baseRepository.getCandidatesByConstituency(constituency).observe(VoterResultDetailsActivity.this, new Observer<List<Candidate>>() {
                        @Override
                        public void onChanged(List<Candidate> candidateList) {
                            if (candidateList != null) {
                                voteCountAdapter.setList(getDistrictVoteCount(convertHashmapToList(districtVote.getResult()), candidateList));
                            }
                        }
                    });

                }
            }
        });

    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.adminVoteCountRecyclerview.setLayoutManager(llm);
        binding.adminVoteCountRecyclerview.setHasFixedSize(true);
        voteCountAdapter = new VoteCountAdapter();
        binding.adminVoteCountRecyclerview.setAdapter(voteCountAdapter);
    }

    public List<VoteCount> convertHashmapToList(HashMap<String, VoteCount> voteCountHashMap) {
        if (voteCountHashMap != null) {
            return new ArrayList<>(voteCountHashMap.values());
        } else {
            return new ArrayList<>();
        }
    }

    public List<DistrictVoteCount> getDistrictVoteCount(List<VoteCount> voteCountList, List<Candidate> candidateList) {
        List<DistrictVoteCount> districtVoteCountList = new ArrayList<>();
        for (VoteCount v : voteCountList) {
            for (Candidate c : candidateList) {
                if (v.getName().equals(c.getName())) {
                    // create new list with photos
                    districtVoteCountList.add(new DistrictVoteCount(v.getName(), v.getParty(), c.getPhoto(), v.getVote()));
                }
            }
        }
        return districtVoteCountList;
    }

}