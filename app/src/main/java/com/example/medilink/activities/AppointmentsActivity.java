package com.example.medilink.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.example.medilink.R;

public class AppointmentsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        setupBottomNavigation();
        setupTabs();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.menuAppointments);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menuHome) {
                finish();
                return true;
            } else if (itemId == R.id.menuBeds) {
                finish();
                return true;
            } else if (itemId == R.id.menuProfile) {
                finish();
                return true;
            }
            return true;
        });
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Past"));
    }
}