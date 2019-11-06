package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;

/**
 * @Title com.example.myapplication.view
 * @date 2019/10/31
 * @autor Zsy
 */

public class ZZZView extends View {

    //    private Bitmap mBitmap;
    private Paint paint;
    private TextPaint tpaint;
    private Matrix matrix;
    int w, h;
    private Bitmap bitmap;

    public ZZZView(Context context) {
        super(context);
    }

    public ZZZView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        paint = new Paint();
        paint.setColor(0xffff0000);
        tpaint = new TextPaint();
        matrix = new Matrix();
//        mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ecgbg);
        //mBitmap.isMutable();//可变
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        tpaint.setTextSize(w / 3);
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        bitmap = Bitmap.createBitmap(w, h, config);
//        bitmap.isMutable();
        Log.e("gltest", "创建bitmap");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Canvas canvas1 = new Canvas();
        canvas1.setBitmap(bitmap);
        Log.e("gltest", "绑定bitmap");
        canvas1.drawLine(0, 0, w, h, paint);
        canvas1.drawText("123456789", 0, h / 2, tpaint);
        canvas1.save(Canvas.ALL_SAVE_FLAG);
        Log.e("gltest", "保存画布");
//        canvas.drawBitmap(bitmap,matrix,paint);
        Log.e("gltest", "绘制bitmap");
//        canvas.drawBitmap(bitmap,0,0,paint);


        Rect src = new Rect();// 图片
        src.left = 0;
        src.top = 0;
        src.right = w;
        src.bottom = h;
        Rect src2 = new Rect();// 图片
        src2.left = 0;
        src2.top = 0;
        src2.right = w / 2;
        src2.bottom = h;
        //展示一部分
        canvas.drawBitmap(bitmap, src, src2, null);
        canvas.drawCircle(w / 2 + 150, h / 2, 50, paint);

    }

    public Bitmap getCanvasBmp() {
        Bitmap canvasBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas cn = new Canvas(canvasBmp);
        cn.save(Canvas.ALL_SAVE_FLAG);
        cn.restore();
        return canvasBmp;
    }

    public void test(ImageView imageView) {
        if (bitmap != null) {
//            Bitmap.createBitmap(bitmap, w / 3, 0, cropWidth, cropHeight, null, false);
            imageView.setImageBitmap(bitmap);
            Toast.makeText(getContext(), "设置", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "bitmap == null", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        return super.onTouchEvent(event);
    }
}
