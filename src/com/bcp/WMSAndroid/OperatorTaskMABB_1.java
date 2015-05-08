package com.bcp.WMSAndroid;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OperatorTaskMABB_1 extends ListActivity {

    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";

    //TransactionCode":"0","SKUCode":"0","Qty":"0","ERPCode":"0","BinCode":"0","DestRackSlot":"0","qtyKonv":"0","NoUrut":"0","ExpDate":"0","selisih":"0","selisihKonv":"0","Keterangan":"0","destBin":"0"
    // url to make request
    private String url = "http://192.168.31.10:9020/ws/operatortaskmoverabb1.php";

    // JSON Node names
    private final String TAG_OPERATORTASKMABB1= "operatortaskmoverabb1";
    private final String TAG_TRANSACTIONCODE = "TransactionCode";
    private final String TAG_SKUCODE = "SKUCode";
    private final String TAG_ERPCODE = "ERPCode";
    private final String TAG_BINCODE = "BinCode";
    private final String TAG_DESTRACKSLOT = "DestRackSlot";
    private final String TAG_QTYKONV = "qtyKonv";
    private final String TAG_NOURUT = "NoUrut";
    private final String TAG_EXPDATE = "ExpDate";
    private final String TAG_SELISIH = "selisih";
    private final String TAG_SELISIHKONV = "selisihKonv";
    private final String TAG_KETERANGAN = "Keterangan";
    private final String TAG_DESTBIN = "destBin";
    private final String TAG_ICON = "1";

    // Cabang JSONArray
    JSONArray operatortaskmabb1 = null;

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] flags = new int[]{
            R.drawable.ic_launcher,
            R.drawable.ic_launcher2,
            R.drawable.ic_launcher,
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskmoverabb1);

        Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> OperatorTaskMABB1List = new ArrayList<HashMap<String, String>>();

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        url = "http://192.168.31.10:9020/ws/operatortaskmoverabb1.php";

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);


        try {
            // Getting Array of Barang
            operatortaskmabb1 = json.getJSONArray(TAG_OPERATORTASKMABB1);

            // looping through All Barang
            for(int i = 0; i < operatortaskmabb1.length(); i++){
                JSONObject c = operatortaskmabb1.getJSONObject(i);

                // Storing each json item in variable
                String bincode = c.getString(TAG_BINCODE);
                String destbin = c.getString(TAG_DESTBIN);
                String destrackslot = c.getString(TAG_DESTRACKSLOT);
                String erpcode = c.getString(TAG_ERPCODE);
                String expdate = c.getString(TAG_EXPDATE);
                String keterangan = c.getString(TAG_KETERANGAN);
                String nourut = c.getString(TAG_NOURUT);
                String qtykonv = c.getString(TAG_QTYKONV);
                String selisih = c.getString(TAG_SELISIH);
                String selisihkonv = c.getString(TAG_SELISIHKONV);
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value

                if (i%2==0){
                    map.put(TAG_ICON,Integer.toString(flags[0]));
                }else{
                    map.put(TAG_ICON,Integer.toString(flags[1]));
                }

                map.put(TAG_BINCODE, bincode);
                map.put(TAG_DESTBIN, destbin );
                map.put(TAG_DESTRACKSLOT, destrackslot);
                map.put(TAG_ERPCODE, erpcode);
                map.put(TAG_EXPDATE, expdate);
                map.put(TAG_KETERANGAN, keterangan);
                map.put(TAG_NOURUT, nourut);
                map.put(TAG_QTYKONV, qtykonv);
                map.put(TAG_SELISIH, selisih);
                map.put(TAG_SELISIHKONV, selisihkonv);

                OperatorTaskMABB1List.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(this, OperatorTaskMABB1List,
                R.layout.listoperatortaskmoverabb1,
                new String[] { TAG_ERPCODE, TAG_SELISIHKONV, TAG_KETERANGAN, TAG_SKUCODE},
                new int[] { R.id.OPTMABB1_ERPCode, R.id.OPTMABB1_Jumlah,R.id.OPTMABB1_NamaBarang, R.id.OPTMABB1_SKUCode });

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String erpcode = ((TextView) view.findViewById(R.id.OPTMABB1_ERPCode)).getText().toString();
                Toast.makeText(getApplicationContext(),erpcode,Toast.LENGTH_SHORT).show();
                //Intent in = new Intent(getApplicationContext(),OperatorTask.class);
                //in.putExtra(TAG_PERUSAHAAN,perusahaan);
                //in.putExtra(TAG_KODENOTA,kodenota);
                //in.putExtra(TAG_NOTE,note);
                //startActivity(in);

            }
        });


    }

}