package com.epam.campus.gymcrm.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.entities.Training;

@Repository
public class TrainingRepository implements BaseRepository<Training>{

    @Override
    public Optional<Training> get(int id) {
        return Optional.of(new Training());
    }

    @Override
    public List<Training> getAll() {
        return new ArrayList<>();
    }

    @Override
    public void save(Training training) {
        
    }

    @Override
    public void update(Training training) {
        save(training);
    }

    @Override
    public void delete(Training training) {

    }

}
