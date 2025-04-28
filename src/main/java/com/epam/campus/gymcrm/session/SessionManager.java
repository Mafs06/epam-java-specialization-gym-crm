package com.epam.campus.gymcrm.session;

import com.epam.campus.gymcrm.models.entities.User;

public class SessionManager {
    private static User loggedUser;

    public static void login(User user) {
        loggedUser = user;
    }

    public static void logout() {
        loggedUser = null;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static boolean isAuthenticated() {
        return loggedUser != null && loggedUser.isAuthenticated();
    }
}
