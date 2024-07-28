package com.example.echolynk.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.echolynk.Model.UserModel;

public class AndroidUtils {

    public static void showTost(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel userModel){
        intent.putExtra("userName", userModel.getUserName());
        intent.putExtra("userId", userModel.getUserId());
        //search user list ekn chat activity ekta pass krna data

    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUserName(intent.getStringExtra("userName"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
