package com.bcp.WMSAndroid;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OperatorTaskRR_1 extends ListActivity {

    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";
    // url to make request
    private  String url = "http://192.168.31.10:9020/ws/operatortaskreceiveretur1.php?operatorcode=1";

    // JSON Node names
    private  final String TAG_OPERATORTASKBPB1= "operatortaskreceiveretur1";
    private  final String TAG_TRANSACTIONCODE = "TransactionCode";
    private  final String TAG_ERPCODE= "ERPCode";
    private  final String TAG_TRANSACTIONDATE = "TransactionDate";
    private  final String TAG_ASSIGNED = "Assigned";
    private  final String TAG_RACKCODE = "RackCode";
    private  final String TAG_ICON = "1";

    private final String TAG_CARIRAK = "operatortaskreceivebpb2getrakslot";

    // Cabang JSONArray
    JSONArray operatortaskbpb1 = null;

    int[] flags = new int[]{
            R.drawable.ic_launcher,
            R.drawable.ic_launcher2,
            R.drawable.ic_launcher,
    };

    TextView   TxtRackName;
    EditText   InputRackSlot;
    ImageView  RackMsg;

    JSONArray  rak=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskreceiveretur1);

        Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);

        RackMsg = (ImageView) findViewById(R.id.OPTRR1_RackDestMsg);
        RackMsg.setVisibility(View.GONE);

        InputRackSlot = (EditText) findViewById(R.id.OPTRR1_Rack);
        InputRackSlot.requestFocus();
        TxtRackName = (TextView) findViewById(R.id.OPTRR1_RackName);
        TxtRackName.setVisibility(View.GONE);

        InputRackSlot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TxtRackName.setVisibility(View.VISIBLE);
                if(InputRackSlot.getText().toString().length()==8){

                    // Creating JSON Parser instance
                    JSONParser jParser = new JSONParser();

                    // 4987176006783 10/00/R/000001";
                    String url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2getrakslot.php?koderak="+InputRackSlot.getText().toString().replace(" ","");

                    // getting JSON string from URL
                    JSONObject json = jParser.getJSONFromUrl(url);

                    try {
                        // Getting Array of Satuan
                        rak = json.getJSONArray(TAG_CARIRAK);
                        // looping through All Barang
                        for (int i = 0; i < rak.length(); i++) {
                            JSONObject c = rak.getJSONObject(i);
                            TxtRackName.setText(c.getString("Name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    TxtRackName.setText("Rak Tidak Ada");
                }

                if(TxtRackName.getText().equals("Rak Tidak Ada")){
                    RackMsg.setVisibility(View.VISIBLE);
                }else{
                    RackMsg.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // Hashmap for ListView
        ArrayList<HashMap<String, String>> OperatorTaskRR1List = new ArrayList<HashMap<String, String>>();

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        url = "http://192.168.31.10:9020/ws/operatortaskreceiveretur1.php?operatorcode="+OperatorCode;

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
                String erpcode = c.getString(TAG_ERPCODE);
                String transactiondate = c.getString(TAG_TRANSACTIONDATE);
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
                map.put(TAG_TRANSACTIONDATE, transactiondate);

                OperatorTaskRR1List.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(this, OperatorTaskRR1List,
                R.layout.listoperatortaskreceiveretur1,
                new String[] { TAG_ERPCODE, TAG_TRANSACTIONDATE, TAG_ASSIGNED, TAG_TRANSACTIONCODE},
                new int[] { R.id.OPTRR1_ERPCode, R.id.OPTRR1_TransactionDate, R.id.OPTRR1_Assigned, R.id.OPTRR1_TransactionCode });

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String erpcode = ((TextView) view.findViewById(R.id.OPTRR1_ERPCode)).getText().toString();
                String tgl = ((TextView) view.findViewById(R.id.OPTRR1_TransactionDate)).getText().toString();
                String trcode = ((TextView) view.findViewById(R.id.OPTRR1_TransactionCode)).getText().toString();

                if(InputRackSlot.getText().toString().length()==0){
                    RackMsg.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Rak/Staging Harus Diisi!",Toast.LENGTH_SHORT).show();
                }
                else if((TxtRackName.getText().equals("Rak Tidak Ada"))||(InputRackSlot.getText().toString().length()<8)){
                    RackMsg.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Rak/Staging Salah!",Toast.LENGTH_SHORT).show();
                }else{
                    Intent in = new Intent(getApplicationContext(), OperatorTaskRR_2.class);
                    in.putExtra(TAG_RACKCODE,InputRackSlot.getText().toString());
                    in.putExtra(TAG_TRANSACTIONCODE, trcode);
                    startActivity(in);
                    Toast.makeText(getApplicationContext(),"Berhasil!",Toast.LENGTH_SHORT).show();
                }




                //Intent in = new Intent(getApplicationContext(),OperatorTask.class);
                //in.putExtra(TAG_PERUSAHAAN,perusahaan);
                //in.putExtra(TAG_KODENOTA,kodenota);
                //in.putExtra(TAG_NOTE,note);
                //startActivity(in);

            }
        });


    }
}