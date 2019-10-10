package com.example.ecg.test;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Title com.example.ecg.test
 * @date 2019/10/10
 * @autor Zsy
 * 坐标系
 * https://blog.csdn.net/qq_31726827/article/details/51265186
 */

public class TestActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(new PumpKinRenderer());
        setContentView(glSurfaceView);
    }

    public class PumpKinRenderer implements GLSurfaceView.Renderer {

        private PumpKin pumpKin;

        public PumpKinRenderer(){
            pumpKin=new PumpKin();
        }
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            gl.glClearColor(0.0f,0.0f,0.0f,1.0f);
            gl.glClearDepthf(1.0f);
            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glDepthFunc(GL10.GL_LEQUAL);
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_NICEST);
            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glDisable(GL10.GL_DITHER);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

            if(height==0){
                height=1;
            }
            float aspect=(float)width/height;

            gl.glViewport(0,0,width,height);

            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.0f);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();

        }

        @Override
        public void onDrawFrame(GL10 gl) {

            gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);

            gl.glLoadIdentity();
            gl.glTranslatef(0.0f,0.0f,-2f);
            //gl.glRotatef(45,1.0f,1.0f,1.0f);
            pumpKin.draw(gl);

        }
    }

    public class PumpKin {
        private FloatBuffer vertexsBuffer;
        private FloatBuffer colorsBuffer;

        private ByteBuffer indicesBuffer;

        private float vertexs[]={
                0.0f,0.0f,0.0f,
                1.0f,0.0f,0.0f,
                0.0f,0.0f,0.0f,
                0.0f,1.0f,0.0f,
                0.0f,0.0f,0.0f,
                0.0f,0.0f,1.0f
        };

        private float colors[]={
                1.0f,0.0f,0.0f,1.0f,
                1.0f,0.0f,0.0f,1.0f,
                0.0f,1.0f,0.0f,1.0f,
                0.0f,1.0f,0.0f,1.0f,
                0.0f,0.0f,1.0f,1.0f,
                0.0f,0.0f,1.0f,1.0f
        };

        private byte indices[]={0,1,2};

        public PumpKin(){

            ByteBuffer vbb=ByteBuffer.allocateDirect(vertexs.length*4);
            vbb.order(ByteOrder.nativeOrder());
            vertexsBuffer=vbb.asFloatBuffer();
            vertexsBuffer.put(vertexs);
            vertexsBuffer.position(0);

            ByteBuffer cbb=ByteBuffer.allocateDirect(colors.length*4);
            cbb.order(ByteOrder.nativeOrder());
            colorsBuffer=cbb.asFloatBuffer();
            colorsBuffer.put(colors);
            colorsBuffer.position(0);

        }

        public void draw(GL10 gl){

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

            gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexsBuffer);
            gl.glColorPointer(4,GL10.GL_FLOAT,0,colorsBuffer);

            gl.glDrawArrays(GL10.GL_LINES,0,vertexs.length/3);

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        }

    }


}
