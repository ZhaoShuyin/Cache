package com.example.myapplication.shape;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

public class Circle {
 
	private Context context;
    private FloatBuffer vertexData;
	// 定义圆心坐标
	private float x;
	private float y;
	// 半径
	private float r;
	// 三角形分割的数量
	private int count = 40;
	// 每个顶点包含的数据个数 （ x 和 y ）
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int BYTES_PER_FLOAT = 4;
 
	private static final String U_COLOR = "u_Color";
	private static final String A_POSITION = "a_Position";
	private int program;
	private int uColorLocation;
	private int aPositionLocation;
	
	public Circle(Context context) {
		this.context = context;
		x = 0f;
		y = 0f;
		r = 0.6f;
		initVertexData();
	}
 
	private void initVertexData() {
	/*	// 顶点的个数，我们分割count个三角形，有count+1个点，再加上圆心共有count+2个点
		final int nodeCount = count + 2;
		float circleCoords[] = new float[nodeCount * POSITION_COMPONENT_COUNT];
		// x y
		int offset = 0;
		circleCoords[offset++] = x;// 中心点
		circleCoords[offset++] = y;
		for (int i = 0; i < count + 1; i++) {
			float angleInRadians = ((float) i / (float) count)
					* ((float) Math.PI * 2f);
			circleCoords[offset++] = x + r * (float)Math.sin(angleInRadians);
			circleCoords[offset++] = y + r * (float)Math.cos(angleInRadians);
		}
		// 为存放形状的坐标，初始化顶点字节缓冲
		ByteBuffer bb = ByteBuffer.allocateDirect(
		// (坐标数 * 4)float占四字节
				circleCoords.length * BYTES_PER_FLOAT);
		// 设用设备的本点字节序
		bb.order(ByteOrder.nativeOrder());
		// 从ByteBuffer创建一个浮点缓冲
		vertexData = bb.asFloatBuffer();
		// 把坐标们加入FloatBuffer中
		vertexData.put(circleCoords);
		// 设置buffer，从第一个坐标开始读
		vertexData.position(0);
		
		getProgram();
		
		uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
		aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
		
		GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
				GLES20.GL_FLOAT, false, 0, vertexData);
 
		GLES20.glEnableVertexAttribArray(aPositionLocation);*/
	}
	
	private void getProgram(){
		/*String vertexShaderSource = TextResourceReader
				.readTextFileFromResource(context, R.raw.simple_vertex_shader);
		String fragmentShaderSource = TextResourceReader
				.readTextFileFromResource(context, R.raw.simple_fragment_shader);
 
		int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader = ShaderHelper
				.compileFragmentShader(fragmentShaderSource);
 
		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
 
		if (LoggerConfig.ON) {
			ShaderHelper.validateProgram(program);
		}
 
		GLES20.glUseProgram(program);*/
	}
	
	public void draw(){
		GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, count +2);
	}
}