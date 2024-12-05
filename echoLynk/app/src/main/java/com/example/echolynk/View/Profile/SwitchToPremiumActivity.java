package com.example.echolynk.View.Profile;

import static com.example.echolynk.Utils.FirebaseUtils.currentUserId;
import static com.example.echolynk.Utils.FirebaseUtils.isUserPremium;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.echolynk.R;
import com.example.echolynk.Utils.PaymentMethod;
import com.example.echolynk.Utils.UpdateCallBack;
import com.example.echolynk.View.MainActivity;

public class SwitchToPremiumActivity extends AppCompatActivity {

    ImageView backBtn;
    Button subscribeBTN;
    RelativeLayout layout3, layout2, layout1;
    private PaymentMethod paymentMethod;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_switch_to_premium);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backBtn = findViewById(R.id.switch_to_premium_back_btn);
        subscribeBTN = findViewById(R.id.subscribeBTN);
        layout3 = findViewById(R.id.layout3);
        layout2 = findViewById(R.id.layout2);
        layout1 = findViewById(R.id.layout1);
        paymentMethod = new PaymentMethod(SwitchToPremiumActivity.this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backBtn = new Intent(SwitchToPremiumActivity.this, MainActivity.class);
                backBtn.putExtra("load_fragment", "profile");
                startActivity(backBtn);
            }
        });

        isUserPremium(currentUserId())
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        queryDocumentSnapshots.forEach(documentSnapshot -> {
                            boolean isPremium = Boolean.TRUE.equals(documentSnapshot.getBoolean("isPremium"));

                            if (isPremium) {
                                subscribeBTN.setVisibility(View.GONE);
                                layout1.setBackgroundColor(Color.parseColor("#6B29BD05"));
                                layout2.setBackgroundColor(Color.parseColor("#6B29BD05"));
                                layout3.setBackgroundColor(Color.parseColor("#6B29BD05"));
                            } else {
                                subscribeBTN.setOnClickListener(view -> {
                                    paymentMethod.firePaymentMethod(currentUserId(), new UpdateCallBack() {
                                        @Override
                                        public void onSuccess(boolean result) {
                                            Intent intent = new Intent(SwitchToPremiumActivity.this, SwitchToPremiumActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(boolean result) {

                                        }
                                    });

                                });
                            }
                        });
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}