package com.example.gevs.data.pojo;

public class DistrictPartyWinner {

    private String constituency, party, name;

    public DistrictPartyWinner() {
    }

    public DistrictPartyWinner(String constituency, String party, String name) {
        this.constituency = constituency;
        this.party = party;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
