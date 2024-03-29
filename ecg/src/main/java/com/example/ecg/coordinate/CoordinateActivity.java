package com.example.ecg.coordinate;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Title glsurfaceview opengl 坐标系 demo
 * @date 2019/10/10
 * @autor Zsy
 * 坐标系
 * https://blog.csdn.net/qq_31726827/article/details/51265186
 */

public class CoordinateActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(new PumpKinRenderer());
        //在设置render之后调用
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(glSurfaceView);
    }

    public class PumpKinRenderer implements GLSurfaceView.Renderer {

        private PumpKin pumpKin;

        public PumpKinRenderer() {
            pumpKin = new PumpKin();
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            gl.glClearDepthf(1.0f);
            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glDepthFunc(GL10.GL_LEQUAL);
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glDisable(GL10.GL_DITHER);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

            if (height == 0) {
                height = 1;
            }
            float aspect = (float) width / height;

            gl.glViewport(0, 0, width, height);

            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            GLU.gluPerspective(gl, 0, aspect, 0.1f, 100.0f);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();

        }

        @Override
        public void onDrawFrame(GL10 gl) {

            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

              //恢复初始坐标系，重置当前指定的矩阵为单位矩阵
//            gl.glLoadIdentity();
            //平移
//            gl.glTranslatef(0.0f, 0.0f, -2f);
            //绕Y轴顺时针旋转30度
            gl.glRotatef(10,1,-1,0);
            //绕X逆时针旋转30度
//            gl.glRotatef(30,1,0,0);
            pumpKin.draw(gl);

        }
    }

    public class PumpKin {
        private FloatBuffer vertexsBuffer;
        private FloatBuffer colorsBuffer;

        private ByteBuffer indicesBuffer;

        private float vertexs[] = {
                0.0f, 0.0f, 0.0f,//原点
                1.0f, 0.0f, 0.0f,//X向右线最右点
                0.0f, 0.0f, 0.0f,//原点
                0.0f, 1.0f, 0.0f,//Y向上线最上点
                0.0f, 0.0f, 0.0f,//原点
                0.0f, 0.0f, 1.0f//Z向外线最外点
        };

        private float colors[] = {
                1.0f, 1.0f, 1.0f, 1.0f,//起点白色
                1.0f, 0.0f, 0.0f, 1.0f,//终点红色
                1.0f, 1.0f, 1.0f, 1.0f,//起点白色
                0.0f, 1.0f, 0.0f, 1.0f,//终点绿色
                1.0f, 1.0f, 1.0f, 1.0f,//起点白色
                0.0f, 0.0f, 1.0f, 1.0f//终点蓝色
        };

        private byte indices[] = {0, 1, 2};

        public PumpKin() {

            ByteBuffer vbb = ByteBuffer.allocateDirect(vertexs.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            vertexsBuffer = vbb.asFloatBuffer();
            vertexsBuffer.put(vertexs);
            vertexsBuffer.position(0);

            ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
            cbb.order(ByteOrder.nativeOrder());
            colorsBuffer = cbb.asFloatBuffer();
            colorsBuffer.put(colors);
            colorsBuffer.position(0);

        }

        public void draw(GL10 gl) {

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

            //设置顶点坐标
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexsBuffer);
            //设置顶点颜色
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorsBuffer);
            //绘制顶点
            gl.glDrawArrays(GL10.GL_LINES, 0, vertexs.length / 3);
            //以GL_COLOR_ARRAY为参数来启用和禁止颜色矩阵，颜色矩阵初始值为禁用，
            // 不允许glDrawArrays方法和glDrawElements方法调用
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        }

    }


    /**
     * 使用时需要从Java的大端字节序(BigEdian) 转换为 小端字节序（LittleEdian）
     *
     * @param arr
     * @return
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

    private IntBuffer bufferInt(int[] arr) {
        IntBuffer mBuffer;
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个int占4个字节
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
        // 数组排列用nativeOrder
        qbb.order(ByteOrder.nativeOrder());
        mBuffer = qbb.asIntBuffer();
        mBuffer.put(arr);
        mBuffer.position(0);
        return mBuffer;
    }

    private ShortBuffer bufferShort(short[] arr){
        ShortBuffer mBuffer;
        // 初始化ByteBuffer，长度为arr数组的长度*2，因为一个short占2个字节
        ByteBuffer dlb = ByteBuffer.allocateDirect(arr.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        mBuffer = dlb.asShortBuffer();
        mBuffer.put(arr);
        mBuffer.position(0);
        return mBuffer;
    }

}
