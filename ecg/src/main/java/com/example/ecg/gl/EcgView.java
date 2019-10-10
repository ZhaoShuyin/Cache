package com.example.ecg.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.ecg.view.DrawUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Title com.example.ecg.gl
 * @date 2019/10/10
 * @autor Zsy
 */

public class EcgView extends GLSurfaceView {

    float DX = -0.9f;
    float xunit = 0.014f;
    float[] lead = new float[]{-3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F, -3.0F * this.xunit, 0.0F, -2.0F * this.xunit, 0.0F, -this.xunit, 0.0F, 0.0F, 0.0F, this.xunit, 0.0F, 2.0F * this.xunit, 0.0F, 3.0F * this.xunit, 0.0F};

    public EcgView(Context context) {
        super(context);
    }

    public EcgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRenderer(new ZRender());
    }


    class ZRender implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            DrawTest(gl);
        }
    }

    float coords[] = {
            0.0f, 0.0f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    public void DrawTest(GL10 gl){

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
        gl.glDisableClientState('聴');
        gl.glFinish();
        DX += 6.0F * xunit;
        if (DX >= 10.12F) {
            DX = -9.0F;
        }
    }

}
