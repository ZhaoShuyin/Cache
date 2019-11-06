package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Title com.example.myapplication
 * @Date 2019/9/24
 * @Autor Zsy
 */

public class EcgView extends SurfaceView implements SurfaceHolder.Callback {
    private String TAG = "gltest";

    private SurfaceHolder holder;
    private int mW, mH;
    private int mCount = 4;
    private int mCurrent = 0;
    private Canvas[] mCanvas;
    private Bitmap[] mBitmaps;
    private Rect[] mRects;
    private Paint paint;

    public EcgView(Context context) {
        this(context, null);
    }

    public EcgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置背景透明
        this.setZOrderOnTop(true);
        //this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        holder = getHolder();
        holder.addCallback(this);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xffff0000);
    }

    private void initDraw() {
        mCanvas = new Canvas[mCount];
        mBitmaps = new Bitmap[mCount];
        mRects = new Rect[mCount];
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        int unitW = mW / mCount;
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(30);
        for (int i = 0; i < mCount; i++) {
            mCanvas[i] = new Canvas();
            mBitmaps[i] = Bitmap.createBitmap(mW / mCount, mH, config);
            mCanvas[i].setBitmap(mBitmaps[i]);
            mRects[i] = new Rect(unitW * i, 0, unitW * (i + 1), mH);
            mCanvas[i].drawColor(getColor(i));
            mCanvas[i].drawText("<" + i + ">", unitW / 2, mH / 2, textPaint);
        }
    }

    private int getColor(int i) {
        switch (i % 10) {
            case 0:
                return 0xffff0000;
            case 1:
                return 0xffdd0000;
            case 2:
                return 0xffff8800;
            case 3:
                return 0xffffff00;
            case 4:
                return 0xff88ff00;
            case 5:
                return 0xff00ff00;
            case 6:
                return 0xff00ff88;
            case 7:
                return 0xff008888;
            case 8:
                return 0xff0088ff;
            case 9:
                return 0xff0000ff;
        }
        return 0xffdddddd;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = w;
        mH = h;
        initDraw();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new MyThread().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    private void drawing() {
        if (mCurrent >= mCount) {
            mCurrent = 0;
        }
        Canvas canvas = holder.lockCanvas();
        for (int i = 0; i < mCount - mCurrent; i++) {
            canvas.drawBitmap(mBitmaps[mCurrent + i], mRects[0], mRects[i], paint);
            Log.e(TAG, "位置:" + i + " 绘制:" + (mCurrent + i));
        }
//        for (int i = 0; i < mCurrent; i++) {
//            canvas.drawBitmap(mBitmaps[i], mRects[0], mRects[i], paint);
//        }
        holder.unlockCanvasAndPost(canvas);
        mCurrent++;
    }


    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                drawing();
            }
        }
    }

}
