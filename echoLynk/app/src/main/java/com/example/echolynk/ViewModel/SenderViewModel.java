package com.example.echolynk.ViewModel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;

public class SenderViewModel extends RecyclerView.ViewHolder {

    public Drawable shapeSenderChat;
    public TextView senderMassage;
    public SenderViewModel(@NonNull View itemView, Context context) {
        super(itemView);

        senderMassage=itemView.findViewById(R.id.massage_text);
        shapeSenderChat= ContextCompat.getDrawable(context,R.drawable.sender_massage_shape);
    }
}
