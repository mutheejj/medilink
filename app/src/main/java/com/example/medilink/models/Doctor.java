package com.example.medilink.models;

public class Doctor {
    private String id;
    private String name;
    private String specialization;
    private String photoUrl;
    private String experience;
    private float rating;

    public Doctor() {}

    public Doctor(String id, String name, String specialization, String photoUrl, String experience, float rating) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.photoUrl = photoUrl;
        this.experience = experience;
        this.rating = rating;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
}