package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.ViewModel.LiveUsersViewModel;

import java.util.List;

public class LiveUserAdapter extends RecyclerView.Adapter<LiveUsersViewModel> {

    Context context;

    List<Integer> imageView;
    onClickListener listener;

    public LiveUserAdapter(Context context, List<Integer> imageView,onClickListener listener) {
        this.context = context;
        this.imageView = imageView;
        this.listener=listener;
    }

    @NonNull
    @Override
    public LiveUsersViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_custom_live_conversation_tab,parent,false);
        return new LiveUsersViewModel(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveUsersViewModel holder, int position) {
       holder.imageView.setImageResource(imageView.get(position));
    }

    @Override
    public int getItemCount() {
        return imageView.size();
    }
}
