package com.example.medilink.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.medilink.models.Staff;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static AuthService instance;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION = 300000; // 5 minutes in milliseconds
    private Map<String, Integer> loginAttempts;
    private Map<String, Long> lockoutTimes;

    private AuthService() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        databaseReference = database.getReference();
        loginAttempts = new HashMap<>();
        lockoutTimes = new HashMap<>();
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
        void onLoading(boolean isLoading);
        void onEmailVerificationRequired();
    }

    public void registerStaff(String email, String password, String name, String role, final AuthCallback callback) {
        callback.onLoading(true);
        
        if (!isValidEmail(email)) {
            callback.onLoading(false);
            callback.onError("Please enter a valid email address");
            return;
        }

        if (!isValidPassword(password)) {
            callback.onLoading(false);
            callback.onError("Password must be at least 6 characters long and contain at least one number");
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
                                                callback.onLoading(false);
                                                callback.onEmailVerificationRequired();
                                            })
                                            .addOnFailureListener(e -> {
                                                callback.onLoading(false);
                                                callback.onError("Failed to create user profile: " + e.getMessage());
                                            });
                                });
                    } else {
                        callback.onError(task.getException() != null ? 
                                task.getException().getMessage() : "Registration failed");
                    }
                });
    }

    public void loginStaff(String email, String password, final AuthCallback callback) {
        callback.onLoading(true);
        
        if (!isValidEmail(email)) {
            callback.onLoading(false);
            callback.onError("Please enter a valid email address");
            return;
        }

        if (isUserLockedOut(email)) {
            callback.onLoading(false);
            long remainingTime = getRemainingLockoutTime(email);
            callback.onError("Too many login attempts. Please try again in " + 
                (remainingTime / 60000) + " minutes");
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().getUser() != null) {
                        FirebaseUser user = task.getResult().getUser();
                        if (!user.isEmailVerified()) {
                            sendVerificationEmail(user);
                            callback.onLoading(false);
                            callback.onEmailVerificationRequired();
                            return;
                        }
                        
                        // Reset login attempts on successful login
                        resetLoginAttempts(email);
                        
                        String userId = user.getUid();
                        databaseReference.child("staff").child(userId)
                                .child("lastActive").setValue(System.currentTimeMillis())
                                .addOnSuccessListener(aVoid -> {
                                    // Keep a local cache of the last active time
                                    databaseReference.child("staff").child(userId).keepSynced(true);
                                    callback.onSuccess();
                                })
                                .addOnFailureListener(e -> {
                                    // If database update fails, still allow login but log the error
                                    android.util.Log.w("AuthService", "Failed to update lastActive: " + e.getMessage());
                                    callback.onSuccess();
                                });
                    } else {
                        String errorMessage = task.getException() != null ? 
                                task.getException().getMessage() : "Invalid email or password";
                        android.util.Log.e("AuthService", "Login failed: " + errorMessage);
                        
                        // Increment login attempts
                        incrementLoginAttempts(email);
                        
                        callback.onLoading(false);
                        if (getLoginAttempts(email) >= MAX_LOGIN_ATTEMPTS) {
                            lockoutUser(email);
                            callback.onError("Too many failed attempts. Account locked for 5 minutes");
                        } else {
                            callback.onError("Invalid credentials. " + 
                                (MAX_LOGIN_ATTEMPTS - getLoginAttempts(email)) + " attempts remaining");
                        }
                    }
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

    private void incrementLoginAttempts(String email) {
        int attempts = loginAttempts.getOrDefault(email, 0) + 1;
        loginAttempts.put(email, attempts);
    }

    private void resetLoginAttempts(String email) {
        loginAttempts.remove(email);
        lockoutTimes.remove(email);
    }

    private int getLoginAttempts(String email) {
        return loginAttempts.getOrDefault(email, 0);
    }

    private void lockoutUser(String email) {
        lockoutTimes.put(email, System.currentTimeMillis());
    }

    private boolean isUserLockedOut(String email) {
        Long lockoutTime = lockoutTimes.get(email);
        if (lockoutTime == null) return false;
        return System.currentTimeMillis() - lockoutTime < LOCKOUT_DURATION;
    }

    private long getRemainingLockoutTime(String email) {
        Long lockoutTime = lockoutTimes.get(email);
        if (lockoutTime == null) return 0;
        long remainingTime = LOCKOUT_DURATION - (System.currentTimeMillis() - lockoutTime);
        return Math.max(0, remainingTime);
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