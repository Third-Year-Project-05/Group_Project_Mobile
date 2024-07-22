package com.example.echolynk.View;

import static android.content.ContentValues.TAG;
import static android.provider.Settings.System.getString;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echolynk.R;
import com.example.echolynk.Utils.ColorUtil;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private EditText userName, userEmail, userPassword, userPhoneNumber;

    private CheckBox termsConditions;
    private Button signUp;
    private ImageButton facebook;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.signUp_name);
        userEmail = findViewById(R.id.signUp_email);
        userPhoneNumber = findViewById(R.id.signUp_phoneNumber);
        userPassword = findViewById(R.id.signUp_password);
        termsConditions = findViewById(R.id.checkBox);
        signUp = findViewById(R.id.signUp_button);
        ImageButton google = findViewById(R.id.sign_up_google);
        facebook = findViewById(R.id.signUp_facebook);

        // signUp with google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google.setOnClickListener(view -> {
            signIn();
        });

        // handel the terms and conditions
        termsConditions.setOnClickListener(view -> {
            boolean termsState = termsConditions.isChecked();
            if (termsState) {
                signUp.setBackgroundColor(Color.parseColor("#3D4E8B"));
                signUp.setClickable(true);
                //3D4E8B
            } else {
                signUp.setBackgroundColor(Color.parseColor("#573D4E8B"));
                signUp.setClickable(false);
            }

        });

        signUp.setOnClickListener(view -> {

            String name = userName.getText().toString();
            String email = userEmail.getText().toString();
            String pass = userPassword.getText().toString();
            String phone = userPhoneNumber.getText().toString();


            if (!name.isEmpty() && !email.isEmpty() && !pass.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length() >= 6 && !phone.isEmpty()) {

                // check the phone number validation
                if ((phone.length() == 12)) {
                    if ((phone.charAt(0) == '+' && phone.charAt(1) == '9' && phone.charAt(2) == '4')) {
                        // handle the sign up function
                        Intent intent = new Intent(SignUp.this, VerifyCode.class);
                        intent.putExtra("email", email);
                        intent.putExtra("name", name);
                        intent.putExtra("password", pass);
                        intent.putExtra("telNumber", phone);
                        startActivity(intent);
                    } else {
                        userPhoneNumber.setError("Phone number should be like +94********* ");
                    }
                } else {
                    userPhoneNumber.setError("Phone number length should be 12.");
                }
            } else if (name.isEmpty()) {
                userName.setError("User name can't be empty.");
                userPassword.setText("");
            } else if (email.isEmpty()) {
                userEmail.setError("User email can't be empty.");
                userPassword.setText("");
            } else if (pass.isEmpty()) {
                userPassword.setError("Password can't be empty.");
                userPassword.setText("");
            } else if (pass.length() >= 6) {
                userPassword.setError("Password length must be grater than 6 characters.");
            } else if (phone.isEmpty()) {
                userPhoneNumber.setError("Phone number can't be empty.");
            } else {
                System.out.println(email);
                userEmail.setError("Please enter valid email.");
                userPassword.setText("");
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
                            // Update UI with user info
                            Intent intent=new Intent(SignUp.this,MainActivity.class);
                            intent.putExtra("name",user.getDisplayName());
                            intent.putExtra("id",user.getUid());
                            startActivity(intent);
                        } else {
                            // Handle error
                            Toast.makeText(SignUp.this,Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Intent intent=new Intent(SignUp.this,MainActivity.class);
                intent.putExtra("name",user.getDisplayName());
                intent.putExtra("id",user.getUid());
                startActivity(intent);

            } else {
                // User is signed out
                Toast.makeText(SignUp.this,"Please signUp", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        }
    }

    public void signInOnclick(View view) {
        startActivity(new Intent(SignUp.this, SignIn.class));
    }

}