package com.example.ecg.test;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import serial.jni.DrawUtils;

/**
 * @Title com.example.ecg.test
 * @date 2019/10/11
 * @autor Zsy
 */

public class TestActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(new TestRender());
        setContentView(glSurfaceView);
        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
    }


    class TestRender implements GLSurfaceView.Renderer {

        private FloatBuffer mVB;
        float coords[] = {
                -0.5f, 0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f
        };

        public TestRender() {
            //大小72,cap
            ByteBuffer vbb = ByteBuffer.allocateDirect(coords.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            String s = vbb.toString();
            mVB = vbb.asFloatBuffer();
            mVB.put(coords);
            mVB.position(0);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //gl.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
           /* //表示清除颜色设为某颜色
            gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            //帧清除屏幕到黑色
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            //设置当前颜色
            gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            //每个顶点的坐标维数，必须是2, 3或者4，初始值是4
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVB);
            *//**
             * GL10.GL_POINTS : 只描绘各个独立的点
             * GL10.GL_LINE_STRIP : 前后两个顶点用线段连接，但不闭合（最后一个点与第一个点不连接）
             * GL10.GL_LINE_LOOP : 前后两个顶点用线段连接，并且闭合（最后一个点与第一个点有线段连接）
             * GL10.GL_TRIANGLES : 每隔三个顶点绘制一个三角形的平面
             * 1.从哪个点起始
             * 2.长度
             *//*
            gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 4);*/


            updata();
            ClearRect(gl);
//            DrawWave(gl);
        }
    }

    private float CX;
    private float DX;
    private float[] lead = new float[]{-3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F};
    private float xunit = 0.014F;
    private float[] rect = new float[]{-6.0F * xunit, 6.0F, 6.0F * xunit, 6.0F, 6.0F * xunit, -6.0F, -6.0F * xunit, 6.0F, 6.0F * xunit, -6.0F, -6.0F * xunit, -6.0F};

    public void updata() {
        this.DX = -9.0F;
        this.CX = -8.6F;
    }


    public void ClearRect(GL10 gl) {
        //使特征矩阵代替当前矩阵
        gl.glLoadIdentity();
        //坐标系平移 ()
        gl.glTranslatef(this.CX, 0.0F, -5.0F);
        //坐标系偏转
//        gl.glRotatef(0.0F, 1.0F, 0.0F, 0.0F);
        //设置颜色
        gl.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
        //计算顶点buffer
        FloatBuffer verBuffer = DrawUtils.makeFloatBuffer(this.rect);
        gl.glVertexPointer(2, 5126, 0, verBuffer);
        gl.glEnableClientState('聴');
        gl.glDrawArrays(4, 0, 3);
        gl.glDrawArrays(4, 3, 3);
        gl.glDisableClientState('聴');
        gl.glFinish();
        this.CX += 6.0F * this.xunit;
        if (this.CX >= 10.12F) {
            this.CX = -9.0F;
        }
    }

    public void DrawWave(GL10 gl) {

        //用特征矩阵代替当前矩阵
        gl.glLoadIdentity();
        // X坐标向左移动 DX , Z坐标向里移动5f
        gl.glTranslatef(DX, 0.0F, -5.0F);

        gl.glRotatef(0.0F, 1.0F, 0.0F, 0.0F);
        gl.glColor4f(1.0f, 1.0f, 0.2f, 1.0f);

        FloatBuffer verBuffer;
        ByteBuffer bb = ByteBuffer.allocateDirect(this.lead.length * 4);
        bb.order(ByteOrder.nativeOrder());
        verBuffer = bb.asFloatBuffer();
        verBuffer.put(this.lead);
        verBuffer.position(0);

        gl.glVertexPointer(2, 5126, 0, verBuffer);
        //启用某功能
        gl.glEnableClientState('聴');
        gl.glDrawArrays(3, 0, 7);
        gl.glDrawArrays(3, 7, 7);
        gl.glDrawArrays(3, 14, 7);
        gl.glDrawArrays(3, 21, 7);
        gl.glDrawArrays(3, 28, 7);
        gl.glDrawArrays(3, 35, 7);
        gl.glDrawArrays(3, 42, 7);
        gl.glDrawArrays(3, 49, 7);
        gl.glDrawArrays(3, 56, 7);
        gl.glDrawArrays(3, 63, 7);
        gl.glDrawArrays(3, 70, 7);
        gl.glDrawArrays(3, 77, 7);
        //禁用某功能
        gl.glDisableClientState('聴');
        gl.glFinish();
        DX += 6.0F * xunit;//0.014  120次为一周期
        if (DX >= 10.12F) {
            DX = -9.0F;
        }
    }

}
