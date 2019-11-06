package com.example.myapplication.render;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.myapplication.utils.BufferUtil;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_TEXTURE_2D;

/**
 * @Title com.example.myapplication.render
 * @date 2019/10/30
 * @autor Zsy
 * GL10方法解析
 * https://blog.csdn.net/yegucheng2618/article/details/39055403
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
        Log.e("gltest", "onSurfaceCreated: " );
        floatBuffer = BufferUtil.floatToBuffer(floats);
        gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        //保存
       /* gl.glCopyTexImage2D(
                GL10.GL_TEXTURE_2D,
                0,
                GL10.GL_ALPHA,
                0,
                0,
                100,
                100,
                0);*/
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e("gltest", "onSurfaceChanged: " );
        gl.glViewport(0, 0, width, height);

    }

    int i = 0;
    Buffer buffer;
    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e("gltest", "onDrawFrame" + (i++));

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, floatBuffer);
        gl.glLoadIdentity();
        gl.glColor4f(1.0f, 0, 0, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);//GL_TRIANGLE_STRIP
        gl.glFinish();
        /** 取出显示
         int target,
         int level,
         int xoffset,
         int yoffset,
         int width,
         int height,
         int format,
         int imageSize,
         java.nio.Buffer data
         */
       /* gl.glCompressedTexSubImage2D(
                GL10.GL_TEXTURE_2D,
                0,
                -100,
                -100,
                100,
                100,
                0,
                0,
                null
        );*/


        /** 保存
         int target,
         int level,
         int internalformat,
         int x,
         int y,
         int width,
         int height,
         int border
         */
//        gl.glCopyTexSubImage2D();
       /* gl.glCopyTexImage2D(
                GL10.GL_TEXTURE_2D,
                0,
                GL10.GL_ALPHA,
                0,
                0,
                100,
                100,
                0);*/
    }

    /**
     * target
     *指定活动纹理单元的目标纹理，必须为 GL_TEXTURE_2D, GL_TEXTURE_CUBE_MAP_POSITIVE_X,GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
     *GL_TEXTURE_CUBE_MAP_POSITIVE_Y, GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
     *GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 或 GL_TEXTURE_CUBE_MAP_NEGATIVE_Z.

     level
     指定细节级别数，0级表示基本图像级别，n级表示第n级mipmap缩小图。

     internalformat
     指定纹理的内部格式。必须为下列符号常量之一：GL_ALPHA, GL_LUMINANCE,GL_LUMINANCE_ALPHA, GL_RGB, 或者 GL_RGBA.

     x, y
     指定待拷贝像素矩形区域左下角坐标

     width
     指定纹理图像的宽度。所有OpenGL实现支持的2D纹理图像都至少为64纹素（texel）宽，立方体映射纹理图像都至少为16纹素宽。

     height
     指明纹理图像的高度，所有OpenGL实现支持的2D纹理图像都至少为64纹素（texel）高，立方体映射纹理图像都至少为16纹素高。

     border
     指定边框宽度，必须为零（梦维：没错，你必须传入0，因为这是OpenGL ES）。
     */
}
