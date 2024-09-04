package com.example.echolynk.View;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.echolynk.Model.UserModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.AndroidUtils;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.View.Call.CallFragment;
import com.example.echolynk.View.Game.GamesFragment;
import com.example.echolynk.View.Home.HomeFragment;
import com.example.echolynk.View.LiveConversation.SpeechFragment;
import com.example.echolynk.View.Profile.ProfileFragment;
import com.example.echolynk.View.SignInSignUp.SignIn;
import com.example.echolynk.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    RelativeLayout mainLayout, mainLayoutHeader;
    TextView userName;
    de.hdodenhof.circleimageview.CircleImageView userImage;
    UserModel userModel;

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

        //set user image
        FirebaseUtils.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtils.setProfilePic(MainActivity.this,uri,userImage);
                    }
                });

        //get user details
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                userModel = task.getResult().toObject(UserModel.class);
                System.out.println(userModel);
                try {
                    userName.setText(userModel.getUserName());
                }catch (Exception e){
                    Log.d("current user", "onCreate: "+e.getLocalizedMessage());
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

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
            else if (Objects.equals(loadFragment, "speech")){
                replaceFragment(new SpeechFragment());
                mainLayoutHeader.setVisibility(View.VISIBLE);
                setHeader(true);
                setHeaderColours(1);
                binding.bottomNavigationView.setSelectedItemId(R.id.speech_frame);
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