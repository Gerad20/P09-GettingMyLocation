package com.myapplicationdev.android.p09_gettingmylocation;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyService extends Service {
    public MyService() {
    }

    boolean started;
    String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        Log.d("Service", "Service created");


        File folder = new File(folderLocation);
        if (folder.exists() == false){
            boolean result = folder.mkdir();
            if (result == true){
                Log.d("File Read/Write", "Folder created");
            }
        }

        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (started == false){
            Toast.makeText(getApplicationContext(),folderLocation, Toast.LENGTH_LONG).show();
            started = true;
            FusedLocationProviderClient fusedLocationClient;
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(MyService.this);
            LocationRequest mLocationRequest= new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setSmallestDisplacement(100);
            LocationCallback mlocationcallBack = new LocationCallback() {
                String msg = "";
                @Override
                public void onLocationResult(LocationResult locationResult){
                    if (locationResult != null){
                        try {
                            Location data = locationResult.getLastLocation();
                            double lat = data.getLatitude();
                            double lng = data.getLatitude();
                        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/P09";
                        File targetFile = new File(folderLocation,"data.txt");
                        FileWriter writer = new FileWriter(targetFile,true);
                        writer.write(String.valueOf(lat) +" " +  String.valueOf(lng));
                        writer.flush();
                        writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(MyService.this, msg, Toast.LENGTH_SHORT).show();
                }
            };
            fusedLocationClient.requestLocationUpdates(mLocationRequest,mlocationcallBack,null);
            Toast.makeText(getApplicationContext(),"Service is running",Toast.LENGTH_LONG).show();
        } else {
            Log.d("Service", "Service is still running");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(),"Service has stopped running",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}
