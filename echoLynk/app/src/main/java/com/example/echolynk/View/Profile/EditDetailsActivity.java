package com.example.echolynk.View.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.echolynk.R;
import com.example.echolynk.View.MainActivity;

public class EditDetailsActivity extends AppCompatActivity {

    TextView cancelBtn, doneBtn, changePassword;
    EditText userName, userEmail, userDOB;


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
                String name = userName.getText().toString().trim();
                String email = userEmail.getText().toString().trim();
                String dob = userDOB.getText().toString().trim();

                if(name.isEmpty()){
                    userName.setError("User name required");
                    return;
                }
                if (email.isEmpty()){
                    userEmail.setError("User Email required");
                    return;
                }
                if(!isValiedEmail(email)){
                    userEmail.setError("Enter valied Email");
                    return;
                }

            }
        });


    }

    private boolean isValiedEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}