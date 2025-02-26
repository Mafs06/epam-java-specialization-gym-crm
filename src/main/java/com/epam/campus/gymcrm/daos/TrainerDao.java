package com.epam.campus.gymcrm.daos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epam.campus.gymcrm.models.Trainer;

public class TrainerDao implements Dao<Trainer>{

    private List<Trainer> trainers = new ArrayList<>();

    public TrainerDao() {
        trainers.add(new Trainer(1, "Miles", "Morales", "milesssmo", "great_responsibilitiy", true, "Mobility"));
        trainers.add(new Trainer(2, "Terry", "Crews ", "terryc", "oldspice", true, "Futbol"));
    }

    @Override
    public Optional<Trainer> get(int id) {
        return Optional.ofNullable(trainers.get(id));
    }

    @Override
    public List<Trainer> getAll() {
        return trainers;
    }

    @Override
    public void save(Trainer trainer) {
        trainers.add(trainer);
    }

    @Override
    public void update(Trainer trainer, String[] params) {
        trainer.setTrainerID(Integer.parseInt(params[0]));
        trainer.setFirstName(params[1]);
        trainer.setLastName(params[2]);
        trainer.setUsername(params[3]);
        trainer.setPassword(params[4]);
        trainer.setActive(Boolean.parseBoolean(params[5]));
        trainer.setSpecialization(params[6]);

        trainers.add(trainer);
    }

    @Override
    public void delete(Trainer trainer) {
        trainers.remove(trainer);
    }

}
