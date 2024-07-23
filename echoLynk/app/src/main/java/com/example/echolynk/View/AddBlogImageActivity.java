package com.example.echolynk.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.echolynk.R;

public class AddBlogImageActivity extends AppCompatActivity {

    ImageView backBtn;
    AppCompatButton publishBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_blog_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backBtn = findViewById(R.id.add_to_blog_image_back_btn);
        publishBtn = findViewById(R.id.publish_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backBtn = new Intent(AddBlogImageActivity.this, AddBlogMainActivity.class);
                startActivity(backBtn);
            }
        });

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent publish = new Intent(AddBlogImageActivity.this, MainActivity.class);
                publish.putExtra("load_fragment", "profile");
                startActivity(publish);
            }
        });

    }
}