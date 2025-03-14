package com.example.medilink.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.medilink.R;

public class ProfileActivity extends AppCompatActivity {
    private TextView textName;
    private TextView textEmail;
    private MaterialButton buttonLogout;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        initializeViews();
        loadUserData();
        setupBottomNavigation();
    }

    private void initializeViews() {
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        buttonLogout = findViewById(R.id.buttonLogout);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        buttonLogout.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            textName.setText(user.getDisplayName());
            textEmail.setText(user.getEmail());
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.menuProfile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menuHome) {
                finish();
                return true;
            } else if (itemId == R.id.menuAppointments) {
                finish();
                return true;
            } else if (itemId == R.id.menuBeds) {
                finish();
                return true;
            }
            return true;
        });
    }

    private void logout() {
        firebaseAuth.signOut();
        finish();
    }
}