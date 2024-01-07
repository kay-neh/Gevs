package com.example.gevs.data.pojo;

public class CandidateVote {

    private String name, photo, party, constituency, partyVoted;
    private Boolean voted;

    public CandidateVote() {
    }

    public CandidateVote(String name, String photo, String party, String constituency, String partyVoted, Boolean voted) {
        this.name = name;
        this.photo = photo;
        this.party = party;
        this.constituency = constituency;
        this.partyVoted = partyVoted;
        this.voted = voted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }

    public String getPartyVoted() {
        return partyVoted;
    }

    public void setPartyVoted(String partyVoted) {
        this.partyVoted = partyVoted;
    }
}
