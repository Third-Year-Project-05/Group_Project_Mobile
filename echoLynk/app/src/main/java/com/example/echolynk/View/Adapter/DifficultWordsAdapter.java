package com.example.echolynk.View.Adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.ViewModel.DifficultWordViewModel;

public class DifficultWordsAdapter extends RecyclerView.Adapter<DifficultWordViewModel> {

    Context context;
    String[] words;
    onClickListener listener;

    public DifficultWordsAdapter(Context context, String[] words, onClickListener listener) {
        this.context = context;
        this.words = words;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DifficultWordViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new DifficultWordViewModel(LayoutInflater.from(context).inflate(R.layout.difficult_word,parent,false),listener);

    }

    @Override
    public void onBindViewHolder(@NonNull DifficultWordViewModel holder, int position) {
        holder.difficultWord.setText(words[position]);

    }

    @Override
    public int getItemCount() {
        return words.length;
    }
}
