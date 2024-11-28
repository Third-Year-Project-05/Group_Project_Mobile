package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
import com.example.echolynk.ViewModel.BlogViewsViewModel;

import java.util.List;

public class BlogViewAdapter extends RecyclerView.Adapter<BlogViewsViewModel> {

    Context context;

    List<Blog> recentBlogs;

    public BlogViewAdapter(Context context, List<Blog> recentBlogs) {
        this.context = context;
        this.recentBlogs = recentBlogs;
    }

    @NonNull
    @Override
    public BlogViewsViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BlogViewsViewModel(LayoutInflater.from(context).inflate(R.layout.activity_blog_view_recent_articles_custom,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewsViewModel holder, int position) {
        Blog currentBlog = recentBlogs.get(position);
        holder.blogTitleView.setText(currentBlog.getTitle());

        String imageUrl = recentBlogs.get(position).getImage(); // Get the image URL
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
        return recentBlogs.size();
    }
}
