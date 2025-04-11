package com.epam.campus.gymcrm.models.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;

    @ManyToMany
    @JoinTable(
        name = "trainee_trainer",
        joinColumns = @JoinColumn(name = "trainee_id"),
        inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private List<Trainer> trainers = new ArrayList<>();

    public Trainee() {}

    public Trainee(TraineeBuilder builder) {
        this.dateOfBirth = builder.dateOfBirth;
        this.address = builder.address;
        this.user = builder.user;
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

    public List<Trainer> getTrainers() {
        return trainers;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
    }

    @Override
    public String toString() {
        return "Trainee [id=" + id + ", user=" + user.toString() + "dateOfBirth=" + dateOfBirth + ", address=" + address + "]";
    }

    public static class TraineeBuilder {
        private LocalDate dateOfBirth;
        private String address;
        private User user;

        public TraineeBuilder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public TraineeBuilder address(String address) {
            this.address = address;
            return this;
        }

        public TraineeBuilder user(User user) {
            this.user = user;
            return this;
        }

        public Trainee build() {
            return new Trainee(this);
        }
    }

}
