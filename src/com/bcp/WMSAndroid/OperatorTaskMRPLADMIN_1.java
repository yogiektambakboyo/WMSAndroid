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

public class OperatorTaskMRPLADMIN_1 extends ListActivity {

    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";

    // url to make request
    private String url = "http://192.168.31.10:9020/ws/operatortaskmoverrpl1.php?operatorcode=1";

    // JSON Node names
    private final String TAG_OPERATORTASKMRPL1= "operatortaskmoverrpl1";
    private final String TAG_TRANSACTIONCODE = "TransactionCode";
    private final String TAG_TRANSACTIONDATE = "TransactionDate";
    private final String TAG_ERPCODE = "ERPCode";
    private final String TAG_ASSIGNED = "Assigned";
    private final String TAG_ICON = "1";

    // Cabang JSONArray
    JSONArray operatortaskmoverrpl1 = null;

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] flags = new int[]{
            R.drawable.ic_launcher,
            R.drawable.ic_launcher2,
            R.drawable.ic_launcher,
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskmoverrpl1);

        Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> OperatorTaskMRPLList = new ArrayList<HashMap<String, String>>();

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        url = "http://192.168.31.10:9020/ws/operatortaskmoverrpl1.php?operatorcode="+OperatorCode;

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);


        try {
            // Getting Array of Barang
            operatortaskmoverrpl1 = json.getJSONArray(TAG_OPERATORTASKMRPL1);

            // looping through All Barang
            for(int i = 0; i < operatortaskmoverrpl1.length(); i++){
                JSONObject c = operatortaskmoverrpl1.getJSONObject(i);

                // Storing each json item in variable
                String assigned = c.getString(TAG_ASSIGNED);
                String erpcode = c.getString(TAG_ERPCODE);
                String tgl = c.getString(TAG_TRANSACTIONDATE);
                String transcationcode = c.getString(TAG_TRANSACTIONCODE);
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value

                if (i%2==0){
                    map.put(TAG_ICON,Integer.toString(flags[0]));
                }else{
                    map.put(TAG_ICON,Integer.toString(flags[1]));
                }

                map.put(TAG_ASSIGNED, assigned);
                map.put(TAG_ERPCODE, erpcode);
                map.put(TAG_TRANSACTIONCODE, transcationcode);
                map.put(TAG_TRANSACTIONDATE, tgl);

                OperatorTaskMRPLList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(this, OperatorTaskMRPLList,
                R.layout.listoperatortaskmoverrpl1,
                new String[] { TAG_TRANSACTIONDATE, TAG_TRANSACTIONCODE, TAG_ERPCODE},
                new int[] { R.id.OPTMRPL1_tgl, R.id.OPTMRPL1_TransactionCode,R.id.OPTMRPL1_ERPCode});

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String kodenota = ((TextView) view.findViewById(R.id.OPTMRPL1_ERPCode)).getText().toString();
                String tgl = ((TextView) view.findViewById(R.id.OPTMRPL1_tgl)).getText().toString();

                Toast.makeText(getApplicationContext(),kodenota,Toast.LENGTH_SHORT).show();

                //Intent in = new Intent(getApplicationContext(),OperatorTask.class);
                //in.putExtra(TAG_PERUSAHAAN,perusahaan);
                //in.putExtra(TAG_KODENOTA,kodenota);
                //in.putExtra(TAG_NOTE,note);
                //startActivity(in);

            }
        });


    }


}