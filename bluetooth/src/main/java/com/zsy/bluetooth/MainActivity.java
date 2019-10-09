package com.zsy.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zsy.bluetooth.api.BasicActivity;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    BluetoothDevice remDevice;

    public void show(final String s) {
        if (textView != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(textView.getText().toString() + "\n" + s);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_show);
        button = findViewById(R.id.bt_scan);
    }

    public void waitService(View view) {
        BtTool.initAcceptService(this);//开启等待服务
    }


    public void connect1(View view) {
        if (BtTool.bluetoothAdapter == null) {
            Toast.makeText(this, "请先初始化", Toast.LENGTH_SHORT).show();
            return;
        }
        BluetoothDevice remoteDevice = BtTool.bluetoothAdapter.getRemoteDevice("A4:93:3F:B1:1C:A3");
        if (remoteDevice == null) {
            Toast.makeText(this, "搜索不到该设备", Toast.LENGTH_SHORT).show();
            return;
        }
        if (remDevice != null) {
            remoteDevice = remDevice;
        }
        show("开始尝试连接到 >> " + remoteDevice.getAddress());
        boolean connect = BtTool.connect(remoteDevice);
    }

    public void connect2(View view) {
        if (BtTool.bluetoothAdapter == null) {
            Toast.makeText(this, "请先初始化", Toast.LENGTH_SHORT).show();
            return;
        }
        BluetoothDevice remoteDevice = BtTool.bluetoothAdapter.getRemoteDevice("88:BF:E4:47:D8:5C");
        if (remoteDevice == null) {
            Toast.makeText(this, "搜索不到该设备", Toast.LENGTH_SHORT).show();
            return;
        }
        if (remDevice != null) {
            remoteDevice = remDevice;
        }
        show("开始尝试连接到 >> " + remoteDevice.getAddress());
        boolean connect = BtTool.connect(remoteDevice);
    }

    public void receive(View view) {
        BtTool.receive(new BtTool.ServiceListener() {
            @Override
            public void received(final String s) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        textView.setText(textView.getText().toString() + "\n 接收: " + s);
//                    }
//                });
            }
        });
    }


    public void send(View view) {
        String msg = String.valueOf(System.currentTimeMillis());
        BtTool.send("消息哈哈哈");
    }


    public void basicuse(View view) {
//        startActivity(new Intent(this, BasicActivity.class));
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        if (BtTool.bluetoothAdapter != null) {
            BtTool.bluetoothAdapter.startDiscovery();
        } else {
            Toast.makeText(this, "请先初始化", Toast.LENGTH_SHORT).show();
        }

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String address = device.getAddress();
                if (address.equals("A4:93:3F:B1:1C:A3")) {
                    remDevice = device;
                    show("发现手机设备");
                    BtTool.bluetoothAdapter.cancelDiscovery();
                }
                if (address.equals("88:BF:E4:47:D8:5C")) {
                    remDevice = device;
                    show("发现平板设备");
                    BtTool.bluetoothAdapter.cancelDiscovery();
                }
            }
        }
    };
}
