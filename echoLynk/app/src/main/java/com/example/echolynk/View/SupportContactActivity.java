package com.example.echolynk.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.echolynk.R;

public class SupportContactActivity extends AppCompatActivity {

    ImageView backBtn;
    EditText supportContactName,supportContactEmail,supportContactNumber,supportContactMessage;
    Button sendMessageBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_support_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        supportContactName = findViewById(R.id.support_contact_name);
        supportContactEmail = findViewById(R.id.support_contact_email);
        supportContactNumber = findViewById(R.id.support_contact_number);
        supportContactMessage = findViewById(R.id.support_contact_message);

        backBtn = findViewById(R.id.support_contact_back_btn);
        sendMessageBtn = findViewById(R.id.send_message_btn);
        supportContactMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    findViewById(R.id.message_box).setPadding(0,0,0,500);
                }
                else {
                    findViewById(R.id.message_box).setPadding(0,0,0,0);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backBtn = new Intent(SupportContactActivity.this, MainActivity.class);
                backBtn.putExtra("load_fragment", "profile");
                startActivity(backBtn);
            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = supportContactName.getText().toString().trim();
                String email = supportContactEmail.getText().toString().trim();
                String number = supportContactNumber.getText().toString().trim();
                String message = supportContactMessage.getText().toString().trim();

                if(name.isEmpty()){
                    supportContactName.setError("Name required");
                    return;
                }
                if (email.isEmpty()){
                    supportContactEmail.setError("Email required");
                    return;
                }
                if(!isValidEmail(email)){
                    supportContactEmail.setError("Enter valid Email");
                    return;
                }
                if(number.isEmpty()){
                    supportContactNumber.setError("Number is required");
                    return;
                }
                if(!isValidNumber(number)){
                    supportContactNumber.setError("Enter valid Mobile Number");
                    return;
                }
                if(message.isEmpty()){
                    supportContactMessage.setError("Message is required");
                    return;
                }

                Intent sendMessage = new Intent(SupportContactActivity.this, MainActivity.class);
                sendMessage.putExtra("load_fragment", "profile");
                startActivity(sendMessage);
            }
        });


    }

    private boolean isValidNumber(String number) {
        return Patterns.PHONE.matcher(number).matches();
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}