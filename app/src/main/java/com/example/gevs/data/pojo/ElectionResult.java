package com.example.gevs.data.pojo;

import java.util.List;

public class ElectionResult {

    private String status, winner;
    private List<SeatCount> seats;

    public ElectionResult() {
    }

    public ElectionResult(String status, String winner, List<SeatCount> seats) {
        this.status = status;
        this.winner = winner;
        this.seats = seats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<SeatCount> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatCount> seats) {
        this.seats = seats;
    }
}
