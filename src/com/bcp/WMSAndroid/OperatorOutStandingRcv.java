package com.bcp.WMSAndroid;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OperatorOutStandingRcv extends ListActivity {
    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";

    // url to make request
    private String url = "http://192.168.31.10:9020/ws/operatoroutstandingrcv.php?operatorcode=1";

    // JSON Node names
    private final String TAG_OPERATOROUTSTANDINGRCV= "operatoroutstandingreceive";
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
    JSONArray operatoroutstandingrcv = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatoroutstandingreceive);

        final Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);

        // Hashmap for ListView
        final ArrayList<HashMap<String, String>> OperatorTaskMBPB2List = new ArrayList<HashMap<String, String>>();

        // Creating JSON Parser instance
        final JSONParser jParser = new JSONParser();

        url = "http://192.168.31.10:9020/ws/operatoroutstandingreceive.php?operatorcode="+OperatorCode+"&operatorrole=10/WHR/002&projectcode=BPB";

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);


        try {
            // Getting Array of Barang
            operatoroutstandingrcv = json.getJSONArray(TAG_OPERATOROUTSTANDINGRCV);

            // looping through All Barang
            for(int i = 0; i < operatoroutstandingrcv.length(); i++){
                JSONObject c = operatoroutstandingrcv.getJSONObject(i);

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
                new String[] { TAG_ERPCODE, TAG_BINCODE, TAG_KETERANGAN, TAG_CURRRACKSLOT, TAG_QTYKONVERSI, TAG_NOURUT, TAG_QUEUENUMBER, TAG_TRANSACTIONCODE, TAG_SKUCODE, TAG_QTY, TAG_RATIO, TAG_RATIONAME, TAG_DESTRACKSLOT},
                new int[] { R.id.OPTMBPB2_kodenota,R.id.OPTMBPB2_BINCode, R.id.OPTMBPB2_NamaBrg, R.id.OPTMBPB2_CurrRack, R.id.OPTMBPB2_Jumlah, R.id.OPTMBPB2_NoUrut, R.id.OPTMBPB2_QueueNumber, R.id.OPTMBPB2_TransactionCode, R.id.OPTMBPB2_SKUCode, R.id.OPTMBPB2_Qty, R.id.OPTMBPB2_Ratio, R.id.OPTMBPB2_RatioName, R.id.OPTMBPB2_DestRackSlot});

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
                String bincode = ((TextView) view.findViewById(R.id.OPTMBPB2_BINCode)).getText().toString();
                String namabrg = ((TextView) view.findViewById(R.id.OPTMBPB2_NamaBrg)).getText().toString();
                String currrack = ((TextView) view.findViewById(R.id.OPTMBPB2_CurrRack)).getText().toString();
                String jumlah = ((TextView) view.findViewById(R.id.OPTMBPB2_Jumlah)).getText().toString();
                String nourut = ((TextView) view.findViewById(R.id.OPTMBPB2_NoUrut)).getText().toString();
                String queuenumber = ((TextView) view.findViewById(R.id.OPTMBPB2_QueueNumber)).getText().toString();
                String transactioncode = ((TextView) view.findViewById(R.id.OPTMBPB2_TransactionCode)).getText().toString();
                String skucode = ((TextView) view.findViewById(R.id.OPTMBPB2_SKUCode)).getText().toString();
                String qty = ((TextView) view.findViewById(R.id.OPTMBPB2_Qty)).getText().toString();
                String ratio = ((TextView) view.findViewById(R.id.OPTMBPB2_Ratio)).getText().toString();
                String rationame = ((TextView) view.findViewById(R.id.OPTMBPB2_RatioName)).getText().toString();
                String destrackslot = ((TextView) view.findViewById(R.id.OPTMBPB2_DestRackSlot)).getText().toString();

                Toast.makeText(getApplicationContext(),kodenota,Toast.LENGTH_SHORT).show();

                Intent in = new Intent(getApplicationContext(),OperatorTaskMBPB_4.class);
                in.putExtra(TAG_NOURUT,nourut);
                in.putExtra(TAG_QUEUENUMBER,queuenumber);
                in.putExtra(TAG_ERPCODE,kodenota);
                in.putExtra(TAG_TRANSACTIONCODE,transactioncode);
                in.putExtra(TAG_BINCODE,bincode);
                in.putExtra(TAG_SKUCODE, skucode);
                in.putExtra(TAG_KETERANGAN, namabrg);
                in.putExtra(TAG_QTY, qty);
                in.putExtra(TAG_QTYKONVERSI, jumlah);
                in.putExtra(TAG_RATIO, ratio);
                in.putExtra(TAG_RATIONAME, rationame);
                in.putExtra(TAG_DESTRACKSLOT, destrackslot);
                in.putExtra(TAG_CURRRACKSLOT, currrack);
                in.putExtra(TAG_OPERATOR,OperatorCode);
                startActivity(in);

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(getApplicationContext(), OperatorMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return false;
    }
}