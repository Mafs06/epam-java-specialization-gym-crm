package com.epam.campus.gymcrm.daos;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.Trainee;

@Repository
public class TraineeDao implements Dao<Trainee>{

    private List<Trainee> trainees = new ArrayList<>();

    public TraineeDao() {
        trainees.add(new Trainee(1, "Pedro", "Picapiedra", "pepicapiedra", "yabadabadu", true, LocalDate.of(1000, Month.JANUARY, 1), "Flintstone House"));
        trainees.add(new Trainee(2, "Beatriz", "Pinzon", "bettypinzon", "bettybetty", true, LocalDate.of(1999, Month.OCTOBER, 25), "Carrera 18A #43A – 59"));
    }

    @Override
    public Optional<Trainee> get(int id) {
        return trainees.stream()
            .filter(trainee -> trainee.getTraineeID() == id)
            .findFirst();
    }

    @Override
    public List<Trainee> getAll() {
        return trainees;
    }

    @Override
    public void save(Trainee trainee) {
        trainees.add(trainee);
    }

    @Override
    public void update(Trainee trainee, String[] params) {
        trainees.removeIf(t -> t.getTraineeID() == trainee.getTraineeID());

        trainee.setTraineeID(Integer.parseInt(params[0]));
        trainee.setFirstName(params[1]);
        trainee.setLastName(params[2]);
        trainee.setUsername(params[3]);
        trainee.setPassword(params[4]);
        trainee.setActive(Boolean.parseBoolean(params[5]));
        trainee.setDateOfBirth(LocalDate.parse(params[6]));  
        trainee.setAddress(params[7]);  

        trainees.add(trainee);
    }

    @Override
    public void delete(Trainee trainee) {
        trainees.remove(trainee);
    }

}
