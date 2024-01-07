package com.example.gevs.data;

import androidx.lifecycle.LiveData;

import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.DistrictVote;
import com.example.gevs.data.pojo.ElectionResult;
import com.example.gevs.data.pojo.Vote;
import com.example.gevs.data.pojo.Voter;

import java.util.HashMap;
import java.util.List;

public interface BaseDataSource {
    void saveVoter(String voterId, Voter voter);

    LiveData<Boolean> isExistingEmail(String email);

    LiveData<Boolean> isUvcValid(String uvc);

    LiveData<Boolean> isUvcUsed(String uvc);

    LiveData<Boolean> isAdmin(String email);

    LiveData<Voter> getVoter(String voterId);

    LiveData<List<Candidate>> getAllCandidates();

    LiveData<List<Voter>> getAllVoters();

    void createElectionData(HashMap<String, DistrictVote> districtVoteHashMap);

    LiveData<List<DistrictVote>> getDistrictVotes();

    LiveData<DistrictVote> getDistrictVoteById(String id);

    LiveData<List<Candidate>> getCandidatesByConstituency(String constituency);

    void createElectionResult(ElectionResult electionResult);

    LiveData<String> getElectionStatus();

    void stopElection();

    LiveData<Candidate> getCandidateById(String id);

    void incrementVoteCount(String constituency, String party);

    void saveVote(String userId, Vote vote);

    LiveData<Boolean> hasVoted(String userId);

    LiveData<Vote> getVote(String userId);

    LiveData<List<Vote>> getVotesByTime();

}
