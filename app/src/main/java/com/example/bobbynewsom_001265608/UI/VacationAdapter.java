package com.example.bobbynewsom_001265608.UI;

import android.content.Intent;
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

    private List<Vacation> mVacations;

    public class VacationViewHolder extends RecyclerView.ViewHolder {

        private final TextView vacationItemView;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.textViewVacationListItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        int position = getAdapterPosition();
                        final Vacation current = mVacations.get(position);
                        Intent intent = new Intent(view.getContext(), VacationDetails.class);
                        intent.putExtra("vacationId", current.getVacationId());
                        intent.putExtra("title", current.getTitle());
                        intent.putExtra("accommodation", current.getAccommodation());
                        intent.putExtra("startDate", current.getStartDate());
                        intent.putExtra("endDate", current.getEndDate());
                        view.getContext().startActivity(intent);
                }
            } );


        }
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mVacations != null){
            return mVacations.size();
        }
        else return 0;
    }

    public void setVacations(List<Vacation> vacations) {
        mVacations = vacations;
        notifyDataSetChanged();
    }


}
