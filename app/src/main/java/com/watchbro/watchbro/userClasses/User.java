package com.watchbro.watchbro.userClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by couderc
 */

public class User {
    public String idUser;
    public ArrayList<Day> days;
    public String username;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String idUser, String username, ArrayList<Day> days) {
        this.idUser = idUser;
        this.username = username;
        this.days = days;
    }

    // Cas où l'utilisateur vient d'être crée
    public User(String idUser, String username) {
        this.idUser = idUser;
        this.username = username;
        this.days = null;
    }
}
