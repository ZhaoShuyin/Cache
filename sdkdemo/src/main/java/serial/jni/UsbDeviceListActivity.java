package serial.jni;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Shows a {@link ListView} of available USB devices.
 *
 * @author mike wakerly (opensource@hoho.com)
 */
public class UsbDeviceListActivity extends Activity {

    private final String TAG = DeviceListActivity.class.getSimpleName();

    private UsbManager mUsbManager;
    private ListView mListView;
    private TextView mProgressBarTitle;
    private ProgressBar mProgressBar;

    private static final int MESSAGE_REFRESH = 101;
    private static final long REFRESH_TIMEOUT_MILLIS = 5000;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_REFRESH:
                    refreshDeviceList();
                    mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, REFRESH_TIMEOUT_MILLIS);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }

    };

    /**
     * Simple container for a UsbDevice and its driver.
     */
    private static class DeviceEntry {
        public UsbDevice device;
        public UsbSerialDriver driver;

        DeviceEntry(UsbDevice device, UsbSerialDriver driver) {
            this.device = device;
            this.driver = driver;
        }
    }

    private List<DeviceEntry> mEntries = new ArrayList<DeviceEntry>();
    private ArrayAdapter<DeviceEntry> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usb_main);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mListView = (ListView) findViewById(R.id.deviceList);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBarTitle = (TextView) findViewById(R.id.progressBarTitle);

        mAdapter = new ArrayAdapter<DeviceEntry>(this, android.R.layout.simple_expandable_list_item_2, mEntries) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final TwoLineListItem row;
                if (convertView == null) {
                    final LayoutInflater inflater =
                            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TwoLineListItem) inflater.inflate(android.R.layout.simple_list_item_2, null);
                } else {
                    row = (TwoLineListItem) convertView;
                }

                final DeviceEntry entry = mEntries.get(position);
                final String title = String.format("Vendor %s Product %s",
                        HexDump.toHexString((short) entry.device.getVendorId()),
                        HexDump.toHexString((short) entry.device.getProductId()));
                row.getText1().setText(title);

                final String subtitle = entry.driver != null ?
                        entry.driver.getClass().getSimpleName() : "No Driver";
                row.getText2().setText(subtitle);

                return row;
            }

        };
        //////////////
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            mUsbDevice = deviceIterator.next();
        }
        if (mUsbDevice != null) {
            mUsbManager.requestPermission(mUsbDevice, mPermissionIntent);
        }
        ///////////////
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Pressed item " + position);
                if (position >= mEntries.size()) {
                    Log.w(TAG, "Illegal position.");
                    return;
                }

                final DeviceEntry entry = mEntries.get(position);
                final UsbSerialDriver driver = entry.driver;
                if (driver == null) {
                    Log.d(TAG, "No driver.");
//                   return;
                }

                showConsoleActivity();
//               showConsoleActivity(driver);
//               Intent intent = new Intent(UsbDeviceListActivity.this, GLActivity.class) ;
//               startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(MESSAGE_REFRESH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(MESSAGE_REFRESH);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(mUsbReceiver);
    }

    private void refreshDeviceList() {
        showProgressBar();

        new AsyncTask<Void, Void, List<DeviceEntry>>() {
            @Override
            protected List<DeviceEntry> doInBackground(Void... params) {
                Log.d(TAG, "Refreshing device list ...");
                SystemClock.sleep(1000);
                final List<DeviceEntry> result = new ArrayList<DeviceEntry>();
                for (final UsbDevice device : mUsbManager.getDeviceList().values()) {
                    final List<UsbSerialDriver> drivers =
                            UsbSerialProber.findAllDevices(mUsbManager);
//                   final List<UsbSerialDriver> drivers =
//                           UsbSerialProber.probeSingleDevice(mUsbManager, device);
                    Log.d(TAG, "Found usb device: " + device);
                    if (drivers.isEmpty()) {
                        Log.d(TAG, "  - No UsbSerialDriver available.");
                        result.add(new DeviceEntry(device, null));
                    } else {
                        for (UsbSerialDriver driver : drivers) {
                            Log.d(TAG, "  + " + driver);
                            result.add(new DeviceEntry(device, driver));
                        }
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<DeviceEntry> result) {
                mEntries.clear();
                mEntries.addAll(result);
                mAdapter.notifyDataSetChanged();
                mProgressBarTitle.setText(
                        String.format("%s device(s) found", Integer.valueOf(mEntries.size())));
                hideProgressBar();
                Log.d(TAG, "Done refreshing, " + mEntries.size() + " entries found.");
            }

        }.execute((Void) null);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBarTitle.setText("refreshing");
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showConsoleActivity() {
        GLActivity.show(this, mUsbDevice, mUsbManager);
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            // call method to set up device communication

                            Toast.makeText(UsbDeviceListActivity.this, "设备可以通信",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(UsbDeviceListActivity.this, "无可用设备", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }

            if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {

            } else if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {

            }
        }
    };
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private PendingIntent mPermissionIntent;
    private UsbDevice mUsbDevice;
}
