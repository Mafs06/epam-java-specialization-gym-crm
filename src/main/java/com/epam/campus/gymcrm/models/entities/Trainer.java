package com.epam.campus.gymcrm.models.entities;

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
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "specialization_id", nullable = false)
    private int specializationID;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;

    public Trainer() {}

    public Trainer(TrainerBuilder builder) {
        this.id = builder.id;
        this.specializationID = builder.specializationID;
        this.user = new User(builder.userID, builder.firstName, builder.lastName, builder.username, builder.password, builder.active);
    }

    public int getId() {
        return id;
    }

    public int getSpecializationID() {
        return specializationID;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Trainer [id=" + id + ", specializationID=" + specializationID + ", user=" + user.toString() + "]";
    }

    public static class TrainerBuilder {
        private int id;
        private int specializationID;

        // User data
        private int userID;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private boolean active;

        public TrainerBuilder userID(int userID) {
            this.userID = userID;
            return this;
        }

        public TrainerBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public TrainerBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public TrainerBuilder username(String username) {
            this.username = username;
            return this;
        }

        public TrainerBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public TrainerBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public TrainerBuilder id(int id) {
            this.id = id;
            return this;
        }

        public TrainerBuilder specialization(int specializationID) {
            this.specializationID = specializationID;
            return this;
        }

        public Trainer build() {
            return new Trainer(this);
        }
    }
    
}
