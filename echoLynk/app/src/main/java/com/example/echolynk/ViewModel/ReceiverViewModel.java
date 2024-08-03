package com.example.echolynk.ViewModel;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.ColorUtil;

public class ReceiverViewModel extends RecyclerView.ViewHolder {


    public TextView receiverMassage,senderMassage;

    public LinearLayout left,right;

    @SuppressLint("WrongViewCast")
    public ReceiverViewModel(@NonNull View itemView, Context context ) {
        super(itemView);
        receiverMassage=itemView.findViewById(R.id.left_chat_textview);
        senderMassage=itemView.findViewById(R.id.right_chat_textview);
        left=itemView.findViewById(R.id.left_chat_layout);
        right=itemView.findViewById(R.id.right_chat_layout);


    }
}
