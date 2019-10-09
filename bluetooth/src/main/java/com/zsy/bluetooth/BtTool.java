package com.zsy.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @Title com.zsy.bluetooth
 * @Date 2019/9/27
 * @Autor Zsy  http://www.jikexueyuan.com/course/11_17.html?ss=2
 */

public class BtTool {
    public static MainActivity activity;
    static String TAG = "btdemo";
    public static BluetoothAdapter bluetoothAdapter;
    private static BluetoothServerSocket serverSocket;
    private static BluetoothSocket bluetoothSocket;
    private static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public interface ServiceListener {
        void received(String s);
    }

    /**
     * 初始化开始等待
     */
    public static void initAcceptService(Context context) {
        activity = (MainActivity) context;
        bluetoothAdapter = getAdapter(context);
        if (bluetoothAdapter == null) {
            return;
        }
        activity.show("创建 bluetoothAdapter ");
        try {
            serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("com.zsy.bluetooth", uuid);
            activity.show("创建 bluetoothAdapter 及 BluetoothServerSocket");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //开启等待模式,等待连接接入
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    activity.show("服务端等待接入......");
                    try {
                        bluetoothSocket = serverSocket.accept();
                        if (bluetoothSocket != null) {
                            activity.show("等待被接入成功");
                            serverSocket.close();
                            break;
                        } else {
                            activity.show("等待被接入失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public static boolean connect(BluetoothDevice device) {
        try {
            BluetoothSocket soc = device.createRfcommSocketToServiceRecord(uuid);
            if (soc != null) {
                bluetoothSocket = soc;
                bluetoothSocket.connect();
                activity.show("连接设备成功>>>");
                return true;
            } else {
                activity.show("连接设备失败");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            activity.show("连接设备失败");
            return false;
        }
    }


    public static void receive(final ServiceListener listener) {
        if (bluetoothSocket == null || listener == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                activity.show("开始开启socket接收......");
               /* try {
                    InputStream in = bluetoothSocket.getInputStream();
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
                    String json;
                    while (true) {
                        while ((json = bReader.readLine()) != null) {
                            Log.e(TAG, "接收消息: "+json );
                            activity.show("接收到消息: "+json);
                            listener.received(json);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    activity.show("开启接收线程失败:"+e.toString());
                }*/

                byte[] buffer = new byte[1024];//定义字节数组装载信息
                int bytes;
                InputStream in = null;
                try {
                    in = bluetoothSocket.getInputStream();
                    while (true) {
                        if ((bytes = in.read(buffer)) != 0) {
                            byte[] buf_data = new byte[bytes];
                            for (int i = 0; i < bytes; i++) {
                                buf_data[i] = buffer[i];
                            }
                            String msg = new String(buf_data);//最后得到String类型消息
                            activity.show("  接收消息: " + msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    activity.show("停止接收消息>>");
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static boolean send(String msg) {
        if (bluetoothSocket == null) {
            return false;
        }
        try {
            OutputStream out = bluetoothSocket.getOutputStream();
            out.write(msg.getBytes());//将消息字节发出
            out.flush();//确保所有数据已经被写出，否则抛出异常
            activity.show("  发送消息: " + msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            activity.show("  发送: " + msg + " 异常");
            return false;
        }
    }


    /**
     * 是否支持蓝牙
     */
    public static boolean hasBlueTooth(Context context) {
        return getAdapter(context) != null;
    }

    /**
     * 开启蓝牙
     */
    public static void startBlueTooth(Activity activity) {
        BluetoothAdapter adapter = getAdapter(activity);
        if (adapter != null && !adapter.enable()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, 1);
        }
    }


    private static BluetoothAdapter getAdapter(Context context) {
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
