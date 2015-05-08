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

public class OperatorTaskMBPB_2 extends ListActivity {

    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";

    // url to make request
    private String url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb2.php?operatorcode=1";

    // JSON Node names
    private final String TAG_OPERATORTASKBPB2= "operatortaskmoverbpb2";
    private final String TAG_NOURUT = "NoUrut";
    private final String TAG_QUEUENUMBER = "QueueNumber";
    private final String TAG_ERPCODE = "ERPCode";
    private final String TAG_TRANSACTIONCODE = "TransactionCode";
    private final String TAG_BINCODE = "BinCode";
    private final String TAG_SKUCODE = "SKUCode";
    private final String TAG_KETERANGAN = "Keterangan";
    private final String TAG_QTY = "Qty";
    private final String TAG_QTYKONVERSI = "QtyKonversi";
    private final String TAG_RATIO = "Ratio";
    private final String TAG_RATIONAME = "RatioName";
    private final String TAG_DESTRACKSLOT= "DestRackSlot";
    private final String TAG_CURRRACKSLOT = "CurrRackSlot";

    String nourut ,queuenumber ,erpcode ,transcationcode ,bincode,skucode,keterangan,qty,qtykonversi,ratio,rationame,destrackslot,currrackslot;

    // Cabang JSONArray
    JSONArray operatortaskmoverbpb2 = null;

    //Form
    EditText InputBIN;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskmoverbpb2);

        final Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);

        // Hashmap for ListView
        final ArrayList<HashMap<String, String>> OperatorTaskMBPB2List = new ArrayList<HashMap<String, String>>();

        // Creating JSON Parser instance
        final JSONParser jParser = new JSONParser();

        url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb2.php?operatorcode="+OperatorCode+"&operatorrole=10/WHR/002&projectcode=BPB&transactioncode="+in.getStringExtra(TAG_TRANSACTIONCODE);

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);


        try {
            // Getting Array of Barang
            operatortaskmoverbpb2 = json.getJSONArray(TAG_OPERATORTASKBPB2);

            // looping through All Barang
            for(int i = 0; i < operatortaskmoverbpb2.length(); i++){
                JSONObject c = operatortaskmoverbpb2.getJSONObject(i);

                // Storing each json item in variable
                nourut = c.getString(TAG_NOURUT);
                queuenumber = c.getString(TAG_QUEUENUMBER);
                erpcode = c.getString(TAG_ERPCODE);
                transcationcode = c.getString(TAG_TRANSACTIONCODE);
                bincode = c.getString(TAG_BINCODE);
                skucode = c.getString(TAG_SKUCODE);
                keterangan = c.getString(TAG_KETERANGAN);
                qty = c.getString(TAG_QTY);
                qtykonversi = c.getString(TAG_QTYKONVERSI);
                ratio = c.getString(TAG_RATIO);
                rationame = c.getString(TAG_RATIONAME);
                destrackslot = c.getString(TAG_DESTRACKSLOT);
                currrackslot = c.getString(TAG_CURRRACKSLOT);
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_NOURUT, nourut);
                map.put(TAG_QUEUENUMBER,  queuenumber);
                map.put(TAG_ERPCODE, erpcode);
                map.put(TAG_TRANSACTIONCODE, transcationcode);
                map.put(TAG_BINCODE, bincode);
                map.put(TAG_SKUCODE, skucode);
                map.put(TAG_KETERANGAN, keterangan);
                map.put(TAG_QTY, qty);
                map.put(TAG_QTYKONVERSI, qtykonversi);
                map.put(TAG_RATIO, ratio);
                map.put(TAG_RATIONAME, rationame);
                map.put(TAG_DESTRACKSLOT, destrackslot);
                map.put(TAG_CURRRACKSLOT, currrackslot);

                OperatorTaskMBPB2List.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(this, OperatorTaskMBPB2List,
                R.layout.listoperatortaskmoverbpb2,
                new String[] { TAG_ERPCODE, TAG_BINCODE, TAG_KETERANGAN, TAG_CURRRACKSLOT, TAG_QTYKONVERSI},
                new int[] { R.id.OPTMBPB2_kodenota,R.id.OPTMBPB2_BINCode, R.id.OPTMBPB2_NamaBrg, R.id.OPTMBPB2_CurrRack, R.id.OPTMBPB2_Jumlah });

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String kodenota = ((TextView) view.findViewById(R.id.OPTMBPB2_kodenota)).getText().toString();

                Toast.makeText(getApplicationContext(),kodenota,Toast.LENGTH_SHORT).show();

                //Intent in = new Intent(getApplicationContext(),OperatorTask.class);
                //in.putExtra(TAG_PERUSAHAAN,perusahaan);
                //in.putExtra(TAG_KODENOTA,kodenota);
                //in.putExtra(TAG_NOTE,note);
                //startActivity(in);

            }
        });

        InputBIN = (EditText) findViewById(R.id.OPTMBPB2_InputBin);
        InputBIN.requestFocus();

        InputBIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(InputBIN.getText().toString().length()==7) {
                    url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb2caribin.php?bincode="+InputBIN.getText().toString()+"&operatorcode="+OperatorCode+"&operatorrole=10/WHR/002&projectcode=BPB&transactioncode="+in.getStringExtra(TAG_TRANSACTIONCODE);

                    // getting JSON string from URL
                    JSONObject json = jParser.getJSONFromUrl(url);


                    try {
                        // Getting Array of Barang
                        operatortaskmoverbpb2 = json.getJSONArray(TAG_OPERATORTASKBPB2);

                        // looping through All Barang
                        for(int i = 0; i < operatortaskmoverbpb2.length(); i++){
                            JSONObject c = operatortaskmoverbpb2.getJSONObject(i);

                            // Storing each json item in variable
                            nourut = c.getString(TAG_NOURUT);
                            queuenumber = c.getString(TAG_QUEUENUMBER);
                            erpcode = c.getString(TAG_ERPCODE);
                            transcationcode = c.getString(TAG_TRANSACTIONCODE);
                            bincode = c.getString(TAG_BINCODE);
                            skucode = c.getString(TAG_SKUCODE);
                            keterangan = c.getString(TAG_KETERANGAN);
                            qty = c.getString(TAG_QTY);
                            qtykonversi = c.getString(TAG_QTYKONVERSI);
                            ratio = c.getString(TAG_RATIO);
                            rationame = c.getString(TAG_RATIONAME);
                            destrackslot = c.getString(TAG_DESTRACKSLOT);
                            currrackslot = c.getString(TAG_CURRRACKSLOT);

                            if (keterangan.equals("0")){
                                Toast.makeText(getApplicationContext(),"Data Tidak Ada",Toast.LENGTH_SHORT).show();
                            }else{
                                Intent in = new Intent(getApplicationContext(),OperatorTaskMBPB_3.class);
                                in.putExtra(TAG_NOURUT,nourut);
                                in.putExtra(TAG_QUEUENUMBER,queuenumber);
                                in.putExtra(TAG_ERPCODE,erpcode);
                                in.putExtra(TAG_TRANSACTIONCODE,transcationcode);
                                in.putExtra(TAG_BINCODE,bincode);
                                in.putExtra(TAG_SKUCODE, skucode);
                                in.putExtra(TAG_KETERANGAN, keterangan);
                                in.putExtra(TAG_QTY, qty);
                                in.putExtra(TAG_QTYKONVERSI, qtykonversi);
                                in.putExtra(TAG_RATIO, ratio);
                                in.putExtra(TAG_RATIONAME, rationame);
                                in.putExtra(TAG_DESTRACKSLOT, destrackslot);
                                in.putExtra(TAG_CURRRACKSLOT, currrackslot);
                                in.putExtra(TAG_OPERATOR,OperatorCode);
                                startActivity(in);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}