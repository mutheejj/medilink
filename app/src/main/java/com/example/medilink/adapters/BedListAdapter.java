package com.example.medilink.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medilink.R;
import com.example.medilink.models.HospitalBed;
import java.util.List;

public class BedListAdapter extends RecyclerView.Adapter<BedListAdapter.BedViewHolder> {
    private List<HospitalBed> bedList;

    public BedListAdapter(List<HospitalBed> bedList) {
        this.bedList = bedList;
    }

    @NonNull
    @Override
    public BedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bed, parent, false);
        return new BedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BedViewHolder holder, int position) {
        HospitalBed bed = bedList.get(position);
        holder.bind(bed);
    }

    @Override
    public int getItemCount() {
        return bedList.size();
    }

    static class BedViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBedNumber;
        private TextView tvWardNumber;
        private TextView tvBedType;
        private TextView tvStatus;

        public BedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBedNumber = itemView.findViewById(R.id.tvBedNumber);
            tvWardNumber = itemView.findViewById(R.id.tvWardNumber);
            tvBedType = itemView.findViewById(R.id.tvBedType);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        public void bind(HospitalBed bed) {
            tvBedNumber.setText(String.format("Bed: %s", bed.getBedNumber()));
            tvWardNumber.setText(String.format("Ward: %s", bed.getWardNumber()));
            tvBedType.setText(bed.getBedType());
            tvStatus.setText(bed.isOccupied() ? "Occupied" : "Available");
            tvStatus.setTextColor(itemView.getContext().getColor(
                bed.isOccupied() ? R.color.bed_occupied : R.color.bed_available
            ));
        }
    }
}