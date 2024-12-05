package com.example.echolynk.View.Blog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.echolynk.Model.Blog;
import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.BlogViewAdapter;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class BlogView extends AppCompatActivity implements onClickListener{

    private RecyclerView recyclerView;
    private TextView articleDescription;
    private RelativeLayout errorMassageLayout, blogEditLayout, blogEditImageLayout;
    private EditText blogEditDescription;
    private Button blogEditSaveBTN, blogEditNextBTN, blogEditSkipBTN;
    private ImageView uploadImage;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickLauncher;
    private Dialog dialog;
    private String currentUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    private String blogId;
    private FrameLayout frameLayout2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blog_view);
        int x = 10;
        String s = String.valueOf(x);
        storage = FirebaseStorage.getInstance();
        // pic image

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();

                            // Dynamically create an ImageView
                            ImageView imageView = new ImageView(this);

                            // Set layout parameters for ImageView
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                    FrameLayout.LayoutParams.MATCH_PARENT
                            );
                            imageView.setLayoutParams(params);
                            // Set the image to the ImageView
                            imageView.setImageURI(selectedImageUri);
                            imageView.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_background));
                            // Scale the image to fit the FrameLayout
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            // Clear any previous views inside the FrameLayout and add the new ImageView
                            frameLayout2.removeAllViews();
                            frameLayout2.addView(imageView);
                        }
                    }
                }
        );

        // set as article data
        TextView articleTitle = findViewById(R.id.blog_view_title);
        ImageView articleImage = findViewById(R.id.blog_view_image);
        TextView articleAuthor = findViewById(R.id.blog_view_author);
        TextView articlePublishDate = findViewById(R.id.blog_view_publish_date);
        articleDescription = findViewById(R.id.blog_view_description);

        dialog = new Dialog(BlogView.this);


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

        recyclerView = findViewById(R.id.blog_view_recent_articles_recycle_view);

        List<Blog> blogList = new ArrayList<>();

        for (Blog tempBlog : blogs) {
            blogList.add(new Blog(tempBlog.getTitle(), tempBlog.getImage()));
        }

        if (recyclerView != null) {

            recyclerView.setAdapter(new BlogViewAdapter(getApplicationContext(), blogList, (onClickListener) this));
        }


        articleDescription.setOnLongClickListener(view -> {
            dialog.setContentView(R.layout.popup_edit_blog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bgckground));
            dialog.setCancelable(false);


            errorMassageLayout = dialog.findViewById(R.id.errorMassageLayout);
            blogEditLayout = dialog.findViewById(R.id.blogEditLayout);
            blogEditImageLayout = dialog.findViewById(R.id.blogEditImageLayout);
            blogEditDescription = dialog.findViewById(R.id.blogEditDescription);
            blogEditSaveBTN = dialog.findViewById(R.id.blogEditSaveBTN);
            blogEditNextBTN = dialog.findViewById(R.id.blogEditNextBTN);
            blogEditSkipBTN = dialog.findViewById(R.id.blogEditSkipBTN);
            uploadImage = dialog.findViewById(R.id.uploadImage1);
            frameLayout2 = dialog.findViewById(R.id.frameLayout2);

            if (articleAuthor.getText().toString().equalsIgnoreCase(currentUserName)) {


                uploadImage.setOnClickListener(view1 -> {
                    ImagePicker.with(BlogView.this).cropSquare().compress(512).maxResultSize(512, 512)
                            .createIntent(new Function1<Intent, Unit>() {
                                @Override
                                public Unit invoke(Intent intent) {
                                    imagePickLauncher.launch(intent);
                                    return null;
                                }
                            });
                });


                // click on the next btn
                blogEditNextBTN.setOnClickListener(view1 -> {
                    if (selectedImageUri == null) {
                        defaultAlert("Warning..!", "Please select image before.");
                    } else {
                        blogEditLayout.setVisibility(View.VISIBLE);
                        errorMassageLayout.setVisibility(View.GONE);
                        blogEditImageLayout.setVisibility(View.GONE);

                        blogEditSaveBTN.setOnClickListener(view2 -> {
                            String text = blogEditDescription.getText().toString().trim();
                            if (!text.isEmpty()) {
                                // update storage image
                                StorageReference imagesRef = storage.getReference().child("blog_img/" + selectedImageUri.getLastPathSegment());
                                imagesRef.putFile(selectedImageUri)
                                        .addOnSuccessListener(taskSnapshot -> {
                                            Toast.makeText(BlogView.this, "Image  successfully", Toast.LENGTH_SHORT).show();

                                            imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                                String UpdateUrl = uri.toString();

                                                // update blog firestore
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
                                                                updates.put("imageUrl", UpdateUrl);
                                                                updates.put("status", "updated");

                                                                // Perform the update
                                                                docRef.update(updates)
                                                                        .addOnSuccessListener(aVoid -> {
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

                                            }).addOnFailureListener(e -> {
                                                Toast.makeText(BlogView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                        }).addOnFailureListener(e -> {
                                            Toast.makeText(BlogView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });


                            } else {
                                defaultAlert("Warning..!", "Description cannot be empty. Please enter your idea before proceeding.");
                            }
                        });
                    }
                });

                blogEditSkipBTN.setOnClickListener(view1 -> {
                    blogEditLayout.setVisibility(View.VISIBLE);
                    errorMassageLayout.setVisibility(View.GONE);
                    blogEditImageLayout.setVisibility(View.GONE);

                    blogEditSaveBTN.setOnClickListener(view2 -> {
                        String text = blogEditDescription.getText().toString().trim();
                        if (!text.isEmpty()) {
                            // update blog firestore
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
                                                    .addOnSuccessListener(aVoid -> {
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
                        } else {
                            defaultAlert("Warning..!", "Description cannot be empty. Please enter your idea before proceeding.");
                        }
                    });
                });

            } else {
                blogEditLayout.setVisibility(View.GONE);
                errorMassageLayout.setVisibility(View.VISIBLE);
            }
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            return true;
        });

        dialog.dismiss();

    }

    public static String calculateTime(Timestamp timestamp) {
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

    private void defaultAlert(String title, String massage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(massage)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onClick(int position, View view) {
        Intent intent = new Intent(BlogView.this, HomeBlog.class);
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