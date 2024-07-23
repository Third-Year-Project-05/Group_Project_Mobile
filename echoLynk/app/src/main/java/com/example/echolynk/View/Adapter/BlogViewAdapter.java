package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.imageView.setImageResource(currentBlog.getImage());
    }

    @Override
    public int getItemCount() {
        return recentBlogs.size();
    }
}
