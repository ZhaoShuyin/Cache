package com.example.myapplication.render;

import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRender implements GLSurfaceView.Renderer {

    private String TAG = "ZGLSurView";
    private FloatBuffer floatBuffer;
    float[] floats;

    private void init() {
        floats = new float[]{
                0.0f, 0.0f,// 0.0f,
                0.1f, 0.1f, //0.0f,

                0.1f, 0.1f,// 0.0f,
                0.2f, 0.1f,// 0.0f,

                0.2f, 0.1f, //0.0f,
                0.3f, 0.2f,// 0.0f,

                0.3f, 0.2f,// 0.0f,
                0.4f, 0.2f //, 0.0f
        };
        floatBuffer = bufferFloat(floats);
    }

    //主动刷新 当绘制每一帧的时候会被调用
    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e(TAG, "onDrawFrame: 帧刷新");
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
        gl.glLineWidth(5);

//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glLoadIdentity();
//        gl.glTranslatef(-1.5f, 0.0f, 0.0f);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, floatBuffer);

        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, floats.length / 2);
//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glFinish();
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.e(TAG, "onSurfaceCreated: 创建");
        //设置整个gl颜色
        gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
            /*gl.glClearDepthf(1.0f);
            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glDepthFunc(GL10.GL_LEQUAL);
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glDisable(GL10.GL_DITHER);*/

        //设置着色模式1.GL_SMOOTH(渐变) 2.GL_FLAT(单元)
        gl.glShadeModel(GL10.GL_SMOOTH);
        //设置最大深度
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e(TAG, "onSurfaceChanged: 变化");
        //设置窗口大小
        gl.glViewport(0, 0, width, height);
           /* if (height == 0) {
                height = 1;
            }
            float aspect = (float) width / height;
            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            GLU.gluPerspective(gl, 0, aspect, 0.1f, 100.0f);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();*/

        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        gl.glMatrixMode(GL10.GL_LINE_STRIP);
        gl.glLoadIdentity();
    }

    /**
     * 使用时需要从Java的大端字节序(BigEdian) 转换为 小端字节序（LittleEdian）
     */
    private FloatBuffer bufferFloat(float[] arr) {
        FloatBuffer mBuffer;
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个int占4个字节
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
        qbb.order(ByteOrder.nativeOrder());   // 数组排列用nativeOrder
        mBuffer = qbb.asFloatBuffer();
        mBuffer.put(arr);
        mBuffer.position(0);
        return mBuffer;
    }


}