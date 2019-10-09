package serial.jni;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GLActivity2 extends Activity {
    private GLView glView;
    private DataUtils data;
    private Context mContext;
    private String strCase;

    private String TAG = "ecgdata";

    private boolean isFinishActivity;
    private int m = 0;
    private int n = 0;
    private int p = 0;
    private int q = 0;
    private Button btnStartConnect;      //开始连接
    private Button btnStopConnect;       //断开连接

    private Button btnStartECGRenderer;  //开始绘图
    private Button btnStopECGRenderer;   //停止绘图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glsurfaceview);
        mContext = this;
        // 取得窗口属性
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        btnStartConnect = (Button) findViewById(R.id.btnStartConnect);
        btnStopConnect = (Button) findViewById(R.id.btnStopConnect);

        btnStartECGRenderer = (Button) findViewById(R.id.btnStartRenderer);
        btnStopECGRenderer = (Button) findViewById(R.id.btnStopRenderer);

        btnStartECGRenderer.setEnabled(false);
        //开始连接
        btnStartConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStartConnect.setEnabled(false);
                data.gatherStart(new NativeMsg());
            }
        });
        //断开连接
        btnStopConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStartECGRenderer.setEnabled(false);
                data.gatherEnd();
                glView.stopRenderer();
                btnStartConnect.setEnabled(true);
            }
        });
    /*    //开始绘图
        btnStartECGRenderer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据源
                mEcgQueue = new ConcurrentLinkedQueue<Short>();
                //绑定数据
                glView.setEcgDataBuf(mEcgQueue);
                //开始绘图
                glView.startRenderer();
            }
        });
        //停止绘图
        btnStopECGRenderer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                glView.stopRenderer();
                mEcgQueue = null;
            }
        });*/

        Intent intent = null;
        intent = getIntent();
        if (intent != null) {
            String dAddress = intent.getExtras().getString("device_address");
            Log.e(TAG, "开始绑定设备 MAC: " + dAddress);
            data = new DataUtils(mContext, "00:04:3E:51:FA:E7",
                    new BluConnectionStateListener() {
                        @Override
                        public void OnBluConnectionInterrupted() {
                            Log.e(TAG, "OnBluConnectionInterrupted: ");
                        }

                        @Override
                        public void OnBluConnectSuccess() {
                            Log.e(TAG, "OnBluConnectSuccess: ");
                        }

                        @Override
                        public void OnBluConnectStart() {
                            Log.e(TAG, "OnBluConnectStart: ");
                        }

                        @Override
                        public void OnBluConnectFaild() {
                            Log.e(TAG, "OnBluConnectFaild: ");

                        }
                    });
        }
        // 以下关于glView操作为必要操作，请不要更改
        glView = (GLView) this.findViewById(R.id.GLWave);

        glView.setMsg(mHandler);
        glView.setZOrderOnTop(true);
        glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        strCase = "AECG";
    }


    private static final int MESSAGE_UPDATE_HR = 0;
    private static final int MESSAGE_UPDATE_LeadOff = 1;
    //连接回调 handler
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: " + msg.what);
            switch (msg.what) {

            }
        }
    };

    /**
     * 连接蓝牙的回调
     */
    class NativeMsg extends NativeCallBack {

        @Override
        public void callHRMsg(short hr) {// 心率
            mHandler.obtainMessage(MESSAGE_UPDATE_HR, hr).sendToTarget();
        }

        @Override
        public void callLeadOffMsg(String flagOff) {// 导联脱落
            // Log.e("LF", flagOff);
            mHandler.obtainMessage(MESSAGE_UPDATE_LeadOff, flagOff).sendToTarget();
        }

        @Override
        public void callProgressMsg(short progress) {// 文件存储进度百分比 progress%
            Log.e("progress", "" + progress);
        }

        @Override
        public void callCaseStateMsg(short state) {
            if (state == 0) {
                Log.e("Save", "start");// 开始存储文件
            } else {
                Log.e("Save", "end");// 存储完成
            }
        }

        @Override
        public void callHBSMsg(short hbs) {// 心率 hbs = 1表示有心跳
            // Log.e("HeartBeat", "Sound"+hbs);
        }

        @Override
        public void callBatteryMsg(short per) {// 采集盒电量
            // Log.e("Battery", ""+per);
        }

        @Override
        public void callCountDownMsg(short per) {// 剩余存储时长
            // Log.e("CountDown", ""+per);
        }

        @Override
        public void callWaveColorMsg(boolean flag) {
            Log.e("WaveColor", "" + flag);
            if (flag) {
                // 波形稳定后颜色变为绿色
                glView.setRendererColor(0, 1.0f, 0, 0);
            }
        }

        @Override
        public void callEcgWaveDataMsg(short[] wave) {
            // TODO Auto-generated method stub
            if (mEcgQueue != null) {
                for (int i = 48; i < 60; i++) {
                    mEcgQueue.offer(wave[i]);
                }
                Log.e("ecgdata", " 数据 " + mEcgQueue.size());
            }
        }
    }

    private ConcurrentLinkedQueue<Short> mEcgQueue;
}
