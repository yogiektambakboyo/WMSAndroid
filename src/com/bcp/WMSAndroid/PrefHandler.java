package com.bcp.WMSAndroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefHandler extends Activity {
    private final String TAG_PREF= "WMSAndroid";
    private final String TAG_LOGINTIME= "logintime";
    private final String TAG_OPERATORCODE = "OperatorCode";
    private final String TAG_OPERATORNAME = "Name";
    private final String TAG_OPERATORSITEID = "SiteId";
    private final String TAG_RECEIVESOURCE = "ReceiveSource";
    private final String TAG_RECEIVEPROBLEM = "ReceiveProblem";
    private final String TAG_SHIPPINGPROBLEM = "ShippingProblem";
    private final String TAG_REPLENISHPROBLEM = "ReplenishProblem";


    public void setPref(String OperatorName, String OperatorCode,String LoginTime, String SiteId, String ReceiveSource, String ReceiveProblem, String ShippingProblem, String ReplenishProblem){
        SharedPreferences LoginPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor LoginPrefEditor = LoginPref.edit();
        LoginPrefEditor.putString(TAG_OPERATORNAME,OperatorName);
        LoginPrefEditor.putString(TAG_OPERATORCODE,OperatorCode);
        LoginPrefEditor.putString(TAG_LOGINTIME,LoginTime);
        LoginPrefEditor.putString(TAG_OPERATORSITEID,SiteId);
        LoginPrefEditor.putString(TAG_RECEIVESOURCE,ReceiveSource);
        LoginPrefEditor.putString(TAG_RECEIVEPROBLEM,ReceiveProblem);
        LoginPrefEditor.putString(TAG_SHIPPINGPROBLEM,ShippingProblem);
        LoginPrefEditor.putString(TAG_REPLENISHPROBLEM,ReplenishProblem);
        LoginPrefEditor.commit();
    }

    public String getPref(String KEY){
        SharedPreferences LoginPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        String Value=LoginPref.getString(KEY,"0");
        return  Value;
    }


}
