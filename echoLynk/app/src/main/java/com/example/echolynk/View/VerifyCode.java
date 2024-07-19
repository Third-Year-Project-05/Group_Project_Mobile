package com.example.echolynk.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echolynk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyCode extends AppCompatActivity {

    // phone number eka validate karanna country code eken

    EditText digitOne,digitTwo,digitThree,digitFour,digitFive,digitSix;

    private FirebaseAuth mAuth;
    private String verificationId;
    private String email;
    private String password;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_code);



        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        password=getIntent().getStringExtra("password");
        String phoneNumber=getIntent().getStringExtra("telNumber");


        mAuth=FirebaseAuth.getInstance();
        digitOne=findViewById(R.id.verifyOneDigit);
        digitTwo=findViewById(R.id.verifyTowDigit);
        digitThree=findViewById(R.id.verifyThreeDigit);
        digitFour=findViewById(R.id.verifyFourDigit);
        digitFive=findViewById(R.id.verifyFiveDigit);
        digitSix=findViewById(R.id.verifySixDigit);



        sentOTP(phoneNumber,mAuth);



    }


    private void sentOTP(String phoneNumber, FirebaseAuth mAuth) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            final String otp = phoneAuthCredential.getSmsCode();
            assert otp != null;
            String[] splitOtp=new String[otp.length()];

            // Convert each character back to an String
            for (int i = 0; i < otp.length(); i++) {
                String digit = String.valueOf(Character.getNumericValue(otp.charAt(i)));
                splitOtp[i]=digit;
            }

            // set up the verification verify boxes.
            digitOne.setText(splitOtp[0]);
            digitTwo.setText(splitOtp[1]);
            digitThree.setText(splitOtp[2]);
            digitFour.setText(splitOtp[3]);
            digitFive.setText(splitOtp[4]);
            digitSix.setText(splitOtp[5]);

            verifyOTP(otp);



        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

    };

    private void verifyOTP(String otp) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,otp);

        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                   // new interface ekata yanne methanin danna one

                    boolean result= emailPasswdValidationSignIn();

                    if (result) {
                        Intent intent=new Intent(VerifyCode.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent=new Intent(VerifyCode.this,SignUp.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(VerifyCode.this, "SignUp failed "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Intent intent=new Intent(VerifyCode.this,SignUp.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(VerifyCode.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean emailPasswdValidationSignIn() {

        final boolean[] value = new boolean[1];
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    value[0] =true;
                }else {
                    value[0] =false;
                }
            }
        });

        return value[0];
    }

}