package com.example.gevs.data.pojo;

public class DistrictVoteCount {

    private String name, party, photo;
    private Long vote;

    public DistrictVoteCount() {
    }

    public DistrictVoteCount(String name, String party, String photo, Long vote) {
        this.name = name;
        this.party = party;
        this.photo = photo;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getVote() {
        return vote;
    }

    public void setVote(Long vote) {
        this.vote = vote;
    }
}
