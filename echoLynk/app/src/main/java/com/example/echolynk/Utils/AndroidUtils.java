package com.example.echolynk.Utils;

import android.content.Intent;

import com.example.echolynk.Model.UserModel;

public class AndroidUtils {

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
}
