package com.epam.campus.gymcrm.models.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "training_type")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "training_type_name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "trainingType", cascade = CascadeType.ALL)
    private List<Training> trainings;

    @OneToMany(mappedBy = "trainingType", cascade = CascadeType.ALL)
    private List<Trainer> trainers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TrainingType [id=" + id + ", name=" + name + "]";
    }

}
