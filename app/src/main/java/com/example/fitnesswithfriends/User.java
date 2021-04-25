package com.example.fitnesswithfriends;

public class User {
    private String userID;
    public String firstName;
    public String lastName;
    public String gender;
    public String favWorkout;
    public String fitLevel;

    public User() {

    }

    public User(String firstName, String lastName, String gender, String favWorkout, String fitLevel, String userID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.favWorkout = favWorkout;
        this.fitLevel = fitLevel;
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() { return gender; }

    public String getFavWorkout() { return favWorkout; }

    public String getFitLevel() { return fitLevel; }

    public String getUserID() { return userID; }
}
