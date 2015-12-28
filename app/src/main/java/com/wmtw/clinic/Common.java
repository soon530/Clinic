package com.wmtw.clinic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import hugo.weaving.DebugLog;

public class Common {
    // location
    public static double[] loc = {0, 0};
    public static LocationManager locationManager;
    public static String provider = "";
    private final static LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public static ArrayList<Clinic.ClinicEntity> clinicArray;
    public static ArrayList<Object> markAry;


    public static boolean isNetwork(Context mContext){//檢查網路
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean is3g = true;boolean isWifi = true;
        try{
            if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!= null){
                is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
            }

            if(manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!= null){
                isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
            }
//			System.out.println("aais3g: " + is3g + manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE));
//			System.out.println("aaisWifi: " + isWifi +manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI));
        }catch(NullPointerException e){e.printStackTrace();}
        if(!is3g && !isWifi)return false;
        else return true;
    }

    public static void setUserLocation(Context context){
        Location location=null;
        try{
//			locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(provider);
            if(Common.isNetwork(context)){
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Common.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 0, locationListener);
            }else if(Common.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Common.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 0, locationListener);
            }


        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(IllegalArgumentException e){
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Common.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 0, locationListener);
        }


        if(location != null ){
            setUserLocation(location.getLatitude(), location.getLongitude());
        }
        //locationManager.requestLocationUpdates(provider, 20000, 0, locationListener);
    }

    @DebugLog
    private static void setUserLocation(double latitude, double longitude) {
        loc[0] = latitude;
        loc[1] = longitude;
    }

    public static void alert(Context mContext,int mesg,int title){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mesg);
        builder.setTitle(title);
        builder.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //打電話
    public static void showCallPhoneAlert(Context context, String msg, String tel){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final Context mContext = context;
        final String Tel = tel;
        builder.setTitle(msg);
        builder.setMessage(tel);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.callout, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Uri uri = Uri.parse("tel:" + Tel);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                mContext.startActivity(intent);

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }


}
