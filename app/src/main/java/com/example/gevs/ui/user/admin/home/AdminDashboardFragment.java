package com.example.gevs.ui.user.admin.home;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.DistrictVote;
import com.example.gevs.data.pojo.ElectionResult;
import com.example.gevs.data.pojo.SeatCount;
import com.example.gevs.data.pojo.VoteCount;
import com.example.gevs.databinding.FragmentAdminDashboardBinding;
import com.example.gevs.util.Constants;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminDashboardFragment extends Fragment {

    FragmentAdminDashboardBinding binding;
    BaseRepository baseRepository;

    public AdminDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_dashboard, container, false);

        baseRepository = new BaseRepository();

        binding.startElectionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showElectionConfirmationDialog(true);
            }
        });

        binding.stopElectionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showElectionConfirmationDialog(false);
            }
        });

        baseRepository.getElectionStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (s.equals(Constants.ELECTION_STATUS_PENDING)) {
                        binding.startElectionFab.setVisibility(View.GONE);
                        binding.stopElectionFab.setVisibility(View.VISIBLE);
                        binding.electionStateTextview.setText("Ongoing");
                        binding.electionTimer.setVisibility(View.VISIBLE);
                    }
                    if (s.equals(Constants.ELECTION_STATUS_COMPLETED)) {
                        binding.startElectionFab.setVisibility(View.VISIBLE);
                        binding.stopElectionFab.setVisibility(View.GONE);
                        binding.electionStateTextview.setText("Starts soon");
                        binding.electionTimer.setVisibility(View.GONE);
                    }
                }
            }
        });

        return binding.getRoot();
    }

    public void showElectionConfirmationDialog(boolean state) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        String positiveButtonText;
        if (state) {
            positiveButtonText = getResources().getString(R.string.start);
            builder.setTitle("Start General Election");
            builder.setMessage("You are about to start the General Election for Shangri-la Valley");
        } else {
            positiveButtonText = getResources().getString(R.string.stop);
            builder.setTitle("Stop General Election");
            builder.setMessage("You are about to stop the General Election for Shangri-la Valley");
        }

        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (state) {
                    startElection();
                } else {
                    stopElection();
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void startElection() {
        createElectionData();
        createElectionResultData();
    }

    public void stopElection() {
        // switch state to completed for now
        baseRepository.stopElection();
    }

    public void createElectionResultData() {
        List<SeatCount> seats = new ArrayList<>();
        seats.add(new SeatCount(Constants.PARTY_BLUE, 0));
        seats.add(new SeatCount(Constants.PARTY_RED, 0));
        seats.add(new SeatCount(Constants.PARTY_YELLOW, 0));
        seats.add(new SeatCount(Constants.PARTY_INDEPENDENT, 0));

        ElectionResult electionResult = new ElectionResult(Constants.ELECTION_STATUS_PENDING, Constants.ELECTION_STATUS_PENDING, seats);
        baseRepository.createElectionResult(electionResult);
    }

    public void createElectionData() {
        baseRepository.getAllCandidates().observe(getViewLifecycleOwner(), new Observer<List<Candidate>>() {
            @Override
            public void onChanged(List<Candidate> candidates) {
                if (candidates != null) {
                    List<Candidate> shangriLaTown = filterCandidatesByConstituency(candidates, Constants.CONSTITUENCY_SHANGRI_LA_TOWN);
                    List<Candidate> northernKunlunMountain = filterCandidatesByConstituency(candidates, Constants.CONSTITUENCY_NORTHERN_KUNLUN_MOUNTAIN);
                    List<Candidate> westernShangriLa = filterCandidatesByConstituency(candidates, Constants.CONSTITUENCY_WESTERN_SHANGRI_LA);
                    List<Candidate> nabooVallery = filterCandidatesByConstituency(candidates, Constants.CONSTITUENCY_NABOO_VALLERY);
                    List<Candidate> newFelucia = filterCandidatesByConstituency(candidates, Constants.CONSTITUENCY_NEW_FELUCIA);

                    baseRepository.createElectionData(createDistrictHashmap(shangriLaTown, northernKunlunMountain, westernShangriLa, nabooVallery, newFelucia));

                }
            }
        });

    }

    public List<Candidate> filterCandidatesByConstituency(List<Candidate> masterList, String constituency) {
        List<Candidate> constituencyList = new ArrayList<>();
        for (Candidate c : masterList) {
            if (c.getConstituency().equals(constituency)) {
                // create party list
                constituencyList.add(c);
            }
        }
        return constituencyList;
    }

    public HashMap<String, DistrictVote> createDistrictHashmap(List<Candidate> shangriLaTown,
                                                               List<Candidate> northernKunlunMountain,
                                                               List<Candidate> westernShangriLa,
                                                               List<Candidate> nabooVallery,
                                                               List<Candidate> newFelucia) {
        HashMap<String, DistrictVote> districtVoteHashMap = new HashMap<>();

        HashMap<String, VoteCount> shangriLaTownVoteCount = new HashMap<>();
        for (Candidate c : shangriLaTown) {
            shangriLaTownVoteCount.put(c.getParty(), new VoteCount(c.getName(), c.getParty(), 0L));
        }

        HashMap<String, VoteCount> northernKunlunMountainVoteCount = new HashMap<>();
        for (Candidate c : northernKunlunMountain) {
            northernKunlunMountainVoteCount.put(c.getParty(), new VoteCount(c.getName(), c.getParty(), 0L));
        }

        HashMap<String, VoteCount> westernShangriLaVoteCount = new HashMap<>();
        for (Candidate c : westernShangriLa) {
            westernShangriLaVoteCount.put(c.getParty(), new VoteCount(c.getName(), c.getParty(), 0L));
        }

        HashMap<String, VoteCount> nabooValleryVoteCount = new HashMap<>();
        for (Candidate c : nabooVallery) {
            nabooValleryVoteCount.put(c.getParty(), new VoteCount(c.getName(), c.getParty(), 0L));
        }

        HashMap<String, VoteCount> newFeluciaVoteCount = new HashMap<>();
        for (Candidate c : newFelucia) {
            newFeluciaVoteCount.put(c.getParty(), new VoteCount(c.getName(), c.getParty(), 0L));
        }

        districtVoteHashMap.put(Constants.CONSTITUENCY_SHANGRI_LA_TOWN, new DistrictVote(Constants.CONSTITUENCY_SHANGRI_LA_TOWN, shangriLaTownVoteCount));
        districtVoteHashMap.put(Constants.CONSTITUENCY_NORTHERN_KUNLUN_MOUNTAIN, new DistrictVote(Constants.CONSTITUENCY_NORTHERN_KUNLUN_MOUNTAIN, northernKunlunMountainVoteCount));
        districtVoteHashMap.put(Constants.CONSTITUENCY_WESTERN_SHANGRI_LA, new DistrictVote(Constants.CONSTITUENCY_WESTERN_SHANGRI_LA, westernShangriLaVoteCount));
        districtVoteHashMap.put(Constants.CONSTITUENCY_NABOO_VALLERY, new DistrictVote(Constants.CONSTITUENCY_NABOO_VALLERY, nabooValleryVoteCount));
        districtVoteHashMap.put(Constants.CONSTITUENCY_NEW_FELUCIA, new DistrictVote(Constants.CONSTITUENCY_NEW_FELUCIA, newFeluciaVoteCount));

        return districtVoteHashMap;
    }


}