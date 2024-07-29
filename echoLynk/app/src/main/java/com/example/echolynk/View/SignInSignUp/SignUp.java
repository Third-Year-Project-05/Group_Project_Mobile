package com.example.echolynk.View.SignInSignUp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private EditText userName, userEmail, userPassword;
    private TextView privacy;

    private CheckBox termsConditions;
    private Button signUp;
    private ImageButton facebook;
    private ImageButton google;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private static final int EMAIL_VERIFICATION_REQUEST_CODE = 1001;
    private GoogleSignInClient mGoogleSignInClient;
    private Handler handler = new Handler();

    private UserModel userModel;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.signUp_name);
        userEmail = findViewById(R.id.signUp_email);
        userPassword = findViewById(R.id.signUp_password);
        termsConditions = findViewById(R.id.checkBox);
        signUp = findViewById(R.id.signUp_button);
        google = findViewById(R.id.sign_up_google);
        facebook = findViewById(R.id.signUp_facebook);
        privacy = findViewById(R.id.signUp_terms_condtions);


        // signUp with google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google.setOnClickListener(view -> {
            signUp();
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

            if (!name.isEmpty() && !email.isEmpty() && !pass.isEmpty() && pass.length() >= 6) {

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    // signUp use for the email
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //send email and check the mail verification

                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    if (user != null) {
                                                        user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (user.isEmailVerified()) {
                                                                    userModel = new UserModel(name.toLowerCase(),"",email, Timestamp.now(), FirebaseUtils.currentUserId(),pass,"user");

                                                                    FirebaseUtils.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                Toast.makeText(SignUp.this, "Email verified.", Toast.LENGTH_SHORT).show();
                                                                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                startActivity(intent);
                                                                                finish();
                                                                                handler.removeCallbacksAndMessages(null);
                                                                            }
                                                                        }
                                                                    });

                                                                } else {
                                                                    // Email is not verified, prompt user to verify email
                                                                    Toast.makeText(SignUp.this, "Please verify your email.", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                                    }

                                                    // Repeat every 5 seconds
                                                    handler.postDelayed(this, 5000);
                                                }
                                            }, 5000);
                                        } else {
                                            Toast.makeText(SignUp.this, "Unsuccessful send the verification mail.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(SignUp.this, "SignUp email password authentication failed ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else if (Patterns.PHONE.matcher(email).matches()) {
                    //signUp use for the phone number
                    Intent intent = new Intent(SignUp.this, VerifyCode.class);
                    intent.putExtra("email", "null");
                    intent.putExtra("name", name);
                    intent.putExtra("password", pass);
                    intent.putExtra("telNumber", email);
                    startActivity(intent);
                } else {
                    userEmail.setError("Email or Phone number not match.");
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
            } else if (pass.length() < 6) {
                userPassword.setError("Password length must be grater than 6 characters.");
            } else {
                userEmail.setError("Please enter valid email. or valid phone number (+94*********)");
                userPassword.setText("");
            }
        });

        // privacy and conditions
        privacy.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.freeprivacypolicy.com/live/b16e4d1a-bfdc-4661-b8ac-539cac1f5f12"));
            view.getContext().startActivity(intent);
        });
//        privacy.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private void signUp() {
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
        } else if (requestCode == EMAIL_VERIFICATION_REQUEST_CODE) {
            // check the mail verification

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

                                        if(userModel == null){
                                            //first time login
                                            userModel = new UserModel(user.getDisplayName().toLowerCase(),user.getPhoneNumber(),user.getEmail(),Timestamp.now(),FirebaseUtils.currentUserId(),"","user");
                                            FirebaseUtils.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    // Update UI with user info
                                                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                                                    intent.putExtra("name", user.getDisplayName());
                                                    intent.putExtra("id", user.getUid());
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                        else {
                                            //2nd time login
                                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                                            intent.putExtra("name", user.getDisplayName());
                                            intent.putExtra("id", user.getUid());
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });

                        } else {
                            // Handle error
                            Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void signInOnclick(View view) {
        startActivity(new Intent(SignUp.this, SignIn.class));
    }

}