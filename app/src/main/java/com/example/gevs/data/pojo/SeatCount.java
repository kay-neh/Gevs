package com.example.gevs.data.pojo;

public class SeatCount {

    private String party;
    private int seat;

    public SeatCount() {
    }

    public SeatCount(String party, int seat) {
        this.party = party;
        this.seat = seat;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

}
