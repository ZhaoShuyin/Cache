package com.example.myapplication.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Title https://www.jianshu.com/p/6581703e1d98
 * @Date 2019/9/24
 * @Autor Zsy
 */
public class GLSView extends GLSurfaceView {

    private String TAG = "ZSurView";

    public GLSView(Context context) {
        super(context);
    }

    public GLSView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG, "GLSView: 构造函数");
        //设置渲染器
        setRenderer(new MyRender());
        //设置非主动渲染(须在 setRenderer() 之后)
        //1.非主动渲染RENDERMODE_WHEN_DIRTY(0) 2.RENDERMODE_CONTINUOUSLY主动渲染(1)
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                drawWave();
            }
        }
    }

    private void drawWave() {

    }

    class MyRender implements Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.e(TAG, "onSurfaceCreated: 创建");
            //设置整个gl颜色
            gl.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.e(TAG, "onSurfaceChanged: 变化");
            //设置窗口大小
            gl.glViewport(0, 0, width, height);
        }

        //主动刷新 当绘制每一帧的时候会被调用
        @Override
        public void onDrawFrame(GL10 gl) {
            Log.e(TAG, "onDrawFrame: 刷新");
            font(gl);

        }
    }

    /**
     * 绘制字体
     */
    private void font(GL10 gl){
        //设置颜色
        gl.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //
//        gl.
    }

}
