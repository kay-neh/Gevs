package com.example.gevs.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gevs.data.BaseDataSource;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.DistrictVote;
import com.example.gevs.data.pojo.ElectionResult;
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GevsRemoteDataSource implements BaseDataSource {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    public GevsRemoteDataSource() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    @Override
    public void saveVoter(String voterId, Voter voter) {
        final DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_VOTERS + "/" + voterId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    databaseReference.setValue(voter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public LiveData<Boolean> isExistingEmail(String email) {
        DatabaseReference usersRef = firebaseDatabase.getReference(Constants.KEY_VOTERS);
        final MutableLiveData<Boolean> data = new MutableLiveData<>();
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    data.setValue(true);
                } else {
                    data.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

    @Override
    public LiveData<Boolean> isUvcValid(String uvc) {
        DatabaseReference universityRef = firebaseDatabase.getReference(Constants.KEY_UVC);
        final MutableLiveData<Boolean> data = new MutableLiveData<>();
        universityRef.orderByKey().equalTo(uvc).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // valid uvc
                    data.setValue(true);
                } else {
                    // invalid uvc
                    data.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

    @Override
    public LiveData<Boolean> isUvcUsed(String uvc) {
        DatabaseReference usersRef = firebaseDatabase.getReference(Constants.KEY_VOTERS);
        final MutableLiveData<Boolean> data = new MutableLiveData<>();
        usersRef.orderByChild("uvc").equalTo(uvc).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // uvc has been used
                    data.setValue(true);
                } else {
                    // uvc has not been used
                    data.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

    @Override
    public LiveData<Boolean> isAdmin(String email) {
        DatabaseReference usersRef = firebaseDatabase.getReference(Constants.KEY_VOTERS);
        final MutableLiveData<Boolean> data = new MutableLiveData<>();
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Voter> voter = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Voter currentVoter = dataSnapshot1.getValue(Voter.class);
                        voter.add(currentVoter);
                    }
                    String role = voter.get(0).getRole();
                    if (role.equals(Constants.ROLE_ADMIN)) {
                        data.setValue(true);
                    } else {
                        data.setValue(false);
                    }

                } else {
                    // user with given email not found
                    data.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return data;
    }

    @Override
    public LiveData<Voter> getVoter(String voterId) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_VOTERS + "/" + voterId);
        final MutableLiveData<Voter> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Voter voter = dataSnapshot.getValue(Voter.class);
                data.setValue(voter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public LiveData<List<Candidate>> getAllCandidates() {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_CANDIDATES);
        final MutableLiveData<List<Candidate>> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Candidate> candidateList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Candidate candidate = dataSnapshot1.getValue(Candidate.class);
                    candidateList.add(candidate);
                }
                data.setValue(candidateList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public LiveData<List<Voter>> getAllVoters() {
        DatabaseReference usersRef = firebaseDatabase.getReference(Constants.KEY_VOTERS);
        final MutableLiveData<List<Voter>> data = new MutableLiveData<>();
        usersRef.orderByChild("role").equalTo(Constants.ROLE_VOTER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Voter> voter = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Voter currentVoter = dataSnapshot1.getValue(Voter.class);
                    voter.add(currentVoter);
                }
                data.setValue(voter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

    @Override
    public void createElectionData(HashMap<String, DistrictVote> districtVoteHashMap) {
        firebaseDatabase.getReference(Constants.KEY_CONSTITUENCY).setValue(districtVoteHashMap);
    }

    @Override
    public LiveData<List<DistrictVote>> getDistrictVotes() {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_CONSTITUENCY);
        final MutableLiveData<List<DistrictVote>> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DistrictVote> districtVoteList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    DistrictVote districtVote = dataSnapshot1.getValue(DistrictVote.class);
                    districtVoteList.add(districtVote);
                }
                data.setValue(districtVoteList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public LiveData<DistrictVote> getDistrictVoteById(String id) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_CONSTITUENCY + "/" + id);
        final MutableLiveData<DistrictVote> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DistrictVote districtVote = dataSnapshot.getValue(DistrictVote.class);
                data.setValue(districtVote);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public LiveData<List<Candidate>> getCandidatesByConstituency(String constituency) {
        DatabaseReference usersRef = firebaseDatabase.getReference(Constants.KEY_CANDIDATES);
        final MutableLiveData<List<Candidate>> data = new MutableLiveData<>();
        usersRef.orderByChild("constituency").equalTo(constituency).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Candidate> candidateList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Candidate currentCandidate = dataSnapshot1.getValue(Candidate.class);
                    candidateList.add(currentCandidate);
                }
                data.setValue(candidateList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

    @Override
    public void createElectionResult(ElectionResult electionResult) {
        firebaseDatabase.getReference(Constants.KEY_RESULTS).setValue(electionResult);
    }

    @Override
    public LiveData<String> getElectionStatus() {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_RESULTS + "/" + "status");
        final MutableLiveData<String> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.setValue(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

    @Override
    public void stopElection() {
        firebaseDatabase.getReference(Constants.KEY_RESULTS + "/" + "status").setValue(Constants.ELECTION_STATUS_COMPLETED);
    }

}
