package com.example.echolynk.View.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.echolynk.R;
import com.example.echolynk.View.MainActivity;

public class AccountDetailsActivity extends AppCompatActivity {

    ImageView backBtn;
    ConstraintLayout changeVoiceBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backBtn = findViewById(R.id.account_details_back_btn);
        changeVoiceBtn = findViewById(R.id.change_voice_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent(AccountDetailsActivity.this, MainActivity.class);
                intentBack.putExtra("load_fragment","profile");
                startActivity(intentBack);
            }
        });

        changeVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeVoice = new Intent(AccountDetailsActivity.this, ChangeVoiceActivity.class);
                startActivity(changeVoice);
            }
        });

    }
}