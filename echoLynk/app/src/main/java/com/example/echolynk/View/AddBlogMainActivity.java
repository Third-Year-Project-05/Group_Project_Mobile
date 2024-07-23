package com.example.echolynk.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.echolynk.R;

public class AddBlogMainActivity extends AppCompatActivity {

    ImageView backbtn;
    EditText blogTitle,blogDescription;
    AppCompatButton saveBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_blog_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backbtn = findViewById(R.id.add_to_blog_back_btn);
        saveBtn = findViewById(R.id.change_voice_button);
        blogTitle = findViewById(R.id.blog_topic);
        blogDescription = findViewById(R.id.blog_description);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backBtn = new Intent(AddBlogMainActivity.this, MainActivity.class);
                backBtn.putExtra("load_fragment", "profile");
                startActivity(backBtn);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = blogTitle.getText().toString().trim();
                String description = blogDescription.getText().toString().trim();

                if (title == null || title.isEmpty()){
                    blogTitle.setError("Title is required");
                    return;
                }
                if(description == null || description.isEmpty()){
                    blogDescription.setError("Description is required");
                    return;
                }

                Intent saveVoice = new Intent(AddBlogMainActivity.this, AddBlogImageActivity.class);
                saveVoice.putExtra("title",title);
                saveVoice.putExtra("description", description);
                startActivity(saveVoice);
            }
        });


    }
}