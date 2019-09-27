package hlq.receivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import hlq.base.bean.BluRxBean;

/**
 * Created by  Huanglinqing on 2018/8/24/024.
 * 蓝牙广播服务,监听各种蓝牙操作
 */
public class BlueToothReceiver extends BroadcastReceiver {

    private String TAG = "btdemo";
    private int findDevice = 1;          //查找设备
    private int findDeviceIsFinished = 2;//扫描完成
    private int findtart = 3;            //开始扫描
    private int connectionSuccess = 4;   //配对成功

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_TURNING_ON://蓝牙打开中
                        Log.d(TAG, "......蓝牙打开中");
                        break;
                    case BluetoothAdapter.STATE_ON://蓝牙打开完成
                        Log.e(TAG, "......蓝牙打开完成");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF://蓝牙关闭中
                        Log.d(TAG, "......蓝牙关闭中");
                        break;
                    case BluetoothAdapter.STATE_OFF://蓝牙关闭完成
                        Log.e(TAG, "......蓝牙关闭完成");
                        break;
                }
                break;
            case BluetoothDevice.ACTION_FOUND:  //找到设备
                Log.d(TAG, "......查找设备");
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                EventBus.getDefault().post(new BluRxBean(findDevice, device));
                break;
            //搜索完成
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                EventBus.getDefault().post(new BluRxBean(findDeviceIsFinished));
                break;
            //开始扫描
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                Log.d(TAG, "......开始扫描");
                EventBus.getDefault().post(new BluRxBean(findtart));
                break;
            //状态改变
            case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                BluetoothDevice de = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (de.getBondState()) {
                    case BluetoothDevice.BOND_NONE:
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        Toast.makeText(context, "配对中", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Toast.makeText(context, "配对成功", Toast.LENGTH_LONG).show();
                        break;
                }
                break;

        }
    }


    /**
     * 蓝牙广播过滤器
     * 蓝牙状态改变
     * 找到设备
     * 搜索完成
     * 开始扫描
     * 状态改变
     *
     * @return
     */
    public IntentFilter makeFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);     //蓝牙状态改变的广播
        filter.addAction(BluetoothDevice.ACTION_FOUND);              //找到设备的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索完成的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //开始扫描的广播
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //状态改变
        return filter;
    }
}


