package com.example.echolynk.ViewModel;


import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;


public class SuggestionMassageViewModel extends RecyclerView.ViewHolder {

   public Button answer;

    public SuggestionMassageViewModel(@NonNull View itemView, onClickListener listener) {
        super(itemView);

        answer=itemView.findViewById(R.id.answer);

        answer.setOnClickListener(view -> listener.onClick(getAdapterPosition(), view));
    }
}
