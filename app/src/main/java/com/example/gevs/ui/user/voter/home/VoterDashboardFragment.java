package com.example.gevs.ui.user.voter.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.databinding.FragmentVoterDashboardBinding;
import com.example.gevs.ui.user.adapters.DistrictCandidateAdapter;
import com.example.gevs.util.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class VoterDashboardFragment extends Fragment {

    FragmentVoterDashboardBinding binding;
    DistrictCandidateAdapter districtCandidateAdapter;
    BaseRepository baseRepository;
    FirebaseAuth firebaseAuth;

    public VoterDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_dashboard, container, false);

        baseRepository = new BaseRepository();
        firebaseAuth = FirebaseAuth.getInstance();
        initAdapter();

        if (firebaseAuth.getCurrentUser() != null) {
            baseRepository.getVoter(firebaseAuth.getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<Voter>() {
                @Override
                public void onChanged(Voter voter) {
                    if (voter != null) {
                        binding.voterDashboardNameText.setText(voter.getFullName() + "!");
                        binding.voterDashboardConstituencyTextview.setText(voter.getConstituency() + " Constituency");
                        baseRepository.getCandidatesByConstituency(voter.getConstituency()).observe(getViewLifecycleOwner(), new Observer<List<Candidate>>() {
                            @Override
                            public void onChanged(List<Candidate> candidateList) {
                                if (candidateList != null) {
                                    districtCandidateAdapter.setList(candidateList);
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
                        binding.voterDashboardElectionStateTextview.setText("Ongoing");
                        binding.voterDashboardElectionTimer.setVisibility(View.VISIBLE);
                    }
                    if (s.equals(Constants.ELECTION_STATUS_COMPLETED)) {
                        binding.voterDashboardElectionStateTextview.setText("Ended");
                        binding.voterDashboardElectionTimer.setVisibility(View.GONE);
                    }
                } else {
                    binding.voterDashboardElectionStateTextview.setText("Starts soon");
                    binding.voterDashboardElectionTimer.setVisibility(View.GONE);
                }
            }
        });


        return binding.getRoot();
    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.voterDashboardCandidatesRecyclerview.setLayoutManager(llm);
        binding.voterDashboardCandidatesRecyclerview.setHasFixedSize(true);
        districtCandidateAdapter = new DistrictCandidateAdapter(new DistrictCandidateAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(String key) {
                showCandidateDetailsDialog(key);
            }
        });
        binding.voterDashboardCandidatesRecyclerview.setAdapter(districtCandidateAdapter);
    }

    public void showCandidateDetailsDialog(String candidateId) {
        // Create a MaterialAlertDialog and set the title and custom view for collecting input and also click listeners
        // for the positive and negative buttons on the dialog.
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_fragment_voter_candidate_details, null);

        MaterialButton dismissButton = dialogView.findViewById(R.id.dialog_candidate_back_button);
        ImageView candidateImage = dialogView.findViewById(R.id.dialog_candidate_imageView);
        MaterialTextView candidateName = dialogView.findViewById(R.id.dialog_candidate_name);
        MaterialTextView candidateParty = dialogView.findViewById(R.id.party_material_textView);
        MaterialTextView candidateConstituency = dialogView.findViewById(R.id.constituency_material_textView);
        MaterialTextView candidateAge = dialogView.findViewById(R.id.age_material_textView);
        MaterialTextView candidateDateOfBirth = dialogView.findViewById(R.id.dob_material_textView);
        MaterialTextView candidateManifesto = dialogView.findViewById(R.id.manifesto_material_textView);

        baseRepository.getCandidateById(candidateId).observe(getViewLifecycleOwner(), new Observer<Candidate>() {
            @Override
            public void onChanged(Candidate candidate) {
                if (candidate != null) {
                    candidateName.setText(candidate.getName());
                    candidateParty.setText(candidate.getParty());
                    candidateConstituency.setText(candidate.getConstituency());
                    candidateAge.setText(candidate.getAge());
                    candidateDateOfBirth.setText(candidate.getDateOfBirth());
                    candidateManifesto.setText(candidate.getManifesto());
                    Glide.with(getActivity().getApplicationContext()).load(candidate.getPhoto()).into(candidateImage);
                }
            }
        });

        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


}
