package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.ViewModel.BlogViewModel;

import java.util.List;

public class MyBlogAdapter extends RecyclerView.Adapter<BlogViewModel>{

    Context context;
    List<Blog> blogs;

    onClickListener listener;

    public MyBlogAdapter(Context context, List<Blog> blogs, onClickListener listener) {
        this.context = context;
        this.blogs = blogs;
        this.listener=listener;
    }


    @NonNull
    @Override
    public BlogViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_blog_card_custom, parent, false);
        return new BlogViewModel(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewModel holder, int position) {
        holder.titleView.setText(blogs.get(position).getTitle());
        holder.authorView.setText(blogs.get(position).getAuthor());

        String imageUrl = blogs.get(position).getImage(); // Get the image URL
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Parse the URI and set it in the ImageView
            Uri parse = Uri.parse(imageUrl);
            Log.e("blog image uri", parse.toString());
            Glide.with(holder.imageView.getContext())
                    .load(parse) // Use Glide to load the image
                    .into(holder.imageView); // Set image in ImageView
        } else {
            // Use a placeholder image when imageUrl is null or empty
            holder.imageView.setImageResource(R.drawable.dummy_blog_img1); // Replace with your placeholder resource ID
        }
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }
}
