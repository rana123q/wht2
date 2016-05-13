package com.example.rana_jabareen.wearablehealthtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isConnected=isNetworkAvailable(context);
        if(isConnected)
        {
            InsertLocation( context);

        }

    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

   public void InsertLocation(Context context)
   {
       String InsertLocationURL="http://ranasaleh-001-site1.atempurl.com/WHT.svc/LocationInsert";
       final DBHelper whtDB=new DBHelper(context);
       int patientId= SaveSharedPreference.getPatientID(context);
       final ArrayList <LocationClass> locationsSets= whtDB.getLocations( patientId);
       LocationUploadTask task = new LocationUploadTask(locationsSets);
       task.delegate = new AsyncResponse() {

           @Override
           public void processFinish(String output) {

               if (output.equals("Upload Successfully"))
               {
                whtDB.UpdateLocations(locationsSets);

               }


           }
       };
       task.execute(InsertLocationURL);



   }


}
