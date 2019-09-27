package com.zsy.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * @Title com.zsy.bluetooth
 * @Date 2019/9/27
 * @Autor Zsy
 */

public class BtTool {
    static String TAG = "btdemo";
    private static BluetoothAdapter bluetoothAdapter;
    private static BluetoothServerSocket serverSocket;
    private static BluetoothSocket socket;
    private static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public interface ServiceListener {
        void received(String s);
    }

    /**
     * 初始化开始等待
     */
    public static void initAcceptService(Context context) {
        bluetoothAdapter = getAdapter(context);
        if (bluetoothAdapter==null){
            return;
        }
        try {
            serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("bttest", uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //开启等待模式,等待连接接入
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.e(TAG, "开始等待接入");
                    try {
                        socket = serverSocket.accept();
                        if (socket != null) {
                            Log.e(TAG, "等待被接入成功" );
                            serverSocket.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void receive(final ServiceListener listener) {
        if (socket == null || listener == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "开启socket接收");
                try {
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader bff = new BufferedReader(new InputStreamReader(inputStream));
                    String json;
                    while (true) {
                        while ((json = bff.readLine()) != null) {
                            Log.e(TAG, "接收到消息"+json);
                            listener.received(json);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static boolean send(String msg) {
        if (socket == null) {
            return false;
        }
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            outputStream.write(msg.getBytes("utf-8"));
            outputStream.flush();
            Log.e(TAG, "发送消息完毕:"+msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 是否支持蓝牙
     */
    public static BluetoothAdapter getAdapter(Context context) {
        BluetoothAdapter adapter = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 || context == null) {
            adapter = BluetoothAdapter.getDefaultAdapter();
        } else {
            BluetoothManager systemService = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            adapter = systemService.getAdapter();
        }
        return adapter;
    }


}
