package com.example.echolynk.View.Blog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.BlogAdapter;
import com.example.echolynk.View.Adapter.EmptyAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeBlog extends AppCompatActivity implements onClickListener {

    RecyclerView recyclerView;

    @Override
    public void onClickDifficultWord(int position, View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_blog);

        recyclerView=findViewById(R.id.blog_recycle_view);

        List<Blog> blogs=new ArrayList<>();

       /* blogs.add(new Blog("What is Text-to-Speech?","by Echolynk",R.drawable.dummy_blog_img1));

        blogs.add(new Blog("hatti pakaya","by Echolynk",R.drawable.dummy_blog_img1));

        blogs.add(new Blog("What is Text-to-Speech?","by Echolynk",R.drawable.dummy_blog_img1));

        blogs.add(new Blog("What is Text-to-Speech?","by Echolynk",R.drawable.dummy_blog_img1));

        blogs.add(new Blog("What is Text-to-Speech?","by Echolynk",R.drawable.dummy_blog_img1));*/


        if (recyclerView != null || !blogs.isEmpty()){
            if (blogs.isEmpty()){
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new EmptyAdapter(getApplicationContext(),R.drawable.empty_icon));
            }else {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new BlogAdapter(getApplicationContext(),blogs,this));
            }
        }else {
            Log.d("recyclerView check", "recyclerView null ");
            //check the load fragment
        }

    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onClick(int position, View view) {
        Intent intent = new Intent(HomeBlog.this, BlogView.class);
        intent.putExtra("blogTitle","What is Text-to-Speech?");
        intent.putExtra("blogImage",R.drawable.dummy_blog_img1);
        intent.putExtra("blogAuthor","by Echolynk");
        intent.putExtra("blogPublishDate","Published on June 13, 2024");
        intent.putExtra("description","Imagine if your books could talk! With text-to-speech (TTS) technology, they can. From War and Peace to DMs, the machine can read it all out loud. And it doesn’t even need to sound like a robot. Once just a sci-fi dream, TTS now brings words to life — with the voice of your choice\n" +
                "\u200D\n" +
                "For those supporting Deaf and hard-of-hearing individuals, in education or at work, TTS offers a bridge to conversations with hearing peers by transforming text into spoken conversation that's more engaging and inclusive than ever.");
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(int position, View view) {
        return true;
    }


    public void backOnclick(View view) {

    }


}