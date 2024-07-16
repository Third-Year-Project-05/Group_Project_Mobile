package com.example.echolynk.View;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.echolynk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignIn extends AppCompatActivity {

    private EditText userEMail,userPassword;
    private Button signInButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        mAuth=FirebaseAuth.getInstance();
        userEMail=findViewById(R.id.sign_in_email);
        userPassword=findViewById(R.id.sign_in_password);
        signInButton=findViewById(R.id.signIn_button);


        signInButton.setOnClickListener(view->{

            String email=userEMail.getText().toString().trim();
            String pass=userPassword.getText().toString();

            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                if (!pass.isEmpty()){

                    mAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent intent=new Intent(SignIn.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
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
            }else {
                userEMail.setError("Please enter valid email.");
            }
        });

    }

    public void signUpOnclick(View view) {
        startActivity(new Intent(SignIn.this, SignUp.class));
    }
}

