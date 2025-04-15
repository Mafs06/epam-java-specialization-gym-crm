package com.epam.campus.gymcrm.repositories;

import java.util.List;
import java.util.Optional;

public interface UserBehaviour {

    List<String> getUsernames();
    
    Optional<Object> getByUsername(String username);

    void updatePassword(String username, String newPassword);

    boolean switchActiveStatus(String username);

    void updateActiveStatus(String username, boolean active);
}
