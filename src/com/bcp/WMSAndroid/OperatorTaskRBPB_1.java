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

public class OperatorTaskRBPB_1 extends ListActivity {

    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";

    // url to make request
    private String url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb1.php?operatorcode=1";

    // JSON Node names
    private final String TAG_OPERATORTASKBPB1= "operatortaskreceivebpb1";
    private final String TAG_TRANSACTIONCODE = "TransactionCode";
    private final String TAG_KODENOTA = "KodeNota";
    private final String TAG_PERUSAHAAN = "Perusahaan";
    private final String TAG_NOTE = "Note";
    private final String TAG_ASSIGNED = "Assigned";
    private final String TAG_ICON = "1";

    // Cabang JSONArray
    JSONArray operatortaskbpb1 = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskreceivebpb1);

        Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> OperatorTaskBPB1List = new ArrayList<HashMap<String, String>>();

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb1.php?operatorcode="+OperatorCode;

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);


        try {
            // Getting Array of Barang
            operatortaskbpb1 = json.getJSONArray(TAG_OPERATORTASKBPB1);

            // looping through All Barang
            for(int i = 0; i < operatortaskbpb1.length(); i++){
                JSONObject c = operatortaskbpb1.getJSONObject(i);

                // Storing each json item in variable
                String assigned = c.getString(TAG_ASSIGNED);
                String kodenota = c.getString(TAG_KODENOTA);
                String note = c.getString(TAG_NOTE);
                String perusahaan = c.getString(TAG_PERUSAHAAN);
                String transcationcode = c.getString(TAG_TRANSACTIONCODE);
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value

                map.put(TAG_ASSIGNED, assigned);
                map.put(TAG_KODENOTA, kodenota);
                map.put(TAG_TRANSACTIONCODE, transcationcode);
                map.put(TAG_NOTE, note);
                map.put(TAG_PERUSAHAAN, perusahaan);

                OperatorTaskBPB1List.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(this, OperatorTaskBPB1List,
                R.layout.listoperatortaskreceivebpb1,
                new String[] { TAG_PERUSAHAAN, TAG_KODENOTA, TAG_NOTE, TAG_TRANSACTIONCODE},
                new int[] {R.id.OPTBPB1_Perusahaan, R.id.OPTBPB1_kodenota,R.id.OPTBPB1_note, R.id.OPTBPB1_TRCode });

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String perusahaan = ((TextView) view.findViewById(R.id.OPTBPB1_Perusahaan)).getText().toString();
                String kodenota = ((TextView) view.findViewById(R.id.OPTBPB1_kodenota)).getText().toString();
                String note = ((TextView) view.findViewById(R.id.OPTBPB1_note)).getText().toString();
                String trcode = ((TextView) view.findViewById(R.id.OPTBPB1_TRCode)).getText().toString();

                Toast.makeText(getApplicationContext(),kodenota,Toast.LENGTH_SHORT).show();

                Intent in = new Intent(getApplicationContext(),OperatorTaskRBPB_2.class);
                in.putExtra(TAG_PERUSAHAAN,perusahaan);
                in.putExtra(TAG_KODENOTA,kodenota);
                in.putExtra(TAG_TRANSACTIONCODE,trcode);
                in.putExtra(TAG_NOTE,note);
                in.putExtra(TAG_OPERATOR,OperatorCode);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });


    }
}