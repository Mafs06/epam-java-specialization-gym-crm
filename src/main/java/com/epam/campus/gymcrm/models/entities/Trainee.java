package com.epam.campus.gymcrm.models.entities;

import java.time.LocalDate;

public class Trainee extends User {

    private int id;
    private LocalDate dateOfBirth;
    private String address;

    public Trainee() {
        super();
    }

    public Trainee(TraineeBuilder builder) {
        super(builder.firstName, builder.lastName, builder.username, builder.password, builder.active);
        this.id = builder.id;
        this.dateOfBirth = builder.dateOfBirth;
        this.address = builder.address;
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

    @Override
    public String toString() {
        return "Trainee [id=" + id + ", user=" + super.toString() + ", dateOfBirth=" + dateOfBirth + ", address=" + address + "]";
    }

    public static class TraineeBuilder {
        private int id;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private boolean active;
        private LocalDate dateOfBirth;
        private String address;

        public TraineeBuilder id(int id) {
            this.id = id;
            return this;
        }

        public TraineeBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public TraineeBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public TraineeBuilder username(String username) {
            this.username = username;
            return this;
        }

        public TraineeBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public TraineeBuilder active(boolean active) {
            this.active = active;
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

        public Trainee build() {
            return new Trainee(this);
        }
    }

}
