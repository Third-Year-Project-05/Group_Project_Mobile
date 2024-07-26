package com.example.echolynk.View.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.echolynk.R;

public class ChangeVoiceActivity extends AppCompatActivity {

    RadioButton radioButtonMale, radioButtonFemale;
    Button buttonSave;
    ImageView backBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_voice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        radioButtonMale = findViewById(R.id.male_voice);
        radioButtonFemale = findViewById(R.id.female_voice);
        buttonSave = findViewById(R.id.change_voice_button);
        backBtn = findViewById((R.id.change_voice_back_btn));

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButtonMale.isChecked()){
                    //set voice male
                } else if (radioButtonFemale.isChecked()) {
                    // set voice female
                }

                Intent save = new Intent(ChangeVoiceActivity.this, AccountDetailsActivity.class);
                startActivity(save);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backBtn = new Intent(ChangeVoiceActivity.this, AccountDetailsActivity.class);
                startActivity(backBtn);
            }
        });
    }



}