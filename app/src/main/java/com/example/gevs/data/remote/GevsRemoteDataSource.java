package com.example.gevs.data.remote;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gevs.data.BaseDataSource;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.DistrictVote;
import com.example.gevs.data.pojo.ElectionResult;
import com.example.gevs.data.pojo.Notification;
import com.example.gevs.data.pojo.SeatCount;
import com.example.gevs.data.pojo.Vote;
import com.example.gevs.data.pojo.VoteCount;
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
        firebaseDatabase.getReference(Constants.KEY_RESULTS + "/status").setValue(Constants.ELECTION_STATUS_COMPLETED);
    }

    @Override
    public LiveData<Candidate> getCandidateById(String id) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_CANDIDATES + "/" + id);
        final MutableLiveData<Candidate> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.setValue(snapshot.getValue(Candidate.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

    @Override
    public void incrementVoteCount(String constituency, String party) {
        firebaseDatabase.getReference(Constants.KEY_CONSTITUENCY + "/" + constituency + "/result/" + party + "/vote").setValue(ServerValue.increment(1));
    }

    @Override
    public void saveVote(String userId, Vote vote) {
        firebaseDatabase.getReference(Constants.KEY_VOTE + "/" + userId).setValue(vote);
    }

    @Override
    public LiveData<Boolean> hasVoted(String userId) {
        DatabaseReference dbRef = firebaseDatabase.getReference(Constants.KEY_VOTE);
        final MutableLiveData<Boolean> data = new MutableLiveData<>();
        dbRef.orderByKey().equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // voted
                    data.setValue(true);
                } else {
                    // not voted
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
    public LiveData<Vote> getVote(String userId) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_VOTE + "/" + userId);
        final MutableLiveData<Vote> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Vote vote = dataSnapshot.getValue(Vote.class);
                data.setValue(vote);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public LiveData<List<Vote>> getVotesByTime() {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_VOTE);
        final MutableLiveData<List<Vote>> data = new MutableLiveData<>();
        databaseReference.orderByChild("voteTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Vote> voteList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Vote vote = dataSnapshot1.getValue(Vote.class);
                    voteList.add(vote);
                }
                data.setValue(voteList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public LiveData<VoteCount> getConstituencyHighestPartyVote(String constituency) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_CONSTITUENCY + "/" + constituency + "/result");
        final MutableLiveData<VoteCount> data = new MutableLiveData<>();
        databaseReference.orderByChild("vote").limitToLast(2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<VoteCount> voteCountList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    VoteCount voteCount = dataSnapshot1.getValue(VoteCount.class);
                    voteCountList.add(voteCount);
                }
                // code
                if (voteCountList.size() == 1) {
                    data.setValue(voteCountList.get(0));
                }
                if (voteCountList.size() == 2) {
                    // check for equal or greater value then return appropriate value
                    if (!Objects.equals(voteCountList.get(0).getVote(), voteCountList.get(1).getVote())) {
                        if (voteCountList.get(0).getVote() > voteCountList.get(1).getVote()) {
                            data.setValue(voteCountList.get(0));
                        } else {
                            data.setValue(voteCountList.get(1));
                        }
                    } else {
                        data.setValue(new VoteCount("", "", 0L));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

    @Override
    public void updateSeatCount(List<SeatCount> seatCountList) {
        firebaseDatabase.getReference(Constants.KEY_RESULTS + "/seats").setValue(seatCountList);
    }

    @Override
    public void updateWinner(String winner) {
        firebaseDatabase.getReference(Constants.KEY_RESULTS + "/winner").setValue(winner);
    }

    @Override
    public void clearVotes() {
        firebaseDatabase.getReference(Constants.KEY_VOTE).setValue(null);
    }

    @Override
    public LiveData<ElectionResult> getElectionResult() {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_RESULTS);
        final MutableLiveData<ElectionResult> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ElectionResult electionResult = dataSnapshot.getValue(ElectionResult.class);
                data.setValue(electionResult);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public void publishResult(Boolean value) {
        firebaseDatabase.getReference(Constants.KEY_PUBLISH + "/Publish Result").setValue(value);
    }

    @Override
    public LiveData<Boolean> isResultPublished() {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_PUBLISH + "/Publish Result");
        final MutableLiveData<Boolean> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.getValue(Boolean.class);
                data.setValue(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public void saveNotification(String userId, Notification notification) {
        firebaseDatabase.getReference(Constants.KEY_NOTIFICATIONS + "/" + userId + "/" + notification.getNotificationTime()).setValue(notification);
    }

    @Override
    public LiveData<List<Notification>> getNotificationsById(String userId) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_NOTIFICATIONS + "/" + userId);
        final MutableLiveData<List<Notification>> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Notification> notificationList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Notification notification = dataSnapshot1.getValue(Notification.class);
                    notificationList.add(notification);
                }
                data.setValue(notificationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public void clearAllNotifications(String userId) {
        firebaseDatabase.getReference(Constants.KEY_NOTIFICATIONS + "/" + userId).setValue(null);
    }

    @Override
    public LiveData<Integer> getRegisteredVotersCount() {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_UVC);
        final MutableLiveData<Integer> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Boolean> booleanList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Boolean aBoolean = dataSnapshot1.getValue(Boolean.class);
                    booleanList.add(aBoolean);
                }
                data.setValue(booleanList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

}
