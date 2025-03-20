package com.epam.campus.gymcrm.models.entities;

import java.time.LocalDate;

public class Trainee {

    private int id;
    private LocalDate dateOfBirth;
    private String address;
    private int userID;

    public Trainee() {}

    public Trainee(TraineeBuilder builder) {
        this.id = builder.id;
        this.dateOfBirth = builder.dateOfBirth;
        this.address = builder.address;
        this.userID = builder.userID;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public int getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "Trainee [id=" + id + ", dateOfBirth=" + dateOfBirth + ", address=" + address + ", userID=" + userID + "]";
    }

    public static class TraineeBuilder {
        private int id;
        private LocalDate dateOfBirth;
        private String address;
        private int userID;

        public TraineeBuilder id(int id) {
            this.id = id;
            return this;
        }

        public TraineeBuilder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public TraineeBuilder address(String address) {
            this.address = address;
            return this;
        }

        public TraineeBuilder userID(int userID) {
            this.userID = userID;
            return this;
        }

        public Trainee build() {
            return new Trainee(this);
        }
    }

}
