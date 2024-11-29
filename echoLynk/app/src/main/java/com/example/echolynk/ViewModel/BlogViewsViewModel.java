package com.example.echolynk.ViewModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;

public class BlogViewsViewModel extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView blogTitleView;

    public BlogViewsViewModel(@NonNull View itemView, onClickListener listener) {
        super(itemView);
        imageView=itemView.findViewById(R.id.blog_view_image);
        blogTitleView=itemView.findViewById(R.id.blog_view_title);

        imageView.setOnClickListener(view -> listener.onClick(getAdapterPosition(),view));
    }


}

