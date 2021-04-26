package com.example.fitnesswithfriends;

public class Workout {
    private String createdBy;
    public String fitLevel;
    public String workoutDescription;
    public String workoutDuration;
    public String workoutLocation;
    public String workoutName;
    public String workoutType;

    public Workout() {

    }

    public Workout(String fitLevel, String workoutDescription, String workoutDuration, String workoutLocation, String workoutName, String workoutType, String createdBy) {
        this.fitLevel = fitLevel;
        this.workoutDescription = workoutDescription;
        this.workoutDuration = workoutDuration;
        this.workoutLocation = workoutLocation;
        this.workoutName = workoutName;
        this.workoutType = workoutType;
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getWorkoutName() { return workoutName; }

}



