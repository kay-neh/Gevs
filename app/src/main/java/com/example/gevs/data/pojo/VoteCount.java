package com.example.gevs.data.pojo;

public class VoteCount {

    private String name, party;
    private Long vote;

    public VoteCount() {
    }

    public VoteCount(String name, String party, Long vote) {
        this.name = name;
        this.party = party;
        this.vote = vote;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public Long getVote() {
        return vote;
    }

    public void setVote(Long vote) {
        this.vote = vote;
    }
}
