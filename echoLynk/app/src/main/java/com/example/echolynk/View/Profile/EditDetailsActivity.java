package com.example.echolynk.View.Profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.echolynk.Model.UserModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.AndroidUtils;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.View.MainActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class EditDetailsActivity extends AppCompatActivity {

    TextView cancelBtn, doneBtn, changePassword,editImageBtn;
    EditText userName, userEmail, userDOB;
    UserModel updateUser;
    String newName,newEmail,newDob;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    ImageView profilePicture;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cancelBtn = findViewById(R.id.edit_details_cancel_btn);
        doneBtn = findViewById(R.id.edit_details_done_btn);
        userName = findViewById(R.id.edit_details_name);
        userEmail = findViewById(R.id.edit_details_email);
        userDOB = findViewById(R.id.edit_details_dob);
        changePassword = findViewById(R.id.change_password_btn);
        editImageBtn = findViewById(R.id.edit_image_Button);
        profilePicture = findViewById(R.id.profile_pic);

        getUserData();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(EditDetailsActivity.this, MainActivity.class);
                cancel.putExtra("load_fragment", "profile");
                startActivity(cancel);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();

                if(selectedImageUri != null){
                    FirebaseUtils.getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    updateDatabase();

                                    Intent intent = new Intent(EditDetailsActivity.this, MainActivity.class);
                                    intent.putExtra("load_fragment", "profile");
                                    startActivity(intent);
                                }
                            });
                }
                else {
                    updateDatabase();
                    Intent intent = new Intent(EditDetailsActivity.this, MainActivity.class);
                    intent.putExtra("load_fragment", "profile");
                    startActivity(intent);
                }
            }
        });

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result ->{
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data != null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            AndroidUtils.setProfilePic(EditDetailsActivity.this, selectedImageUri,profilePicture);
                        }
                    }
                }
                );

        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(EditDetailsActivity.this).cropSquare().compress(512).maxResultSize(512,512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLauncher.launch(intent);
                                return null;
                            }
                        });
            }
        });
    }

    private void getUserData(){

        FirebaseUtils.getCurrentProfilePicStorageRef().getDownloadUrl()
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Uri uri = task.getResult();
                                AndroidUtils.setProfilePic(EditDetailsActivity.this,uri,profilePicture);
                            }
                        });

        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                updateUser = task.getResult().toObject(UserModel.class);
                userName.setText(updateUser.getUserName());
                userEmail.setText(updateUser.getEmail());
            }
        });
    }

    private void updateDatabase() {

        updateUser.setUserName(newName);
        //enter all the update details here

        FirebaseUtils.currentUserDetails().set(updateUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    AndroidUtils.showTost(EditDetailsActivity.this, "Successfully Updated");
                }
                else {
                    AndroidUtils.showTost(EditDetailsActivity.this, "Update failed");
                }
            }
        });

    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void validateInputs(){
        newName = userName.getText().toString().trim();
        newEmail = userEmail.getText().toString().trim();
        newDob = userDOB.getText().toString().trim();

        if(newName.isEmpty()){
            userName.setError("User name required");
            return;
        }
        if (newEmail.isEmpty()){
            userEmail.setError("User Email required");
            return;
        }
        if(!isValidEmail(newEmail)){
            userEmail.setError("Enter valid Email");
            return;
        }
    }
}