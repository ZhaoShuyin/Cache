package com.example.ecg;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.ecg.view.DrawUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Title com.example.ecg
 * @Date 2019/10/9
 * @Autor Zsy
 */
public class GLSurfaceActivity extends Activity {

    public static String TAG = "zsysurface";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "GLSurfaceView", Toast.LENGTH_SHORT).show();
        setContentView(new MyGLSurfaceView(this));
    }

    /**
     *
     */
    public class MyGLSurfaceView extends GLSurfaceView {
        public MyGLSurfaceView(Context context) {
            this(context, null);
        }

        public MyGLSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setRenderer(new MyRender());
        }

        //1.首先添加至窗口
        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            Log.e(TAG, "onAttachedToWindow: ");
        }

        //2.创建surface
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            super.surfaceCreated(holder);
            Log.e(TAG, "surfaceCreated: ");
        }

        //3.surface开始测量
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            super.surfaceChanged(holder, format, w, h);
            Log.e(TAG, "surfaceChanged: ");
        }

        //4.
        @Override
        public void surfaceRedrawNeeded(SurfaceHolder holder) {
            super.surfaceRedrawNeeded(holder);
            Log.e(TAG, "surfaceRedrawNeeded: ");
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            Log.e(TAG, "finalize: ");
        }

        @Override
        public void requestRender() {
            super.requestRender();
            Log.e(TAG, "requestRender: ");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            super.surfaceDestroyed(holder);
            Log.e(TAG, "surfaceDestroyed: ");
        }


        @Override
        public void surfaceRedrawNeededAsync(SurfaceHolder holder, Runnable finishDrawing) {
            super.surfaceRedrawNeededAsync(holder, finishDrawing);
            Log.e(TAG, "surfaceRedrawNeededAsync: ");
        }


        //暂停周期
        @Override
        public void onPause() {
            super.onPause();
            Log.e(TAG, "onPause: ");
        }

        //恢复周期
        @Override
        public void onResume() {
            super.onResume();
            Log.e(TAG, "onResume: ");
        }

        @Override
        public void queueEvent(Runnable r) {
            super.queueEvent(r);
            Log.e(TAG, "queueEvent: ");
        }


        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            Log.e(TAG, "onDetachedFromWindow: ");
        }
    }

    /**
     * GLSurface使用render来绘制
     * API
     * https://blog.csdn.net/ocean2006/article/details/6853565
     * 折线图
     * https://blog.csdn.net/u011361385/article/details/79984814
     * 坐标系
     * https://blog.csdn.net/qq_31726827/article/details/51265186
     */
    private static class MyRender implements GLSurfaceView.Renderer {
        private FloatBuffer mVB;
        int i = 0;

        MyRender() {
            //坐标(3*6=18)
          /*  float coords[] = {
                    -0.5f, 0.5f, 0.0f,
                    -0.5f, -0.5f, 0.0f,
                    0.5f, -0.5f, 0.0f,
                    0.5f, -0.5f, 0.0f,
                    0.5f, 0.5f, 0.0f,
                    -0.5f, 0.5f, 0.0f
            };*/
            float coords[] = {
                    0.0f, 0.0f, 0.0f,
                    -0.5f, -0.5f, 0.0f,
                    0.5f, -0.5f, 0.0f
            };
            //大小72,cap
            ByteBuffer vbb = ByteBuffer.allocateDirect(coords.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            String s = vbb.toString();
            Log.e(TAG, "ByteBuffer: " + s);
            mVB = vbb.asFloatBuffer();
            mVB.put(coords);
            mVB.position(0);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.e(TAG, "Render---onSurfaceCreated: ");
            //顶点阵列
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.e(TAG, "Render---onSurfaceChanged: width:" + width + " height:" + height);
            gl.glViewport(0, 0, width, height);
        }

        /**
         * 方法持续调用,刷新每帧
         * 绘制点,线,面 https://blog.csdn.net/weixin_34242819/article/details/92066250
         */
        @Override
        public void onDrawFrame(GL10 gl) {
            Log.e(TAG, "Render---onDrawFrame: ");
            //表示清除颜色设为某颜色
            gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            //帧清除屏幕到黑色
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            //移动坐标系
             gl.glTranslatef(0.0f, 0.0f, 0.0F);
            //设置当前颜色
            gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            //定义一个顶点坐标矩阵(调用glDrawArrays方法或glDrawElements方法时会使用)
            //每个顶点的坐标维数，必须是2, 3或者4，初始值是4
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVB);
            //由矩阵数据渲染图元
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
//            gL(gl);
        }
    }




    /**
     * GL10接口包含了Java（TM）程序语言为OpenGL绑定的核心功能。
     * 一个以x或xv为后缀的方法是属于OES_fixed_point扩展功能的，并且需要一个或更多的修正的点为参数。
     */
    public static void gL(GL10 gl) {
        //
//        gl.glActiveTexture(0);
    }




}
