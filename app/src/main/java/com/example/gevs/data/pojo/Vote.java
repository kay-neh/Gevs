package com.example.gevs.data.pojo;

public class Vote {

    public String name, constituency, party;
    public Long voteTime;

    public Vote() {
    }

    public Vote(String name, String constituency, String party, Long voteTime) {
        this.name = name;
        this.constituency = constituency;
        this.party = party;
        this.voteTime = voteTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public Long getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(Long voteTime) {
        this.voteTime = voteTime;
    }
}
