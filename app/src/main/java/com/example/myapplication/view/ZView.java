package com.example.myapplication.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * @Title com.example.myapplication.view
 * @Date 2019/9/26
 * @Autor Zsy
 */

public class ZView extends View {
    private int mW, mH, cW, cH;
    Paint mPaint;

    public ZView(Context context) {
        super(context);
    }

    public ZView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(0xffff0000);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = w;
        mH = h;
        cW = mW / 2;
        cH = mH / 2;
    }


    @SuppressLint("Range")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*//直线渐变
        LinearGradient linearGradient1 = new LinearGradient(0,0,mW,mH,new int[]{Color.RED,Color.BLUE},null, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient1);
        canvas.drawLine(0,mH-100,mW,mH-100,paint);
        //直线渐变
        LinearGradient linearGradient2 = new LinearGradient(0,0,mW,mH,new int[]{Color.RED,Color.BLUE},null, Shader.TileMode.MIRROR);
        paint.setShader(linearGradient2);
        canvas.drawLine(0,mH-50,mW,mH-50,paint);
        //直线渐变
        LinearGradient linearGradient3 = new LinearGradient(0,0,mW,mH,new int[]{Color.RED,Color.BLUE},null, Shader.TileMode.REPEAT);
        paint.setShader(linearGradient3);
        canvas.drawLine(0,mH,mW,mH,paint);

        //圆心渐变
        int radius = Math.min(mW, mH) / 3;
        RadialGradient radialGradient = new RadialGradient(cW,cH, radius,new int[]{Color.RED,Color.BLUE},null,Shader.TileMode.MIRROR);
        Paint p = new Paint();
        p.setShader(radialGradient);
        canvas.drawCircle(cW,cH, radius,p);*/
        //角度渐变
        /*SweepGradient sweepGradient = new SweepGradient(cW, cH, new int[]{Color.RED, Color.BLUE}, null);
        Matrix localM = new Matrix();
        localM.setRotate(-90, cW, cH);
        sweepGradient.setLocalMatrix(localM);
        paint.setShader(sweepGradient);
        RectF oval = new RectF(50, 50, mW - 50, mH - 50);
        canvas.drawArc(oval, 0, 360, false, paint);*/

       /* float[] floats = new float[]{
                0,0,100,100,
                100,100,200,100,
                200,100,300,200,
                300,200,400,300};

        //1.偏移个数,2.使用数据个数
        canvas.drawLines(floats,4,12,paint);
//        canvas.drawLines(floats,paint);*/


        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, mPaint);

        mPaint.setColor(Color.RED);
        canvas.saveLayerAlpha(100, 100, 200, 200, 120, Canvas.ALL_SAVE_FLAG);
        canvas.drawCircle(200, 200, 100, mPaint);
        canvas.restore();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.makeText(getContext(), "刷新", Toast.LENGTH_SHORT).show();
        invalidate();
        return true;
    }
}
