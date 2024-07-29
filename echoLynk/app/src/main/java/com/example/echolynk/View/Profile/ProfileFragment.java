package com.example.echolynk.View.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.echolynk.Model.SignIn;
import com.example.echolynk.R;
import com.example.echolynk.Utils.AndroidUtils;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.View.Blog.AddBlogMainActivity;
import com.example.echolynk.View.SplashActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ConstraintLayout accountDetails,addToBlog,switchToPremium,statistics,supportContact;
        ImageView editProfileDetails;
        ImageView logoutBtn;
        CircleImageView profilePic;


        accountDetails = view.findViewById(R.id.account_details_btn);
        addToBlog = view.findViewById(R.id.add_blog_btn);
        switchToPremium = view.findViewById(R.id.switch_to_premium_btn);
        statistics = view.findViewById(R.id.statistics_btn);
        supportContact = view.findViewById(R.id.support_contact_btn);
        editProfileDetails = view.findViewById(R.id.edit_profile_btn);
        logoutBtn = view.findViewById(R.id.logout_btn);
        profilePic = view.findViewById(R.id.profile_pic_profile_fragment);


        FirebaseUtils.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtils.setProfilePic(getContext(),uri,profilePic);
                    }
                });


        accountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAccountDetails = new Intent(getActivity(), AccountDetailsActivity.class);
                startActivity(intentAccountDetails);
            }
        });

        addToBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addBlog = new Intent(getActivity(), AddBlogMainActivity.class);
                startActivity(addBlog);
            }
        });
        switchToPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchPremium = new Intent(getActivity(), SwitchToPremiumActivity.class);
                startActivity(switchPremium);
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statistics = new Intent(getActivity(), StatisticsActivity.class);
                startActivity(statistics);
            }
        });

        supportContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent supportContact = new Intent(getActivity(), SupportContactActivity.class);
                startActivity(supportContact);
            }
        });

        editProfileDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editDetails = new Intent(getActivity(), EditDetailsActivity.class);
                startActivity(editDetails);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtils.logOut();
                Intent intent = new Intent(getContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}