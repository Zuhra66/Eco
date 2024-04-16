package com.example.eco.viewHolders;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.eco.database.entity.EcoTrackLog;

public class EcoTrackAdapter extends ListAdapter<EcoTrackLog, EcoTrackViewHolder> {
    public EcoTrackAdapter(@NonNull DiffUtil.ItemCallback<EcoTrackLog> diffCallback){
        super(diffCallback);
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
        holder.bind(current.toString());
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
