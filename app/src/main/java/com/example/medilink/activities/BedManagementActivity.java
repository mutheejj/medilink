package com.example.medilink.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.medilink.R;
import com.example.medilink.adapters.BedListAdapter;
import com.example.medilink.models.HospitalBed;
import com.example.medilink.services.BedManagementService;
import com.example.medilink.services.NotificationService;
import java.util.ArrayList;
import java.util.List;

public class BedManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BedListAdapter adapter;
    private List<HospitalBed> bedList;
    private BedManagementService bedManagementService;
    private NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_management);

        bedManagementService = BedManagementService.getInstance();
        notificationService = NotificationService.getInstance(this);
        
        recyclerView = findViewById(R.id.recyclerViewBeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        bedList = new ArrayList<>();
        adapter = new BedListAdapter(bedList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAddBed = findViewById(R.id.fabAddBed);
        fabAddBed.setOnClickListener(v -> showAddBedDialog());

        loadBeds();
        subscribeToNotifications();
    }

    private void loadBeds() {
        bedManagementService.listenToBedUpdates(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bedList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HospitalBed bed = snapshot.getValue(HospitalBed.class);
                    if (bed != null) {
                        bedList.add(bed);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BedManagementActivity.this, 
                    "Failed to load beds: " + databaseError.getMessage(),
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddBedDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_bed, null);
        TextInputEditText etWardNumber = dialogView.findViewById(R.id.etWardNumber);
        TextInputEditText etBedNumber = dialogView.findViewById(R.id.etBedNumber);
        TextInputEditText etBedType = dialogView.findViewById(R.id.etBedType);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Add New Bed")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String wardNumber = etWardNumber.getText().toString().trim();
                    String bedNumber = etBedNumber.getText().toString().trim();
                    String bedType = etBedType.getText().toString().trim();

                    if (wardNumber.isEmpty() || bedNumber.isEmpty() || bedType.isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    HospitalBed newBed = new HospitalBed(null, wardNumber, bedNumber, bedType);
                    bedManagementService.addBed(newBed, new BedManagementService.BedManagementCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(BedManagementActivity.this, 
                                "Bed added successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(BedManagementActivity.this, 
                                "Failed to add bed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void subscribeToNotifications() {
        notificationService.subscribeToTopic("bed_updates");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationService.unsubscribeFromTopic("bed_updates");
    }
}