package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * @Title com.example.myapplication.view
 * @date 2019/10/31
 * @autor Zsy
 */

public class ZZView extends View {

    int mW, mH;
    private Paint paint;
    private Rect rect;
    Canvas canvas;

    public ZZView(Context context) {
        super(context);
    }

    public ZZView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        paint = new Paint();
        paint.setColor(0xff00ffff);
        rect = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = w;
        mH = h;
        rect.set(0, mH - 100, mW, mH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(0xff00ffff);
        canvas.drawCircle(mW / 2, mH / 2, 50, paint);

        paint.setColor(0xffff00ff);
        canvas.drawCircle(mW / 2, mH - 100, 50, paint);


       /* Paint paint = new Paint();
        paint.setColor(0xffff0000);
        canvas.drawLine(0,0,50,50,paint);
//        Matrix matrix = canvas.getMatrix();
        canvas.save();

        Matrix matrix = canvas.getMatrix();
        Log.e("gltest", "matrix 1: "+matrix.toShortString() );
        matrix.postTranslate(100,0);
        Log.e("gltest", "matrix 2: "+matrix.toShortString() );
        canvas.setMatrix(matrix);
        canvas.save();*/
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.makeText(getContext(), "点击了", Toast.LENGTH_SHORT).show();
//        Canvas canvas = new Canvas();
//        canvas.drawColor(0xff00ffff);
//        draw(canvas);
//        invalidate(rect);
//        canvas.drawColor(0xffdddddd);
        Log.e("ZSurView", "onTouchEvent: 1 "+rect.toShortString() );
        getDrawingRect(rect);
        Log.e("ZSurView", "onTouchEvent: 2 " +rect.toShortString() );
        return super.onTouchEvent(event);
    }
}
