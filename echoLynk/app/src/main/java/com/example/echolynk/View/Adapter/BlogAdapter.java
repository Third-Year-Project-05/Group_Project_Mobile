package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
import com.example.echolynk.ViewModel.BlogViewModel;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogViewModel> {

    Context context;
    List<Blog> blogs;

    public BlogAdapter(Context context, List<Blog> blogs) {
        this.context = context;
        this.blogs = blogs;
    }

    @NonNull
    @Override
    public BlogViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BlogViewModel(LayoutInflater.from(context).inflate(R.layout.activity_blog_card_custom,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewModel holder, int position) {
        holder.titleView.setText(blogs.get(position).getTitle());
        holder.authorView.setText(blogs.get(position).getAuthor());
        holder.descriptionView.setText(blogs.get(position).getDescription());
        holder.imageView.setImageResource(blogs.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }
}
