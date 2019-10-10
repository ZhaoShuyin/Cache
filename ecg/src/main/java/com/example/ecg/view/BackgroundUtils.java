//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.ecg.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BackgroundUtils {
    private int DEFAULT_BACKGROUND_COLOR = -16777216;
    private int DEFAULT_BACKGROUND_GRID_COLOR = Color.rgb(111, 110, 110);
    private float perMillmeter = 5.245F;
    private Drawable backgroundDrawable;
    private int mViewWidth;
    private int mViewHeight;

    public BackgroundUtils() {
    }

    public Drawable getBackgroundDrawable(int viewWidth, int viewHeight, int bkColor, int gridColor) {
        if(viewWidth <= 0) {
            viewWidth = 768;
            viewHeight = 869;
        }

        this.mViewWidth = viewWidth;
        this.mViewHeight = viewHeight;
        this.DEFAULT_BACKGROUND_COLOR = bkColor;
        this.DEFAULT_BACKGROUND_GRID_COLOR = gridColor;
        this.perMillmeter = 2.8000002F * (float)viewWidth / 512.5F;
        this.changeBackgroundDrawable();
        return this.backgroundDrawable;
    }

    public void changeBackgroundDrawable() {
        int backgroudColor = this.DEFAULT_BACKGROUND_COLOR;
        int gridColor = this.DEFAULT_BACKGROUND_GRID_COLOR;
        Bitmap bitmap = Bitmap.createBitmap(this.mViewWidth, this.mViewHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(backgroudColor);
        Paint mPaint = new Paint();
        mPaint.setStrokeWidth(1.0F);
        mPaint.setColor(gridColor);

        for(int i = 0; (float)i < (float)this.mViewHeight / this.perMillmeter; ++i) {
            float y = (float)i * this.perMillmeter;
            if(i % 5 == 0) {
                canvas.drawLine(0.0F, y, (float)this.mViewWidth, y, mPaint);
            }

            for(int j = 0; (float)j < (float)this.mViewWidth / this.perMillmeter; ++j) {
                float x = (float)j * this.perMillmeter;
                if(j % 5 == 0 && i == 0) {
                    canvas.drawLine(x, y, x, (float)this.mViewHeight, mPaint);
                } else if(j % 5 != 0 && i % 5 != 0) {
                    canvas.drawPoint(x, y, mPaint);
                }
            }
        }

        canvas.save(31);
        canvas.restore();
        this.backgroundDrawable = new BitmapDrawable(bitmap);
    }
}
