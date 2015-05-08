package com.bcp.WMSAndroid;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class OperatorMenu extends ListActivity {

    //DB Handler
    private DBHandler   db;
    private String      DB_PATH= Environment.getExternalStorageDirectory()+"/WMS";
    private String      DB_MAINMENU="MAINMENU";
    private final String TAG_STATUS = "status";


    private final String TAG_OPERATORCODE = "OperatorCode";
    private final String TAG_LOGINTIME= "logintime";
    private final String TAG_OPERATORNAME = "Name";
    private String OperatorCode = "";
    private String OperatorName = "";
    private String OperatorLoginTime = "";

    // url to make request
    private String url = "http://192.168.31.10:9020/ws/operatormenu.php?username=1";

    // JSON Node names
    private final String TAG_PREF= "WMSAndroid";
    private final String TAG_OPERATORMENU= "operatormenu";
    private final String TAG_WHROLECODE = "WHRoleCode";
    private final String TAG_NAME = "Name";
    private final String TAG_ICON = "1";
    private final String TAG_COLOR = "color";

    TextView OperatorNameTxt,OperatorLoginTimeTxt;

    // Cabang JSONArray
    JSONArray operatormenu = null;

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] flags = new int[]{
            R.drawable.wh_receiver,
            R.drawable.wh_mover,
            R.drawable.wh_picking,
            R.drawable.wh_shipping
    };


    private final String TAG_OUTPCK = "StatusPCK";
    private final String TAG_OUTRCV = "StatusRCV";
    private final String TAG_OUTRPL = "StatusRPL";
    private final String TAG_OUTRPLMANUAL = "StatusRPLManual";
    private final String TAG_OUTSPG = "StatusSPG";
    String outpck ="";
    String outrcv ="";
    String outrpl ="";
    String outrplmanual ="";
    String outspg ="";
    JSONArray taskoutstanding = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatormenu);

        OperatorCode = getPref(TAG_OPERATORCODE);
        OperatorLoginTime = getPref(TAG_LOGINTIME);
        OperatorName = getPref(TAG_OPERATORNAME);

        OperatorNameTxt = (TextView) findViewById(R.id.OperatorCode);
        OperatorLoginTimeTxt = (TextView) findViewById(R.id.OperatorLoginTime);

        OperatorNameTxt.setText("User : "+OperatorName);
        OperatorLoginTimeTxt.setText("Login Time : "+OperatorLoginTime);

        // Get Data Fron DB
        db = new DBHandler(getApplicationContext(),DB_PATH, DB_MAINMENU);
        File dbFile = new File(DB_PATH+"/"+DB_MAINMENU);
        // Hashmap for ListView
        ArrayList<HashMap<String, String>> OperatorMenuList = new ArrayList<HashMap<String, String>>();

        JSONObject MenuJSON = null;


        // Check OutStanding
        url = "http://192.168.31.10:9020/ws/operatoroutstanding.php?operatorcode="+OperatorCode;
        JSONParser jParser = new JSONParser();
        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        try {
            // Getting Array of Outstanding
            taskoutstanding = json.getJSONArray("operatoroutstanding");

            for(int i = 0; i < taskoutstanding.length(); i++){
                JSONObject c = taskoutstanding.getJSONObject(i);
                // Storing each json item in variable
                outpck = c.getString(TAG_OUTPCK);
                outrcv = c.getString(TAG_OUTRCV);
                outrpl = c.getString(TAG_OUTRPL);
                outrplmanual = c.getString(TAG_OUTRPLMANUAL);
                outspg = c.getString(TAG_OUTSPG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        if(outpck.equals("1")||outrcv.equals("1")||outrpl.equals("1")||outrplmanual.equals("1")||outspg.equals("1")){
            Intent in = new Intent(getApplicationContext(),OperatorOutStandingMenu.class);
            in.putExtra(TAG_OUTPCK,outpck);
            in.putExtra(TAG_OUTRCV,outrcv);
            in.putExtra(TAG_OUTRPL,outrpl);
            in.putExtra(TAG_OUTRPLMANUAL,outrplmanual);
            in.putExtra(TAG_OUTSPG,outspg);
            startActivity(in);
            Toast.makeText(getApplicationContext(),"Welcome "+ " - " + OperatorName + " You Have Outstanding",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Welcome "+ " - " + OperatorName,Toast.LENGTH_SHORT).show();
        }
        // End

        if(dbFile.exists()){
            try {
                MenuJSON = db.GetMenu(OperatorCode);
                int status = MenuJSON.getInt(TAG_STATUS);
                if(status==0){
                    // Creating JSON Parser instance
                    jParser = new JSONParser();

                    url = "http://192.168.31.10:9020/ws/operatormenu.php?username="+OperatorCode;

                    // getting JSON string from URL
                    MenuJSON = jParser.getJSONFromUrl(url);

                    try {
                        // Getting Array of Barang
                        operatormenu = MenuJSON.getJSONArray(TAG_OPERATORMENU);

                        // looping through All Barang
                        for(int i = 0; i < operatormenu.length(); i++){
                            JSONObject c = operatormenu.getJSONObject(i);
                            db.InsertMenu(c.getString(TAG_OPERATORCODE),c.getString(TAG_WHROLECODE),c.getString(TAG_NAME));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else{
            File WMSDir = new File(DB_PATH);
            WMSDir.mkdirs();
            db.CreateMenu();

            // Creating JSON Parser instance
            jParser = new JSONParser();

            url = "http://192.168.31.10:9020/ws/operatormenu.php?username="+OperatorCode;

            // getting JSON string from URL
            MenuJSON = jParser.getJSONFromUrl(url);

            try {
                // Getting Array of Barang
                operatormenu = MenuJSON.getJSONArray(TAG_OPERATORMENU);

                // looping through All Barang
                for(int i = 0; i < operatormenu.length(); i++){
                    JSONObject c = operatormenu.getJSONObject(i);
                    db.InsertMenu(c.getString(TAG_OPERATORCODE),c.getString(TAG_WHROLECODE),c.getString(TAG_NAME));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            // Getting Array of Barang
            operatormenu = MenuJSON.getJSONArray(TAG_OPERATORMENU);

            // looping through All Barang
            for(int i = 0; i < operatormenu.length(); i++){
                JSONObject c = operatormenu.getJSONObject(i);

                // Storing each json item in variable
                String whrolecode = c.getString(TAG_WHROLECODE);
                String name = c.getString(TAG_NAME);
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_COLOR,Integer.toString(i));

                if (whrolecode.equals("10/WHR/001")){
                    map.put(TAG_ICON,Integer.toString(flags[0]));
                }
                if (whrolecode.equals("10/WHR/002")){
                    map.put(TAG_ICON,Integer.toString(flags[1]));
                }
                if (whrolecode.equals("10/WHR/003")){
                    map.put(TAG_ICON,Integer.toString(flags[2]));
                }
                if (whrolecode.equals("10/WHR/004")){
                    map.put(TAG_ICON,Integer.toString(flags[3]));
                }
                map.put(TAG_NAME, name);
                map.put(TAG_WHROLECODE, whrolecode);

                OperatorMenuList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();



        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new CustomSimpleAdapter(this, OperatorMenuList,
                R.layout.listoperatormenu,
                new String[] { TAG_ICON, TAG_WHROLECODE, TAG_NAME },
                new int[] { R.id.imageViewOP, R.id.WHRoleCode, R.id.Keterangan });

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String nama = ((TextView) view.findViewById(R.id.Keterangan)).getText().toString();
                String kode = ((TextView) view.findViewById(R.id.WHRoleCode)).getText().toString();

                Intent in = new Intent(getApplicationContext(),OperatorTask.class);
                in.putExtra(TAG_NAME,nama);
                in.putExtra(TAG_WHROLECODE,kode);
                in.putExtra(TAG_OPERATORCODE,OperatorCode);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Yakin ingin logout?").setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();
        }
        return false;
    }

    public String getPref(String KEY){
        SharedPreferences LoginPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        String Value=LoginPref.getString(KEY,"0");
        return  Value;
    }
}