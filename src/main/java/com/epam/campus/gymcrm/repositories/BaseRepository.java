package com.epam.campus.gymcrm.repositories;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {

    Optional<T> get(int id);
    
    List<T> getAll();
    
    void save(T t);
    
    void update(T t);
    
    void delete(T t);
}
