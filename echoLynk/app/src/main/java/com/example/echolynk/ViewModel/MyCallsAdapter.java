package com.example.echolynk.ViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.Call_Item;
import com.example.echolynk.R;

import java.util.List;

public class MyCallsAdapter extends RecyclerView.Adapter<MyCallsViewHolder> {
    public MyCallsAdapter(Context context, List<Call_Item> call_items) {
        this.context = context;
        this.call_items = call_items;
    }

    Context context;
    List<Call_Item> call_items;

    @NonNull
    @Override
    public MyCallsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCallsViewHolder(LayoutInflater.from(context).inflate(R.layout.call_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCallsViewHolder holder, int position) {
        holder.nameTextView.setText(call_items.get(position).getName());
        holder.lastMessageTextView.setText(call_items.get(position).getLastMessage());
        holder.dateTextView.setText(call_items.get(position).getTime());
        holder.countMessageTextView.setText(call_items.get(position).getCount());
        holder.circleImageView.setImageResource(call_items.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return call_items.size();
    }
}
