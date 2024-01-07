package com.example.gevs.data;

import androidx.lifecycle.LiveData;

import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.DistrictVote;
import com.example.gevs.data.pojo.ElectionResult;
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.data.remote.GevsRemoteDataSource;

import java.util.HashMap;
import java.util.List;

public class BaseRepository {

    private final GevsRemoteDataSource remoteDataSource;

    public BaseRepository() {
        remoteDataSource = new GevsRemoteDataSource();
    }

    public void saveVoter(String voterId, Voter voter) {
        remoteDataSource.saveVoter(voterId, voter);
    }

    public LiveData<Boolean> isExistingEmail(String email) {
        return remoteDataSource.isExistingEmail(email);
    }

    public LiveData<Boolean> isUvcValid(String uvc) {
        return remoteDataSource.isUvcValid(uvc);
    }

    public LiveData<Boolean> isUvcUsed(String uvc) {
        return remoteDataSource.isUvcUsed(uvc);
    }

    public LiveData<Boolean> isAdmin(String email) {
        return remoteDataSource.isAdmin(email);
    }

    public LiveData<Voter> getVoter(String voterId) {
        return remoteDataSource.getVoter(voterId);
    }

    public LiveData<List<Candidate>> getAllCandidates() {
        return remoteDataSource.getAllCandidates();
    }

    public LiveData<List<Voter>> getAllVoters() {
        return remoteDataSource.getAllVoters();
    }

    public void createElectionData(HashMap<String, DistrictVote> districtVoteHashMap) {
        remoteDataSource.createElectionData(districtVoteHashMap);
    }

    public LiveData<List<DistrictVote>> getDistrictVotes() {
        return remoteDataSource.getDistrictVotes();
    }

    public LiveData<DistrictVote> getDistrictVoteById(String id) {
        return remoteDataSource.getDistrictVoteById(id);
    }

    public LiveData<List<Candidate>> getCandidatesByConstituency(String constituency) {
        return remoteDataSource.getCandidatesByConstituency(constituency);
    }

    public void createElectionResult(ElectionResult electionResult) {
        remoteDataSource.createElectionResult(electionResult);
    }

    public LiveData<String> getElectionStatus() {
        return remoteDataSource.getElectionStatus();
    }

    public void stopElection() {
        remoteDataSource.stopElection();
    }

}
