package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.ViewModel.SuggestionMassageViewModel;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<SuggestionMassageViewModel> {

    Context context;
    List<String> answer;
    onClickListener listener;

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
    }

    @Override
    public int getItemCount() {
        return answer.size();
    }
}
