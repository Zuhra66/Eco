package com.example.eco.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eco.R;

public class EcoTrackViewHolder extends RecyclerView.ViewHolder {
    private  final TextView ecoTrackLogViewItem;
    private  EcoTrackViewHolder(View ecoTrackLogView){
        super(ecoTrackLogView);
        ecoTrackLogViewItem = ecoTrackLogView.findViewById(R.id.recyclerItemTextView);

    }
    public void bind(String text){
        ecoTrackLogViewItem.setText(text);

    }
    static EcoTrackViewHolder create(ViewGroup parent){
    View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.ecotracklog_recycler_item, parent,false);
        return new EcoTrackViewHolder(view);
    }
}
