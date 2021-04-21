package com.example.fitnesswithfriends;

public class User {
    public String firstName;
    public String lastName;
    public String gender;
    public String favWorkout;
    public String fitLevel;
    public String streetAddress;
    public  String city;
    public String state;
    public  String zipCode;
    public User() {

    }

    public User(String firstName, String lastName, String gender, String favWorkout, String fitLevel) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.favWorkout = favWorkout;
        this.fitLevel = fitLevel;

    }

    public String getFirstName() {
        return firstName;
    }

    public String getName() {
        return firstName+lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() { return gender; }

    public String getFavWorkout() { return favWorkout; }

    public String getFitLevel() { return fitLevel; }

    public String getStreetAddress() { return streetAddress; }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

}
