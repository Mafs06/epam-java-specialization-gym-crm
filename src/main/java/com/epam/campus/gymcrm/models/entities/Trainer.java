package com.epam.campus.gymcrm.models.entities;

public class Trainer {

    private int id;
    private int specializationID;
    private int userID;

    public Trainer() {}

    public Trainer(TrainerBuilder builder) {
        this.id = builder.id;
        this.specializationID = builder.specializationID;
        this.userID = builder.userID;
    }

    public int getId() {
        return id;
    }

    public int getSpecializationID() {
        return specializationID;
    }

    public int getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "Trainer [id=" + id + " specializationID=" + specializationID + ", userID=" + userID + "]";
    }

    public static class TrainerBuilder {
        private int id;
        private int specializationID;
        private int userID;

        public TrainerBuilder id(int id) {
            this.id = id;
            return this;
        }

        public TrainerBuilder specialization(int specializationID) {
            this.specializationID = specializationID;
            return this;
        }

        public TrainerBuilder userID(int userID) {
            this.userID = userID;
            return this;
        }

        public Trainer build() {
            return new Trainer(this);
        }
    }
    
}
