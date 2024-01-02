package com.example.gevs.data;

import androidx.lifecycle.LiveData;

import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.Voter;

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
}
