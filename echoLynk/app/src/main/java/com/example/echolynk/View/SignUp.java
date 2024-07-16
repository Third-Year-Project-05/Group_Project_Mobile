package com.example.echolynk.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echolynk.R;
import com.example.echolynk.Utils.ColorUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private EditText userName,userEmail,userPassword;

    private CheckBox termsConditions;
    private Button signUp;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        mAuth=FirebaseAuth.getInstance();
        userName=findViewById(R.id.signUp_name);
        userEmail=findViewById(R.id.signUp_email);
        userPassword=findViewById(R.id.signUp_password);
        termsConditions=findViewById(R.id.checkBox);
        signUp=findViewById(R.id.signUp_button);

        termsConditions.setOnClickListener(view -> {
            boolean termsState=termsConditions.isChecked();
            if (termsState){
                signUp.setBackgroundColor(Color.parseColor("#3D4E8B"));
                signUp.setClickable(true);
                //3D4E8B
            }else {
                signUp.setBackgroundColor(Color.parseColor("#573D4E8B"));
                signUp.setClickable(false);
            }

        });

        signUp.setOnClickListener(view -> {

           String name= userName.getText().toString();
           String email= userEmail.getText().toString();
           String pass= userPassword.getText().toString();


           if (!name.isEmpty() && !email.isEmpty() && !pass.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length()>=6){

               // handle the sign up function
               mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           Intent intent=new Intent(SignUp.this,SignIn.class);
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);
                       }else {
                           Toast.makeText(SignUp.this, "SignUp failed "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });


           }else if (name.isEmpty()){
               userName.setError("User name can't be empty.");
               userPassword.setText("");
           }else if (email.isEmpty()){
               userEmail.setError("User email can't be empty.");
               userPassword.setText("");
           } else if (pass.isEmpty()) {
               userPassword.setError("Password can't be empty.");
               userPassword.setText("");
           }else if (pass.length()>=6){
               userPassword.setError("Password length must be grater than 6 characters.");
           }else {
               userEmail.setError("Please enter valid email.");
               userPassword.setText("");
           }

        });

    }

    public void signInOnclick(View view) {
        startActivity(new Intent(SignUp.this, SignIn.class));
    }
}