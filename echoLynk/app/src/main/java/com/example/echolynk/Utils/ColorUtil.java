package com.example.echolynk.Utils;

import android.graphics.Color;

import java.util.Random;

public class ColorUtil {
    public static int getRandomColor(){
        Random random=new Random();
        return Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));
    }
}
