package com.example.echolynk.View.Profile;

import static com.example.echolynk.View.Blog.BlogView.calculateTime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;

import java.util.List;

public class StaticView extends AppCompatActivity {

    private String blogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_static_view);


        TextView articleTitle = findViewById(R.id.blog_view_title);
        ImageView articleImage = findViewById(R.id.blog_view_image);
        TextView articleAuthor = findViewById(R.id.blog_view_author);
        TextView articlePublishDate = findViewById(R.id.blog_view_publish_date);
        TextView articleDescription = findViewById(R.id.blog_view_description);

        int position = getIntent().getIntExtra("position", -1);
        List<Blog> blogs = getIntent().getParcelableArrayListExtra("blogList");
        Log.e("blog image uri 1", blogs.toString());

        // set up blog content
        blogId = blogs.get(position).getId();
        articleTitle.setText(blogs.get(position).getTitle());
        articleAuthor.setText(blogs.get(position).getAuthor());
        articlePublishDate.setText(calculateTime(blogs.get(position).getTimestamp()));
        articleDescription.setText(blogs.get(position).getDescription());

        // handle image uri
        String imageUrl = blogs.get(position).getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.d("blog image uri", imageUrl);
            Glide.with(StaticView.this)
                    .load(imageUrl) // Load the image URL
                    .placeholder(R.drawable.conversation_default) // Placeholder image
                    .error(R.drawable.conversation_default) // Error image in case loading fails
                    .into(articleImage); // Set the loaded image into the ImageView

        } else {
            Log.d("blog image uri", "null image");
            articleImage.setImageResource(R.drawable.conversation_default);
        }
    }

    public void backOnclick(View view) {
        Intent intent = new Intent(StaticView.this, StatisticsActivity.class);
        startActivity(intent);
    }
}