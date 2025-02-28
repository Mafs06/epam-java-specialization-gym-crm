package com.epam.campus.gymcrm.models;

import java.time.LocalDate;

public class Trainee extends User {

    private LocalDate dateOfBirth;
    private String address;
    private int traineeID;

    

    public Trainee() {
        super();
    }

    public Trainee(int traineeID, String firstName, String lastName, String username, String password, boolean active, LocalDate dateOfBirth, String address) {
        super(firstName, lastName, username, password, active);
        this.traineeID = traineeID;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTraineeID() {
        return traineeID;
    }

    public void setTraineeID(int traineeID) {
        this.traineeID = traineeID;
    }

    @Override
    public String toString() {
        return "Trainee [traineeID=" + traineeID + ", user=" + super.toString() + ", dateOfBirth=" + dateOfBirth + ", address=" + address + "]";
    }

}
