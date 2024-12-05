package com.example.echolynk.View.Blog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.BlogAdapter;
import com.example.echolynk.View.Adapter.EmptyAdapter;
import com.example.echolynk.View.MainActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeBlog extends AppCompatActivity implements onClickListener {

    private RecyclerView recyclerView;
    private FirebaseFirestore db ;
    private List<Blog> blogs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_blog);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.blog_recycle_view);

        db.collection("blogs")
                .whereEqualTo("status", "approved")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            blogs.add(
                                    new Blog(
                                            document.getId(),
                                            document.getString("title"),
                                            document.getString("author"),
                                            document.getString("content"),
                                            document.getTimestamp("timestamp"),
                                            document.getString("imageUrl")
                                    )
                            );

                        }

                        if (recyclerView != null || !blogs.isEmpty()) {
                            if (blogs.isEmpty()) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                                recyclerView.setAdapter(new EmptyAdapter(getApplicationContext(), R.drawable.empty_icon));
                            } else {
                                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                                recyclerView.setAdapter(new BlogAdapter(getApplicationContext(), blogs, this));
                            }
                        } else {
                            Log.d("recyclerView check", "recyclerView null ");
                            //check the load fragment
                        }

                    } else {
                        Toast.makeText(HomeBlog.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onClick(int position, View view) {
        Intent intent = new Intent(HomeBlog.this, BlogView.class);
        intent.putExtra("position",position);
        intent.putParcelableArrayListExtra("blogList", new ArrayList<>(blogs));
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(int position, View view) {
        return true;
    }


    public void backOnclick(View view) {
        Intent intent = new Intent(HomeBlog.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickDifficultWord(int position, View view) {

    }


}