package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Title com.example.myapplication.view
 * @Date 2019/9/26
 * @Autor Zsy
 */

public class ZView extends View {
    private int mW, mH, cW, cH;
    Paint paint;

    public ZView(Context context) {
        super(context);
    }

    public ZView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(0xffff0000);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = w;
        mH = h;
        cW = mW / 2;
        cH = mH / 2;
    }


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
        SweepGradient sweepGradient = new SweepGradient(cW, cH, new int[]{Color.RED, Color.BLUE}, null);
        Matrix localM = new Matrix();
        localM.setRotate(-90, cW, cH);
        sweepGradient.setLocalMatrix(localM);
        paint.setShader(sweepGradient);
        RectF oval = new RectF(50, 50, mW - 50, mH - 50);
        canvas.drawArc(oval, 0, 360, false, paint);
    }
}
