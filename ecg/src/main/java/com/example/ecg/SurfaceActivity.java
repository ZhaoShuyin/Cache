package com.example.ecg;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * https://blog.csdn.net/freekiteyu/article/details/79483406
 *
 * @author zsy
 * @date 2019/10/9
 */
public class SurfaceActivity extends Activity {

    String TAG = "zsysurface";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MySrufaceView(this));
    }

    // 视图内部类
    class MySrufaceView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder holder;//操作SurfaceView句柄
        private MyThread myThread;   //子线程刷新

        //MyView的构造方法
        public MySrufaceView(Context context) {
            super(context);
            holder = this.getHolder();
            holder.addCallback(this);
            myThread = new MyThread(holder);// 创建一个绘图线程
        }

        //1.首先调用view添加至窗口
        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            Log.e(TAG, "onAttachedToWindow: ");
        }

        //2.添加至窗口后,显示
        @Override
        protected void onWindowVisibilityChanged(int visibility) {
            super.onWindowVisibilityChanged(visibility);
            Log.e(TAG, "onWindowVisibilityChanged: ");
        }

        //从窗口拆卸
        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            Log.e(TAG, "onDetachedFromWindow: ");
        }


        //测量View (调用两次)
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            Log.e(TAG, "onMeasure: ");
        }


        //View的绘制
        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            Log.e(TAG, "draw: ");
        }


        //一般在该方法中启动绘图的线程
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.e(TAG, "surfaceCreated: ");
            myThread.isRun = true;
            myThread.start();
        }

        //surface尺寸发生改变的时候调用,如横竖屏切换
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.e(TAG, "surfaceChanged: ");
        }

        //surface被销毁的时候调用,一般在该方法中停止绘图线程。
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.e(TAG, "surfaceDestroyed: ");
            myThread.isRun = false;
        }

        //分发绘制事件
        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            Log.e(TAG, "dispatchDraw: ");
        }

        //收集透明范围
        @Override
        public boolean gatherTransparentRegion(Region region) {
            Log.e(TAG, "gatherTransparentRegion: ");
            return super.gatherTransparentRegion(region);
        }



    }

    // 线程内部类
    class MyThread extends Thread {
        private SurfaceHolder holder;
        public boolean isRun;

        public MyThread(SurfaceHolder holder) {
            this.holder = holder;
            isRun = true;
        }

        @Override
        public void run() {
            int count = 0;
            while (isRun) {
                Canvas c = null;
                try {
                    synchronized (holder) {
                        // 锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                        c = holder.lockCanvas();
                        c.drawColor(Color.BLACK);// 设置画布背景颜色
                        Paint p = new Paint(); // 创建画笔
                        p.setTextSize(100);
                        p.setColor(Color.WHITE);
                        Rect r = new Rect(100, 50, 500, 150);
                        c.drawRect(r, p);
                        c.drawText("这是第" + (count++) + "秒", 200, 600, p);
                        Thread.sleep(1000);// 睡眠时间为1秒
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
                    }
                }
            }
        }
    }
}
