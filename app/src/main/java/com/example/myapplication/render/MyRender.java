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

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
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

        /*
         * glEnable(可设置多种参数)
         * GL10.GL_DEPTH_TEST
         * 用来开启更新深度缓冲区的功能，
         * 也就是，如果通过比较后深度值发生变化了，会进行更新深度缓冲区的操作。
         * 启动它，OpenGL就可以跟踪再Z轴上的像素，
         * 这样，它只会再那个像素前方没有东西时，才会绘画这个像素。
         */
        gl.glEnable(GL10.GL_DEPTH_TEST);

        /**
         * glDepthFunc 目标像素与当前像素在z方向上值大小比较”的函数，
         *             符合该函数关系的目标像素才进行绘制，否则对目标像素不予绘制
         *  GL_NEVER,不通过（输入的深度值不取代参考值）
         *  GL_LESS,如果输入的深度值小于参考值，则通过
         *  GL_EQUAL,如果输入的深度值等于参考值，则通过
         *  GL_LEQUAL,如果输入的深度值小于或等于参考值，则通过
         *  GL_GREATER,如果输入的深度值大于参考值，则通过
         *  GL_NOTE_QUAL,如果输入的深度值不等于参考值，则通过
         *  GL_GEQUAL,如果输入的深度值大于或等于参考值，则通过
         *  GL_ALWAYS,总是通过（输入的深度值取代参考值）
         */
        gl.glDepthFunc(GL10.GL_LEQUAL);
        /**
         ***<target>
         *GL_FOG_HINT：
         *         指定雾化计算的精度。
         *         如果OpenGL实现不能有效的支持每个像素的雾化计算，
         *         则GL_DONT_CARE和GL_FASTEST雾化效果中每个定点的计算。
         *GL_LINE_SMOOTH_HINT：
         *         指定反走样线段的采样质量。
         *         如果应用较大的滤波函数，GL_NICEST在光栅化期间可以生成更多的像素段。
         *GL_PERSPECTIVE_CORRECTION_HINT：
         *         指定颜色和纹理坐标的差值质量。
         *         如果OpenGL不能有效的支持透视修正参数差值，
         *         那么GL_DONT_CARE 和 GL_FASTEST可以执行颜色、纹理坐标的简单线性差值计算。
         *GL_POINT_SMOOTH_HINT：
         *         指定反走样点的采样质量，如果应用较大的滤波函数，
         *         GL_NICEST在光栅化期间可以生成更多的像素段。
         *GL_POLYGON_SMOOTH_HINT：
         *         指定反走样多边形的采样质量，
         *         如果应用较大的滤波函数，GL_NICEST在光栅化期间可以生成更多的像素段。
         ***<mod>：指定所采取行为的符号常量，可以是以下值
         *GL_FASTEST：选择速度最快选项。
         *GL_NICEST：选择最高质量选项。
         *GL_DONT_CARE：对选项不做考虑。
         */
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        //开启可修改数据模式
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

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
            //设置投影视角
            GLU.gluPerspective(gl, 0, aspect, 0.1f, 100.0f);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();*/

        //GL_PROJECTION 投影, GL_MODELVIEW 模型视图, GL_TEXTURE 纹理
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        /**
         *设置投影视角
         */
        float ratio = (float) width / height;
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        /**
         * 设置渲染模式 (折线 GL_LINE_STRIP)
         */
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