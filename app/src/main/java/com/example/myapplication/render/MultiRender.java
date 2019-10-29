package com.example.myapplication.render;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Title 测试多层渲染
 * @date 2019/10/29
 * @autor Zsy
 */
public class MultiRender implements GLSurfaceView.Renderer{
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //设置图层透明度
        //gl.glAlphaFunc();
        gl.
    }
}
