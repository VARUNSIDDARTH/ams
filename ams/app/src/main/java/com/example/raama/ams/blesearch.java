package com.example.raama.ams;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class blesearch extends AppCompatActivity {

    public Set<BleDevice> history_of_Ble_devices=new HashSet<BleDevice>();
    public Set<BleDevice> current_Ble_Devices=new HashSet<BleDevice>();

    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Button startScanningButton;
    Button stopScanningButton;
    TextView peripheralTextView;
    TextView tv;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final long SCAN_PERIOD = 1000;

    boolean new_dev=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blesearch);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bluetooth Scan");
        peripheralTextView = (TextView) findViewById(R.id.PeripheralTextView);
        peripheralTextView.setMovementMethod(new ScrollingMovementMethod());

        tv = (TextView) findViewById(R.id.tv);
        tv.setMovementMethod(new ScrollingMovementMethod());
        //tv.append("all devices so far:\n");
        //tv.append("\n");

        startScanningButton = (Button) findViewById(R.id.StartScanButton);
        startScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startScanning();
            }
        });

//        stopScanningButton = (Button) findViewById(R.id.StopScanButton);
//        stopScanningButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                stopScanning();
//            }
//        });
//        stopScanningButton.setVisibility(View.INVISIBLE);

        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();


        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }

        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }


    }
    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            // working code
            //peripheralTextView.append("res:"+result.toString()+"\n");



            String s=result.toString();
            byte[] a=result.getScanRecord().getBytes();
            int len=a.length;
            int uuid_start_index=9;//uuid index from 9 to 24
            int major_index=26;
            int minor_index=28;
            int major_id=0;
            int minor_id=0;
            String uuid_string=new String("");
            for(int i=uuid_start_index;i<uuid_start_index+16;i++)
            {
                uuid_string=uuid_string.concat(Integer.toHexString(a[i]&0xFF));
                peripheralTextView.append("actual:"+a[i]+" removing 2s complement:"+(a[i]&0xFF)+" convert:"+Integer.toHexString(a[i]&0xFF)+"\n");
            }
            major_id=((a[major_index-1]&0xFF)<<8)+(a[major_index]&0xFF);
            minor_id=((a[minor_index-1]&0xFF)<<8)+(a[minor_index]&0xFF);




//            peripheralTextView.append("uuid "+uuid_string+"\n");
//            peripheralTextView.append("major id "+major_id+"\n");
//            peripheralTextView.append("minor id "+minor_id+"\n");



//                Iterator<BleDevice> itr=al.iterator();
//                if(itr!=null) {
//                    while (itr.hasNext()) {
//                        BleDevice dev1 = itr.next();
//                        peripheralTextView.append("device uuid: " + dev1.getUuid() + "\n");
//                        peripheralTextView.append("device major: " + dev1.getMajor_id() + "\n");
//                        peripheralTextView.append("device minor: " + dev1.getMinor_id() + "\n");
//                    }
//                }
//                else
//                {
//                    peripheralTextView.append("iterator null \n");
//                }
           // peripheralTextView.append("\n");

            //method1 for debugging

//            peripheralTextView.append("scan result:"+s+"\n");
//            peripheralTextView.append("len:"+a.length+"\n");
//            for(int i=0;i<len;i++)
//            {
//                peripheralTextView.append("data"+i+": "+a[i]+"\n");
//            }
//
//            peripheralTextView.append("\n");

            //method 3

//            peripheralTextView.append("1"+result.toString()+"\n");
//            peripheralTextView.append("2"+result.describeContents()+"\n");
//            peripheralTextView.append("\n");


//            peripheralTextView.append("1"+result.describeContents()+"\n");
//            peripheralTextView.append("1"+result.describeContents()+"\n");
//            peripheralTextView.append("1"+result.describeContents()+"\n");

            //method 4

//            List<ADStructure> structures =
//                    ADPayloadParser.getInstance().parse(result.getScanRecord());
//
//            // For each AD structure contained in the advertising packet.
//            for (ADStructure structure : structures)
//            {
//                if (structure instanceof IBeacon)
//                {
//                    // iBeacon was found.
//                    IBeacon iBeacon = (IBeacon)structure;
//
//                    // Proximity UUID, major number, minor number and power.
//
//
//                    UUID uuid = iBeacon.getUUID();
//                    int major = iBeacon.getMajor();
//                    int minor = iBeacon.getMinor();
//                    int power = iBeacon.getPower();
            //test


            //method2 not working
//            if(result.getDevice().fetchUuidsWithSdp())
//            {
//                ParcelUuid[] uuids = result.getDevice().getUuids();
//                if (null == uuids) {
//                    peripheralTextView.append("uuid: null value that\n");
//                }
//
//                else {
//                    peripheralTextView.append("enter else part\n");
//                    for (int i = 0; i < uuids.length; i++) {
//                        ParcelUuid uuid = uuids[i];
//                        peripheralTextView.append("uuid: " + uuid.toString() + "\n");
//                    }
//                }

            //for storing the ble devices
            BleDevice dev =new BleDevice(uuid_string,major_id,minor_id);

            history_of_Ble_devices.add(dev);
            current_Ble_Devices.add(dev);

            // auto scroll for text view
            final int scrollAmount = peripheralTextView.getLayout().getLineTop(peripheralTextView.getLineCount()) - peripheralTextView.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0)
                peripheralTextView.scrollTo(0, scrollAmount);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    public void startScanning() {
        System.out.println("start scanning");
        peripheralTextView.setText("");
        current_Ble_Devices.clear();
        startScanningButton.setVisibility(View.INVISIBLE);
       //topScanningButton.setVisibility(View.VISIBLE);
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                btScanner.startScan(leScanCallback);
//            }
//        });
        btScanner.startScan(leScanCallback);
        peripheralTextView.setText("Scanning");
        tv.setText("Scanning");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // mScanning = false;
                Iterator<BleDevice> current_itr=current_Ble_Devices.iterator();
                Iterator<BleDevice> history_itr=history_of_Ble_devices.iterator();
                btScanner.stopScan(leScanCallback);
                peripheralTextView.setText("current Ble devices:\n");
                while(current_itr.hasNext())
                {
                    BleDevice dev=current_itr.next();
                    peripheralTextView.append("device uuid: "+dev.getUuid()+"\n");
                    peripheralTextView.append("device major_id: "+dev.getMajor_id()+"\n");
                    peripheralTextView.append("device minor_id: "+dev.getMinor_id()+"\n");
                    peripheralTextView.append("\n");

                }
                tv.setText("all Ble devices so far:\n");
                while(history_itr.hasNext())
                {
                    BleDevice dev=history_itr.next();
                    tv.append("device uuid: "+dev.getUuid()+"\n");
                    tv.append("device major_id: "+dev.getMajor_id()+"\n");
                    tv.append("device minor_id: "+dev.getMinor_id()+"\n");
                    tv.append("\n");

                }

                startScanningButton.setVisibility(View.VISIBLE);

            }
        }, SCAN_PERIOD);


        //peripheralTextView.clearFocus();
    }

//    public void stopScanning() {
//        System.out.println("stopping scanning");
//        peripheralTextView.append("Stopped Scanning");
//        startScanningButton.setVisibility(View.VISIBLE);
//        stopScanningButton.setVisibility(View.INVISIBLE);
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                btScanner.stopScan(leScanCallback);
//                Iterator<BleDevice> itr=history_of_Ble_devices.iterator();
//                while(itr.hasNext())
//                {
//                    BleDevice dev=itr.next();
//                    peripheralTextView.append("device uuid: "+dev.getUuid()+"\n");
//                    peripheralTextView.append("device major_id: "+dev.getMajor_id()+"\n");
//                    peripheralTextView.append("device minor_id: "+dev.getMinor_id()+"\n");
//                }
//
//
//            }
//        });
//    }
}


