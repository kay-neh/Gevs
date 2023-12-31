package com.example.gevs.data.pojo;

public class Candidate {
    private String name, photo, party, constituency;

    public Candidate() {
    }

    public Candidate(String name, String photo, String party, String constituency) {
        this.name = name;
        this.photo = photo;
        this.party = party;
        this.constituency = constituency;
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
}
