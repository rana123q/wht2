package com.example.rana_jabareen.wearablehealthtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by RANA_JABAREEN on 01/09/2016.
 */
public class SaveSharedPreference
{
    static final String PREF_PatientID= "patientID";
    static final String PREF_USER_PICTURE= "PICID";
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setPatientID(Context ctx, int id)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_PatientID, id);
        editor.commit();
    }

    public static int getPatientID(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(PREF_PatientID, 0);
    }
    public static void setPatientPIC(Context ctx, String picData )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_PICTURE, picData);
        editor.commit();
    }

    public static String getPatientPIC(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_PICTURE,"");
    }
}
