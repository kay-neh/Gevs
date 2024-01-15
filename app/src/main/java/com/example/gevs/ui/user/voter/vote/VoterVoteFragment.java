package com.example.gevs.ui.user.voter.vote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.CandidateVote;
import com.example.gevs.data.pojo.Vote;
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.databinding.FragmentVoterVoteBinding;
import com.example.gevs.ui.user.adapters.DistrictCandidateVoteAdapter;
import com.example.gevs.ui.user.voter.vote.votedetails.VoterVoteDetailsActivity;
import com.example.gevs.util.Constants;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class VoterVoteFragment extends Fragment {

    FragmentVoterVoteBinding binding;
    DistrictCandidateVoteAdapter districtCandidateVoteAdapter;
    BaseRepository baseRepository;
    FirebaseAuth firebaseAuth;
    String votedParty;

    public VoterVoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_vote, container, false);

        baseRepository = new BaseRepository();
        firebaseAuth = FirebaseAuth.getInstance();
        initAdapter();

        if (firebaseAuth.getCurrentUser() != null) {
            baseRepository.getVoter(firebaseAuth.getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<Voter>() {
                @Override
                public void onChanged(Voter voter) {
                    if (voter != null) {
                        baseRepository.getCandidatesByConstituency(voter.getConstituency()).observe(getViewLifecycleOwner(), new Observer<List<Candidate>>() {
                            @Override
                            public void onChanged(List<Candidate> candidateList) {
                                if (candidateList != null) {
                                    createAdapterList(candidateList, firebaseAuth.getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<List<CandidateVote>>() {
                                        @Override
                                        public void onChanged(List<CandidateVote> candidateVotes) {
                                            districtCandidateVoteAdapter.setList(candidateVotes);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }

        baseRepository.getElectionStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (s.equals(Constants.ELECTION_STATUS_PENDING)) {
                        binding.votersEmptyState.setVisibility(View.GONE);
                        binding.voterVoteCandidatesRecyclerview.setVisibility(View.VISIBLE);
                    }
                    if (s.equals(Constants.ELECTION_STATUS_COMPLETED)) {
                        binding.votersEmptyState.setVisibility(View.VISIBLE);
                        binding.adminVotersEmptyText.setText("Ballots boxed, decisions made!");
                        binding.adminVotersEmptyExtraText.setText("Thank you for voting");
                        binding.voterVoteCandidatesRecyclerview.setVisibility(View.GONE);
                    }
                } else {
                    binding.votersEmptyState.setVisibility(View.VISIBLE);
                    binding.adminVotersEmptyText.setText("Polls haven't opened yet!");
                    binding.adminVotersEmptyExtraText.setText("Stay tuned for election time");
                    binding.voterVoteCandidatesRecyclerview.setVisibility(View.GONE);
                }
            }
        });


        return binding.getRoot();
    }

    public LiveData<List<CandidateVote>> createAdapterList(List<Candidate> candidateList, String userId) {
        final MutableLiveData<List<CandidateVote>> data = new MutableLiveData<>();
        List<CandidateVote> candidateVoteList = new ArrayList<>();
        baseRepository.hasVoted(userId).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null) {
                    candidateVoteList.clear();
                    if (aBoolean) {
                        // has voted
                        baseRepository.getVote(userId).observe(getViewLifecycleOwner(), new Observer<Vote>() {
                            @Override
                            public void onChanged(Vote vote) {
                                if (vote != null) {
                                    votedParty = vote.getParty();
                                    for (Candidate c : candidateList) {
                                        candidateVoteList.add(new CandidateVote(c.getName(), c.getPhoto(), c.getParty(), c.getConstituency(), votedParty, true));
                                    }
                                    data.setValue(candidateVoteList);
                                }
                            }
                        });
                    } else {
                        for (Candidate c : candidateList) {
                            candidateVoteList.add(new CandidateVote(c.getName(), c.getPhoto(), c.getParty(), c.getConstituency(), votedParty, false));
                        }
                        data.setValue(candidateVoteList);
                    }

                }
            }
        });

        return data;
    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.voterVoteCandidatesRecyclerview.setLayoutManager(llm);
        binding.voterVoteCandidatesRecyclerview.setHasFixedSize(true);
        districtCandidateVoteAdapter = new DistrictCandidateVoteAdapter(new DistrictCandidateVoteAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(String key) {
                Intent i = new Intent(getContext(), VoterVoteDetailsActivity.class);
                i.putExtra("CandidateId", key);
                startActivity(i);
            }
        });
        binding.voterVoteCandidatesRecyclerview.setAdapter(districtCandidateVoteAdapter);
    }

}
