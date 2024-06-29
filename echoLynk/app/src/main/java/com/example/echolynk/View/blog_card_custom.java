package com.example.echolynk.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.echolynk.R;

public class blog_card_custom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blog_card_custom);

        /*ListView listView=new ArrayAdapter<>()*/


        /*TextView textView = findViewById(R.id.blog_description);

        textView.setClickable(true);
        textView.setFocusable(true);

        textView.setOnClickListener(view -> {
            Log.d("blog read more 1", "readBlog: fire on click function");
            readBlog(view);
        });*/

    }




    public void readBlog(View view) {
        Log.d("blog read more 2", "readBlog: fire on click function");
        Intent intent=new Intent(blog_card_custom.this,BlogView.class);
        startActivity(intent);
    }
}