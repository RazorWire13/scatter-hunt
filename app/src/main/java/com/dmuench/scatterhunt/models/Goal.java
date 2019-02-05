package com.dmuench.scatterhunt.models;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseUser;

public class Goal {
    private String title;
    private double latitude;
    private double longitude;
    private int geoDistance;
    private String clueOne;
    private String clueTwo;
    private String clueThree;
    private FirebaseUser createdBy;

    public Goal () {}

    public Goal (String title, double latitude, double longitude, int geoDistance, String[] clues, FirebaseUser user) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoDistance = geoDistance;
        this.clueOne = clues[0];
        this.clueTwo = clues[1];
        this.clueThree = clues[2];
        this.createdBy = user;

    }

    // Getters

    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getGeoDistance() {
        return geoDistance;
    }

    public String getClueOne() {
        return clueOne;
    }

    public String getClueTwo() {
        return clueTwo;
    }

    public String getClueThree() {
        return clueThree;
    }

    public FirebaseUser getCreatedBy() {
        return createdBy;
    }

    // Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setGeoDistance(int geoDistance) {
        this.geoDistance = geoDistance;
    }

    public void setClueOne(String clueOne) {
        this.clueOne = clueOne;
    }

    public void setClueTwo(String clueTwo) {
        this.clueTwo = clueTwo;
    }

    public void setClueThree(String clueThree) {
        this.clueThree = clueThree;
    }

    public void setCreatedBy(FirebaseUser createdBy) {
        this.createdBy = createdBy;
    }
}
