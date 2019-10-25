package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.myapplication.R;

/**
 * @Title com.example.myapplication
 * @Date 2019/9/24
 * @Autor Zsy
 */

public class SurView extends SurfaceView implements SurfaceHolder.Callback {

    String TAG = "ZSurView";
    public static boolean isRunning;
    private int position = 0;
    private int mWidth, mHeight, mUnit;//控件宽度,高度
    private Paint mPaint;
    private Rect rect;
    private SurfaceHolder holder;

    public SurView(Context context) {
        super(context);
    }

    public SurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG, "SurView: 构造函数");
        mPaint = new Paint();
        mPaint.setStrokeWidth(10);
        mPaint.setColor(0xffff0000);
        rect = new Rect();
        //设置背景透明
        this.setZOrderOnTop(true);
        //this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: 尺寸测量");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged: 尺寸变化 w: " + w + ", h:" + h);
        mWidth = w;
        mHeight = h;
        mUnit = mWidth / 5;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.e(TAG, "draw: View普通绘制");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated: Surface创建");
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.parseColor("#FF00FFFF"));
        holder.unlockCanvasAndPost(canvas);
        isRunning = true;
        new MyThread().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceCreated: Surface尺寸变化");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated: Surface销毁");
        isRunning = false;
    }


    private void drawWave() {
        position++;
        if ((position - 1) * mUnit >= mWidth) {
            position = 0;
        }
        Log.e(TAG, "帧刷新 position: " + position);
        int left = mUnit * position;
        int top = 0;
        int right = mUnit * (position + 1);
        int bottom = mHeight;
        rect.set(left, top, right, bottom);
        Canvas canvas = holder.lockCanvas(rect);//锁定绘制区域
        int startX = left + mUnit / 2;
        int endX = left + mUnit;
        int startY = (mHeight / 5 * position);
        int endY = (mHeight / 5 * position);
        canvas.drawLine(startX, startY, endX, endY, mPaint);
        holder.unlockCanvasAndPost(canvas);    //释放执行绘制区域
    }


    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                drawWave();
            }
        }
    }
}
