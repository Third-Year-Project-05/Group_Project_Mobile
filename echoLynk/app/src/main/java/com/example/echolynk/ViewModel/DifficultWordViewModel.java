package com.example.echolynk.ViewModel;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;

public class DifficultWordViewModel extends RecyclerView.ViewHolder {

    public TextView difficultWord;

    public DifficultWordViewModel(@NonNull View itemView) {
        super(itemView);
        difficultWord=itemView.findViewById(R.id.word);

    }
}
