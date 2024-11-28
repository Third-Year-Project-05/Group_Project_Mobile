package com.example.echolynk.View.Blog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
import com.example.echolynk.View.Adapter.BlogViewAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class BlogView extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView articleDescription;
    RelativeLayout errorMassageLayout,blogEditLayout;
    EditText blogEditDescription;
    Button blogEditSaveBTN;
    private Dialog dialog;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    String currentUserName= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    String blogId;

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
        articleDescription=findViewById(R.id.blog_view_description);

        dialog=new Dialog(BlogView.this);



        int position = getIntent().getIntExtra("position", -1);
        List<Blog> blogs = getIntent().getParcelableArrayListExtra("blogList");
        Log.e("blog image uri 1", blogs.toString());

        // set up blog content
        blogId=blogs.get(position).getId();
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


        articleDescription.setOnLongClickListener(view->{
            dialog.setContentView(R.layout.popup_edit_blog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bgckground));
            dialog.setCancelable(false);


            errorMassageLayout=dialog.findViewById(R.id.errorMassageLayout);
            blogEditLayout=dialog.findViewById(R.id.blogEditLayout);
            blogEditDescription=dialog.findViewById(R.id.blogEditDescription);
            blogEditSaveBTN=dialog.findViewById(R.id.blogEditSaveBTN);

            if (articleAuthor.getText().toString().equalsIgnoreCase(currentUserName)) {
                blogEditLayout.setVisibility(View.VISIBLE);
                errorMassageLayout.setVisibility(View.GONE);

                blogEditSaveBTN.setOnClickListener(view1 -> {
                    String text = blogEditDescription.getText().toString().trim();
                    Log.d("discription", text);

                    if (!text.isEmpty()) {
                        db.collection("blogs")
                                .whereEqualTo("id", blogId)  // Replace 'blogId' with the actual ID
                                .get()
                                .addOnSuccessListener(querySnapshot -> {
                                    if (!querySnapshot.isEmpty()) {
                                        // Get the document reference (assuming only one document matches the "id")
                                        DocumentReference docRef = querySnapshot.getDocuments().get(0).getReference();

                                        // Create a map with the updated fields
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("content", text);

                                        // Perform the update
                                        docRef.update(updates)
                                                .addOnSuccessListener(aVoid ->{
                                                    Toast.makeText(this, "Blog updated successfully!", Toast.LENGTH_SHORT).show();
                                                    backOnclick(view);
                                                }).addOnFailureListener(e ->
                                                        Toast.makeText(this, "Failed to update blog: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                    } else {
                                        Toast.makeText(this, "No document found with the specified ID.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Warning..!")
                                .setMessage("Description cannot be empty. Please enter your idea before proceeding.")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }
                });
            }else {
                blogEditLayout.setVisibility(View.GONE);
                errorMassageLayout.setVisibility(View.VISIBLE);
            }
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            return true;
        });

        dialog.dismiss();

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