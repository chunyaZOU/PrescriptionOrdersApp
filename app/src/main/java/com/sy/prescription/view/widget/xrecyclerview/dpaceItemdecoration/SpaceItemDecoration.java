package com.sy.prescription.view.widget.xrecyclerview.dpaceItemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
    int leftspacemSpace;
    int rightspacemSpace;
    int bottomspacemSpace;
    int topspacemSpace;
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = leftspacemSpace;
        outRect.right = rightspacemSpace;
        outRect.bottom = bottomspacemSpace;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = topspacemSpace;
        }

    }

    public SpaceItemDecoration(int leftspace,int rightspace,int bottomspace,int topspace ) {
        this.leftspacemSpace = leftspace;
        this.rightspacemSpace = rightspace;
        this.bottomspacemSpace = bottomspace;
        this.topspacemSpace = topspace;
    }
}
