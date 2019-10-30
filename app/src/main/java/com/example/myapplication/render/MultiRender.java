package com.example.myapplication.render;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.myapplication.utils.BufferUtil;

import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Title 测试多层渲染
 * @date 2019/10/29
 * @autor Zsy
 */
public class MultiRender implements GLSurfaceView.Renderer {

    float[] floats = new float[]{
            0.0f, 0.1f, 0, 0f
            - 0.1f, -0.1f, 0.0f,
            0.1f, -0.1f, 0.0f,

            0.1f, -0.1f, 0.0f,
            0.1f, 0.1f, 0.0f,
            -0.1f, -0.1f, 0.0f,
            -0.1f, 0.1f, 0.0f,
    };
    private FloatBuffer floatBuffer;
    private FloatBuffer circleBuffer;


    private void init() {
        int count = 4;
        // 顶点的个数，我们分割count个三角形，有count+1个点，再加上圆心共有count+2个点
        float circleCoords[] = new float[(count + 1) * 2];
        // x y
        /*float r = 0.5F;
        circleCoords[0] = 0;// 中心点
        circleCoords[1] = 0;
        double angle = Math.PI * 2 / count ;
        for (int i = 0; i < count; i++) {
            circleCoords[2 * i + 2] = (float) (r * Math.sin(angle * i));
            circleCoords[2 * i + 3] = (float) (r * Math.cos(angle * i));
        }*/
        circleCoords[0] = 0;
        circleCoords[1] = 0;

        circleCoords[2] = 0.5f;
        circleCoords[3] = 0;

        circleCoords[4] = 0;
        circleCoords[5] = 0.5f;

        circleCoords[6] = -0.5f;
        circleCoords[7] = 0;

        circleCoords[8] = -0.5f;
        circleCoords[9] = -0.5f;
        Log.e("ZGLSurView", " " + Arrays.toString(circleCoords));
        circleBuffer = BufferUtil.floatToBuffer(circleCoords);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        floatBuffer = BufferUtil.floatToBuffer(floats);
        init();
        gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

       /* gl.glVertexPointer(3, GL10.GL_FLOAT, 0, floatBuffer);

        gl.glLoadIdentity();
        gl.glColor4f(1.0f, 0, 0, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);//GL_TRIANGLE_STRIP
        gl.glFinish();//先执行一段(等待GPU)

        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.5f, 0.0f);
        gl.glColor4f(0.0f, 1.0f, 0, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 3, 4);
        gl.glFlush();//再执行一段(不等GPU)*/

        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, circleBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 5);

    }
}
