package com.example.echolynk.View.SignInSignUp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echolynk.Model.UserModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.View.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


public class VerifyCode extends AppCompatActivity {

    // phone number eka validate karanna country code eken

    private EditText digitOne, digitTwo, digitThree, digitFour, digitFive, digitSix;
    private Button verifyBUtton;
    private TextView resendOTP;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String email;
    private String password;
    private String phoneNumber;
    private String name;
    final Handler handler = new Handler();
    Timer timer = new Timer();
    UserModel userModel;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_code);


        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        phoneNumber = getIntent().getStringExtra("telNumber");

        verifyBUtton = findViewById(R.id.verify_button);

        mAuth = FirebaseAuth.getInstance();
        digitOne = findViewById(R.id.verifyOneDigit);
        digitTwo = findViewById(R.id.verifyTowDigit);
        digitThree = findViewById(R.id.verifyThreeDigit);
        digitFour = findViewById(R.id.verifyFourDigit);
        digitFive = findViewById(R.id.verifyFiveDigit);
        digitSix = findViewById(R.id.verifySixDigit);
        resendOTP=findViewById(R.id.verification_resend_code);

        setupEditTextNavigation(digitOne, digitTwo);
        setupEditTextNavigation(digitTwo, digitThree);
        setupEditTextNavigation(digitThree, digitFour);
        setupEditTextNavigation(digitFour, digitFive);
        setupEditTextNavigation(digitFive, digitSix);
        setupEditTextNavigation(digitSix, null);

        if (email.equalsIgnoreCase("null")) {
            sentOTP(phoneNumber, mAuth);
        }

        verifyBUtton.setOnClickListener(view -> {
            String otp = digitOne.getText().toString() + digitTwo.getText().toString() + digitThree.getText().toString() + digitFour.getText().toString() + digitFive.getText().toString() + digitSix.getText().toString();
            Log.d(TAG, "SMS : " + otp);
            verifyOTP(otp.trim());
        });

        resendOTP.setOnClickListener(view -> {
            resendOTP(phoneNumber);
        });


    }

    private void resendOTP(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(resendToken)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    VerifyCode.this.verificationId = verificationId;
                    resendToken = token;
                    Toast.makeText(VerifyCode.this, "OTP sent", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

                }
            };


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

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationId = s;
        }


        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            final String code = phoneAuthCredential.getSmsCode();
            String[] splitOtp = new String[code.length()];

            // Convert each character back to an String
            for (int i = 0; i < code.length(); i++) {
                String digit = String.valueOf(Character.getNumericValue(code.charAt(i)));
                splitOtp[i] = digit;
            }

            // set up the verification verify boxes.
            digitOne.setText(splitOtp[0]);
            Log.d("1", splitOtp[0]);
            digitTwo.setText(splitOtp[1]);
            Log.d("2", splitOtp[1]);
            digitThree.setText(splitOtp[2]);
            Log.d("3", splitOtp[2]);
            digitFour.setText(splitOtp[3]);
            Log.d("4", splitOtp[3]);
            digitFive.setText(splitOtp[4]);
            Log.d("5", splitOtp[4]);
            digitSix.setText(splitOtp[5]);
            Log.d("6", splitOtp[5]);


            signInWithCredential(phoneAuthCredential);

            verifyOTP(code);


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e.getMessage().contains("We have blocked all requests from this device")) {
                Toast.makeText(VerifyCode.this, "Request blocked due to unusual activity. Try again later.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(VerifyCode.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    };

    private void verifyOTP(String otp) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
            signInWithCredential(credential);
        } catch (Exception e) {
            Log.d("credintional error", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(VerifyCode.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                   // userModel = new UserModel(name.toLowerCase(), phoneNumber, email, Timestamp.now(), FirebaseUtils.currentUserId());
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUtils.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(VerifyCode.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(VerifyCode.this, "SignUp failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VerifyCode.this, SignUp.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });

                } else {
                    Toast.makeText(VerifyCode.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(VerifyCode.this, SignUp.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

    }


    private void setupEditTextNavigation(final EditText currentEditText, final EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    char inputChar = s.charAt(0);
                    if (Character.isDigit(inputChar)) {
                        if (nextEditText != null) {
                            nextEditText.requestFocus();
                        }
                    } else {
                        Toast.makeText(VerifyCode.this, "Please enter a digit (0-9)", Toast.LENGTH_SHORT).show();
                        s.clear();
                    }
                }
            }
        });

    }
}