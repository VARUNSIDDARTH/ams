package com.example.raama.ams;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
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
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class enterotp extends AppCompatActivity {

    static int correctOTP;
    Set<BleDevice> devs=new HashSet<BleDevice>();
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    TextView studentView;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final long SCAN_PERIOD = 1000;
    Boolean isBlepresent=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterotp);

        studentView = (TextView) findViewById(R.id.studentScanView);
        studentView.setMovementMethod(new ScrollingMovementMethod());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Enter OTP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getting the correct otp

        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("otp/otp");
        ValueEventListener listener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String s = dataSnapshot.getValue().toString();
                correctOTP=Integer.parseInt(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(enterotp.this,"no database connection",Toast.LENGTH_SHORT).show();
            }
        };
        myRef.addValueEventListener(listener);


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
                //peripheralTextView.append("actual:"+a[i]+" removing 2s complement:"+(a[i]&0xFF)+" convert:"+Integer.toHexString(a[i]&0xFF)+"\n");
            }
            major_id=((a[major_index-1]&0xFF)<<8)+(a[major_index]&0xFF);
            minor_id=((a[minor_index-1]&0xFF)<<8)+(a[minor_index]&0xFF);

            BleDevice dev =new BleDevice(uuid_string,major_id,minor_id);

            devs.add(dev);
            //Toast.makeText(enterotp.this,"inside scanresults "+uuid_string,Toast.LENGTH_SHORT).show();
           // studentView.append("uuid "+uuid_string+"\n");
            //studentView.append("major id "+major_id+"\n");
            //studentView.append("minor id "+minor_id+"\n");



            //studentView.append("\n");

            // auto scroll for text view
            final int scrollAmount = studentView.getLayout().getLineTop(studentView.getLineCount()) - studentView.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0)
                studentView.scrollTo(0, scrollAmount);

        }
    };

    public void check(View view)
    {
        //verification part
        EditText editText=findViewById(R.id.entered_otp);
        String a=editText.getText().toString();
        int enteredOTP=Integer.parseInt(a);


//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                btScanner.startScan(leScanCallback);
//            }
//        });
//
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                btScanner.stopScan(leScanCallback);
//            }
//        });

         //method 2
        //boolean mScanning;


        //Handler handler;

        //studentView.append("entering startscan\n");
            // Stops scanning after a pre-defined scan period.
           // btScanner.startScan(leScanCallback);
        //studentView.append("entering endscan\n");



        btScanner.startScan(leScanCallback);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   // mScanning = false;
                    btScanner.stopScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            //mScanning = true;



        String hard_coded_Ble="22afaaf6b87f41be83a210d192b89ec8";
        Iterator<BleDevice> itr=devs.iterator();
        while(itr.hasNext())
        {
            BleDevice dev=itr.next();
            //studentView.append("uuid: "+dev.getUuid()+"\n");
            if(dev.getUuid().contentEquals(hard_coded_Ble))
            {
                isBlepresent=true;
            }
        }



        if(isBlepresent)//blecard in proximity
        {
            if(enteredOTP==correctOTP)
            {
                Toast.makeText(enterotp.this,"correct otp",Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(enterotp.this,"wrong otp",Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(enterotp.this,"not in proximity",Toast.LENGTH_SHORT).show();
        }




    }

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
}
