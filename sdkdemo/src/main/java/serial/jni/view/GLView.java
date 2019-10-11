//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package serial.jni.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Process;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import java.util.concurrent.ConcurrentLinkedQueue;

import serial.jni.BackgroundUtils;

public class GLView extends GLSurfaceView {
    public static Handler msg = null;
    public static boolean isGather = false;
    private static boolean isRenderer = false;
    private MyRenderer myRenderer;
    private int vWidth;
    private int vHeight;
    private BackgroundUtils bk = new BackgroundUtils();
    private GLView.WaveRenderer waveRenderer;

    public GLView(Context context, AttributeSet attr) {
        super(context, attr);
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.myRenderer = new MyRenderer();
        this.setRenderer(this.myRenderer);
        this.setRenderMode(0);
    }

    public void setMsg(Handler message) {
        msg = message;
    }

    public void setRendererColor(final float r, final float g, final float b, final float a) {
        this.queueEvent(new Runnable() {
            public void run() {
                GLView.this.myRenderer.setWaveColor(r, g, b, a);
            }
        });
    }

    public void startRenderer() {
        if(!isRenderer) {
            isGather = true;
            isRenderer = true;
            if(this.waveRenderer == null) {
                this.waveRenderer = new GLView.WaveRenderer();
            }

            this.myRenderer.setClearScreen(true);
            this.myRenderer.setEcgDataFlag(true);
            this.waveRenderer.start();
        }
    }

    public void stopRenderer() {
        if(isRenderer) {
            isGather = false;
            isRenderer = false;
            this.myRenderer.setEcgDataFlag(false);
            if(this.waveRenderer != null && this.waveRenderer.isAlive()) {
                this.waveRenderer.interrupt();
                this.waveRenderer = null;
            }

        }
    }

    public void onResume() {
        super.onResume();
        this.myRenderer.setWaveColor(1.0F, 1.0F, 0.2F, 1.0F);
        this.myRenderer.setEcgDataFlag(true);
        Log.e("GLView", "onResume");
    }

    public void onPause() {
        super.onPause();
        this.stopRenderer();
        this.myRenderer.setEcgDataFlag(false);
        Log.e("GLView", "onPause");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        this.waveRenderer = null;
        Log.e("GLView", "surfaceDestroyed");
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        super.surfaceChanged(holder, format, width, height);
        this.vWidth = width;
        this.vHeight = height;
        Log.e("GL", this.vWidth + "+" + this.vHeight);
    }

    public void setBackground(int colorBK, int colorGrid) {
        this.setBackgroundDrawable(this.bk.getBackgroundDrawable(this.vWidth, this.vHeight, colorBK, colorGrid));
    }

    public void setEcgDataBuf(ConcurrentLinkedQueue<Short> data) {
        this.myRenderer.setEcgDataBuf(data);
    }

    class WaveRenderer extends Thread {
        WaveRenderer() {
        }

        public void run() {
            Process.setThreadPriority(-8);

            try {
                sleep(2000L);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }

            while(GLView.isRenderer) {
                GLView.this.requestRender();

                try {
                    sleep(20L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }
            }

        }
    }
}
