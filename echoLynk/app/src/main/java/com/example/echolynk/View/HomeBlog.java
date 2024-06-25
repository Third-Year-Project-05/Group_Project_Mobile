package com.example.echolynk.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
import com.example.echolynk.View.Adapter.BlogAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeBlog extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_blog);

        recyclerView=findViewById(R.id.blog_recycle_view);

        List<Blog> blogs=new ArrayList<>();
        blogs.add(new Blog("What is Text-to-Speech?","by Echolynk",R.drawable.dummy_blog_img1,"Imagine if your books could talk! With text-to-speech (TTS) technology, they can. From War and Peace to DMs, the machine can read it all out loud. And it doesn’t even need to sound like a robot. Once just a sci-fi dream, TTS now brings words to life — with the voice of your choice\n" +
                "\u200D\n" +
                "For those supporting Deaf and hard-of-hearing individuals, in education or at work, TTS offers a bridge to conversations with hearing peers by transforming text into spoken conversation that's more engaging and inclusive than ever."));

        blogs.add(new Blog("hatti pakaya","by Echolynk",R.drawable.dummy_blog_img1,"Imagine if your books could talk! With text-to-speech (TTS) technology, they can. From War and Peace to DMs, the machine can read it all out loud. And it doesn’t even need to sound like a robot. Once just a sci-fi dream, TTS now brings words to life — with the voice of your choice\n" +
                "\u200D\n" +
                "For those supporting Deaf and hard-of-hearing individuals, in education or at work, TTS offers a bridge to conversations with hearing peers by transforming text into spoken conversation that's more engaging and inclusive than ever."));

        blogs.add(new Blog("What is Text-to-Speech?","by Echolynk",R.drawable.dummy_blog_img1,"Imagine if your books could talk! With text-to-speech (TTS) technology, they can. From War and Peace to DMs, the machine can read it all out loud. And it doesn’t even need to sound like a robot. Once just a sci-fi dream, TTS now brings words to life — with the voice of your choice\n" +
                "\u200D\n" +
                "For those supporting Deaf and hard-of-hearing individuals, in education or at work, TTS offers a bridge to conversations with hearing peers by transforming text into spoken conversation that's more engaging and inclusive than ever."));

        blogs.add(new Blog("What is Text-to-Speech?","by Echolynk",R.drawable.dummy_blog_img1,"Imagine if your books could talk! With text-to-speech (TTS) technology, they can. From War and Peace to DMs, the machine can read it all out loud. And it doesn’t even need to sound like a robot. Once just a sci-fi dream, TTS now brings words to life — with the voice of your choice\n" +
                "\u200D\n" +
                "For those supporting Deaf and hard-of-hearing individuals, in education or at work, TTS offers a bridge to conversations with hearing peers by transforming text into spoken conversation that's more engaging and inclusive than ever."));

        blogs.add(new Blog("What is Text-to-Speech?","by Echolynk",R.drawable.dummy_blog_img1,"Read More >"));


        if (recyclerView != null){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new BlogAdapter(getApplicationContext(),blogs));
        }else {
            Log.d("recyclerView check", "recyclerView null ");
        }


    }

    public void backOnclick(View view) {
    }
}