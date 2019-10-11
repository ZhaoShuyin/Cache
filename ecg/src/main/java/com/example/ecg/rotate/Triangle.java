package com.example.ecg.rotate;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Triangle {
	//三角形顶点的Buffer
	private FloatBuffer vertexBuffer;
	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	public void setVertexBuffer(FloatBuffer vertexBuffer) {
		this.vertexBuffer = vertexBuffer;
	}

	//三角形颜色数组的Buffer
	private FloatBuffer colorBuffer;
	public FloatBuffer getColorBuffer() {
		return colorBuffer;
	}

	public void setColorBuffer(FloatBuffer colorBuffer) {
		this.colorBuffer = colorBuffer;
	}

	//三角形索引值得Buffer
	private ShortBuffer indexBuffer;

	public ShortBuffer getIndexBuffer() {
		return indexBuffer;
	}

	public void setIndexBuffer(ShortBuffer indexBuffer) {
		this.indexBuffer = indexBuffer;
	}

	public Triangle() {
		// TODO Auto-generated constructor stub
		initTriangle();
	}

	private int numberOfPoint;

	public int getNumberOfPoint() {
		return numberOfPoint;
	}

	public void setNumberOfPoint(int numberOfPoint) {
		this.numberOfPoint = numberOfPoint;
	}

	//初始化三角形
	private void initTriangle()
	{
		float[] coords = {
				-0.5f,-0.5f,0f,//x1,y1,z1
				0.5f,0.5f,0f,//x2,y2,z2
				0f,0.5f,0f //x3,y3,z3
		};

		float[] colors = {
				1f, 0f, 0f, 1f, // point 1
				0f, 1f, 0f, 1f, // point 2
				0f, 0f, 1f, 1f, // point 3
		};

		short[] indicesArray = {
				0, 2, 1, // rbg
		};

		numberOfPoint = coords.length/3;

		//float类型有四个字节，分配内存
		ByteBuffer vbb = ByteBuffer.allocateDirect(coords.length*4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();

		//short类型有2个字节，分配内存
		ByteBuffer ibb = ByteBuffer.allocateDirect(indicesArray.length*2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();

		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asFloatBuffer();

		vertexBuffer.put(coords);
		indexBuffer.put(indicesArray);
		colorBuffer.put(colors);

		vertexBuffer.position(0);
		indexBuffer.position(0);
		colorBuffer.position(0);
	}

}
