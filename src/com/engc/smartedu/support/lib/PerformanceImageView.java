package com.engc.smartedu.support.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * User: qii
 * Date: 12-11-15
 */
public class PerformanceImageView extends ImageView {

    private boolean mMeasuredExactly = false;
    private boolean mBlockMeasurement = false;

    public PerformanceImageView(Context context) {
        super(context);
    }

    public PerformanceImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PerformanceImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mBlockMeasurement = true;
        super.setImageBitmap(bm);
        mBlockMeasurement = false;
    }

    @Override
    public void requestLayout() {
        if (mBlockMeasurement && mMeasuredExactly) {
            // Ignore request

        } else {
            super.requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasuredExactly = isMeasuredExactly(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean isMeasuredExactly(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        return widthMeasureSpecMode == MeasureSpec.EXACTLY && heightMeasureSpecMode == MeasureSpec.EXACTLY;
    }
}