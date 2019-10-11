package com.example.ecg.rotate;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {
	private static final String LOG = OpenGLRenderer.class.getSimpleName();
	private float red = 0.9f;
	private float green = 0.2f;
	private float blue = 0.2f;

	private Triangle tr;

	private float xAngle;
	private float yAngle;

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		tr = new Triangle();

		/**
		 * 设置OpenGL使用vertex数组来画
		 */
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		/**
		 * 设置颜色来自数组
		 */
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		gl.glViewport(0, 0, width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		/**
		 * 我们通过glClearColor()方法为底色定义了颜色。
		 * 底色是在我们能看到的所有东西的后面，所以所有在底色后面的东西都是不可见的。
		 * 可以想象这种东西为浓雾，挡住了所有的东西。
		 */
		gl.glClearColor(red, green, blue, 1.0f);

		//清除颜色的Buffer然后让现实上面我们通过glClearColor来定义的颜色
		/**
		 * 为了让颜色变化可见，我们必须调用glClear()以及颜色缓冲的Mask来清空buffer，
		 * 然后为我们的底色使用新的底色。
		 */
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		/**
		 *  重置当前的模型观察矩阵
		 *  近似于重置。它将所选的矩阵状态恢复成其原始状态
		 */
		gl.glLoadIdentity();
		/**
		 * 旋转，四个参数分别是旋转度、x轴、y轴、z轴
		 * 后面三个值来决定围绕那个轴线来旋转
		 */
		gl.glRotatef(xAngle, 1f, 0f, 0f);
		gl.glRotatef(yAngle, 0f, 1f, 0f);
		/**
		 * 第一个参数是大小，也是顶点的维数。我们使用的是x,y,z三维坐标。
		 * 第二个参数，GL_FLOAT定义buffer中使用的数据类型。
		 * 第三个变量是0，是因为我们的坐标是在数组中紧凑的排列的，没有使用offset。
		 * 最后,第四个参数顶点缓冲。
		 */
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, tr.getVertexBuffer());
		/**
		 * 参数4表示RGBA(RGBA刚好是四个值），其余的几个参数大家都比较熟悉了。
		 */
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, tr.getColorBuffer());
		/**
		 * 将所有这些元素画出来。第一个参数定义了什么样的图元将被画出来。
		 * 第二个参数定义有多少个元素，
		 * 第三个是indices使用的数据类型。
		 * 最后一个是绘制顶点使用的索引缓冲。
		 */
		gl.glDrawElements(GL10.GL_TRIANGLES, tr.getNumberOfPoint(), GL10.GL_UNSIGNED_SHORT, tr.getIndexBuffer());
	}

	public float getXAngle() {
		return xAngle;
	}

	public float getYAngle() {
		return yAngle;
	}

	public void setYAngle(float angle) {
		yAngle = angle;
	}

	public void setXAngle(float angle) {
		xAngle = angle;
	}

	/**
	 * 设置颜色的值
	 * @param r Red值
	 * @param g Green值
	 * @param b Blue值
	 */
	public void setColor(float r,float g,float b)
	{
		red = r;
		green = g;
		blue = b;
	}

	/**
	 * 设置三角形的旋转度
	 * @param x x轴上的旋转度
	 * @param y y轴上的旋转度
	 */
	public void setAngle(float x,float y)
	{
		xAngle = x;
		yAngle = y;
	}
}
