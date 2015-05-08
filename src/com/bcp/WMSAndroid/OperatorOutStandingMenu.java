package com.bcp.WMSAndroid;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OperatorOutStandingMenu extends ListActivity {

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
    private final String TAG_WHROLECODE = "WHRoleCode";
    private final String TAG_NAME = "Name";
    private final String TAG_ICON = "1";
    private final String TAG_COLOR = "color";

    private final String TAG_OUTPCK = "StatusPCK";
    private final String TAG_OUTRCV = "StatusRCV";
    private final String TAG_OUTRPL = "StatusRPL";
    private final String TAG_OUTRPLMANUAL = "StatusRPLManual";
    private final String TAG_OUTSPG = "StatusSPG";

    private final String TAG_TRANSACTIONCODE = "TransactionCode";
    private final String TAG_DESTBIN = "DestBin";
    private final String TAG_ERPCODE = "ERPCode";

    String outpck ="0";
    String outrcv ="0";
    String outrpl ="0";
    String outrplmanual ="0";
    String outspg ="0";
    String TransactionCode ="0";
    String DestBin ="0";
    String ERPCode ="0";

    TextView OperatorNameTxt,OperatorLoginTimeTxt;

    JSONArray operatortaskppckoustanding=null;


    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] flags = new int[]{
            R.drawable.wh_receiver,
            R.drawable.wh_mover,
            R.drawable.wh_picking,
            R.drawable.wh_shipping
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatoroutstanding);

        Intent in = getIntent();
        outpck = in.getStringExtra(TAG_OUTPCK);
        outrcv = in.getStringExtra(TAG_OUTRCV);
        outrpl = in.getStringExtra(TAG_OUTRPL);
        outrplmanual = in.getStringExtra(TAG_OUTRPLMANUAL);
        outspg = in.getStringExtra(TAG_OUTSPG);

        OperatorCode = getPref(TAG_OPERATORCODE);
        OperatorLoginTime = getPref(TAG_LOGINTIME);
        OperatorName = getPref(TAG_OPERATORNAME);

        OperatorNameTxt = (TextView) findViewById(R.id.OperatorCode);
        OperatorLoginTimeTxt = (TextView) findViewById(R.id.OperatorLoginTime);

        OperatorNameTxt.setText("User : "+OperatorName);
        OperatorLoginTimeTxt.setText("Login Time : "+OperatorLoginTime);


        // Hashmap for ListView
        ArrayList<HashMap<String, String>> OperatorMenuList = new ArrayList<HashMap<String, String>>();

            if (outrcv.equals("1")){
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_ICON,Integer.toString(flags[0]));
                map.put(TAG_NAME,"Receiving & Retur");
                map.put(TAG_WHROLECODE,"1");
                map.put(TAG_COLOR,Integer.toString(1));
                OperatorMenuList.add(map);
            }
            if (outpck.equals("1")){
                // Creating JSON Parser instance
                JSONParser jParser = new JSONParser();

                url = "http://192.168.31.10:9020/ws/operatortaskpickingpckgetoutstanding.php?operatorcode="+OperatorCode;

                // getting JSON string from URL
                JSONObject json = jParser.getJSONFromUrl(url);


                try {
                    // Getting Array of Barang
                    operatortaskppckoustanding = json.getJSONArray("operatortaskpickingpckgetoutstanding");

                    // looping through All Barang
                    for(int i = 0; i < operatortaskppckoustanding.length(); i++){
                        JSONObject c = operatortaskppckoustanding.getJSONObject(i);
                        TransactionCode = c.getString(TAG_TRANSACTIONCODE);
                        DestBin = c.getString(TAG_DESTBIN);
                        ERPCode = c.getString(TAG_ERPCODE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_ICON,Integer.toString(flags[1]));
                map.put(TAG_NAME,"Picking");
                map.put(TAG_WHROLECODE,"2");
                map.put(TAG_COLOR,Integer.toString(2));
                OperatorMenuList.add(map);
            }
            if (outrpl.equals("1")){
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_ICON,Integer.toString(flags[2]));
                map.put(TAG_NAME,"Replenish");
                map.put(TAG_WHROLECODE,"3");
                map.put(TAG_COLOR,Integer.toString(3));
                map.put(TAG_TRANSACTIONCODE,TransactionCode);
                map.put(TAG_DESTBIN,DestBin);
                OperatorMenuList.add(map);
            }
            if (outrplmanual.equals("1")){
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_ICON,Integer.toString(flags[2]));
                map.put(TAG_NAME,"Replenish Manual");
                map.put(TAG_WHROLECODE,"4");
                map.put(TAG_COLOR,Integer.toString(3));

                OperatorMenuList.add(map);
            }
            if (outspg.equals("1")){
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_ICON,Integer.toString(flags[3]));
                map.put(TAG_NAME,"Shipping");
                map.put(TAG_WHROLECODE,"5");
                OperatorMenuList.add(map);
            }

        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new CustomSimpleAdapter(this, OperatorMenuList,
                R.layout.listoperatormenu,
                new String[] { TAG_ICON, TAG_WHROLECODE, TAG_NAME},
                new int[] { R.id.imageViewOP, R.id.WHRoleCode, R.id.Keterangan});

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

                if(kode.equals("1")){
                    Intent in = new Intent(getApplicationContext(),OperatorOutStandingRcv.class);
                    in.putExtra(TAG_NAME,nama);
                    in.putExtra(TAG_WHROLECODE,kode);
                    in.putExtra(TAG_OPERATORCODE,OperatorCode);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }

                if(kode.equals("2")){
                    Intent in = new Intent(getApplicationContext(),OperatorTaskPPCK_2.class);
                    in.putExtra(TAG_NAME,nama);
                    in.putExtra(TAG_WHROLECODE,kode);
                    in.putExtra(TAG_OPERATORCODE,OperatorCode);
                    in.putExtra(TAG_TRANSACTIONCODE,TransactionCode);
                    in.putExtra(TAG_ERPCODE, ERPCode);
                    in.putExtra(TAG_DESTBIN, DestBin);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }
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