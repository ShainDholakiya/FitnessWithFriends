package com.example.fitnesswithfriends;

public class User {
    public String first_name;
    public String last_name;

    public User() {

    }

    public User(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }
}
