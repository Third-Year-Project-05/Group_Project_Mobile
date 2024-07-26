package com.example.echolynk.View.Blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
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

        // set as article data
        TextView articleTitle=findViewById(R.id.blog_view_title);
        ImageView articleImage=findViewById(R.id.blog_view_image);
        TextView articleAuthor=findViewById(R.id.blog_view_author);
        TextView articlePublishDate=findViewById(R.id.blog_view_publish_date);
        TextView articleDescription=findViewById(R.id.blog_view_description);

        articleTitle.setText(getIntent().getStringExtra("blogTitle"));
        articleImage.setImageResource(getIntent().getIntExtra("blogImage",R.drawable.conversation_default));
        articleAuthor.setText(getIntent().getStringExtra("blogAuthor"));
        articlePublishDate.setText(getIntent().getStringExtra("blogPublishDate"));
        articleDescription.setText(getIntent().getStringExtra("description"));

      // set as   recent articles

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