package com.example.echolynk.Utils;

import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewCustomItemDirection extends RecyclerView.ItemDecoration {
    private final int spaceHeight;

    public RecycleViewCustomItemDirection(int spaceHeight) {
        this.spaceHeight = spaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {
        outRect.left=spaceHeight;
    }
}
