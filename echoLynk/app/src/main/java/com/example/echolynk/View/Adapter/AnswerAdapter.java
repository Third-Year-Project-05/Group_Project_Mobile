package com.example.echolynk.View.Adapter;

import android.content.Context;
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
import com.example.echolynk.ViewModel.SuggestionMassageViewModel;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<SuggestionMassageViewModel> {

    Context context;
    List<String> answer;
    onClickListener listener;

    private final LiveConversationChat liveConversationChat=new LiveConversationChat();

    public AnswerAdapter(Context context, List<String> answer, onClickListener listener) {
        this.context = context;
        this.answer = answer;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SuggestionMassageViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_suggestion_massage,parent,false);
        return new SuggestionMassageViewModel(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionMassageViewModel holder, int position) {
        holder.answer.setText(answer.get(position));
        holder.itemView.setOnClickListener(view -> {
            Log.d("sugesstion click", "onBindViewHolder: click wenwa ");
            liveConversationChat.massageList.add(new MassageModel(answer.get(position),0));
            liveConversationChat.setUpLiveChat(liveConversationChat.chatRecycleView, liveConversationChat.massageList);
        });
    }

    @Override
    public int getItemCount() {
        return answer.size();
    }
}
