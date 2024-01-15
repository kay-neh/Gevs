package com.example.gevs.ui.user.admin.home;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.DistrictPartyWinner;
import com.example.gevs.data.pojo.DistrictVote;
import com.example.gevs.data.pojo.ElectionResult;
import com.example.gevs.data.pojo.SeatCount;
import com.example.gevs.data.pojo.Vote;
import com.example.gevs.data.pojo.VoteCount;
import com.example.gevs.databinding.FragmentAdminDashboardBinding;
import com.example.gevs.ui.user.adapters.VoteAdapter;
import com.example.gevs.util.Constants;
import com.example.gevs.util.FCMSender;
import com.example.gevs.util.NotificationMessage;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdminDashboardFragment extends Fragment {

    FragmentAdminDashboardBinding binding;
    BaseRepository baseRepository;
    VoteAdapter voteAdapter;

    public AdminDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_dashboard, container, false);

        baseRepository = new BaseRepository();
        initAdapter();

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

        baseRepository.getVotesByTime().observe(getViewLifecycleOwner(), new Observer<List<Vote>>() {
            @Override
            public void onChanged(List<Vote> votes) {
                if (votes != null) {
                    Collections.reverse(votes);
                    voteAdapter.setList(votes);
                }
            }
        });

        baseRepository.getElectionStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (s.equals(Constants.ELECTION_STATUS_PENDING)) {
                        binding.startElectionFab.setVisibility(View.INVISIBLE);
                        binding.stopElectionFab.setVisibility(View.VISIBLE);
                        binding.electionStateTextview.setText("Ongoing");
                        binding.electionTimer.setVisibility(View.VISIBLE);
                        binding.adminLiveUpdateRecyclerview.setVisibility(View.VISIBLE);
                        binding.adminLiveUpdateEmptyState.setVisibility(View.GONE);
                    }
                    if (s.equals(Constants.ELECTION_STATUS_COMPLETED)) {
                        binding.startElectionFab.setVisibility(View.VISIBLE);
                        binding.stopElectionFab.setVisibility(View.INVISIBLE);
                        binding.electionStateTextview.setText("Starts soon");
                        binding.electionTimer.setVisibility(View.GONE);
                        binding.adminLiveUpdateRecyclerview.setVisibility(View.GONE);
                        binding.adminLiveUpdateEmptyState.setVisibility(View.VISIBLE);
                        binding.adminVotersEmptyText.setText("No active Election!");
//                        baseRepository.getElectionResult().observe(getViewLifecycleOwner(), new Observer<ElectionResult>() {
//                            @Override
//                            public void onChanged(ElectionResult electionResult) {
//                                if (electionResult != null) {
//                                    if (electionResult.getWinner().equals(Constants.ELECTION_STATUS_PENDING)) {
//                                        baseRepository.updateWinner(Constants.HUNG_PARLIAMENT);
//                                    }
//                                }
//                            }
//                        });

                    }
                }
            }
        });

        return binding.getRoot();
    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.adminLiveUpdateRecyclerview.setLayoutManager(llm);
        binding.adminLiveUpdateRecyclerview.setHasFixedSize(true);
        voteAdapter = new VoteAdapter();
        binding.adminLiveUpdateRecyclerview.setAdapter(voteAdapter);
    }

    public void showElectionConfirmationDialog(boolean state) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        String positiveButtonText;
        if (state) {
            positiveButtonText = getResources().getString(R.string.start);
            builder.setTitle("Start General Election");
            builder.setMessage("You are about to start the General Election for Shangri-La Valley");
        } else {
            positiveButtonText = getResources().getString(R.string.stop);
            builder.setTitle("Stop General Election");
            builder.setMessage("You are about to stop the General Election for Shangri-La Valley");
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

    public void createNotification(String title, String message) {
        FCMSender fcmSender = new FCMSender();
        fcmSender.send(String.format(NotificationMessage.message, Constants.NOTIFICATION_TOPIC_ELECTION, title, message), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }

    public void startElection() {
        baseRepository.clearVotes();
        createElectionData();
        createElectionResultData();
        baseRepository.publishResult(false);
        createNotification("Election has started", "Sound the alarm, it's election time! ⏰ Make your voice heard and cast your vote!");
    }

    public void stopElection() {
        // switch state to completed for now
        baseRepository.stopElection();
        computeWinner().observe(getViewLifecycleOwner(), new Observer<List<DistrictPartyWinner>>() {
            @Override
            public void onChanged(List<DistrictPartyWinner> districtPartyWinners) {
                if (districtPartyWinners != null) {
                    List<String> winningParties = new ArrayList<>();
                    for (DistrictPartyWinner w : districtPartyWinners) {
                        winningParties.add(w.getParty());
                    }
                    // check value with max occurrence
                    getWinningParty(winningParties);
                }
            }
        });
        createNotification("Election has ended", "The wait is almost over. Results will be available shortly. ⏱️");
    }

    public LiveData<List<DistrictPartyWinner>> computeWinner() {
        String[] allConstituency = getResources().getStringArray(R.array.constituency);
        final MutableLiveData<List<DistrictPartyWinner>> data = new MutableLiveData<>();
        List<DistrictPartyWinner> districtPartyWinners = new ArrayList<>();
        for (String s : allConstituency) {
            baseRepository.getConstituencyHighestPartyVote(s).observe(getViewLifecycleOwner(), new Observer<VoteCount>() {
                @Override
                public void onChanged(VoteCount voteCount) {
                    if (voteCount != null) {
                        if (voteCount.getVote() > 0) {
                            baseRepository.getCandidateById(voteCount.getName()).observe(getViewLifecycleOwner(), new Observer<Candidate>() {
                                @Override
                                public void onChanged(Candidate candidate) {
                                    districtPartyWinners.add(new DistrictPartyWinner(s, voteCount.getParty(), voteCount.getName(), candidate.getPhoto()));
                                    data.setValue(districtPartyWinners);
                                }
                            });

                        }
                    }
                }
            });
        }
        return data;
    }

    public void getWinningParty(List<String> winningParties) {
        Set<String> winningPartiesSet = new HashSet<>(winningParties);
        List<SeatCount> seatCountList = new ArrayList<>();
        for (String key : winningPartiesSet) {
            System.out.println(key + ": " + Collections.frequency(winningParties, key));
            seatCountList.add(new SeatCount(key, Collections.frequency(winningParties, key)));
        }
        updateResult(seatCountList);
    }

    public void updateResult(List<SeatCount> seatCountList) {
        List<SeatCount> seats = new ArrayList<>();
        seats.add(new SeatCount(Constants.PARTY_BLUE, 0));
        seats.add(new SeatCount(Constants.PARTY_RED, 0));
        seats.add(new SeatCount(Constants.PARTY_YELLOW, 0));
        seats.add(new SeatCount(Constants.PARTY_INDEPENDENT, 0));

        for (SeatCount s : seats) {
            for (SeatCount t : seatCountList) {
                if (s.getParty().equals(t.getParty())) {
                    s.setSeat(t.getSeat());
                }
            }
        }

        // push result to db
        baseRepository.updateSeatCount(seats);
        baseRepository.updateWinner(declareWinner(seats));
    }

    public String declareWinner(List<SeatCount> seats) {
        String winner = Constants.HUNG_PARLIAMENT;
        Log.e("Final size of seat", String.valueOf(seats.size()));
        if (seats.size() != 0) {
            for (SeatCount s : seats) {
                if (s.getSeat() >= 3) {
                    winner = s.getParty();
                }
            }
        }
        return winner;
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