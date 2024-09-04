package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.MassageModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.LiveConversation.LiveConversationChat;
import com.example.echolynk.ViewModel.ReceiverViewModel;

import java.util.List;

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverViewModel> {

    Context context;
    List<MassageModel> massgeList;
    onClickListener listener;



    public ReceiverAdapter(Context context, List<MassageModel> massgeList, onClickListener listener) {
        this.context = context;
        this.massgeList = massgeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReceiverViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.activity_reserve_massage,parent,false);
        return new ReceiverViewModel(view,context,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiverViewModel holder, int position) {

        if (massgeList.get(position).getType() == 1) {
            holder.receiverMassage.setText(massgeList.get(position).getMassage());
            holder.right.setVisibility(View.GONE);
            holder.left.setVisibility(View.VISIBLE);
        } else {
            holder.senderMassage.setText(massgeList.get(position).getMassage());
            holder.right.setVisibility(View.VISIBLE);
            holder.left.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return massgeList.size();
    }
}
