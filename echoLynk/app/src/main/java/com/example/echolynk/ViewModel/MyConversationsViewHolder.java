package com.example.echolynk.ViewModel;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;

public class MyConversationsViewHolder extends RecyclerView.ViewHolder {

    TextView TitleTextView, DescriptionTextView,DateTextView, TimeTextView;

    public MyConversationsViewHolder(@NonNull View itemView) {
        super(itemView);
        TitleTextView = itemView.findViewById(R.id.conversationNameView);
        DescriptionTextView = itemView.findViewById(R.id.conversationDescriptionTextView);
        DateTextView = itemView.findViewById(R.id.conversationDateTextView);
        TimeTextView = itemView.findViewById(R.id.conversationDurationTextView);
    }
}
