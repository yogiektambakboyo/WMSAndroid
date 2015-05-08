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

public class OperatorTaskRBPB_2_ListBrg extends ListActivity {
    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";
    private String TransactionCode = "";

    // url to make request
    private String url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb1.php?operatorcode=1";

    // JSON Node names
    private final String TAG_OPERATORTASKBPB2LISTBRG= "operatortaskreceivebpb2listbrg";
    private final String TAG_TRANSACTIONCODE = "TransactionCode";
    private final String TAG_KETERANGAN = "Keterangan";
    private final String TAG_ERPCODE = "ERPCode";
    private final String TAG_BINCODE = "BinCode";
    private final String TAG_EXPDATE = "ExpDate";
    private final String TAG_QTY = "Qty";
    private final String TAG_NAME = "Name";

    // Cabang JSONArray
    JSONArray operatortaskreceivebpb2 = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskreceivebpb2listbrg);


        Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);
        TransactionCode = in.getStringExtra(TAG_TRANSACTIONCODE);

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> OperatorTaskRBPB2List = new ArrayList<HashMap<String, String>>();

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2listbrg.php?transactioncode="+TransactionCode+"&operatorcode="+OperatorCode;

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);


        try {
            // Getting Array of Barang
            operatortaskreceivebpb2 = json.getJSONArray(TAG_OPERATORTASKBPB2LISTBRG);

            // looping through All Barang
            for(int i = 0; i < operatortaskreceivebpb2.length(); i++){
                JSONObject c = operatortaskreceivebpb2.getJSONObject(i);

                // Storing each json item in variable
                String erpcode = c.getString(TAG_ERPCODE);
                String bincode = c.getString(TAG_BINCODE);
                String keterangan = c.getString(TAG_KETERANGAN);
                String qty = c.getString(TAG_QTY);
                String expdate = c.getString(TAG_EXPDATE);
                String name = c.getString(TAG_NAME);
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_ERPCODE, erpcode);
                map.put(TAG_BINCODE, bincode);
                map.put(TAG_KETERANGAN, keterangan);
                map.put(TAG_QTY, qty);
                map.put(TAG_EXPDATE, expdate);
                map.put(TAG_NAME, name);

                OperatorTaskRBPB2List.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(this, OperatorTaskRBPB2List,
                R.layout.listoperatortaskreceivebpb2listbrg,
                new String[] {TAG_ERPCODE, TAG_BINCODE, TAG_NAME, TAG_KETERANGAN, TAG_EXPDATE, TAG_QTY},
                new int[] { R.id.OPTRBPB2LB_KodeNota,R.id.OPTRBPB2LB_KodeBin,R.id.OPTRBPB2LB_Rack, R.id.OPTRBPB2LB_NamaBrg, R.id.OPTRBPB2LB_Expdate,R.id.OPTRBPB2LB_Jumlah });

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String kodenota = ((TextView) view.findViewById(R.id.OPTRBPB2LB_KodeNota)).getText().toString();

                Toast.makeText(getApplicationContext(),kodenota,Toast.LENGTH_SHORT).show();

            }
        });
    }
}