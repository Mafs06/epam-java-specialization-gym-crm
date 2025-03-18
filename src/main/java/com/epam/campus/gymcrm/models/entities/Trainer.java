package com.epam.campus.gymcrm.models.entities;

public class Trainer extends User {

    private int id;
    private String specialization;

    public Trainer() {
        super();
    }

    public Trainer(TrainerBuilder builder) {
        super(builder.firstName, builder.lastName, builder.username, builder.password, builder.active);
        this.specialization = builder.specialization;
        this.id = builder.id;
    }

    public int getId() {
        return id;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String toString() {
        return "Trainer [id=" + id + ", user=" + super.toString() + " specialization=" + specialization + "]";
    }

    public static class TrainerBuilder {
        private int id;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private boolean active;
        private String specialization;

        public TrainerBuilder id(int id) {
            this.id = id;
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

        public TrainerBuilder specialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        public Trainer build() {
            return new Trainer(this);
        }
    }
    
}
