package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.ViewModel.EmptyViewModel;

public class EmptyAdapter extends RecyclerView.Adapter<EmptyViewModel> {
    Context context;
    int imageView;

    public EmptyAdapter(Context context, int imageView) {
        this.context = context;
        this.imageView = imageView;
    }

    @NonNull
    @Override
    public EmptyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new  EmptyViewModel(LayoutInflater.from(context).inflate(R.layout.activity_empty_page,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmptyViewModel holder, int position) {
        holder.imageView.setImageResource(imageView);
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
