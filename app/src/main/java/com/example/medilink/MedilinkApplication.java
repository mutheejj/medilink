package com.example.medilink;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class MedilinkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
    }
} 