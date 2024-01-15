package com.example.gevs.ui.user.voter.result.analytics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.DistrictPartyWinner;
import com.example.gevs.data.pojo.ElectionResult;
import com.example.gevs.data.pojo.SeatCount;
import com.example.gevs.data.pojo.Vote;
import com.example.gevs.data.pojo.VoteCount;
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.databinding.ActivityVoterAnalyticsBinding;
import com.example.gevs.ui.user.adapters.DistrictWinnerAdapter;
import com.example.gevs.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class VoterAnalyticsActivity extends AppCompatActivity {

    ActivityVoterAnalyticsBinding binding;
    BaseRepository baseRepository;
    DistrictWinnerAdapter districtWinnerAdapter;
    Cartesian cartesian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voter_analytics);

        baseRepository = new BaseRepository();
        initAdapter();

        baseRepository.getElectionResult().observe(this, new Observer<ElectionResult>() {
            @Override
            public void onChanged(ElectionResult electionResult) {
                if (electionResult != null) {
                    initBarChart(electionResult.getSeats());
                    binding.anyChartView.setChart(cartesian);
                }
            }
        });

        binding.adminAnalyticsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getDistrictWinners().observe(this, new Observer<List<DistrictPartyWinner>>() {
            @Override
            public void onChanged(List<DistrictPartyWinner> districtPartyWinners) {
                if (districtPartyWinners != null) {
                    districtWinnerAdapter.setList(districtPartyWinners);
                }
            }
        });

        // registered voters
        baseRepository.getRegisteredVotersCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer registeredVotersCount) {
                if (registeredVotersCount != null) {
                    binding.valueRegisteredVotersTextView.setText(String.valueOf(registeredVotersCount));
                }
            }
        });


        // verified voters
        baseRepository.getAllVoters().observe(this, new Observer<List<Voter>>() {
            @Override
            public void onChanged(List<Voter> voters) {
                if (voters != null) {
                    int verifiedVoters = voters.size();
                    binding.valueVerifiedVotersTextView.setText(String.valueOf(verifiedVoters));
                }
            }
        });

        // active voters
        baseRepository.getVotesByTime().observe(this, new Observer<List<Vote>>() {
            @Override
            public void onChanged(List<Vote> votes) {
                if (votes != null) {
                    int activeVoters = votes.size();
                    binding.valueActiveVotersTextView.setText(String.valueOf(activeVoters));
                }
            }
        });

    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.analyticsRecyclerview.setLayoutManager(llm);
        binding.analyticsRecyclerview.setHasFixedSize(true);
        districtWinnerAdapter = new DistrictWinnerAdapter();
        binding.analyticsRecyclerview.setAdapter(districtWinnerAdapter);
    }

    public List<DataEntry> getBarChartList(List<SeatCount> seatCountList) {
        List<DataEntry> data = new ArrayList<>();
        for (SeatCount s : seatCountList) {
            if (s.getParty().equals(Constants.PARTY_INDEPENDENT)) {
                data.add(new ValueDataEntry("Independent", s.getSeat()));
            } else {
                data.add(new ValueDataEntry(s.getParty(), s.getSeat()));
            }
        }
        return data;
    }

    public void initBarChart(List<SeatCount> seatCountList) {
        cartesian = AnyChart.column();
        Column column = cartesian.column(getBarChartList(seatCountList));
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Valley of Shangri-La Election Result");
        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Political Party");
        cartesian.yAxis(0).title("MP Seats Won");
    }

    public LiveData<List<DistrictPartyWinner>> getDistrictWinners() {
        String[] allConstituency = getResources().getStringArray(R.array.constituency);
        final MutableLiveData<List<DistrictPartyWinner>> data = new MutableLiveData<>();
        List<DistrictPartyWinner> districtPartyWinners = new ArrayList<>();
        for (String s : allConstituency) {
            baseRepository.getConstituencyHighestPartyVote(s).observe(this, new Observer<VoteCount>() {
                @Override
                public void onChanged(VoteCount voteCount) {
                    if (voteCount != null) {
                        if (voteCount.getVote() > 0) {
                            baseRepository.getCandidateById(voteCount.getName()).observe(VoterAnalyticsActivity.this, new Observer<Candidate>() {
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


}