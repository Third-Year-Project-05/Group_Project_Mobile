package com.example.echolynk.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
import com.example.echolynk.View.Adapter.BlogAdapter;
import com.example.echolynk.View.Adapter.BlogViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class BlogView extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blog_view);

       recyclerView=findViewById(R.id.blog_view_recent_articles_recycle_view);

        List<Blog> blogList=new ArrayList<>();

        blogList.add(new Blog("What is Text-to-Speech?",R.drawable.dummy_blog_img1));
        blogList.add(new Blog("What is Text-to-Speech?",R.drawable.dummy_blog_img1));
        blogList.add(new Blog("What is Text-to-Speech?",R.drawable.dummy_blog_img1));
        blogList.add(new Blog("What is Text-to-Speech?",R.drawable.dummy_blog_img1));
        blogList.add(new Blog("What is Text-to-Speech?",R.drawable.dummy_blog_img1));

        if (recyclerView!=null) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                    1, // span count
                    StaggeredGridLayoutManager.HORIZONTAL // orientation
            );
            recyclerView.setAdapter(new BlogViewAdapter(getApplicationContext(),blogList));
        }

    }

    public void backOnclick(View view) {
        Intent intent = new Intent(BlogView.this, HomeBlog.class);
        startActivity(intent);
    }
}