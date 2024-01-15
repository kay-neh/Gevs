package com.example.gevs.data.pojo;

public class Candidate {
    private String name, photo, party, constituency, age, dateOfBirth, manifesto;

    public Candidate() {
    }

    public Candidate(String name, String photo, String party, String constituency) {
        this.name = name;
        this.photo = photo;
        this.party = party;
        this.constituency = constituency;
    }

    public Candidate(String name, String photo, String party, String constituency, String age, String dateOfBirth, String manifesto) {
        this.name = name;
        this.photo = photo;
        this.party = party;
        this.constituency = constituency;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.manifesto = manifesto;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getManifesto() {
        return manifesto;
    }

    public void setManifesto(String manifesto) {
        this.manifesto = manifesto;
    }
}
