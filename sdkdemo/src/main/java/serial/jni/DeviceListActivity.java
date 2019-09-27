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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceListActivity extends Activity {

    private String TAG = "devices";
    private static final int REQUEST_ENABLE_BT = 3;
    public String EXTRA_DEVICE_ADDRESS = "device_address";
    /**
     * 系统蓝牙连接Adapter
     * Member fields
     */
    private BluetoothAdapter bluetoothAdapter = null;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    private Button mScanButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_list);
        mScanButton = (Button) findViewById(R.id.button_scan);
        mScanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mNewDevicesArrayAdapter.clear();  //清空已显示设备
                v.setEnabled(false);              //设置不可点击
                doDiscovery();                    //开始查找设备
            }
        });
        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        ListView listView = (ListView) findViewById(R.id.ls_devices);
        listView.setAdapter(mNewDevicesArrayAdapter);
        listView.setOnItemClickListener(mDeviceClickListener);
        String noDevices = "start暂无发现蓝牙设备";
        mNewDevicesArrayAdapter.add(noDevices);

        //接收蓝牙搜寻广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        //接收蓝牙发现广播
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        //
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙不存在", Toast.LENGTH_LONG).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }


    //开始查找蓝牙设备
    private void doDiscovery() {
        setProgressBarIndeterminateVisibility(true);//状态栏显示刷新条
        setTitle("scanning");
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

    /**
     * 跳转
     */
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            bluetoothAdapter.cancelDiscovery();
            String info = ((TextView) v).getText().toString();
            if (info.length() < 17) {
                return;
            }
            String address = info.substring(info.length() - 17);
            Intent intent;
            intent = new Intent(DeviceListActivity.this, GLActivity.class);
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            startActivity(intent);
        }
    };


    /**
     * 蓝牙设备广播接收者
     * 监听蓝牙的1.
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.e(TAG, "onReceive: 发现一个设备");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if ((device.getName() != null) && (device.getName().contains("ECGWS") || device.getName().contains("8000GW"))) {
                    mNewDevicesArrayAdapter.remove(device.getName() + "\n" + device.getAddress());
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);   //状态栏取消刷新条
                setTitle("搜索设备完毕");
                Log.e(TAG, "onReceive: 完毕");
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = "搜索没有设备";
                    mNewDevicesArrayAdapter.add(noDevices);
                }
                findViewById(R.id.button_scan).setEnabled(true);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }

}
