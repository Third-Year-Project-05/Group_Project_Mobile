package com.example.echolynk.ViewModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.LiveConversation.VoiceAssistanceThree;

public class ReceiverViewModel extends RecyclerView.ViewHolder {


    public TextView receiverMassage,senderMassage;

    public LinearLayout left,right;



    @SuppressLint({"WrongViewCast", "ClickableViewAccessibility"})
    public ReceiverViewModel(@NonNull View itemView, Context context, onClickListener listener) {
        super(itemView);
        receiverMassage=itemView.findViewById(R.id.left_chat_textview);
        senderMassage=itemView.findViewById(R.id.right_chat_textview);
        left=itemView.findViewById(R.id.left_chat_layout);
        right=itemView.findViewById(R.id.right_chat_layout);


        senderMassage.setOnClickListener(view -> listener.onClick(getAdapterPosition(),view));
        receiverMassage.setOnClickListener(view -> listener.onClick(getAdapterPosition(),view));

        senderMassage.setOnLongClickListener(view -> listener.onLongClick(getAdapterPosition(),view));





    }
}
