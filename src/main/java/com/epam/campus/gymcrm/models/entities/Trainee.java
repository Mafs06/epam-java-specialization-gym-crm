package com.epam.campus.gymcrm.models.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Trainee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String address;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;

    public Trainee() {}

    public Trainee(TraineeBuilder builder) {
        this.id = builder.id;
        this.dateOfBirth = builder.dateOfBirth;
        this.address = builder.address;
        this.user = new User(builder.userID, builder.firstName, builder.lastName, builder.username, builder.password, builder.active);
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

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Trainee [id=" + id + ", dateOfBirth=" + dateOfBirth + ", address=" + address + ", user=" + user.toString() + "]";
    }

    public static class TraineeBuilder {
        private int id;
        private LocalDate dateOfBirth;
        private String address;

        // User data
        private int userID;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private boolean active;

        public TraineeBuilder userID(int userID) {
            this.userID = userID;
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

        public Trainee build() {
            return new Trainee(this);
        }
    }

}
