package com.watchbro.watchbro.userClasses;

import java.util.List;

/**
 * Created by couderc
 */

public class User {
    public List<Day> days;
    public String username;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, List<Day> days) {
        this.username = username;
        this.days = days;
    }

    // Cas où l'utilisateur vient d'être crée
    public User(String username) {
        this.username = username;
        this.days = null;
    }
}
