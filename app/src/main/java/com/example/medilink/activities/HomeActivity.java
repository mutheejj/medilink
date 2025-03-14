package com.example.medilink.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import android.text.Editable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.medilink.R;
import com.example.medilink.adapters.CategoriesAdapter;
import com.example.medilink.adapters.DoctorsAdapter;
import com.example.medilink.models.Category;
import com.example.medilink.models.Doctor;
import java.util.ArrayList;
import java.util.List;
import android.text.TextWatcher;

public class HomeActivity extends AppCompatActivity implements CategoriesAdapter.OnCategoryClickListener, DoctorsAdapter.OnDoctorClickListener {
    private TextView textWelcome;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView doctorsRecyclerView;
    private BottomNavigationView bottomNavigationView;
    private MaterialCardView appointmentsCard;
    private MaterialCardView bedsCard;
    private FirebaseAuth firebaseAuth;
    private List<Category> categories;
    private List<Doctor> doctors;
    private CategoriesAdapter categoriesAdapter;
    private DoctorsAdapter doctorsAdapter;
    private TextInputEditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        initializeViews();
        setupRecyclerViews();
        loadUserData();
        setupBottomNavigation();
        setupQuickAccess();
        loadCategories();
        loadDoctors();
    }

    private void initializeViews() {
        textWelcome = findViewById(R.id.textWelcome);
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        doctorsRecyclerView = findViewById(R.id.doctorsRecyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        appointmentsCard = findViewById(R.id.appointmentsCard);
        bedsCard = findViewById(R.id.bedsCard);
        searchInput = findViewById(R.id.searchInput);

        setupSearchInput();
    }

    private void setupSearchInput() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDoctors(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterDoctors(String query) {
        if (query.isEmpty()) {
            doctorsAdapter.updateDoctors(doctors);
            return;
        }

        List<Doctor> filteredDoctors = new ArrayList<>();
        for (Doctor doctor : doctors) {
            if (doctor.getName().toLowerCase().contains(query.toLowerCase()) ||
                doctor.getSpecialization().toLowerCase().contains(query.toLowerCase())) {
                filteredDoctors.add(doctor);
            }
        }
        doctorsAdapter.updateDoctors(filteredDoctors);
    }

    private void setupRecyclerViews() {
        categories = new ArrayList<>();
        doctors = new ArrayList<>();

        categoriesRecyclerView.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        doctorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoriesAdapter = new CategoriesAdapter(categories, this);
        doctorsAdapter = new DoctorsAdapter(doctors, this);

        categoriesRecyclerView.setAdapter(categoriesAdapter);
        doctorsRecyclerView.setAdapter(doctorsAdapter);
    }

    private void loadUserData() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            textWelcome.setText("Welcome back, " + name);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.menuHome);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menuAppointments) {
                startActivity(new Intent(this, AppointmentsActivity.class));
                return true;
            } else if (itemId == R.id.menuBeds) {
                startActivity(new Intent(this, BedManagementActivity.class));
                return true;
            } else if (itemId == R.id.menuProfile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return true;
        });
    }

    private void setupQuickAccess() {
        appointmentsCard.setOnClickListener(v -> 
            startActivity(new Intent(this, AppointmentsActivity.class)));
        
        bedsCard.setOnClickListener(v -> 
            startActivity(new Intent(this, BedManagementActivity.class)));
    }

    private void loadCategories() {
        FirebaseDatabase.getInstance().getReference("categories")
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    categories.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Category category = snapshot.getValue(Category.class);
                        if (category != null) {
                            categories.add(category);
                        }
                    }
                    categoriesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
    }

    private void loadDoctors() {
        FirebaseDatabase.getInstance().getReference("doctors")
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    doctors.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Doctor doctor = snapshot.getValue(Doctor.class);
                        if (doctor != null) {
                            doctors.add(doctor);
                        }
                    }
                    doctorsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
    }

    @Override
    public void onCategoryClick(Category category) {
        // Handle category click
    }

    @Override
    public void onDoctorClick(Doctor doctor) {
        // Navigate to doctor details
    }

    @Override
    public void onBookAppointmentClick(Doctor doctor) {
        Intent intent = new Intent(this, AppointmentsActivity.class);
        intent.putExtra("doctor_id", doctor.getId());
        startActivity(intent);
    }
}