package com.example.eco.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eco.R;

public class EcoTrackViewHolder extends RecyclerView.ViewHolder {
    private final TextView ecoTrackLogViewItem;
    final Button deleteButton;

    private EcoTrackViewHolder(View ecoTrackLogView) {
        super(ecoTrackLogView);
        ecoTrackLogViewItem = ecoTrackLogView.findViewById(R.id.recyclerItem_usernameTextView);
        deleteButton = ecoTrackLogView.findViewById(R.id.deleteButton);
    }

    public void bind(String text, boolean isAdmin) {
        ecoTrackLogViewItem.setText(text);
        if (deleteButton != null) {
            if (isAdmin) {
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.GONE);
            }
        }
    }

    public void setOnDeleteClickListener(View.OnClickListener listener) {
        if (deleteButton != null) {
            deleteButton.setOnClickListener(listener);
        }
    }

    static EcoTrackViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ecotracklog_recycler_item, parent, false);
        return new EcoTrackViewHolder(view);
    }
}