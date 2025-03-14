package com.example.medilink.models;

public class Patient {
    private String patientId;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String contactNumber;
    private String emergencyContact;
    private String bloodGroup;
    private String medicalHistory;
    private String currentBedId;
    private long admissionDate;
    private String status; // Admitted, Discharged, In-Treatment

    public Patient() {
        // Required empty constructor for Firebase
    }

    public Patient(String patientId, String name, String dateOfBirth, String gender) {
        this.patientId = patientId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.admissionDate = System.currentTimeMillis();
        this.status = "Admitted";
    }

    // Getters and Setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public String getCurrentBedId() { return currentBedId; }
    public void setCurrentBedId(String currentBedId) { this.currentBedId = currentBedId; }

    public long getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(long admissionDate) { this.admissionDate = admissionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}