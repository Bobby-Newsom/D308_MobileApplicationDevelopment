package com.example.bobbynewsom_001265608.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobbynewsom_001265608.R;
import com.example.bobbynewsom_001265608.entities.Excursion;

import java.util.ArrayList;
import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> excursions = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        Excursion currentExcursion = excursions.get(position);
        holder.titleTextView.setText(currentExcursion.getTitle());
        holder.dateTextView.setText(currentExcursion.getDate());
    }

    @Override
    public int getItemCount() {
        return excursions.size();
    }

    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
        notifyDataSetChanged();
    }

    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView dateTextView;

        public ExcursionViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.excursionTitle);
            dateTextView = itemView.findViewById(R.id.excursionDate);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(excursions.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Excursion excursion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
