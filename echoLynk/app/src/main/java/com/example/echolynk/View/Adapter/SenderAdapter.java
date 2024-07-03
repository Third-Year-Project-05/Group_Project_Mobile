package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.ColorUtil;
import com.example.echolynk.ViewModel.SenderViewModel;

import java.util.List;

public class SenderAdapter extends RecyclerView.Adapter<SenderViewModel> {

    Context context;

    List<String> senderMassageList;

    public SenderAdapter(Context context, List<String> senderMassageList) {
        this.context = context;
        this.senderMassageList = senderMassageList;
    }

    @NonNull
    @Override
    public SenderViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.activity_sender_massage,parent,false);
        return new SenderViewModel(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull SenderViewModel holder, int position) {
        holder.senderMassage.setText(senderMassageList.get(position));
        holder.shapeSenderChat.setTint(ColorUtil.getRandomColor());
    }

    @Override
    public int getItemCount() {
        return senderMassageList.size();
    }
}
