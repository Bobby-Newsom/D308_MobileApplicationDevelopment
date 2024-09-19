package com.example.bobbynewsom_001265608.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobbynewsom_001265608.R;
import com.example.bobbynewsom_001265608.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> vacations;

    public VacationAdapter(List<Vacation> vacations) {
        this.vacations = vacations;
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation currentVacation = vacations.get(position);
        holder.textViewTitle.setText(currentVacation.getTitle());
        holder.textViewAccommodation.setText(currentVacation.getAccommodation());
        holder.textViewStartDate.setText(currentVacation.getStartDate()); // Set start date
        holder.textViewEndDate.setText(currentVacation.getEndDate());     // Set end date
    }

    @Override
    public int getItemCount() {
        return vacations.size();
    }

    // ViewHolder class
    public static class VacationViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewAccommodation;
        private TextView textViewStartDate;
        private TextView textViewEndDate;

        public VacationViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewVacationTitleLabel);
            textViewAccommodation = itemView.findViewById(R.id.textViewAccommodationLabel);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDateLabel);
            textViewEndDate = itemView.findViewById(R.id.textViewEndDateLabel);
        }
    }
}
