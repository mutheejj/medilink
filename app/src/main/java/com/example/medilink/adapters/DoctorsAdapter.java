package com.example.medilink.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.example.medilink.R;
import com.example.medilink.models.Doctor;
import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder> {
    private List<Doctor> doctors;
    private OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
        void onBookAppointmentClick(Doctor doctor);
    }

    public DoctorsAdapter(List<Doctor> doctors, OnDoctorClickListener listener) {
        this.doctors = doctors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        holder.bind(doctor);
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    class DoctorViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView doctorImage;
        private TextView doctorName;
        private TextView doctorSpecialization;
        private RatingBar doctorRating;
        private TextView doctorExperience;
        private MaterialButton buttonBookAppointment;

        DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorImage = itemView.findViewById(R.id.doctorImage);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorSpecialization = itemView.findViewById(R.id.doctorSpecialization);
            doctorRating = itemView.findViewById(R.id.doctorRating);
            doctorExperience = itemView.findViewById(R.id.doctorExperience);
            buttonBookAppointment = itemView.findViewById(R.id.buttonBookAppointment);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDoctorClick(doctors.get(position));
                }
            });

            buttonBookAppointment.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onBookAppointmentClick(doctors.get(position));
                }
            });
        }

        void bind(Doctor doctor) {
            doctorName.setText(doctor.getName());
            doctorSpecialization.setText(doctor.getSpecialization());
            doctorRating.setRating(doctor.getRating());
            doctorExperience.setText(doctor.getExperience());

            if (doctor.getPhotoUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(doctor.getPhotoUrl())
                        .circleCrop()
                        .into(doctorImage);
            }
        }
    }

    public void updateDoctors(List<Doctor> newDoctors) {
        this.doctors = newDoctors;
        notifyDataSetChanged();
    }
}