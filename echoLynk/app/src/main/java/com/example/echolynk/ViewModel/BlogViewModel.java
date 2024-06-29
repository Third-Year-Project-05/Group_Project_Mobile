package com.example.echolynk.ViewModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.OnBlogClickListener;

public class BlogViewModel extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView titleView;
    public TextView authorView;
    public TextView descriptionView;


    public BlogViewModel(@NonNull View itemView,OnBlogClickListener listener) {
        super(itemView);
        imageView=itemView.findViewById(R.id.blog_image);
        titleView=itemView.findViewById(R.id.blog_title);
        authorView=itemView.findViewById(R.id.blog_author);
        descriptionView=itemView.findViewById(R.id.blog_description);


        descriptionView.setOnClickListener(view -> listener.onBlogClick(getAdapterPosition(), view));
    }
}
