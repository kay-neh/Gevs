package com.example.gevs.ui.user.admin.result;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.gevs.data.pojo.ElectionResult;
import com.example.gevs.databinding.FragmentAdminResultBinding;
import com.example.gevs.ui.user.adapters.ConstituencyAdapter;
import com.example.gevs.ui.user.admin.result.analytics.AdminAnalyticsActivity;
import com.example.gevs.ui.user.admin.result.resultdetails.AdminResultDetailsActivity;
import com.example.gevs.util.Constants;
import com.example.gevs.util.CardDecoration;
import com.example.gevs.util.FCMSender;
import com.example.gevs.util.NotificationMessage;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

        binding.analyticsTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminAnalyticsActivity.class));
            }
        });

        binding.analyticsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminAnalyticsActivity.class));
            }
        });

        binding.publishElectionResultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPublishDialog();
            }
        });


        baseRepository.getDistrictVotes().observe(getViewLifecycleOwner(), new Observer<List<DistrictVote>>() {
            @Override
            public void onChanged(List<DistrictVote> districtVotes) {
                if (districtVotes != null) {
                    constituencyAdapter.setList(districtVotes);
                }
            }
        });

        baseRepository.getElectionStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (s.equals(Constants.ELECTION_STATUS_PENDING)) {
                        binding.electionResultCard.setVisibility(View.GONE);
                        binding.publishElectionResultBtn.setVisibility(View.GONE);
                    }
                    if (s.equals(Constants.ELECTION_STATUS_COMPLETED)) {
                        binding.electionResultCard.setVisibility(View.VISIBLE);
                        binding.publishElectionResultBtn.setVisibility(View.VISIBLE);
                        baseRepository.isResultPublished().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean != null) {
                                    if (aBoolean) {
                                        binding.publishElectionResultBtn.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });
                    }
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

    public void initAdapter() {
        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        int spacing = 16;
        binding.adminDistrictResultRecyclerview.setPadding(spacing, spacing, spacing, spacing);
        binding.adminDistrictResultRecyclerview.setClipToPadding(false);
        binding.adminDistrictResultRecyclerview.setClipChildren(false);
        binding.adminDistrictResultRecyclerview.addItemDecoration(new CardDecoration(spacing));
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

    public void showPublishDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Publish Result?");
        builder.setMessage("You are about to publish the election results!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                baseRepository.publishResult(true);
                createNotification("Election result finalized", "Voices heard, votes counted. The verdict is in!");
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
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

}