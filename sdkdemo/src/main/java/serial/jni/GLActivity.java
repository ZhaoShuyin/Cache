package serial.jni;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

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

public class GLActivity extends Activity {
    private GLView glView;
    private DataUtils data;
    private Context mContext;
    private static UsbManager mUsbManager;
    private static UsbDevice mUsbDevice;
    private String strCase;

    private String TAG = "ecgdata";

    /**
     * 2018-05 isFinishActivity
     * 此变量根据实际使用场景设置，当返回执行关闭当前波形显示acticity时，由于可能蓝牙正在连接中，会返回连接失败的消息
     * 需要判断相关处理操作是否已经执行，避免多次执行打开某个activity的操作或对其它变量及状态的处理，造成不必要的错误
     */
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

        DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;                            //屏幕宽度
        int height = dm.heightPixels;                          //屏幕高度
        Log.e("Activity WxH", width + "x" + height);
        Log.e("Density", "" + dm.densityDpi);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Button b1 = (Button) this.findViewById(R.id.btn01);
        Button b2 = (Button) this.findViewById(R.id.btn02);
        Button b3 = (Button) this.findViewById(R.id.btn03);
        Button b4 = (Button) this.findViewById(R.id.btn04);
        Button b5 = (Button) this.findViewById(R.id.btn05);
        Button b6 = (Button) this.findViewById(R.id.btn06);
        Button b7 = (Button) this.findViewById(R.id.btn07);
        Button b8 = (Button) this.findViewById(R.id.btn08);
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
        //开始绘图
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
        });
        //all
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 分析数据文件，结果存储为xml
             /*int ret = data.ecgAnalyzeToXml(
                        Environment.getExternalStorageDirectory() + "/"
                                + strCase,
                        Environment.getExternalStorageDirectory()
                                + "/BECG_advice.xml",
                        Environment.getExternalStorageDirectory()
                                + "/conclusion.cn");*/
                //自定义心率上下限
                int ret = data.ecgAnalyzeToXml("/mnt/sdcard/AECG",
                        "/mnt/sdcard/BECG_advice.xml",
                        "/mnt/sdcard/conclusion.cn", 50, 60);

                Log.e("ANA", "ecgAnalyzeToXml ret = " + ret);
            }
        });
        //ac
        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // data.setGain(DataUtils.DISPLAY_GAIN__20);
                // 数据文件转换成aecg格式病例
                // int ret = data.ecgDataToAECG(
                // Environment.getExternalStorageDirectory() + "/"
                // + strCase + ".c8k",
                // Environment.getExternalStorageDirectory() + "/BECG.xml");
                int ret = data.ecgDataToAECG(
                        Environment.getExternalStorageDirectory() + "/"
                                + "20170419152220.c8k",
                        Environment.getExternalStorageDirectory() + "/BECG.xml");
/*
                18导时使用ecg18DataToAECG这个方法进行aecg转换
                int ret = data.ecg18DataToAECG(
                        Environment.getExternalStorageDirectory() + "/"
                                + "20170419152220.c8k",
                        Environment.getExternalStorageDirectory() + "/BECG.xml");
*/
                Log.e("aecg", "ecgDataToAECG ret = " + ret);
            }
        });
        //emg
        b3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//				data.setSpeed(DataUtils.DISPLAY_SPEED_50);
                if (n % 5 == 0) {
                    data.setFilter(0);
                    Toast.makeText(mContext, "滤波全关", Toast.LENGTH_LONG).show();
                } else if (n % 5 == 1) {
                    data.setFilter(1);
                    Toast.makeText(mContext, "工频滤波", Toast.LENGTH_LONG).show();
                } else if (n % 5 == 2) {
                    data.setFilter(2);
                    Toast.makeText(mContext, "肌电滤波", Toast.LENGTH_LONG).show();
                } else if (n % 5 == 3) {
                    data.setFilter(4);
                    Toast.makeText(mContext, "基线滤波", Toast.LENGTH_LONG).show();
                } else if (n % 5 == 4) {
                    data.setFilter(7);
                    Toast.makeText(mContext, "滤波全开", Toast.LENGTH_LONG).show();
                }
                n++;
            }
        });
        //bl
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                data.cancelCase();// 取消正在保存的文件
/*                if (!isFinishActivity) {
                    isFinishActivity = true;
                    Intent intent = new Intent();
                    intent.setClass(mContext, DeviceListActivity.class);
                    startActivity(intent);
                    finish();
                }*/
                finish();
            }
        });
        //no
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.saveCase(Environment.getExternalStorageDirectory() + "/",
                        strCase, 10);// 存储文件 参数为路径，文件名，存储秒数
            }
        });
        //displaymode 显示模式
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (q % 4 == 0) {
                    data.setDisplayMode(DataUtils.DISPLAY_MODE_12x1);
                    Toast.makeText(mContext, "12x1显示，仅12导有效！", Toast.LENGTH_LONG).show();
                } else if (q % 4 == 1) {
                    data.setDisplayMode(DataUtils.DISPLAY_MODE_6x2);
                    Toast.makeText(mContext, "6x2显示，仅12导有效！", Toast.LENGTH_LONG).show();
                } else if (q % 4 == 2) {
                    data.setDisplayMode(DataUtils.DISPLAY_MODE_2x6_LIMB);
                    Toast.makeText(mContext, "6导（肢体导联）显示", Toast.LENGTH_LONG).show();
                } else if (q % 4 == 2) {
                    data.setDisplayMode(DataUtils.DISPLAY_MODE_2x6_CHEST);
                    Toast.makeText(mContext, "6导（胸导导联）显示", Toast.LENGTH_LONG).show();
                }
                q++;
            }
        });
        //speed
        b7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (p % 5 == 0) {
                    data.setSpeed(DataUtils.DISPLAY_SPEED_25);
                    Toast.makeText(mContext, "25mm/s", Toast.LENGTH_LONG).show();
                } else if (p % 5 == 1) {
                    data.setSpeed(DataUtils.DISPLAY_SPEED_50);
                    Toast.makeText(mContext, "50mm/s", Toast.LENGTH_LONG).show();
                } else if (p % 5 == 2) {
                    data.setSpeed(DataUtils.DISPLAY_SPEED_125);
                    Toast.makeText(mContext, "12.5mm/s", Toast.LENGTH_LONG).show();
                } else if (p % 5 == 3) {
                    data.setSpeed(DataUtils.DISPLAY_SPEED_5);
                    Toast.makeText(mContext, "5mm/s", Toast.LENGTH_LONG).show();
                } else if (p % 5 == 4) {
                    data.setSpeed(DataUtils.DISPLAY_SPEED_10);
                    Toast.makeText(mContext, "10mm/s", Toast.LENGTH_LONG).show();
                }
                p++;

            }
        });
        //gain
        b8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (m % 6 == 0) {
                    data.setGain(DataUtils.DISPLAY_GAIN_5);
                    Toast.makeText(mContext, "5mm/mV", Toast.LENGTH_LONG).show();
                } else if (m % 6 == 1) {
                    data.setGain(DataUtils.DISPLAY_GAIN_10);
                    Toast.makeText(mContext, "10mm/mV", Toast.LENGTH_LONG).show();
                } else if (m % 6 == 2) {
                    data.setGain(DataUtils.DISPLAY_GAIN_20);
                    Toast.makeText(mContext, "20mm/mV", Toast.LENGTH_LONG).show();
                } else if (m % 6 == 3) {
                    data.setGain(DataUtils.DISPLAY_GAIN_2_5);
                    Toast.makeText(mContext, "2.5mm/mV", Toast.LENGTH_LONG).show();
                    ;
                } else if (m % 6 == 4) {
                    data.setGain(DataUtils.DISPLAY_GAIN_Limb10_Chest5);
                    Toast.makeText(mContext, "肢导10mm/mV,胸导5mm/mV", Toast.LENGTH_LONG).show();
                } else if (m % 6 == 5) {
                    data.setGain(DataUtils.DISPLAY_GAIN_Limb20_Chest10);
                    Toast.makeText(mContext, "肢导20mm/mV,胸导10mm/mV", Toast.LENGTH_LONG).show();
                }
                m++;
            }
        });
        // data对象包含所有心电采集相关操作
        // glView负责显示
        // 蓝牙采集
//*
        Intent intent = null;
        intent = getIntent();
        if (intent != null) {
            String dAddress = intent.getExtras().getString("device_address");
            Log.e(TAG, "开始绑定设备 MAC: " + dAddress);
            data = new DataUtils(mContext, dAddress,
                    new BluConnectionStateListener() {
                        @Override
                        public void OnBluConnectionInterrupted() {
                            Log.e(TAG, "OnBluConnectionInterrupted: ");
                            mHandler.obtainMessage(MESSAGE_CONNECT_INTERRUPTED,
                                    -1, -1).sendToTarget();
                        }

                        @Override
                        public void OnBluConnectSuccess() {
                            Log.e(TAG, "OnBluConnectSuccess: ");
                            mHandler.obtainMessage(MESSAGE_CONNECT_SUCCESS, -1, -1).sendToTarget();
                        }

                        @Override
                        public void OnBluConnectStart() {
                            Log.e(TAG, "OnBluConnectStart: ");
                            mHandler.obtainMessage(MESSAGE_CONNECT_START).sendToTarget();
                        }

                        @Override
                        public void OnBluConnectFaild() {
                            Log.e(TAG, "OnBluConnectFaild: ");
                            mHandler.obtainMessage(MESSAGE_CONNECT_FAILED, -1, -1).sendToTarget();

                        }
                    });
        }
        //*/
        // 演示文件采集
//         data = new DataUtils(Environment.getExternalStorageDirectory().getPath()+"/demo.ecg");
/*
        // USB 8000G 设备支持
		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		data = new DataUtils(mUsbManager, new USBConnectionStateListener() {

			@Override
			public void OnUSBConnectionError(int state) {
				// TODO Auto-generated method stub
				switch (state) {

				case USBConnectionStateListener.ERROR_REMOVE_DEVICE:
					mHandler.obtainMessage(MESSAGE_USB_CONNECT_REMOVE_DEVICE)
							.sendToTarget();
					break;
				case USBConnectionStateListener.ERROR_NO_USB_PERMISSION:
					mHandler.obtainMessage(
							MESSAGE_USB_CONNECT_NO_USB_PERMISSION)
							.sendToTarget();
					break;
				case USBConnectionStateListener.ERROR_INTERRUPTED:
					mHandler.obtainMessage(MESSAGE_USB_CONNECT_INTERRUPTED)
							.sendToTarget();
					break;
				case USBConnectionStateListener.ERROR_SETTING_DEVICE:
					mHandler.obtainMessage(
							MESSAGE_USB_CONNECT_ERROR_SETTING_DEVICE)
							.sendToTarget();
				case USBConnectionStateListener.ERROR_OPEN_DEVICE:
					mHandler.obtainMessage(
							MESSAGE_USB_CONNECT_ERROR_OPEN_DEVICE)
							.sendToTarget();
					break;
				}
			}

			@Override
			public void OnUSBConnectSuccess() {
				// TODO Auto-generated method stub
				mHandler.obtainMessage(MESSAGE_USB_CONNECT_SUCCESS)
						.sendToTarget();
			}

			@Override
			public void OnUSBConnectStart() {
				// TODO Auto-generated method stub
				mHandler.obtainMessage(MESSAGE_USB_CONNECT_START)
						.sendToTarget();
			}

			@Override
			public void OnUSBConnectFaild() {
				// TODO Auto-generated method stub
				mHandler.obtainMessage(MESSAGE_USB_CONNECT_FAILED)
						.sendToTarget();
			}
		});

*/

        // 以下关于glView操作为必要操作，请不要更改
        glView = (GLView) this.findViewById(R.id.GLWave);
//		glView.setBackground(Color.TRANSPARENT, Color.rgb(111, 110, 110));//2018-06-21 注释掉,避免Bitmap出现异常

        glView.setMsg(mHandler);
        glView.setZOrderOnTop(true);
        glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        textHR = (TextView) this.findViewById(R.id.textHR);
        textLF = (TextView) this.findViewById(R.id.textLeadOff);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        // strCase = formatter.format(curDate);
        strCase = "AECG";
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 此函数执行时，不要执行强制退出、多次点击返回按键等影响android生命周期管理的操作！
        glView.onPause();
        data.gatherEnd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
//        data.gatherStart(new nativeMsg());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (!isFinishActivity) {
                isFinishActivity = true;
                Intent intent = new Intent();
                intent.setClass(mContext, DeviceListActivity.class);
//                intent.setClass(mContext, UsbDeviceListActivity.class);
                startActivity(intent);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private TextView textHR;
    private TextView textLF;
    private static final int MESSAGE_UPDATE_HR = 0;
    private static final int MESSAGE_UPDATE_LeadOff = 1;
    public static final int MESSAGE_CONNECT_START = 0x100;   //256 开始绑定
    public static final int MESSAGE_CONNECT_SUCCESS = 0x200; //512 绑定成功
    public static final int MESSAGE_CONNECT_FAILED = 0x300;  //
    public static final int MESSAGE_CONNECT_INTERRUPTED = 0x400;

    public static final int MESSAGE_USB_CONNECT_START = 0xA010;
    public static final int MESSAGE_USB_CONNECT_SUCCESS = 0xA020;
    public static final int MESSAGE_USB_CONNECT_FAILED = 0xA030;
    public static final int MESSAGE_USB_CONNECT_INTERRUPTED = 0xA040;
    public static final int MESSAGE_USB_CONNECT_NO_USB_PERMISSION = 0xA050;
    public static final int MESSAGE_USB_CONNECT_ERROR_SETTING_DEVICE = 0xA060;
    public static final int MESSAGE_USB_CONNECT_REMOVE_DEVICE = 0xA070;
    public static final int MESSAGE_USB_CONNECT_ERROR_OPEN_DEVICE = 0xD050;
    //连接回调 handler
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: " + msg.what);
            switch (msg.what) {
                case MESSAGE_UPDATE_HR:
                    textHR.setText(msg.obj.toString() + "bpm");
                    break;
                case MESSAGE_UPDATE_LeadOff:
                    textLF.setText(msg.obj.toString());
                    break;
                case MESSAGE_CONNECT_START:  //256 开始
                    Toast.makeText(mContext, "Connect Start", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_CONNECT_SUCCESS: //512 绑定成功
//				Log.e("BL", "Connect Success");
                    btnStartECGRenderer.setEnabled(true);
                    Toast.makeText(mContext, "Connect Success", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_CONNECT_INTERRUPTED:
//				Log.e("BL", "INT");
                    btnStartConnect.setEnabled(true);
                    btnStartECGRenderer.setEnabled(false);
                    glView.stopRenderer();
                    mEcgQueue = null;
                    Toast.makeText(mContext, "Connect INT", Toast.LENGTH_SHORT).show();
                  /*if (!isFinishActivity) {
                        isFinishActivity = true;
                        Intent interrupt = new Intent(GLActivity.this,
                                DeviceListActivity.class);
                        startActivity(interrupt);
                        finish();
                    }*/
                    break;
                case MESSAGE_CONNECT_FAILED:
                    Log.e("BL", "Connnect Failed");
                    btnStartConnect.setEnabled(true);
                    /*if (!isFinishActivity) {
                        isFinishActivity = true;
                        Intent intent = new Intent(GLActivity.this,
                                DeviceListActivity.class);
                        startActivity(intent);
                        finish();
                    }*/
                    break;
                case MESSAGE_USB_CONNECT_REMOVE_DEVICE:
                    Log.e("BL", "MESSAGE_USB_CONNECT_REMOVE_DEVICE");
                    Toast.makeText(mContext, "USB设备被移除", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case MyRenderer.MESSAGE_GATHER_START:
                    Toast.makeText(mContext, "开始绘图", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_USB_CONNECT_FAILED:
                    Toast.makeText(mContext, "USB IOE", Toast.LENGTH_SHORT).show();
                    Intent mintent = new Intent(GLActivity.this,
                            DeviceListActivity.class);
                    startActivity(mintent);
                    finish();
                    break;
                case MESSAGE_USB_CONNECT_START:
                    Toast.makeText(mContext, "USB START", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_USB_CONNECT_SUCCESS:
                    Toast.makeText(mContext, "USB SUCCESS", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_USB_CONNECT_ERROR_OPEN_DEVICE:
                    Toast.makeText(mContext, "USB OPEN ERR", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_USB_CONNECT_INTERRUPTED:
                    Toast.makeText(mContext, "USB INTERRUPTED", Toast.LENGTH_SHORT)
                            .show();
                    break;
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
                // 以下操作可以实现自动开始保存文件
                // data.saveCase(Environment.getExternalStorageDirectory() + "/",strCase, 20);// 存储文件 参数为路径，文件名，存储秒数
            }
        }

        @Override
        public void callEcgWaveDataMsg(short[] wave) {
            // TODO Auto-generated method stub
            if (mEcgQueue != null) {
                for (int i = 48; i < 60; i++) {
                    mEcgQueue.offer(wave[i]);
                }
//                Log.e("ecgdata", " 数据 " + mEcgQueue.size());
            }
        }
    }

    static void show(Context context, UsbDevice usbDevice, UsbManager usbManager) {
        mUsbDevice = usbDevice;
        mUsbManager = usbManager;
        final Intent intent = new Intent(context, GLActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    private ConcurrentLinkedQueue<Short> mEcgQueue;
}
