package com.example.echolynk.Utils;

import android.view.MotionEvent;
import android.view.View;

public interface onClickListener {
    void onInit(int status);

    void onClick(int position, View view);

    void onClickDifficultWord(int position,View view);

    boolean onLongClick(int position, View view);

}
