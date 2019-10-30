package com.example.myapplication.render;

import android.opengl.GLSurfaceView;

import com.example.myapplication.utils.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Title com.example.myapplication.render
 * @date 2019/10/30
 * @autor Zsy
 */

public class VBORender implements GLSurfaceView.Renderer {

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
        //初始化
        //glGenBuffersARB(1, &nVBOVertices); //生成一个句柄
        //glBindBufferARB(GL_ARRAY_BUFFER_ARB, nVBOVertices); //声明该句柄为一个vbo句柄，并选择之
        //glBufferDataARB(GL_ARRAY_BUFFER_ARB, sizeof(vertices), vertices,GL_STATIC_DRAW); //将顶点集上传至server端

        gl.glGenTextures(1,intBuffer);
//        gl.glBindTexture(GL10.GL_VERTEX_ARRAY,intBuffer);

        //使用
        //1. glEnableClientState(GL_VERTEX_ARRAY); //开始使用vbo
//        2. glBindBufferARB(GL_ARRAY_BUFFER_ARB, nVBOVertices);  //选择当前使用的vbo
//        3. glVertexPointer(3, GL_FLOAT, 0, BUFFER_OFFSET(0));  //指定vbo顶点格式
//        4. glDrawArrays( GL_TRIANGLES, 0, g_pMesh->m_nVertexCount ); //画吧
//        5. glDisableClientState(GL_VERTEX_ARRAY); //停止使用vbo

        //收尾
//        1. glDeleteBuffersARB(1,&nVBOVertices); //删除句柄，同时删除server端顶点缓冲

    }
}
