package com.example.medilink.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.medilink.models.Staff;

public class AuthService {
    private static AuthService instance;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private AuthService() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public interface AuthCallback {
        void onSuccess();
        void onError(String error);
    }

    public void registerStaff(String email, String password, String name, String role, final AuthCallback callback) {
        if (!isValidEmail(email)) {
            callback.onError("Invalid email format");
            return;
        }

        if (!isValidPassword(password)) {
            callback.onError("Password must be at least 6 characters and contain at least one number");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().getUser() != null) {
                        FirebaseUser user = task.getResult().getUser();
                        user.updateProfile(new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build())
                                .addOnCompleteListener(profileTask -> {
                                    String userId = user.getUid();
                                    Staff staff = new Staff(userId, name, email, role);
                                    staff.setLastActive(System.currentTimeMillis());
                                    databaseReference.child("staff").child(userId).setValue(staff)
                                            .addOnSuccessListener(aVoid -> {
                                                sendVerificationEmail(user);
                                                callback.onSuccess();
                                            })
                                            .addOnFailureListener(e -> callback.onError(e.getMessage()));
                                });
                    } else {
                        callback.onError(task.getException() != null ? 
                                task.getException().getMessage() : "Registration failed");
                });
    }

    public void loginStaff(String email, String password, final AuthCallback callback) {
        if (!isValidEmail(email)) {
            callback.onError("Invalid email format");
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().getUser() != null) {
                        FirebaseUser user = task.getResult().getUser();
                        if (!user.isEmailVerified()) {
                            callback.onError("Please verify your email first");
                            return;
                        }
                        
                        String userId = user.getUid();
                        databaseReference.child("staff").child(userId)
                                .child("lastActive").setValue(System.currentTimeMillis())
                                .addOnSuccessListener(aVoid -> callback.onSuccess())
                                .addOnFailureListener(e -> callback.onError(e.getMessage()));
                    } else {
                        callback.onError(task.getException() != null ? 
                                task.getException().getMessage() : "Login failed");
                });
    }

    public void logoutStaff() {
        firebaseAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    private void sendVerificationEmail(FirebaseUser user) {
        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification();
        }
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 && password.matches(".*\\d.*");
    }

    public void resetPassword(String email, final AuthCallback callback) {
        if (!isValidEmail(email)) {
            callback.onError("Invalid email format");
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(task.getException() != null ? 
                                task.getException().getMessage() : "Password reset failed");
                    }
                });
    }
}