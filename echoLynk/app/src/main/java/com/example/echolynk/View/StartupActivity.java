package com.example.echolynk.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.echolynk.R;
import com.example.echolynk.View.SignInSignUp.SignIn;

public class StartupActivity extends AppCompatActivity {

    private Button startUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_startup);

        startUpBtn=findViewById(R.id.start_up_btn);

        startUpBtn.setOnClickListener(view -> {
            // Mark that the Get Started page has been shown
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();

            // Navigate to the main activity
            Intent intent = new Intent(StartupActivity.this, SignIn.class);
            startActivity(intent);
            finish();
        });

    }
}