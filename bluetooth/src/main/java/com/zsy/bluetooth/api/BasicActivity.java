package com.zsy.bluetooth.api;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zsy.bluetooth.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Title com.zsy.bluetooth.api
 * https://android-doc.com/reference/android/bluetooth/BluetoothAdapter.html
 * https://blog.csdn.net/huangliniqng/article/details/82185635
 * @Date 2019/9/25
 * @Autor Zsy
 */
public class BasicActivity extends Activity {
    Context context;
    String TAG = "btdemo";
    TextView tvShow;
    ArrayAdapter listAdapter;


    Map<String, BluetoothDevice> map = new HashMap<>();
    /**
     * 代表本地蓝牙适配器（蓝牙无线电）。BluetoothAdapter是所有蓝牙交互的入口。
     */
    BluetoothAdapter bluetoothAdapter;

    private void show(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvShow.setText(tvShow.getText().toString() + "\n" + s);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_basic);
        tvShow = findViewById(R.id.tv_show);
        listAdapter = new ArrayAdapter(this, R.layout.item_bt_device);
        ListView listView = findViewById(R.id.ls_devices);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //连接先关闭搜索
                bluetoothAdapter.cancelDiscovery();
                String address = ((TextView) view).getText().toString();
                BluetoothDevice device = map.get(address);
                Log.e(TAG, ">>>>>>>>>>开始连接 : " + address + " >>>>>>>>>>>>>>" + device.getBondState());
                if (device.getBondState() == 12) { //已配对
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            communication();
                        }
                    }).start();
                } else {                          //未配对
                    bond(device);
                }
            }
        });
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        register();
    }


    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);     //蓝牙状态改变的广播
        filter.addAction(BluetoothDevice.ACTION_FOUND);              //找到设备的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索完成的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //开始扫描的广播
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //状态改变
        this.registerReceiver(mReceiver, filter);
    }

    /**
     * 蓝牙设备广播接收者
     * 监听蓝牙的1.
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e(TAG, "发现一个设备: name:" + device.getName()
                        + " , address:" + device.getAddress() +
                        " , type:" + device.getType() +
                        " , uuid:" + device.getUuids() +
                        " , state:" + device.getBondState());//已配对:12,未配对:10
                String tag = device.getAddress() + " - " + device.getName()+" - "+device.getBondState();
                listAdapter.add(tag);
                map.put(tag, device);
            } else {
                Log.e(TAG, "广播 action: " + action);
            }
        }
    };

    /**
     * 1.蓝牙配对
     */
    private void bond(BluetoothDevice device) {
        try {
            Method method = BluetoothDevice.class.getMethod("createBond");
            method.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static BluetoothServerSocket serverSocket;
    public static BluetoothSocket socket;
    public static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * !.从蓝牙适配器中创建一个蓝牙服务作为服务端，在获得蓝牙适配器后创建服务器端
     */
    private void communication() {
        show("尝试开启 serverSocket ....... ");
        try {
            serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("bttest", uuid);
            show("开启serverSocket 正常");
        } catch (IOException e) {
            e.printStackTrace();
            show("serverSocket 异常");
            return;
        }
        socket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                show("开启接收线程");
                receiveMessage();
            }
        }).start();
    }

    private void socket(){
        int number = 0;
        while(true){
            show("开启等待 socket ....... ("+(++number)+")");
            try {
                socket = serverSocket.accept();
                if (socket != null) {
                    show("开启 socket 正常");
                    serverSocket.close();
                    show("关闭 serverSocket");
                }else{
                    show("开启socket失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                show("socket 异常");
                return;
            }
        }
    }


    /**
     * *开启消息接收*
     */
    public void receiveMessage() {
        try {
            InputStream inputStream = socket.getInputStream();
            // 从客户端获取信息
            BufferedReader bff = new BufferedReader(new InputStreamReader(inputStream));
            String json;
            while (true) {
                while ((json = bff.readLine()) != null) {
                    show(json);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送消息
     */
    public void sendmsg(View view) {
        String message = String.valueOf(System.currentTimeMillis());
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            outputStream.write(message.getBytes("utf-8"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void connectDevice(String mac) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(mac);
        //
        BluetoothGatt gatt = device.connectGatt(context, false, new BluetoothGattCallback() {
            //
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                Log.e(TAG, "onConnectionStateChange: status:" + status + ", newState:" + newState);
                if (BluetoothProfile.STATE_CONNECTED == newState) {
                    Log.e(TAG, "连接成功");
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                Log.e(TAG, "onServicesDiscovered: status:" + status);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                Log.e(TAG, "onCharacteristicRead: status:" + status);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                Log.e(TAG, "onCharacteristicWrite: status:" + status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                Log.e(TAG, "onCharacteristicChanged:");
            }
        });
    }

    /**
     * 搜索设备
     */
    protected void scan(View view) {
        map.clear();
        listAdapter.clear();
        bluetoothAdapter.startDiscovery();
    }

    /**
     * 取消搜索设备
     */
    protected void cancelscan(View view) {
        bluetoothAdapter.cancelDiscovery();
    }


}
