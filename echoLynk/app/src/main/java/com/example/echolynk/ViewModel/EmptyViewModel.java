package com.example.echolynk.ViewModel;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;

public class EmptyViewModel extends RecyclerView.ViewHolder {
    public ImageView imageView;


    public EmptyViewModel(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.empty_page_image);
    }
}
