package com.example.echolynk.ViewModel;

import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
public class LiveUsersViewModel extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public LiveUsersViewModel(@NonNull View itemView,onClickListener listener ) {
        super(itemView);

        imageView=itemView.findViewById(R.id.profile_image);

        imageView.setOnClickListener(view -> listener.onClick(getAdapterPosition(),view));
    }
}
