package com.example.medilink.models;

public class HospitalBed {
    private String bedId;
    private String wardNumber;
    private String bedNumber;
    private boolean isOccupied;
    private String patientId;
    private String bedType; // ICU, General, Emergency, etc.
    private long lastUpdated;

    public HospitalBed() {
        // Required empty constructor for Firebase
    }

    public HospitalBed(String bedId, String wardNumber, String bedNumber, String bedType) {
        this.bedId = bedId;
        this.wardNumber = wardNumber;
        this.bedNumber = bedNumber;
        this.bedType = bedType;
        this.isOccupied = false;
        this.lastUpdated = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getBedId() { return bedId; }
    public void setBedId(String bedId) { this.bedId = bedId; }

    public String getWardNumber() { return wardNumber; }
    public void setWardNumber(String wardNumber) { this.wardNumber = wardNumber; }

    public String getBedNumber() { return bedNumber; }
    public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { 
        isOccupied = occupied;
        lastUpdated = System.currentTimeMillis();
    }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getBedType() { return bedType; }
    public void setBedType(String bedType) { this.bedType = bedType; }

    public long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
}