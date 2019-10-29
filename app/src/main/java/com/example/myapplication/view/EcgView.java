package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
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

    String TAG = "ZSurView";
    public static boolean isRunning;
    private int position = 0;
    private int mWidth, mHeight, mUnit;//控件宽度,高度

    private Paint mPaint;
    private Rect rect;
    private SurfaceHolder holder;
    private ConcurrentLinkedQueue<Short> queue;

    public EcgView(Context context) {
        super(context);
    }

    public EcgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG, "SurView: 构造函数");
        mPaint = new Paint();
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0xffff0000);
        mPaint.setAntiAlias(true);
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
        mUnit = mWidth / 100;
        mLastY = mHeight / 2;
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
        for (int i = 0; i < 100; i++) {
            integers.add((i % 5) * 20);
        }
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

    private List<Integer> integers = new ArrayList<>();
    private int mLastY, mTempY;

    int i = 0;

    private void drawWave() {
        position++;
        if (position >= 100) {
            position = 0;
            i++;
            if (i % 3 == 0) {
                mPaint.setColor(0xffff0000);
            }
            if (i % 3 == 1) {
                mPaint.setColor(0xff00ff00);
            }
            if (i % 3 == 2) {
                mPaint.setColor(0xff0000ff);
            }
        }
//        Log.e(TAG, "帧刷新 position: " + position);
        int left = mUnit * position;
        int top = 0;
        int right = mUnit * (position + 1) + 20;
        int bottom = mHeight;
        rect.set(left, top, right, bottom);

        Canvas canvas = holder.lockCanvas(rect);//锁定绘制区域
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mTempY = integers.get(position) + mHeight / 2;
        canvas.drawLine(left, mLastY, left + mUnit, mTempY, mPaint);
        mLastY = mTempY;

        holder.unlockCanvasAndPost(canvas);    //释放执行绘制区域
    }

    public void setQueue(ConcurrentLinkedQueue<Short> queue) {
        this.queue = queue;
    }


    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isRunning) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                drawWave();
            }
        }
    }
}
