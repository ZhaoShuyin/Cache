package com.example.ecg.rotate;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class OpenGLView extends GLSurfaceView {
	private static final String LOG = OpenGLView.class.getSimpleName();
	private OpenGLRenderer or;

	private float xAngle;
	private float yAngle;

	public OpenGLView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		or = new OpenGLRenderer();
		setRenderer(or);
	}

	// 监听touch事件，用来改变颜色、改变旋转度
	public boolean onTouchEvent(final MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE
				|| event.getAction() == MotionEvent.ACTION_DOWN) {
			queueEvent(new Runnable() {
				public void run() {
					or.setColor(event.getX() / getWidth(), event.getY()
							/ getHeight(), 1.0f);
				}
			});
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xAngle = event.getX();
			yAngle = event.getY();
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			final float xdiff = (xAngle - event.getX());
			final float ydiff = (yAngle - event.getY());
			queueEvent(new Runnable() {
				public void run() {
					or.setXAngle(or.getXAngle() + ydiff);
					or.setYAngle(or.getYAngle() + xdiff);
				}
			});
			xAngle = event.getX();
			yAngle = event.getY();
		}
		return true;
	}
}
