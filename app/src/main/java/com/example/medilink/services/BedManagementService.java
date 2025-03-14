package com.example.medilink.services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.example.medilink.models.HospitalBed;
import com.example.medilink.models.Patient;

public class BedManagementService {
    private static BedManagementService instance;
    private DatabaseReference databaseReference;

    private BedManagementService() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized BedManagementService getInstance() {
        if (instance == null) {
            instance = new BedManagementService();
        }
        return instance;
    }

    public interface BedManagementCallback {
        void onSuccess();
        void onError(String error);
    }

    public void addBed(HospitalBed bed, final BedManagementCallback callback) {
        String bedId = databaseReference.child("beds").push().getKey();
        if (bedId != null) {
            bed.setBedId(bedId);
            databaseReference.child("beds").child(bedId).setValue(bed)
                    .addOnSuccessListener(aVoid -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onError(e.getMessage()));
        } else {
            callback.onError("Failed to generate bed ID");
        }
    }

    public void assignBedToPatient(String bedId, Patient patient, final BedManagementCallback callback) {
        String patientId = databaseReference.child("patients").push().getKey();
        if (patientId != null) {
            patient.setPatientId(patientId);
            patient.setCurrentBedId(bedId);

            databaseReference.child("beds").child(bedId).child("isOccupied").setValue(true);
            databaseReference.child("beds").child(bedId).child("patientId").setValue(patientId);
            databaseReference.child("patients").child(patientId).setValue(patient)
                    .addOnSuccessListener(aVoid -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onError(e.getMessage()));
        } else {
            callback.onError("Failed to generate patient ID");
        }
    }

    public void releaseBed(String bedId, String patientId, final BedManagementCallback callback) {
        databaseReference.child("beds").child(bedId).child("isOccupied").setValue(false);
        databaseReference.child("beds").child(bedId).child("patientId").setValue(null);
        databaseReference.child("patients").child(patientId).child("currentBedId").setValue(null);
        databaseReference.child("patients").child(patientId).child("status").setValue("Discharged")
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void listenToBedUpdates(ValueEventListener listener) {
        databaseReference.child("beds").addValueEventListener(listener);
    }

    public void removeBedListener(ValueEventListener listener) {
        databaseReference.child("beds").removeEventListener(listener);
    }

    public void getBedAvailability(String wardNumber, ValueEventListener listener) {
        databaseReference.child("beds")
                .orderByChild("wardNumber")
                .equalTo(wardNumber)
                .addListenerForSingleValueEvent(listener);
    }
}