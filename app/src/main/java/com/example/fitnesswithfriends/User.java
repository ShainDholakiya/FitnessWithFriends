package com.example.fitnesswithfriends;

public class User {
    public String first_name;
    public String last_name;
    public String gender;
    public String fav_workout;
    public String fit_level;

    public User() {

    }

    public User(String first_name, String last_name, String gender, String fav_workout, String fit_level) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.fav_workout = fav_workout;
        this.fit_level = fit_level;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getGender() { return gender; }

    public String getFavWorkout() { return fav_workout; }

    public String getFitLevel() { return fit_level; }
}
