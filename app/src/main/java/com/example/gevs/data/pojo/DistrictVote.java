package com.example.gevs.data.pojo;

import java.util.HashMap;
import java.util.List;

public class DistrictVote {

    private String constituency;
    private HashMap<String, VoteCount> result;

    public DistrictVote() {
    }

    public DistrictVote(String constituency, HashMap<String, VoteCount> result) {
        this.constituency = constituency;
        this.result = result;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public HashMap<String, VoteCount> getResult() {
        return result;
    }

    public void setResult(HashMap<String, VoteCount> result) {
        this.result = result;
    }
}
