package com.example.echolynk.View.SignInSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ForgotPassword extends AppCompatActivity {

    private EditText editText,otp;
    private Button btnSubmit,btnSubmitOTP;
    private FirebaseAuth mAuth;

    private String verificationId;
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
                    sendOTP(etText);
                }else {
                    editText.setError("Email or phone number not match.");
                }
            }else {
                editText.setError("Please enter email or phone number.");
            }
        });


        btnSubmitOTP.setOnClickListener(view -> {
            verifyOtp(otp.getText().toString().trim());
        });

    }

    private void verifyOtp(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // OTP verification successful, set OTP as new password
                mAuth.getCurrentUser().updatePassword(otp).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPassword.this, SignIn.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ForgotPassword.this, "Failed to reset password", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(ForgotPassword.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOTP(String etText) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                etText,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        // Automatically retrieve the code
                        otp.setText(phoneAuthCredential.getSmsCode());
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(ForgotPassword.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        editText.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.GONE);
                        otp.setVisibility(View.VISIBLE);
                        btnSubmitOTP.setVisibility(View.VISIBLE);
                    }
                }
        );
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