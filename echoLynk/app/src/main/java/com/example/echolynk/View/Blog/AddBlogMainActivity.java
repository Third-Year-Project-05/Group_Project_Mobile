package com.example.echolynk.View.Blog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.echolynk.R;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.View.MainActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class AddBlogMainActivity extends AppCompatActivity {

    private ImageView backbtn, uploadImage;
    private EditText blogTitle, blogDescription;
    private TextView blogTopicTextView, descriptionTextView;
    private AppCompatButton saveBtn, publish_button;
    private ActivityResultLauncher<Intent> imagePickLauncher;
    private FrameLayout frameLayout2;
    private FirebaseAuth mAuth;
    private String title = null;
    private String description = null;
    private Uri selectedImageUri;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseFirestore db;

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

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        db = FirebaseFirestore.getInstance();

        backbtn = findViewById(R.id.add_to_blog_back_btn);
        saveBtn = findViewById(R.id.change_voice_button);
        blogTitle = findViewById(R.id.blog_topic);
        blogDescription = findViewById(R.id.blog_description);
        mAuth = FirebaseAuth.getInstance();
        uploadImage = findViewById(R.id.uploadImage);
        publish_button = findViewById(R.id.publish_button);
        frameLayout2 = findViewById(R.id.frameLayout2);
        blogTopicTextView = findViewById(R.id.blogTopicTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

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

                title = blogTitle.getText().toString().trim();
                description = blogDescription.getText().toString().trim();

                if (title == null || title.isEmpty()) {
                    blogTitle.setError("Title is required");
                    return;
                } else if (description == null || description.isEmpty()) {
                    blogDescription.setError("Description is required");
                    return;
                } else {

                    blogTopicTextView.setVisibility(View.GONE);
                    blogTitle.setVisibility(View.GONE);
                    blogDescription.setVisibility(View.GONE);
                    descriptionTextView.setVisibility(View.GONE);
                    saveBtn.setVisibility(View.GONE);

                    frameLayout2.setVisibility(View.VISIBLE);
                    publish_button.setVisibility(View.VISIBLE);


                }


            }

        });

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


        uploadImage.setOnClickListener(view -> {
            ImagePicker.with(AddBlogMainActivity.this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });

        publish_button.setOnClickListener(view -> {

            FirebaseUser currentUser = mAuth.getCurrentUser();


            Uri file = selectedImageUri;

            StorageReference imagesRef = storageRef.child("blog_img/" + file.getLastPathSegment());

            imagesRef.putFile(file)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Handle successful uploads
                        Toast.makeText(AddBlogMainActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                        // Get the download URL after successful upload
                        imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            // You can use this URL to store it in the Firestore or Realtime Database
                            Log.d("FirebaseStorage", "Download URL: " + imageUrl);



                            Map<String, Object> authorAllData = new HashMap<>();
                            assert currentUser != null;
                            authorAllData.put("email", currentUser.getEmail());
                            authorAllData.put("id", currentUser.getUid());
                            authorAllData.put("timestamp", new com.google.firebase.Timestamp(new java.util.Date()));


                            Map<String, Object> newData = new HashMap<>();
                            newData.put("author", currentUser.getDisplayName());
                            newData.put("content", description);
                            newData.put("email", currentUser.getEmail());
                            newData.put("status", "dismissed");
                            newData.put("timestamp", new com.google.firebase.Timestamp(new java.util.Date()));
                            newData.put("title", title);
                            newData.put("authorAll", authorAllData);
                            newData.put("imageUrl", imageUrl);


                            db.collection("blogs") // Specify your collection name
                                    .add(newData) // Add data with a generated document ID
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(AddBlogMainActivity.this, "Blog Added", Toast.LENGTH_SHORT).show();

                                        Intent saveVoice = new Intent(AddBlogMainActivity.this, MainActivity.class);
                                        startActivity(saveVoice);

                                    })
                                    .addOnFailureListener(e -> {
                                        System.err.println("Error adding document: " + e);
                                        Toast.makeText(AddBlogMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        });

                    })
                    .addOnFailureListener(e -> {
                        // Handle unsuccessful uploads
                        Toast.makeText(AddBlogMainActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}