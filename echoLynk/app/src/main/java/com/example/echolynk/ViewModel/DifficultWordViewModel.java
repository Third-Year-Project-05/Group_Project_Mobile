package com.example.echolynk.ViewModel;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;

public class DifficultWordViewModel extends RecyclerView.ViewHolder {

    public TextView difficultWord;

    public DifficultWordViewModel(@NonNull View itemView, onClickListener listener) {
        super(itemView);
        difficultWord=itemView.findViewById(R.id.word);

        difficultWord.setOnClickListener(view -> listener.onClickDifficultWord(getAdapterPosition(),view));
    }
}
