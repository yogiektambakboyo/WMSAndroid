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

public class OperatorTaskPPCK_3 extends ListActivity {
    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";
    private String TransactionCode = "";
    private String DestBin = "";
    private String ERPCode = "";

    // url to make request
    private String url = "http://192.168.31.10:9020/ws/operatortaskpickingpck3.php?operatorcode=1";


    // JSON Node names
    private final String TAG_OPERATORTASKPPCK2= "operatortaskpickingpck2";
    private final String TAG_TRANSACTIONCODE = "TransactionCode";
    private final String TAG_ERPCODE = "ERPCode";
    private final String TAG_SKUCODE = "SKUCode";
    private final String TAG_NOURUT = "NoUrut";
    private final String TAG_KETERANGAN = "Keterangan";
    private final String TAG_QTYNEEDSTART = "QtyNeedStart";
    private final String TAG_QTY = "Qty";
    private final String TAG_SRCRACKSLOT = "SrcRackSlot";
    private final String TAG_SRCBIN = "SrcBin";
    private final String TAG_DESTBIN = "DestBin";
    private final String TAG_NAME = "Name";

    private final String TAG_TRANSACTIONDATE = "TransactionDate";
    private final String TAG_WHCODE = "WHCode";
    private final String TAG_EDPANJANG = "EDPanjang";
    private final String TAG_ASSIGNED = "Assigned";

    // Cabang JSONArray
    JSONArray operatortaskppck3 = null;

    //Form
    TextView TxtNoPickList,TxtKodeBin;
    Button   BtnTaruhBin;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskpickingpck3);

        Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> OperatorTaskPPCK = new ArrayList<HashMap<String, String>>();

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        url = "http://192.168.31.10:9020/ws/operatortaskpickingpck3.php?operatorcode="+OperatorCode;

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        try {
            // Getting Array of Barang
            operatortaskppck3 = json.getJSONArray(TAG_OPERATORTASKPPCK2);

            // looping through All Barang
            for(int i = 0; i < operatortaskppck3.length(); i++){
                JSONObject c = operatortaskppck3.getJSONObject(i);

                //    "TransactionCode"=>"0","ERPCode"=>"Koneksi Server Terputus","SKUCode"=>"0","NoUrut"=>"0","Keterangan"=>"0","QtyNeedStart"=>"0","Qty"=>"0","SrcRackSlot"=>"0","SrcBin"=>"0","DestBin"=>"0","Name"=>"0")
                // Storing each json item in variable
                String erpcode = c.getString(TAG_ERPCODE);
                String transactioncode = c.getString(TAG_TRANSACTIONCODE);
                String skucode = c.getString(TAG_SKUCODE);
                String keterangan = c.getString(TAG_KETERANGAN);
                String qtyneedstart = c.getString(TAG_QTYNEEDSTART);
                String srcrackslot = c.getString(TAG_SRCRACKSLOT);
                String srcbin = c.getString(TAG_SRCBIN);
                String qty = c.getString(TAG_QTY);
                String destbin = c.getString(TAG_DESTBIN);
                String name = c.getString(TAG_NAME);
                String nourut = c.getString(TAG_NOURUT);

                ERPCode = c.getString(TAG_ERPCODE);
                DestBin = c.getString(TAG_DESTBIN);
                TransactionCode = c.getString(TAG_TRANSACTIONCODE);

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_ERPCODE, erpcode);
                map.put(TAG_TRANSACTIONCODE, transactioncode);
                map.put(TAG_SKUCODE, skucode);
                map.put(TAG_KETERANGAN, keterangan);
                map.put(TAG_QTYNEEDSTART, qtyneedstart);
                map.put(TAG_SRCRACKSLOT, srcrackslot);
                map.put(TAG_SRCBIN, srcbin);
                map.put(TAG_QTY, qty);
                map.put(TAG_DESTBIN, destbin);
                map.put(TAG_NAME, name);
                map.put(TAG_NOURUT, nourut);

                OperatorTaskPPCK.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TxtNoPickList = (TextView) findViewById(R.id.OPTPPCK3_PickList);
        TxtNoPickList.setText(ERPCode);
        TxtKodeBin = (TextView) findViewById(R.id.OPTPPCK3_BinCode);
        TxtKodeBin.setText(DestBin);

        BtnTaruhBin = (Button) findViewById(R.id.OPTPPCK3_BtnTaruhBin);

        if(TransactionCode.equals("0")){
            BtnTaruhBin.setVisibility(View.GONE);
        }

        BtnTaruhBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),OperatorTaskPPCK_4.class);
                in.putExtra(TAG_DESTBIN,DestBin);
                in.putExtra(TAG_OPERATOR,OperatorCode);
                in.putExtra(TAG_TRANSACTIONCODE,TransactionCode);
                startActivity(in);
            }
        });

        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(this, OperatorTaskPPCK,
                R.layout.listoperatortaskpickingpck3,
                new String[] { TAG_TRANSACTIONCODE, TAG_ERPCODE, TAG_SKUCODE, TAG_NOURUT, TAG_KETERANGAN, TAG_QTY, TAG_QTYNEEDSTART, TAG_SRCRACKSLOT, TAG_SRCBIN, TAG_DESTBIN, TAG_NAME, TAG_NOURUT},
                new int[] { R.id.OPTPPCK3_TransactionCode,R.id.OPTPPCK3_ERPCode,R.id.OPTPPCK3_SKUCode,R.id.OPTPPCK3_NoUrut,R.id.OPTPPCK3_Brg,R.id.OPTPPCK3_Qty, R.id.OPTPPCK3_QtyNeedStart,R.id.OPTPPCK3_SrcRackSlot, R.id.OPTPPCK3_SrcBin,R.id.OPTPPCK3_DestBin, R.id.OPTPPCK3_Name, R.id.OPTPPCK3_NoUrut});

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String namabrg = ((TextView) view.findViewById(R.id.OPTPPCK3_Brg)).getText().toString();

                Toast.makeText(getApplicationContext(),namabrg,Toast.LENGTH_SHORT).show();

                //Intent in = new Intent(getApplicationContext(),OperatorTask.class);
                //in.putExtra(TAG_PERUSAHAAN,perusahaan);
                //in.putExtra(TAG_KODENOTA,kodenota);
                //in.putExtra(TAG_NOTE,note);
                //startActivity(in);
            }
        });
    }


}