package com.example.gevs.ui.user.voter.result;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.DistrictVote;
import com.example.gevs.data.pojo.DistrictVoteCount;
import com.example.gevs.data.pojo.ElectionResult;
import com.example.gevs.data.pojo.VoteCount;
import com.example.gevs.databinding.FragmentVoterResultBinding;
import com.example.gevs.ui.user.adapters.VoteCountAdapter;
import com.example.gevs.ui.user.voter.result.analytics.VoterAnalyticsActivity;
import com.example.gevs.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoterResultFragment extends Fragment {

    FragmentVoterResultBinding binding;
    BaseRepository baseRepository;
    List<String> expandableListTitle;
    HashMap<String, List<DistrictVoteCount>> expandableListDetail;
    VoteCountAdapter voteCountAdapter;

    public VoterResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_result, container, false);

        baseRepository = new BaseRepository();

        binding.analyticsTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), VoterAnalyticsActivity.class));
            }
        });

        binding.expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });

        baseRepository.isResultPublished().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        binding.contentScrollView.setVisibility(View.VISIBLE);
                        binding.resultEmptyState.setVisibility(View.GONE);
                    } else {
                        binding.contentScrollView.setVisibility(View.GONE);
                        binding.resultEmptyState.setVisibility(View.VISIBLE);
                        binding.adminVotersEmptyText.setText("Vote counting is in progress...");
                        binding.adminVotersEmptyExtraText.setText("Results coming soon! ");
                    }
                } else {
                    binding.contentScrollView.setVisibility(View.GONE);
                    binding.resultEmptyState.setVisibility(View.VISIBLE);
                    binding.adminVotersEmptyText.setText("Polls haven't opened yet!");
                    binding.adminVotersEmptyExtraText.setText("Stay tuned for election time ");
                }
            }
        });

        // HERE
        baseRepository.getDistrictVotes().observe(getViewLifecycleOwner(), new Observer<List<DistrictVote>>() {
            @Override
            public void onChanged(List<DistrictVote> districtVotes) {
                if (districtVotes != null) {
                    createHashmap(districtVotes).observe(getViewLifecycleOwner(), new Observer<HashMap<String, List<DistrictVoteCount>>>() {
                        @Override
                        public void onChanged(HashMap<String, List<DistrictVoteCount>> stringListHashMap) {
                            expandableListDetail = stringListHashMap;
                            expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
                            voteCountAdapter = new VoteCountAdapter(getContext(), expandableListTitle, expandableListDetail);
                            binding.expandableListView.setAdapter(voteCountAdapter);
                        }
                    });
                }
            }
        });

        baseRepository.getElectionResult().observe(getViewLifecycleOwner(), new Observer<ElectionResult>() {
            @Override
            public void onChanged(ElectionResult electionResult) {
                if (electionResult != null) {
                    if (electionResult.getWinner().equals(Constants.PARTY_BLUE)) {
                        binding.electionWinnerTextview.setText(electionResult.getWinner() + " Wins");
                        binding.winnerTextview.setText("Winner");
                        binding.winnerPartyImageCardView.setVisibility(View.VISIBLE);
                        binding.winnerPartyImageView.setImageResource(R.drawable.blue);
                        binding.hungImageView.setVisibility(View.GONE);
                    } else if (electionResult.getWinner().equals(Constants.PARTY_RED)) {
                        binding.electionWinnerTextview.setText(electionResult.getWinner() + " Wins");
                        binding.winnerTextview.setText("Winner");
                        binding.winnerPartyImageCardView.setVisibility(View.VISIBLE);
                        binding.winnerPartyImageView.setImageResource(R.drawable.red);
                        binding.hungImageView.setVisibility(View.GONE);
                    } else if (electionResult.getWinner().equals(Constants.PARTY_YELLOW)) {
                        binding.electionWinnerTextview.setText(electionResult.getWinner() + " Wins");
                        binding.winnerTextview.setText("Winner");
                        binding.winnerPartyImageCardView.setVisibility(View.VISIBLE);
                        binding.winnerPartyImageView.setImageResource(R.drawable.yellow);
                        binding.hungImageView.setVisibility(View.GONE);
                    } else if (electionResult.getWinner().equals(Constants.PARTY_INDEPENDENT)) {
                        binding.electionWinnerTextview.setText(electionResult.getWinner() + " Wins");
                        binding.winnerTextview.setText("Winner");
                        binding.winnerPartyImageCardView.setVisibility(View.VISIBLE);
                        binding.winnerPartyImageView.setImageResource(R.drawable.independent);
                        binding.hungImageView.setVisibility(View.GONE);
                    } else {
                        binding.electionWinnerTextview.setText(electionResult.getWinner());
                        binding.winnerTextview.setText("No winner");
                        binding.winnerPartyImageCardView.setVisibility(View.INVISIBLE);
                        binding.hungImageView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return binding.getRoot();
    }

    public LiveData<HashMap<String, List<DistrictVoteCount>>> createHashmap(List<DistrictVote> masterList) {
        final MutableLiveData<HashMap<String, List<DistrictVoteCount>>> data = new MutableLiveData<>();
        HashMap<String, List<DistrictVoteCount>> expandableListDetail = new HashMap<>();

        for (DistrictVote d : masterList) {
            baseRepository.getCandidatesByConstituency(d.getConstituency()).observe(getViewLifecycleOwner(), new Observer<List<Candidate>>() {
                @Override
                public void onChanged(List<Candidate> candidateList) {
                    expandableListDetail.put(d.getConstituency(), getDistrictVoteCountList(convertHashmapToList(d.getResult()), candidateList));
                    data.setValue(expandableListDetail);
                }
            });
        }

        return data;
    }

    public List<VoteCount> convertHashmapToList(HashMap<String, VoteCount> voteCountHashMap) {
        if (voteCountHashMap != null) {
            return new ArrayList<>(voteCountHashMap.values());
        } else {
            return new ArrayList<>();
        }
    }

    public List<DistrictVoteCount> getDistrictVoteCountList(List<VoteCount> voteCountList, List<Candidate> sortedCandidateList) {
        List<DistrictVoteCount> districtVoteCountList = new ArrayList<>();
        for (VoteCount v : voteCountList) {
            for (Candidate c : sortedCandidateList) {
                if (v.getName().equals(c.getName())) {
                    // create new list with photos
                    districtVoteCountList.add(new DistrictVoteCount(v.getName(), v.getParty(), c.getPhoto(), v.getVote()));
                }
            }
        }
        return districtVoteCountList;
    }

}