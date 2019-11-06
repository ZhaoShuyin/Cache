package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

public class SbookView extends View {

    Context mcontext;
    Paint mpaint;
    Path mpath;

    public SbookView(Context context) {
        super(context);
        init();
    }

    public SbookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SbookView(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);
        init();
    }

    private void init() {
        mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setStrokeWidth(6);
        mpaint.setTextSize(16);
        mpaint.setTextAlign(Paint.Align.RIGHT);
        mpath = new Path();
    }

    /**
     * https://blog.csdn.net/u013135085/article/details/81661390
     * DIFFERENCE是第一次不同于第二次的部分显示出来
     * REPLACE是显示第二次的
     * REVERSE_DIFFERENCE 是第二次不同于第一次的部分显示
     * INTERSECT交集显示
     * UNION全部显示
     * XOR补集 就是全集的减去交集生育部分显示
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        mpaint.setColor(0xffff0000);
        canvas.drawLine(0, 0, 200, 200, mpaint);


        Matrix matrix = canvas.getMatrix();
//        float[] floats = new float[];
//        matrix.getValues(floats);
//        Log.e("ZSurView", "onDraw: "+ Arrays.toString(floats));


      /*  Matrix matrix = new Matrix();
        matrix.setTranslate(100,100);
        canvas.concat(matrix);*/
       /* canvas.save();

        canvas.clipRect(0,0,200,200);
        canvas.translate(100,0);
        mpaint.setColor(0xff0000ff);
        canvas.drawCircle(50,50,20,mpaint);
        canvas.save();*/

      /*  canvas.save();
        canvas.translate(10, 10);
        drawScene(canvas);
        canvas.restore();//回滚状态

        canvas.save();   //保存状态
        canvas.translate(200, 0);
        canvas.clipRect(10, 10, 90, 90);
//        canvas.clipRect(30, 30, 70, 70, Region.Op.XOR);
        drawScene(canvas);
        canvas.restore();*/

//        canvas.save();
//        canvas.translate(10, 160);
//        mpath.reset();
//        mpath.cubicTo(0, 0, 100, 0, 100, 100);
//        mpath.cubicTo(100, 100, 0, 100, 0, 0);
//        canvas.clipPath(mpath, Region.Op.REPLACE);
//        drawScene(canvas);
//        canvas.restore();

//        canvas.save();
//        canvas.translate(160, 160);
//        canvas.clipRect(0, 0, 60, 60);
//        canvas.clipRect(40, 40, 100, 100, Region.Op.UNION);
//        drawScene(canvas);
//        canvas.restore();

//        canvas.save();
//        canvas.translate(10, 310);
//        canvas.clipRect(0, 0, 60, 60);
//        canvas.clipRect(40, 40, 100, 100, Region.Op.XOR);
//        drawScene(canvas);
//        canvas.restore();

//        canvas.save();
//        canvas.translate(160, 310);
//        canvas.clipRect(0, 0, 60, 60);
//        canvas.clipRect(40, 40, 100, 100, Region.Op.REVERSE_DIFFERENCE);
//        drawScene(canvas);
//        canvas.restore();


        //画布移动的测试
       /* canvas.drawRect(new RectF(0,0,150,150), mPaint);
        canvas.save();
        //向X轴方向移动200，向Y轴方向移动200
        canvas.translate(100,100);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(new RectF(0,0,100,100), mPaint);
//        canvas.restore();
        //向X轴方向移动200，向Y轴方向移动200
        canvas.translate(100,100);
        mPaint.setColor(Color.RED);
        canvas.drawRect(new RectF(0,0,50,50), mPaint);*/
    }

    private void drawScene(Canvas canvas) {
        canvas.clipRect(0, 0, 100, 100);
        canvas.drawColor(Color.WHITE);
        mpaint.setColor(Color.RED);
        canvas.drawLine(0, 0, 100, 100, mpaint);
        mpaint.setColor(Color.GREEN);
        canvas.drawCircle(30, 70, 30, mpaint);
        mpaint.setColor(Color.BLUE);
        canvas.drawText("clipping", 100, 30, mpaint);
    }
}