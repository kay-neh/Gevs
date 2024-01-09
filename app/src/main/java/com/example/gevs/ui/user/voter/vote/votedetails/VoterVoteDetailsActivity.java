package com.example.gevs.ui.user.voter.vote.votedetails;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.Vote;
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.databinding.ActivityVoterVoteDetailsBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class VoterVoteDetailsActivity extends AppCompatActivity {

    ActivityVoterVoteDetailsBinding binding;
    BaseRepository baseRepository;
    FirebaseAuth firebaseAuth;
    String candidateId;
    String party;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voter_vote_details);

        candidateId = getIntent().getStringExtra("CandidateId");
        baseRepository = new BaseRepository();
        firebaseAuth = FirebaseAuth.getInstance();

        baseRepository.getCandidateById(candidateId).observe(this, new Observer<Candidate>() {
            @Override
            public void onChanged(Candidate candidate) {
                if (candidate != null) {
                    party = candidate.getParty();
                    binding.dialogCandidateName.setText(candidate.getName());
                    binding.partyMaterialTextView.setText(candidate.getParty());
                    binding.constituencyMaterialTextView.setText(candidate.getConstituency());
                    Glide.with(getApplicationContext()).load(candidate.getPhoto()).into(binding.dialogCandidateImageView);
                }
            }
        });

        binding.voteCandidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cast vote
                castVote();
                showVoteSuccessDialog();
            }
        });

        binding.voterVoteDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void castVote() {
        saveVote();
        incrementVoteCount();
    }

    public void saveVote() {
        if (firebaseAuth.getCurrentUser() != null) {
            baseRepository.getVoter(firebaseAuth.getCurrentUser().getUid()).observe(this, new Observer<Voter>() {
                @Override
                public void onChanged(Voter voter) {
                    if (voter != null) {
                        String name = voter.getFullName();
                        String constituency = voter.getConstituency();
                        Long voteTime = System.currentTimeMillis();
                        Vote vote = new Vote(name, constituency, party, voteTime);
                        baseRepository.saveVote(firebaseAuth.getCurrentUser().getUid(), vote);
                    }
                }
            });
        }
    }

    public void incrementVoteCount() {
        if (firebaseAuth.getCurrentUser() != null) {
            baseRepository.getVoter(firebaseAuth.getCurrentUser().getUid()).observe(this, new Observer<Voter>() {
                @Override
                public void onChanged(Voter voter) {
                    if (voter != null) {
                        baseRepository.incrementVoteCount(voter.getConstituency(), party);
                    }
                }
            });
        }
    }

    public void showVoteSuccessDialog() {
        // Create a MaterialAlertDialog and set the title and custom view for collecting input and also click listeners
        // for the positive and negative buttons on the dialog.
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_vote_successful, null);

        MaterialButton dismissButton = dialogView.findViewById(R.id.dialog_vote_successful_continue_button);

        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        alertDialog.show();
    }


}