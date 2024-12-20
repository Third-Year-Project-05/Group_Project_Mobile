package com.example.echolynk.View.SignInSignUp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echolynk.Model.UserModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.View.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class SignIn extends AppCompatActivity {

    private EditText userEMail,userPassword;
    private TextView forgotPassword;
    private Button signInButton,verifyOTP,sendOTPButton;
    private ImageButton google,facebook;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private UserModel userModel;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        mAuth=FirebaseAuth.getInstance();
        userEMail=findViewById(R.id.sign_in_email);
        userPassword=findViewById(R.id.sign_in_password);
        signInButton=findViewById(R.id.signIn_button);
        facebook=findViewById(R.id.sign_in_facebook);
        google=findViewById(R.id.sign_in_google);
        forgotPassword=findViewById(R.id.forgotPassword);
        verifyOTP=findViewById(R.id.signIn_verifyOTP);
        sendOTPButton=findViewById(R.id.send_otp);




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        userEMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phoneNumber=editable.toString();
                if (Patterns.PHONE.matcher(phoneNumber).matches()){
                    sendOTPButton.setVisibility(View.VISIBLE);
                    signInButton.setVisibility(View.GONE);
                }else {
                    sendOTPButton.setVisibility(View.GONE);
                    signInButton.setVisibility(View.VISIBLE);
                }
            }
        });

        sendOTPButton.setOnClickListener(view -> {
            Log.d(TAG, "onCreate send otp : userEMail.getText().toString().trim()");

            sentOTP(userEMail.getText().toString().trim(),mAuth);
            sendOTPButton.setVisibility(View.GONE);
            verifyOTP.setVisibility(View.VISIBLE);
        });

        signInButton.setOnClickListener(view->{

            String email=userEMail.getText().toString().trim();
            String pass=userPassword.getText().toString();

            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                if (!pass.isEmpty()){
                    mAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //set sheard preference
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isLogged", true)
                                    .apply();

                            Intent intent=new Intent(SignIn.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            Toast.makeText(SignIn.this, "Login successful ", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignIn.this, "Login failed ", Toast.LENGTH_SHORT).show();
                            userPassword.findFocus();
                        }
                    });

                }else {
                    userPassword.setError("Password can't be empty.");
                }
            }else if (email.isEmpty()){
                userEMail.setError("Email can't be empty.");
            }else if (Patterns.PHONE.matcher(email).matches()){
                //sentOTP(email,mAuth);
            }else {
                userEMail.setError("Please enter valid email or phone number.");
            }
        });

        google.setOnClickListener(view -> {
            signIn();
        });

        facebook.setOnClickListener(view -> {

        });

        forgotPassword.setOnClickListener(view -> {
            Intent intent=new Intent(SignIn.this,ForgotPassword.class);
            startActivity(intent);
        });

        verifyOTP.setOnClickListener(view -> {
            verifyOTP(userPassword.getText().toString().trim());
        });

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

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            userPassword.setText(code);
            signInButton.setVisibility(View.GONE);
            verifyOTP.setVisibility(View.VISIBLE);

            // automatically verify OTP
            verifyOTP(code);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e.getMessage().contains("We have blocked all requests from this device")) {
                Toast.makeText(SignIn.this, "Request blocked due to unusual activity. Try again later.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(SignIn.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private void verifyOTP(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception e) {
            Toast.makeText(SignIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //set sheard preference
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit()
                            .putBoolean("isLogged", true)
                            .apply();

                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(SignIn.this, "SignUp failed "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignIn.this, "SignIn failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        userModel = task.getResult().toObject(UserModel.class);

                                        if (userModel == null){
                                            //first time login
                                            userModel = new UserModel(user.getDisplayName().toLowerCase(),user.getPhoneNumber(),user.getEmail(),Timestamp.now(),FirebaseUtils.currentUserId(),"","user",false);
                                            FirebaseUtils.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    //set sheard preference
                                                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                                            .edit()
                                                            .putBoolean("isLogged", true)
                                                            .apply();
                                                    // Update UI with user info
                                                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                                                    intent.putExtra("name", user.getDisplayName());
                                                    intent.putExtra("id", user.getUid());
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                        else {
                                            //2nd time login
                                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                                            intent.putExtra("name", user.getDisplayName());
                                            intent.putExtra("id", user.getUid());
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });
                        } else {
                            // Handle error
                            Toast.makeText(SignIn.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void signUpOnclick(View view) {
        startActivity(new Intent(SignIn.this, SignUp.class));
    }
}