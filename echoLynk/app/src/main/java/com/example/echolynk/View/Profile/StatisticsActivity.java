package com.example.echolynk.View.Profile;

import static com.example.echolynk.Utils.FirebaseUtils.currentUserEmail;
import static com.example.echolynk.Utils.FirebaseUtils.currentUserId;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.EmptyAdapter;
import com.example.echolynk.View.Adapter.MyBlogAdapter;
import com.example.echolynk.View.MainActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatisticsActivity extends AppCompatActivity implements onClickListener {

    ImageView backBtn;

    private RecyclerView recyclerView;
    private FirebaseFirestore db ;
    private List<Blog> blogs=new ArrayList<>();
    private String userId=currentUserId();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);


        backBtn = findViewById(R.id.statistics_back_btn);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.my_blogs);


        db.collection("blogs")
                .whereEqualTo("status","approved").get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (Objects.equals(document.getString("email"), currentUserEmail())){
                                        Log.d("blogs my 1", blogs.toString());
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
                                    }else {
                                        Log.d("blogs my 2", blogs.toString());
                                    }
                                }

                                if (recyclerView != null || !blogs.isEmpty()) {
                                    Log.d("blogs my", currentUserEmail());
                                    if (blogs.isEmpty()) {
                                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                                        recyclerView.setAdapter(new EmptyAdapter(getApplicationContext(), R.drawable.empty_icon));
                                    } else {
                                        try {
                                            recyclerView.setLayoutManager(new LinearLayoutManager(this));
                                            recyclerView.setAdapter(new MyBlogAdapter((Context) this, blogs, this));
                                        }catch (ClassCastException e){
                                            Log.d("ClassCastException", e.getMessage());
                                        }
                                    }
                                } else {
                                    Log.d("recyclerView check", "recyclerView null ");
                                    //check the load fragment
                                }
                            }
                        }).addOnFailureListener(e -> {
                    Toast.makeText(StatisticsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backBtn = new Intent(StatisticsActivity.this, MainActivity.class);
                backBtn.putExtra("load_fragment", "profile");
                startActivity(backBtn);
            }
        });
    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onClick(int position, View view) {
        Intent intent = new Intent(StatisticsActivity.this, StaticView.class);
        intent.putExtra("position",position);
        intent.putParcelableArrayListExtra("blogList", new ArrayList<>(blogs));
        startActivity(intent);
    }

    @Override
    public void onClickDifficultWord(int position, View view) {

    }

    @Override
    public boolean onLongClick(int position, View view) {
        return false;
    }
}