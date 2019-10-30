package com.example.myapplication.render;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.myapplication.utils.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Title com.example.myapplication.render
 * @date 2019/10/30
 * @autor Zsy
 */

public class FBORender implements GLSurfaceView.Renderer {
    float[] floats = new float[]{
            0.0f, 0.1f, 0, 0f
            - 0.1f, -0.1f, 0.0f,
            0.1f, -0.1f, 0.0f
    };
    private FloatBuffer floatBuffer;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        floatBuffer = BufferUtil.floatToBuffer(floats);
        gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }

    IntBuffer intBuffer;
    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, floatBuffer);
        gl.glLoadIdentity();
        gl.glColor4f(1.0f, 0, 0, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);//GL_TRIANGLE_STRIP
        gl.glGenTextures(1,intBuffer);
    }
}
