package com.example.echolynk.View.Blog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
import com.example.echolynk.View.Adapter.BlogViewAdapter;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class BlogView extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blog_view);
         int x=10;
        String s = String.valueOf(x);

        // set as article data
        TextView articleTitle=findViewById(R.id.blog_view_title);
        ImageView articleImage=findViewById(R.id.blog_view_image);
        TextView articleAuthor=findViewById(R.id.blog_view_author);
        TextView articlePublishDate=findViewById(R.id.blog_view_publish_date);
        TextView articleDescription=findViewById(R.id.blog_view_description);

        int position = getIntent().getIntExtra("position", -1);
        List<Blog> blogs = getIntent().getParcelableArrayListExtra("blogList");
        Log.e("blog image uri 1", blogs.toString());

        // set up blog content
        articleTitle.setText(blogs.get(position).getTitle());
        articleAuthor.setText(blogs.get(position).getAuthor());
        articlePublishDate.setText(calculateTime(blogs.get(position).getTimestamp()));
        articleDescription.setText(blogs.get(position).getDescription());
        // handle image uri
        String imageUrl = blogs.get(position).getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.d("blog image uri", imageUrl);
            Glide.with(BlogView.this)
                    .load(imageUrl) // Load the image URL
                    .placeholder(R.drawable.conversation_default) // Placeholder image
                    .error(R.drawable.conversation_default) // Error image in case loading fails
                    .into(articleImage); // Set the loaded image into the ImageView

        } else {
            Log.d("blog image uri", "null image");
           articleImage.setImageResource(R.drawable.conversation_default);
        }


      // set as   recent articles

       recyclerView=findViewById(R.id.blog_view_recent_articles_recycle_view);

        List<Blog> blogList=new ArrayList<>();

        for (Blog tempBlog:blogs) {
            blogList.add(new Blog(tempBlog.getTitle(),tempBlog.getImage()));
        }

        if (recyclerView!=null) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                    1, // span count
                    StaggeredGridLayoutManager.HORIZONTAL // orientation
            );
            recyclerView.setAdapter(new BlogViewAdapter(getApplicationContext(),blogList));
        }

    }

    private String calculateTime(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

        // Set the desired time zone (for example, UTC+5:30)
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+5:30"));

        // Format the date
        return sdf.format(date);
    }

    public void backOnclick(View view) {
        Intent intent = new Intent(BlogView.this, HomeBlog.class);
        startActivity(intent);
    }
}