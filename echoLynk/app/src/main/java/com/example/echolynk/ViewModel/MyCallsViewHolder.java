package com.example.echolynk.ViewModel;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCallsViewHolder extends RecyclerView.ViewHolder {

    CircleImageView circleImageView;
    TextView nameTextView, lastMessageTextView, dateTextView, countMessageTextView;

    public MyCallsViewHolder(@NonNull View itemView) {
        super(itemView);
        circleImageView = itemView.findViewById(R.id.contactImageView);
        nameTextView = itemView.findViewById(R.id.contactNameView);
        lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
        dateTextView = itemView.findViewById(R.id.dateTextView);
        countMessageTextView = itemView.findViewById(R.id.countTextView);
    }
}
