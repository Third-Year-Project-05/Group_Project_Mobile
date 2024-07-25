package com.example.echolynk.View.SignInSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echolynk.R;
import com.example.echolynk.View.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText editText,otp;
    private Button btnSubmit,btnSubmitOTP;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        editText=findViewById(R.id.etEmail);
        btnSubmit=findViewById(R.id.btnSubmit);
        otp=findViewById(R.id.verifyOTP);
        btnSubmitOTP=findViewById(R.id.otpSubmit);

        mAuth=FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(view -> {
            String etText=editText.getText().toString().trim();

            if (!etText.isEmpty()) {
                if (Patterns.EMAIL_ADDRESS.matcher(etText).matches()){

                    resetPassword(etText);

                } else if (Patterns.PHONE.matcher(etText).matches()) {


                }else {
                    editText.setError("Email or phone number not match.");
                }
            }else {
                editText.setError("Please enter email or phone number.");
            }
        });
    }

    private void resetPassword(String etText) {
        mAuth.sendPasswordResetEmail(etText).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ForgotPassword.this,"Reset password sent your registered email.",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ForgotPassword.this, SignIn.class);
                startActivity(intent);
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPassword.this,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}