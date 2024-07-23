package com.example.echolynk.View;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.echolynk.R;
import com.example.echolynk.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    RelativeLayout mainLayout, mainLayoutHeader;
    TextView userName;
    de.hdodenhof.circleimageview.CircleImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);

      /*  setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.verifyCode), (v, insets) -> {*/

        setContentView(binding.getRoot());
    //    replaceFragment(new HomeFragment());
//        binding.bottomNavigationView.setSelectedItemId(R.id.home_frame);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mainLayout = findViewById(R.id.main);
        userName = findViewById(R.id.user_name);
        userImage = findViewById(R.id.user_image);
        mainLayoutHeader = findViewById(R.id.main_header);

//        replaceFragment(new HomeFragment());
//        setHeader(true);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("load_fragment")){
            String loadFragment = intent.getStringExtra("load_fragment");
            if (Objects.equals(loadFragment, "profile")){
                replaceFragment(new ProfileFragment());
                mainLayoutHeader.setVisibility(View.GONE);
                setHeader(false);
                binding.bottomNavigationView.setSelectedItemId(R.id.profile_frame);
            }
            else{
                replaceFragment(new HomeFragment());
                setHeader(true);
                binding.bottomNavigationView.setSelectedItemId(R.id.home_frame);

            }
        }else{
            replaceFragment(new HomeFragment());
            setHeader(true);
            binding.bottomNavigationView.setSelectedItemId(R.id.home_frame);
        }


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home_frame){
                replaceFragment(new HomeFragment());
                setHeaderColours(0);
                setHeader(true);
            } else if (item.getItemId() == R.id.call_frame) {
                replaceFragment(new CallFragment());
                mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                setHeaderColours(1);
                setHeader(true);

            } else if (item.getItemId() == R.id.speech_frame) {
                replaceFragment(new SpeechFragment());
                setHeaderColours(1);
                setHeader(true);
            } else if (item.getItemId() == R.id.games_frame) {
                replaceFragment(new GamesFragment());
                setHeaderColours(0);
                setHeader(true);
            } else if (item.getItemId() == R.id.profile_frame) {
                replaceFragment(new ProfileFragment());
                mainLayoutHeader.setVisibility(View.GONE);
                setHeader(false);
            } else {
                replaceFragment(new TestFragment());
                setHeader(true);
            }
            return true;
        });

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
            binding.bottomNavigationView.setSelectedItemId(R.id.home_frame);
            replaceFragment(new HomeFragment());

    }

    public void setHeader(boolean show){
        if (show){
            mainLayoutHeader.setVisibility(View.VISIBLE);
        }
        else{
            mainLayoutHeader.setVisibility(View.GONE);
        }
    }

    private void setHeaderColours(int backColour){
        if (backColour == 1){

            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            userName.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
            userImage.setBorderColor(ContextCompat.getColor(this, R.color.primary_blue));
            
            
            Drawable bellIcon;
            CircleImageView circleImageView = findViewById(R.id.circleImageView2);
            bellIcon = ContextCompat.getDrawable(this,R.drawable.bell_icon_with_circle);
            bellIcon.setColorFilter(ContextCompat.getColor(this,R.color.primary_blue), PorterDuff.Mode.SRC_IN);
            circleImageView.setImageDrawable(bellIcon);
        }
        else {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_blue));
            userName.setTextColor(ContextCompat.getColor(this, R.color.white));
            userImage.setBorderColor(ContextCompat.getColor(this, R.color.white));


            Drawable bellIcon;
            CircleImageView circleImageView = findViewById(R.id.circleImageView2);
            bellIcon = ContextCompat.getDrawable(this,R.drawable.bell_icon_with_circle);
            bellIcon.setColorFilter(ContextCompat.getColor(this,R.color.white), PorterDuff.Mode.SRC_IN);
            circleImageView.setImageDrawable(bellIcon);
        }
    }

}