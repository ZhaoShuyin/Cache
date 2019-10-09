package com.example.ecg;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import serial.jni.BluConnectionStateListener;
import serial.jni.DataUtils;
import serial.jni.NativeCallBack;

public class ECGActivity extends AppCompatActivity {

    TextView textView;
    TextView tvEcg;
    BluetoothDevice bluetoothDevice;
    DataUtils data;
    private BluetoothAdapter bluetoothAdapter;
    private boolean isConnecting = false;
    private boolean isWaitting = false;
    private short[] shorts = new short[12];
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg);
        textView = findViewById(R.id.tv_msg);
        tvEcg = findViewById(R.id.tv_ecg);
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


    public void showEcg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String s = tvEcg.getText().toString();
                if (s.length() > 300) {
                    s = s.substring(0, 299);
                }
                tvEcg.setText(msg + "\n" + s);
            }
        });
    }


    public void connect(View view) {
        //接收蓝牙搜寻广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        //接收蓝牙发现广播
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        filter.addAction(BluetoothAdapter.EXTRA_SCAN_MODE);//
        filter.addAction(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE);//
        filter.addAction(BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE);//
        filter.addAction(BluetoothAdapter.EXTRA_LOCAL_NAME);//
        filter.addAction(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION);//
        filter.addAction(BluetoothAdapter.EXTRA_CONNECTION_STATE);//
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//
        filter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);//
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);//
        filter.addAction(BluetoothAdapter.EXTRA_PREVIOUS_STATE);//
        filter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);//
        filter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);//
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//
        filter.addAction(BluetoothAdapter.EXTRA_STATE);//


        this.registerReceiver(mReceiver, filter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }


    /**
     * 蓝牙设备广播接收者
     * 监听蓝牙的1.
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String msg = "******* action : ( " + action + " ) ******* ";
            Log.e("ecgdata", msg);
            show(msg);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if ((device.getName() != null) && (device.getName().contains("ECGWS") || device.getName().contains("8000GW"))) {
                    show("........ 搜索到特定设备 : <" + device.getName()+">");
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }

                    bluetoothDevice = device;
                } else {
                    show("........ 搜索到设备 <" + device.getName() + "> 但不是 <ECGWS|8000GW>");
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (bluetoothDevice == null) {
                    show("........ 没有搜索到 <ECGWS 8000GW>");
                } else {
                    bind();
                }
            }
        }
    };


    public void bind() {
        String address = bluetoothDevice.getAddress();
        show("........ 开始绑定设备 MAC:" + address);
        data = new DataUtils(this, address,
                new BluConnectionStateListener() {
                    //连接中断
                    @Override
                    public void OnBluConnectionInterrupted() {
                        show(">>>>>>>>>>>>>绑定设备连接中断");
                        isConnecting = false;
                        if (!isWaitting) {
                            waitting();
                        }
                    }

                    //连接成功
                    @Override
                    public void OnBluConnectSuccess() {
                        isConnecting = true;
                        isWaitting = false;
                        show(">>>>>>>>>>>>>绑定设备连接成功");
                    }

                    //开始
                    @Override
                    public void OnBluConnectStart() {
                        show(">>>>>>>>>>>>>绑定设备连接开始");
                    }

                    //连接失败
                    @Override
                    public void OnBluConnectFaild() {
                        isConnecting = false;
                        show(">>>>>>>>>>>>>绑定设备连接失败");
                    }
                });
    }


    public void waitting() {
        isWaitting = true;
        show(" ,,,,,连接中断 , 开启线程尝试重新连接,,,,,,");
        new Thread(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                while (!isConnecting) {
                    try {
                        Thread.sleep(3000);
                        if (data != null) {
                            data.gatherStart(new NativeMsg());
                        }
                        i++;
                        show(" ~~ 间隔 3秒 尝试重连 次数 : " + (i) + " ~~");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 连接蓝牙的回调
     */
    class NativeMsg extends NativeCallBack {

        @Override
        public void callLeadOffMsg(short[] flagOff) {
            super.callLeadOffMsg(flagOff);
            show(" ----- callLeadOffMsg : ");
        }

        @Override
        public void callOverloadMsg(String overload) {
            super.callOverloadMsg(overload);
            show(" ----- callOverloadMsg : ");
        }

        // 心率
        @Override
        public void callHRMsg(short hr) {
            show(" ----- HR 心率 : " + hr);
        }

        // 导联脱落
        @Override
        public void callLeadOffMsg(String flagOff) {
            show(" ----- 导联脱落");
        }

        // 文件存储进度百分比 progress%
        @Override
        public void callProgressMsg(short progress) {
            Log.e("progress", "" + progress);
            show(" ----- 文件存储进度百分比: " + progress);
        }

        @Override
        public void callCaseStateMsg(short state) {
            if (state == 0) {
                show(" ----- 开始存储文件");
            } else {
                show(" ----- 存储完成");
            }
        }

        // 心率 hbs = 1表示有心跳
        @Override
        public void callHBSMsg(short hbs) {
            show(" ----- HBS 心率 : " + hbs);
        }

        // 采集盒电量
        @Override
        public void callBatteryMsg(short per) {
            show(" ----- 电量 :" + per);
        }

        // 剩余存储时长
        @Override
        public void callCountDownMsg(short per) {
            show(" ----- 剩余储存时长 : " + per);
        }

        @Override
        public void callWaveColorMsg(boolean flag) {
            show(" ------ " + (flag ? "波形稳定" : "波形不稳定"));
            if (flag) {
                // 波形稳定后颜色变为绿色
                // 以下操作可以实现自动开始保存文件
                // data.saveCase(Environment.getExternalStorageDirectory() + "/",strCase, 20);// 存储文件 参数为路径，文件名，存储秒数
            }
        }

        @Override
        public void callEcgWaveDataMsg(short[] wave) {
            i++;
            if (i % 5 == 0) {
                for (int i = 48; i < 60; i++) {
                    shorts[i - 48] = wave[i];
                }
                String s = Arrays.toString(shorts);
                showEcg(i + " : " + s);
                Log.e("ecgdata", "数据 : " + s);
            }
        }
    }

    int i = 0;


    public void start(View view) {
        if (data != null) {
            data.gatherStart(new NativeMsg());
        } else {
            show("没有绑定设备");
        }

    }

    public void stop(View view) {
        if (data != null) {
            data.gatherEnd();
        } else {
            show("没有绑定设备");
        }
    }

}
