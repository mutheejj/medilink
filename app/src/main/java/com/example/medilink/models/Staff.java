package com.example.medilink.models;

public class Staff {
    private String staffId;
    private String name;
    private String email;
    private String role; // Admin, Doctor, Nurse
    private String department;
    private String contactNumber;
    private boolean isOnDuty;
    private long lastActive;

    public Staff() {
        // Required empty constructor for Firebase
    }

    public Staff(String staffId, String name, String email, String role) {
        this.staffId = staffId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.isOnDuty = false;
        this.lastActive = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public boolean isOnDuty() { return isOnDuty; }
    public void setOnDuty(boolean onDuty) {
        isOnDuty = onDuty;
        lastActive = System.currentTimeMillis();
    }

    public long getLastActive() { return lastActive; }
    public void setLastActive(long lastActive) { this.lastActive = lastActive; }
}