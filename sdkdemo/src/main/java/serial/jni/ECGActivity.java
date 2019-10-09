package serial.jni;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

public class ECGActivity extends Activity {

    TextView textView;
    BluetoothDevice bluetoothDevice;
    DataUtils data;
    boolean hasBind = false;
    private BluetoothAdapter bluetoothAdapter;
    private String TAG = "ecgdata";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg);
        textView = (TextView) findViewById(R.id.tv_msg);
        Log.e(TAG, "onCreate: 绑定 MAC : 00:04:3E:51:FA:E7");
        data = new DataUtils(this, "00:04:3E:51:FA:E7",
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

    public void show(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String s = textView.getText().toString();
                textView.setText(msg + "\n" + s);
            }
        });
    }


    public void connect(View view) {
        bind();
       /* //接收蓝牙搜寻广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        //接收蓝牙发现广播
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();*/
    }


    /**
     * 蓝牙设备广播接收者
     * 监听蓝牙的1.
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if ((device.getName() != null) && (device.getName().contains("ECGWS") || device.getName().contains("8000GW"))) {
                    show(">>>>>>>>>>>>>搜索到特定设备 : " + device.getName());
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }

                    bluetoothDevice = device;
                } else {
                    show("搜索到设备 " + device.getName() + " 但不是 ECGWS|8000GW");
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (bluetoothDevice == null) {
                    show(">>>>>>>>>>>>>>>>没有搜索到 ECGWS 8000GW");
                } else {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           bind();
                       }
                   });
                }
            }
        }
    };


    public void bind() {
//        String address = bluetoothDevice.getAddress();
//        show(">>>>>>>>>>>>>>>开始绑定设备 MAC:" + address);
        show(">>>>>>>>>>>>>>>开始绑定设备 MAC: 00:04:3E:51:FA:E7");
        data = new DataUtils(this, "00:04:3E:51:FA:E7",
                new BluConnectionStateListener() {
                    //连接中断
                    @Override
                    public void OnBluConnectionInterrupted() {
                        Log.e("ecgdata", "OnBluConnectionInterrupted: " );
                        show("绑定设备连接中断");
                    }

                    //连接成功
                    @Override
                    public void OnBluConnectSuccess() {
                        Log.e("ecgdata", "OnBluConnectSuccess: " );
                        show("绑定设备连接成功");
                        hasBind = true;
                    }

                    //开始
                    @Override
                    public void OnBluConnectStart() {
                        Log.e("ecgdata", "OnBluConnectStart: ");
                        show("绑定设备连接开始");
                    }

                    //连接失败
                    @Override
                    public void OnBluConnectFaild() {
                        Log.e("ecgdata", "OnBluConnectFaild: " );
                        show("绑定设备连接失败");
                    }
                });
    }


    /**
     * 连接蓝牙的回调
     */
    class NativeMsg extends NativeCallBack {

        @Override
        public void callHRMsg(short hr) {// 心率
        }

        @Override
        public void callLeadOffMsg(String flagOff) {// 导联脱落
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

        // 心率 hbs = 1表示有心跳
        @Override
        public void callHBSMsg(short hbs) {
        }

        // 采集盒电量
        @Override
        public void callBatteryMsg(short per) {
        }

        // 剩余存储时长
        @Override
        public void callCountDownMsg(short per) {
        }

        @Override
        public void callWaveColorMsg(boolean flag) {
            Log.e("WaveColor", "" + flag);
            if (flag) {
                // 波形稳定后颜色变为绿色
                // 以下操作可以实现自动开始保存文件
                // data.saveCase(Environment.getExternalStorageDirectory() + "/",strCase, 20);// 存储文件 参数为路径，文件名，存储秒数
            }
        }

        @Override
        public void callEcgWaveDataMsg(short[] wave) {
            short[] shorts = new short[12];
            for (int i = 48; i < 60; i++) {
                shorts[i - 48] = wave[i];
            }
            String s = Arrays.toString(shorts);
            show(s);
        }
    }


    public void start(View view) {
        if (data!=null) {
            data.gatherStart(new NativeMsg());
        } else {
            show("没有绑定设备");
        }

    }

    public void stop(View view) {
        if (data!=null) {
            data.gatherEnd();
        } else {
            show("没有绑定设备");
        }
    }

}
