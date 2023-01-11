package com.example.native_flu;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.widget.TextView;

import android.os.Build;
import android.window.SplashScreenView;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends FlutterActivity {

   TextView txt_animation;
   private MethodChannel channel;
   private static final String BATTERY_CHANNEL="vivek/getBattery";

   private WifiManager wifiManager;
   WifiReceiver receiverWifi;
    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    List<ScanResult> wifiList;


    private int getBatteryLevel() {
    int batteryLevel = -1;
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
      batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    } else {
      Intent intent = new ContextWrapper(getApplicationContext()).
          registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
      batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
          intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    }

    return batteryLevel;
  }


   @Override
   public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {

        System.out.println("ConfigureFlutterEngine method called");
        
       channel=new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(),BATTERY_CHANNEL);
       channel.setMethodCallHandler((call, result) -> {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
       if (!wifiManager.isWifiEnabled()) {
           wifiManager.setWifiEnabled(true);
       }
       if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
               != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(
                   MainActivity.this,
                   new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION
           );
       } else {
           if(
           wifiManager.startScan()){
               List<ScanResult> availableNetworks=wifiManager.getScanResults();

                if(availableNetworks.size()>0){
//                  System.out.println(availableNetworks);
                    ArrayList<String> deviceList = new ArrayList<String>();

                    for (int i = 0; i < availableNetworks.size(); i++) {
                        System.out.println(availableNetworks.get(i).SSID);
                        deviceList.add(availableNetworks.get(i).SSID.toString());

                    }
//                     ArrayList<String> deviceList = new ArrayList<>();
                    // for (ScanResult scanResult : wifiList) {

                    //     deviceList.add(scanResult.SSID + " - " + scanResult.capabilities);
                    // }
                    // System.out.println(deviceList);
                   result.success(deviceList);
                }

           }
           System.out.println("task is");
           
//           Handler handler=new Handler();
//           handler.postDelayed(new Runnable() {
//               @Override
//               public void run() {
//                   System.out.println("Waiting Colmpleted");
//                   System.out.println(wifiManager.getScanResults());
//
//               }
//           },10000);

       }
          // wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

          //  if (!wifiManager.isWifiEnabled()) {
          //      result.success("Not started wifi");
          //      wifiManager.setWifiEnabled(true);
          //  }
           
          //  wifiManager.startScan();
//            List<ScanResult> availableNetworks=wifiManager.getScanResults();
          //  result.success(availableNetworks);

//            if(availableNetworks.size()>0){
//                String wifis[] =new String[availableNetworks.size()];
//                for (int i = 0; i < availableNetworks.size(); i++) {
//                    wifis[i]=availableNetworks.get(i).toString();
//                }
//                result.success(wifis);
//            }
          //  result.success("get wifi list");
       });






//      channel= new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(),BATTERY_CHANNEL);
//      channel.setMethodCallHandler((call, result) -> {
//         // if(call.method=="getBatteryinfo"){
//            result.success(getBatteryLevel());
//         // }
//      }
//      );
   }

    // @Override
    // public void onPostResume() {
    //     super.onPostResume();
    //     receiverWifi=new WifiReceiver(wifiManager);
    //     IntentFilter intentFilter=new IntentFilter();
    //     intentFilter.addAction(wifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
    //     registerReceiver(receiverWifi,intentFilter);
    //     getWifi();
    // }
    private void getWifi() {
        System.out.println("getWifi method called");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            } else {

                wifiManager.startScan();
            }
        } else {

            wifiManager.startScan();
        }
    }



    @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
          
      System.out.println("onCreate method called");
      // setContentView(R.layout.main);
      // txt_animation=findViewById(R.id.idTVHeading);
      // YoYo.with(Techniques.RubberBand).duration(5000).repeat(1).playOn(txt_animation);
      

   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("onRequestPermissionsResult method called");
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    wifiManager.startScan();
                } else {
                    return;
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPaused method called");

        unregisterReceiver(receiverWifi);
    }
}

class WifiReceiver extends BroadcastReceiver{

    WifiManager wifiManager;
    StringBuffer sb;
    List<ScanResult> wifiList;

    public WifiReceiver(WifiManager wifiManager){
        this.wifiManager=wifiManager;
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive method called");

        String action=intent.getAction();
        if(wifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)){
            sb=new StringBuffer();
            wifiList=wifiManager.getScanResults();
            System.out.println(wifiList);

        }
    }
}
