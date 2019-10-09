package com.example.ecg;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "GLSurfaceView", Toast.LENGTH_SHORT).show();
        setContentView(new MyGLSurfaceView(this));
    }

    public class MyGLSurfaceView extends GLSurfaceView {
        public MyGLSurfaceView(Context context) {
            this(context, null);
        }

        public MyGLSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setRenderer(new MyRender());
        }
    }

    private static class MyRender implements GLSurfaceView.Renderer {
        private FloatBuffer mVB;

        MyRender() {
            //坐标
            float coords[] = {
                    -0.5f, 0.5f, 0.0f,
                    -0.5f, -0.5f, 0.0f,
                    0.5f, -0.5f, 0.0f,
                    0.5f, -0.5f, 0.0f,
                    0.5f, 0.5f, 0.0f,
                    -0.5f, 0.5f, 0.0f
            };

            ByteBuffer vbb = ByteBuffer.allocateDirect(coords.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            mVB = vbb.asFloatBuffer();
            mVB.put(coords);
            mVB.position(0);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //顶点阵列
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVB);
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
        }
    }


}
