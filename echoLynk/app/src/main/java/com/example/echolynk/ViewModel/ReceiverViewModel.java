package com.example.echolynk.ViewModel;

import static androidx.core.content.ContentProviderCompat.requireContext;

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

    public Drawable shapeReceiverChat;
    public TextView receiverMassage;

    public ReceiverViewModel(@NonNull View itemView,Context context ) {
        super(itemView);
        receiverMassage=itemView.findViewById(R.id.reserve_massage_text);
        shapeReceiverChat= ContextCompat.getDrawable(context,R.drawable.resever_chat_shape);

    }
}
