package com.example.myapplication.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @Title com.example.myapplication.view
 * @Date 2019/9/26
 * @Autor Zsy
 */

public class ABCView extends View {

    private int mW;
    private int mH;
    private int cX;
    private int cY;
    private int redis;
    private int dynamicRedis, dynamicWidth;  //动态弧半径,宽度
    private int staticRedis, staticWidth;    //静态环半径,宽度
    private int dottedRedis;                 //虚线圆半径
    private int value = 80;                  //分数值
    private Paint paint;                     //画笔
    private ValueAnimator animator;          //动画器
    private final int duration = 2000;       //动画时间
    private float aniAngel = 1.0f;           //动画值
    private int startColor = Color.RED;      //渐变色起始值
    private int endColor = Color.BLUE;       //渐变色结束值
    private SweepGradient sweepGradient;
    private double mAngle = Math.PI * 2 / 360;

    /**
     * 设置值
     */
    public void setValue(int value) {
        this.value = value;
        animator.start();
    }

    public ABCView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        animator = ValueAnimator.ofFloat(1f);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                aniAngel = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = w;
        mH = h;
        cX = mW / 2;
        cY = mH / 2;
        redis = Math.min(mW, mH) / 2;
        dynamicRedis = redis - 5;  //设置动态弧半径
        dynamicWidth = 5;          //设置动态弧宽度
        staticRedis = redis - 50;  //设置静态环半径
        staticWidth = 10;          //设置静态环宽度
        dottedRedis = redis - 100; //设置虚线圆半径
        //设置画笔渐变色
        Matrix localM = new Matrix();
        localM.setRotate(-90, cX, cY);//偏移90度
        sweepGradient = new SweepGradient(cX, cY, new int[]{startColor, endColor}, null);
        sweepGradient.setLocalMatrix(localM);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //抗锯齿
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        //圆环
        paint.setShader(sweepGradient);
        paint.setStrokeWidth(staticWidth);
        canvas.drawCircle(cX, cY, staticRedis, paint);
        //动态弧
        paint.setStrokeWidth(dynamicWidth);
        int dif = redis - dynamicRedis;
        RectF oval = new RectF(dif, dif, mW - dif, mH - dif);
        float v = value * aniAngel / 100;
        float sweepAngle = 270f * v;
        canvas.drawArc(oval, -45, sweepAngle, false, paint);
        double angle = Math.PI * 3 / 2 * v - Math.PI / 4;
        float dY = (int) (Math.sin(angle) * dynamicRedis) + cX;
        float dX = (int) (Math.cos(angle) * dynamicRedis) + cY;
        canvas.drawCircle(dX, dY, 5, paint);

        //虚线圆
        paint.setShader(null);
        paint.setColor(0xffdddddd);         //虚线圆颜色
        int inRedius = dottedRedis - 5;     //虚线长度
        int outRedius = dottedRedis + 5;    //虚线长度

        for (int i = 0; i <= 360; i++) {
            if (i % 5 == 0) {              //每隔5度画一条
                float sY = (int) (Math.sin(mAngle * i) * outRedius) + cX;
                float sX = (int) (Math.cos(mAngle * i) * outRedius) + cY;
                float eY = (int) (Math.sin(mAngle * i) * inRedius) + cX;
                float eX = (int) (Math.cos(mAngle * i) * inRedius) + cY;
                canvas.drawLine(sX, sY, eX, eY, paint);
            }
        }


    }
}
