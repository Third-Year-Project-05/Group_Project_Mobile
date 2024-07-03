package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.ColorUtil;
import com.example.echolynk.ViewModel.ReceiverViewModel;
import com.example.echolynk.ViewModel.SenderViewModel;

import java.util.List;

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverViewModel> {

    Context context;
    LinearLayout linearLayout;

    List<String> receiverMassageList;

    public ReceiverAdapter(Context context, List<String> receiverMassageList) {
        this.context = context;
        this.receiverMassageList = receiverMassageList;
    }

    @NonNull
    @Override
    public ReceiverViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.activity_reserve_massage,parent,false);
        return new ReceiverViewModel(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiverViewModel holder, int position) {
        holder.receiverMassage.setText(receiverMassageList.get(position));

        holder.shapeReceiverChat.setTint(ColorUtil.getRandomColor());
    }

    @Override
    public int getItemCount() {
        return receiverMassageList.size();
    }
}
