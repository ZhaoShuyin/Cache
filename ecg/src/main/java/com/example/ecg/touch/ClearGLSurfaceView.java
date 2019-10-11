package com.example.ecg.touch;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * GLSurfaceView
 */
class ClearGLSurfaceView extends GLSurfaceView {
    ClearRenderer mRenderer;
    public ClearGLSurfaceView(Context context) {
        super(context);
        mRenderer = new ClearRenderer();
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        //传递触摸事件
        queueEvent(new Runnable() {
            public void run() {
                mRenderer.setColor(event.getX() / getWidth(),
                        event.getY() / getHeight(), 1.0f);
            }
        });
        return true;
    }
}