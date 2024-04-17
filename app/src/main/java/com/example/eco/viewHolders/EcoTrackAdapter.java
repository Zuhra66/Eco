package com.example.eco.viewHolders;

import static com.example.eco.database.EcoTrackRepository.repository;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.eco.database.entity.EcoTrackLog;

import java.util.ArrayList;
import java.util.List;

public class EcoTrackAdapter extends ListAdapter<EcoTrackLog, EcoTrackViewHolder> {
    private boolean isAdmin;

    public EcoTrackAdapter(@NonNull DiffUtil.ItemCallback<EcoTrackLog> diffCallback, boolean isAdmin) {
        super(diffCallback);
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public EcoTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return EcoTrackViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull EcoTrackViewHolder holder, int position) {
        EcoTrackLog current = getItem(position);
        // Display the total emissions along with other information
        holder.bind(current.toString(), isAdmin);

        // Check if the current user is admin1
        if ("admin1".equals(current.getUsername())) {
            holder.showDeleteButton(); // Assuming you have a method to show the delete button in EcoTrackViewHolder
            // Set click listener for delete button
            holder.setDeleteButtonClickListener(v -> {
                // Handle delete button click
                if (isAdmin) {
                    // Notify the adapter about the item to be deleted
                  deleteItem(position);
                }
            });
       } else {
            holder.hideDeleteButton(); // Assuming you have a method to hide the delete button in EcoTrackViewHolder
        }
    }


    public static class EcoTrackLogDiff extends DiffUtil.ItemCallback<EcoTrackLog> {
        @Override
        public boolean areItemsTheSame(@NonNull EcoTrackLog oldItem, @NonNull EcoTrackLog newItem) {
            return oldItem.getId() == newItem.getId(); // Change this to match your EcoTrackLog identifier
        }

        @Override
        public boolean areContentsTheSame(@NonNull EcoTrackLog oldItem, @NonNull EcoTrackLog newItem) {
            return oldItem.equals(newItem);
        }
    }

    // Method to remove an item from the data list
    public void deleteItem(int position) {
        repository.deleteEcoTrackLog(getItem(position));
    }
}