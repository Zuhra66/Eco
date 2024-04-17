package com.example.eco.viewHolders;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.eco.database.entity.EcoTrackLog;

public class EcoTrackAdapter extends ListAdapter<EcoTrackLog, EcoTrackViewHolder> {
    private boolean isAdmin;
    public EcoTrackAdapter(@NonNull DiffUtil.ItemCallback<EcoTrackLog> diffCallback, boolean isAdmin){
        super(diffCallback);
        this.isAdmin = isAdmin;
    }
    @NonNull
    @Override
    public EcoTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return  EcoTrackViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull EcoTrackViewHolder holder, int position) {
        EcoTrackLog current = getItem(position);
        // Display the total emissions along with other information
        holder.bind(current.toString(), isAdmin);

        // Set visibility of delete button based on admin status
        if (isAdmin) {
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }

        // Set click listener for delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click
                // Implement the logic to delete the EcoTrackLog item
                // You can use the position parameter to get the item to delete
            }
        });

    }

    public static class EcoTrackLogDiff extends DiffUtil.ItemCallback<EcoTrackLog>{
        @Override
        public boolean areItemsTheSame(@NonNull EcoTrackLog oldItem, @NonNull EcoTrackLog newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull EcoTrackLog oldItem, @NonNull EcoTrackLog newItem) {
            return oldItem.equals(newItem);
        }
    }
}
