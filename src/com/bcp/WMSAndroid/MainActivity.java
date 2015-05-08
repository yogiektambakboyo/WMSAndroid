package com.bcp.WMSAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.*;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {

    // url to make request
    private String username = "";
    private String password = "";
    private String url = "http://192.168.31.10:9020/login.php?username=1&password=1";

    // JSON Node names
    private final String TAG_PREF= "WMSAndroid";
    private final String TAG_LOGIN= "login";
    private final String TAG_LOGINTIME= "logintime";
    private final String TAG_JUMLAH = "jumlah";
    private final String TAG_OPERATORCODE = "OperatorCode";
    private final String TAG_OPERATORNAME = "Name";
    private final String TAG_OPERATORSITEID = "SiteId";
    private final String TAG_RECEIVESOURCE = "ReceiveSource";
    private final String TAG_RECEIVEPROBLEM = "ReceiveProblem";
    private final String TAG_SHIPPINGPROBLEM = "ShippingProblem";
    private final String TAG_REPLENISHPROBLEM = "ReplenishProblem";

    String OperatorName ="";
    String ReceiveSource ="";
    String ReceiveProblem ="";
    String ShippingProblem ="";
    String ReplenishProblem ="";
    String SiteId ="";
    String jumlah ="";
    String operator ="";

    // Login JSONArray
    JSONArray login = null;


    int jum=0;
    int networkstatus = 0;

    NetConStatus netConStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Button btnlogin=(Button) findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ((EditText) findViewById(R.id.txtusername)).getText().toString();
                password = ((EditText) findViewById(R.id.txtpassword)).getText().toString();

                username = username.replace(" ", "");
                password = password.replace(" ", "");

                netConStatus = new NetConStatus();
                networkstatus= netConStatus.getConnectivityStatusString(getBaseContext());

                if ((username.equals(""))||(password.equals(""))){
                    Toast.makeText(getApplicationContext(), "Login Gagal - Tolong isi dengan lengkap!", Toast.LENGTH_SHORT).show();
                }else{
                    if(networkstatus==1){
                        new LoginTask(MainActivity.this).execute();
                    }else{
                        Toast.makeText(getApplicationContext(), "Tolong cek koneksi anda, pastikan koneksi ke WiFi aktif!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wmsactionbar, menu);
        return true;
    }

    public class LoginTask extends AsyncTask<Void, Integer, Void> {

        Context context;
        Handler handler;
        Dialog dialog;
        TextView txtLoadingProgress;
        ProgressBar ProgressBar;

        LoginTask(Context context, Handler handler){
            this.context=context;
            this.handler=handler;

        }

        LoginTask(Context context){
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // create dialog
            dialog=new Dialog(context);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressdialogwms);
            txtLoadingProgress =(TextView) dialog.findViewById(R.id.txtLoading);
            ProgressBar =(ProgressBar)dialog.findViewById(R.id.progressBar);
            txtLoadingProgress.setText("Logging In. . .");
            dialog.show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            username = ((EditText) findViewById(R.id.txtusername)).getText().toString();
            password = ((EditText) findViewById(R.id.txtpassword)).getText().toString();

            username = username.replace(" ", "");
            password = password.replace(" ", "");

            url = "http://192.168.31.10:9020/ws/login.php?username="+username+"&password="+password;
            final JSONParser jParser = new JSONParser();
            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url);

            try {
                    // Getting Array of Login
                    login = json.getJSONArray(TAG_LOGIN);
                    jumlah = "";
                    operator = "";

                    // looping through All Barang
                    for(int i = 0; i < login.length(); i++){
                        JSONObject c = login.getJSONObject(i);

                        // Storing each json item in variable
                        jumlah = c.getString(TAG_JUMLAH);
                        operator = c.getString(TAG_OPERATORCODE);
                        OperatorName = c.getString(TAG_OPERATORNAME);
                        SiteId = c.getString(TAG_OPERATORSITEID);
                        ReceiveSource = c.getString(TAG_RECEIVESOURCE);
                        ReceiveProblem = c.getString(TAG_RECEIVEPROBLEM);
                        ShippingProblem = c.getString(TAG_SHIPPINGPROBLEM);
                        ReplenishProblem = c.getString(TAG_REPLENISHPROBLEM);

                        jum = Integer.parseInt(jumlah);
                    }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate();
            txtLoadingProgress.setText("Logging In. . .");
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (jum > 0){
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String LoginTime = sdf.format(cal.getTime());

                setPref(OperatorName,operator,LoginTime,SiteId,ReceiveSource,ReceiveProblem,ShippingProblem,ReplenishProblem);
                Intent in = new Intent(getApplicationContext(),OperatorMenu.class);
                startActivity(in);
            }else{
                Toast.makeText(context,"Login Gagal - "+operator,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            MainActivity.this.finish();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Yakin ingin keluar?").setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();
        }
        return false;
    }

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

}
