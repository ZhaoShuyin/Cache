package com.example.myapplication.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.AttributeSet;
import android.util.Log;

import com.example.myapplication.render.MyRender;
import com.example.myapplication.render.OpenGLRender;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Title 尝试 Gl 渲染 波形图
 * @Date 2019/9/24
 * @Autor Zsy
 * 参考:
 * 0.导入使用
 * https://www.jianshu.com/p/6581703e1d98
 * 1.基本方法介绍
 * https://blog.csdn.net/luoshiyong123/article/details/82744611
 * 2.坐标系介绍
 * https://blog.csdn.net/qq_31726827/article/details/51265186
 * 3.平移和旋转
 * https://blog.csdn.net/digitalkee/article/details/79211295
 * 4.简单设置
 * https://www.cnblogs.com/arxive/p/7002305.html
 * 5.原理 openGL EL
 * https://wenku.baidu.com/view/27a8a847960590c69fc3767a.html
 * 6.多图层
 * https://blog.csdn.net/u010462297/article/details/50589991
 * 7.中文API
 * https://blog.csdn.net/flycatdeng/article/details/82588903
 * 8.学习网站
 * https://learnopengl-cn.readthedocs.io/zh/latest/
 * 9.glMatrixMode()方法
 * https://blog.csdn.net/jiangdf/article/details/8460012
 * 10.opengl官网
 * https://www.khronos.org/opengl/wiki
 * 11.帧缓存对象 FBO
 *
 */
public class GLEcgView extends GLSurfaceView {

    private String TAG = "ZGLSurView";

    public GLEcgView(Context context) {
        super(context);
    }

    public GLEcgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG, "GLSView: 构造函数");
        //设置渲染器
//        setRenderer(new OpenGLRender());
        setRenderer(new MyRender());
        //设置非主动渲染(须在 setRenderer() 之后)
        //1.非主动渲染RENDERMODE_WHEN_DIRTY(0) 2.RENDERMODE_CONTINUOUSLY主动渲染(1)
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    Thread.sleep(500);
                    requestRender();//渲染
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}