package com.example.echolynk.ViewModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;

public class BlogViewsViewModel extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView blogTitleView;

    public BlogViewsViewModel(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.blog_view_image);
        blogTitleView=itemView.findViewById(R.id.blog_view_title);
    }


}

