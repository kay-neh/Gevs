package com.example.gevs.data.pojo;

public class Voter {

    private String email, fullName, constituency, uvc, role;
    private Long dateOfBirth;

    public Voter() {
    }

    public Voter(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public Voter(String email, String fullName, String constituency, String uvc, String role, Long dateOfBirth) {
        this.email = email;
        this.fullName = fullName;
        this.constituency = constituency;
        this.uvc = uvc;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public String getUvc() {
        return uvc;
    }

    public void setUvc(String uvc) {
        this.uvc = uvc;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
