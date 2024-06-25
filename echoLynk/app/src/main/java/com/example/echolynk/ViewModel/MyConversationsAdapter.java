package com.example.echolynk.ViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.Conversation_Item;
import com.example.echolynk.R;

import java.util.List;

public class MyConversationsAdapter extends RecyclerView.Adapter<MyConversationsViewHolder> {

    Context context;

    public MyConversationsAdapter(Context context, List<Conversation_Item> conversationItems) {
        this.context = context;
        this.conversationItems = conversationItems;
    }

    List<Conversation_Item> conversationItems;
    @NonNull
    @Override
    public MyConversationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyConversationsViewHolder(LayoutInflater.from(context).inflate(R.layout.conversation_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyConversationsViewHolder holder, int position) {
        holder.TitleTextView.setText(conversationItems.get(position).getTitle());
        holder.DescriptionTextView.setText(conversationItems.get(position).getDescription());
        holder.DateTextView.setText(conversationItems.get(position).getDate());
        holder.TimeTextView.setText(conversationItems.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return conversationItems.size();
    }
}
