package com.bcp.WMSAndroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class OperatorTaskPPCK_2 extends ListActivity {
    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";
    private String DestBin = "";
    private String WHCode = "";
    private String EDPanjang = "";
    private String TransactionDate = "";
    private String TransactionCode = "";
    private String ERPCode = "";

    // url to make request
    private String url = "http://192.168.31.10:9020/ws/operatortaskpickingpck2.php?transactioncode=1";


    // JSON Node names
    private final String TAG_OPERATORTASKPPCK2= "operatortaskpickingpck2";
    private final String TAG_TRANSACTIONCODE = "TransactionCode";
    private final String TAG_ERPCODE = "ERPCode";
    private final String TAG_SKUCODE = "SKUCode";
    private final String TAG_NOURUT = "NoUrut";
    private final String TAG_KETERANGAN = "Keterangan";
    private final String TAG_QTY = "Qty";
    private final String TAG_SATUAN = "Satuan";
    private final String TAG_NEEDED = "Needed";
    private final String TAG_PICKED = "Picked";
    private final String TAG_KONVESI = "Konversi";
    private final String TAG_ADDTASK = "AddTask";
    private final String TAG_STATADDTASK = "NormalTask";
    private final String TAG_DESTBIN = "DestBin";

    private final String TAG_TRANSACTIONDATE = "TransactionDate";
    private final String TAG_WHCODE = "WHCode";
    private final String TAG_EDPANJANG = "EDPanjang";
    private final String TAG_ASSIGNED = "Assigned";

    private final String TAG_RACKSLOTNAME = "RackSlotName";
    private final String TAG_RACKSLOTLVL = "RackSlotLvl";
    private final String TAG_EXPDATE = "ExpDate";
    private final String TAG_QTYTEXT = "QtyText";
    private final String TAG_PRIORITY = "Priority";

    // Cabang JSONArray
    JSONArray operatortaskppck2 = null;

    TextView TxtTransactionCode;
    TextView TxtERPCode;
    TextView TxtKodeBin;

    Button BtnTaruhBin,BtnRefresh;

    JSONArray suggestionRak=null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> SuggestionPPCK = new ArrayList<HashMap<String, String>>();

    public Comparator<Map<String, String>> mapComparator = new Comparator<Map<String, String>>() {
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            return m1.get(TAG_KETERANGAN).compareTo(m2.get(TAG_KETERANGAN));
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskpickingpck2);

        Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);
        WHCode = in.getStringExtra(TAG_WHCODE);
        EDPanjang = in.getStringExtra(TAG_EDPANJANG);
        TransactionDate = in.getStringExtra(TAG_TRANSACTIONDATE);
        TransactionCode = in.getStringExtra(TAG_TRANSACTIONCODE);
        ERPCode = in.getStringExtra(TAG_ERPCODE);


        TxtTransactionCode = (TextView) findViewById(R.id.OPTPPCK2_TransactionCode);
        TxtERPCode = (TextView) findViewById(R.id.OPTPPCK2_PickList);

        TxtTransactionCode.setText(in.getStringExtra(TAG_TRANSACTIONCODE));
        TxtERPCode.setText(in.getStringExtra(TAG_ERPCODE));

        BtnTaruhBin = (Button) findViewById(R.id.OPTPPCK2_BtnListBinKu);
        BtnTaruhBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),OperatorTaskPPCK_3.class);
                in.putExtra(TAG_OPERATOR,OperatorCode);
                startActivity(in);
            }
        });

        TxtKodeBin = (TextView) findViewById(R.id.OPTPPCK2_BinCode);
        DestBin = in.getStringExtra(TAG_DESTBIN);
        if (DestBin!=null){
            TxtKodeBin.setText(DestBin);
        }

        BtnRefresh = (Button) findViewById(R.id.OPTPPCK2_BtnRefresh);
        BtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),OperatorTaskPPCK_2.class);
                in.putExtra(TAG_ERPCODE,ERPCode);
                in.putExtra(TAG_TRANSACTIONDATE, TransactionDate);
                in.putExtra(TAG_EDPANJANG,EDPanjang);
                in.putExtra(TAG_WHCODE,WHCode);
                in.putExtra(TAG_TRANSACTIONCODE,TransactionCode);
                in.putExtra(TAG_DESTBIN,DestBin);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> OperatorTaskPPCK = new ArrayList<HashMap<String, String>>();

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        url = "http://192.168.31.10:9020/ws/operatortaskpickingpck2.php?transactioncode="+in.getStringExtra(TAG_TRANSACTIONCODE);

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        try {
            // Getting Array of Barang
            operatortaskppck2 = json.getJSONArray(TAG_OPERATORTASKPPCK2);

            // looping through All Barang
            for(int i = 0; i < operatortaskppck2.length(); i++){
                JSONObject c = operatortaskppck2.getJSONObject(i);

                // Storing each json item in variable
                String erpcode = c.getString(TAG_ERPCODE);
                String transactioncode = c.getString(TAG_TRANSACTIONCODE);
                String skucode = c.getString(TAG_SKUCODE);
                String keterangan = c.getString(TAG_KETERANGAN);
                String needed = c.getString(TAG_NEEDED);
                String picked = c.getString(TAG_PICKED);
                String satuan = c.getString(TAG_SATUAN);
                String qty = c.getString(TAG_QTY);
                String konversi = c.getString(TAG_KONVESI);
                String addtask = c.getString(TAG_ADDTASK);
                String nourut = c.getString(TAG_NOURUT);
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_ERPCODE, erpcode);
                map.put(TAG_TRANSACTIONCODE, transactioncode);
                map.put(TAG_SKUCODE, skucode);
                map.put(TAG_KETERANGAN, keterangan);
                map.put(TAG_NEEDED, needed);
                map.put(TAG_PICKED, picked);
                map.put(TAG_SATUAN, satuan);
                map.put(TAG_QTY, qty);
                map.put(TAG_KONVESI, konversi);
                map.put(TAG_ADDTASK, addtask);
                map.put(TAG_NOURUT, nourut);

                if(addtask.equals("0")){
                    map.put(TAG_STATADDTASK,"Normal Task");
                }else{
                    map.put(TAG_STATADDTASK,"Task Tambahan");
                }

                OperatorTaskPPCK.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(OperatorTaskPPCK,mapComparator);

        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(this, OperatorTaskPPCK,
                R.layout.listoperatortaskpickingpck2,
                new String[] { TAG_TRANSACTIONCODE, TAG_ERPCODE, TAG_SKUCODE, TAG_NOURUT, TAG_KETERANGAN, TAG_QTY, TAG_SATUAN, TAG_NEEDED, TAG_PICKED, TAG_KONVESI, TAG_ADDTASK, TAG_STATADDTASK},
                new int[] { R.id.OPTPPCK2_TransactionCode,R.id.OPTPPCK2_ERPCode,R.id.OPTPPCK2_SKUCode,R.id.OPTPPCK2_NoUrut,R.id.OPTPPCK2_Brg,R.id.OPTPPCK2_Qty, R.id.OPTPPCK2_Satuan,R.id.OPTPPCK2_Needed, R.id.OPTPPCK2_Picked,R.id.OPTPPCK2_Konversi, R.id.OPTPPCK2_AddTask, R.id.OPTPPCK2_StatAddTask});

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String namabrg = ((TextView) view.findViewById(R.id.OPTPPCK2_Brg)).getText().toString();
                String sku = ((TextView) view.findViewById(R.id.OPTPPCK2_SKUCode)).getText().toString();
                String qty = ((TextView) view.findViewById(R.id.OPTPPCK2_Qty)).getText().toString();
                String qtykonversi = ((TextView) view.findViewById(R.id.OPTPPCK2_Konversi)).getText().toString();
                String erpcode = ((TextView) view.findViewById(R.id.OPTPPCK2_ERPCode)).getText().toString();
                String nourut = ((TextView) view.findViewById(R.id.OPTPPCK2_ERPCode)).getText().toString();
                String satuan = ((TextView) view.findViewById(R.id.OPTPPCK2_ERPCode)).getText().toString();
                String needed = ((TextView) view.findViewById(R.id.OPTPPCK2_ERPCode)).getText().toString();
                String picked = ((TextView) view.findViewById(R.id.OPTPPCK2_ERPCode)).getText().toString();
                String addtask = ((TextView) view.findViewById(R.id.OPTPPCK2_ERPCode)).getText().toString();


                // Get data from web service Rak Suggestion

                // Creating JSON Parser instance
                JSONParser jParser = new JSONParser();

                SuggestionPPCK.clear();

                url = "http://192.168.31.10:9020/ws/operatortaskpickingpck4suggestion.php?skucode="+sku+"&qty="+qty+"&whcode="+WHCode+"&edpanjang="+EDPanjang;

                // getting JSON string from URL
                JSONObject json = jParser.getJSONFromUrl(url);

                try {
                    // Getting Array of Barang
                    suggestionRak = json.getJSONArray("operatortaskpickingpck4suggestion");

                    // looping through All Barang
                    for(int i = 0; i < suggestionRak.length(); i++){
                        JSONObject c = suggestionRak.getJSONObject(i);

                        // Storing each json item in variable
                        String rackslotname = c.getString(TAG_RACKSLOTNAME);
                        String rackslotlvl = c.getString(TAG_RACKSLOTLVL);
                        String expdate = c.getString(TAG_EXPDATE);
                        String qtys = c.getString(TAG_QTY);
                        String qtytext = c.getString(TAG_QTYTEXT);
                        String priority = c.getString(TAG_PRIORITY);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_RACKSLOTNAME, rackslotname);
                        map.put(TAG_RACKSLOTLVL, rackslotlvl);
                        map.put(TAG_EXPDATE, expdate.substring(0,10));
                        map.put(TAG_QTY, qtys);
                        map.put(TAG_QTYTEXT, qtytext);
                        map.put(TAG_PRIORITY, priority);

                        SuggestionPPCK.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showSuggestionRak(sku, namabrg, qty, qtykonversi, SuggestionPPCK,erpcode, nourut, satuan,needed,picked,addtask);
            }
        });
    }

    private void showSuggestionRak(final String SKU,final String Nama,final String Qty,final String QtyKonversi, ArrayList<HashMap<String, String>> Suggestion, final String ERPCode,final String NoUrut,final String Satuan, final String Needed, final String Picked, final String AddTask){
        final Dialog dialog = new Dialog(OperatorTaskPPCK_2.this, android.R.style.Theme_Dialog);
        dialog.setTitle("SKU : "+SKU);
        dialog.setContentView(R.layout.dialogpicking2);
        Context dContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) dContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialogpicking2, null);
        final TextView txtNama=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Nama);
        txtNama.setText(Nama);
        final TextView txtQty=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Qty);
        txtQty.setText(QtyKonversi);

        final TextView txtRak_1=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Rak_1);
        final TextView txtRakLvl_1=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_RakLvl_1);
        final TextView txtExpDate_1=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_ExpDate_1);
        final TextView txtJumlah_1=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Jumlah_1);

        final TextView txtRak_2=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Rak_2);
        final TextView txtRakLvl_2=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_RakLvl_2);
        final TextView txtExpDate_2=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_ExpDate_2);
        final TextView txtJumlah_2=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Jumlah_2);

        final TextView txtRak_3=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Rak_3);
        final TextView txtRakLvl_3=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_RakLvl_3);
        final TextView txtExpDate_3=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_ExpDate_3);
        final TextView txtJumlah_3=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Jumlah_3);

        final TextView txtRak_4=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Rak_4);
        final TextView txtRakLvl_4=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_RakLvl_4);
        final TextView txtExpDate_4=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_ExpDate_4);
        final TextView txtJumlah_4=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Jumlah_4);

        final TextView txtRak_5=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Rak_5);
        final TextView txtRakLvl_5=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_RakLvl_5);
        final TextView txtExpDate_5=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_ExpDate_5);
        final TextView txtJumlah_5=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Jumlah_5);

        final TextView txtRak_6=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Rak_6);
        final TextView txtRakLvl_6=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_RakLvl_6);
        final TextView txtExpDate_6=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_ExpDate_6);
        final TextView txtJumlah_6=(TextView) dialog.findViewById(R.id.OPTPPCKDialog_Jumlah_6);

        final Button BtnBatal = (Button) dialog.findViewById(R.id.OPTPPCKDialog_BtnBatal);
        BtnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final Button BtnSubmit = (Button) dialog.findViewById(R.id.OPTPPCKDialog_BtnSubmit);
        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),OperatorTaskPPCK_2AmbilBrg.class);
                in.putExtra(TAG_DESTBIN,DestBin);
                in.putExtra(TAG_KETERANGAN,Nama);
                in.putExtra(TAG_QTYTEXT,QtyKonversi);
                in.putExtra(TAG_QTY,Qty);
                in.putExtra(TAG_SKUCODE,SKU);
                in.putExtra(TAG_ERPCODE,ERPCode);
                in.putExtra(TAG_NOURUT,NoUrut);
                in.putExtra(TAG_SATUAN,Satuan);
                in.putExtra(TAG_NEEDED,Needed);
                in.putExtra(TAG_PICKED,Picked);
                in.putExtra(TAG_ADDTASK,AddTask);

                in.putExtra(TAG_TRANSACTIONDATE, TransactionDate);
                in.putExtra(TAG_EDPANJANG,EDPanjang);
                in.putExtra(TAG_WHCODE,WHCode);
                in.putExtra(TAG_TRANSACTIONCODE,TransactionCode);
                startActivity(in);
            }
        });

        //Set it gone
        txtRak_1.setVisibility(View.GONE);
        txtRakLvl_1.setVisibility(View.GONE);
        txtExpDate_1.setVisibility(View.GONE);
        txtJumlah_1.setVisibility(View.GONE);

        txtRak_2.setVisibility(View.GONE);
        txtRakLvl_2.setVisibility(View.GONE);
        txtExpDate_2.setVisibility(View.GONE);
        txtJumlah_2.setVisibility(View.GONE);

        txtRak_3.setVisibility(View.GONE);
        txtRakLvl_3.setVisibility(View.GONE);
        txtExpDate_3.setVisibility(View.GONE);
        txtJumlah_3.setVisibility(View.GONE);

        txtRak_4.setVisibility(View.GONE);
        txtRakLvl_4.setVisibility(View.GONE);
        txtExpDate_4.setVisibility(View.GONE);
        txtJumlah_4.setVisibility(View.GONE);

        txtRak_5.setVisibility(View.GONE);
        txtRakLvl_5.setVisibility(View.GONE);
        txtExpDate_5.setVisibility(View.GONE);
        txtJumlah_5.setVisibility(View.GONE);

        txtRak_6.setVisibility(View.GONE);
        txtRakLvl_6.setVisibility(View.GONE);
        txtExpDate_6.setVisibility(View.GONE);
        txtJumlah_6.setVisibility(View.GONE);

        LinearLayout R1 = (LinearLayout) dialog.findViewById(R.id.OPTPPCK4_R1);
        LinearLayout R2 = (LinearLayout) dialog.findViewById(R.id.OPTPPCK4_R2);
        LinearLayout R3 = (LinearLayout) dialog.findViewById(R.id.OPTPPCK4_R3);
        LinearLayout R4 = (LinearLayout) dialog.findViewById(R.id.OPTPPCK4_R4);
        LinearLayout R5 = (LinearLayout) dialog.findViewById(R.id.OPTPPCK4_R5);
        LinearLayout R6 = (LinearLayout) dialog.findViewById(R.id.OPTPPCK4_R6);
        LinearLayout StatusStok = (LinearLayout) dialog.findViewById(R.id.OPTPPCKDialog_Status);


        R1.setVisibility(View.GONE);
        R2.setVisibility(View.GONE);
        R3.setVisibility(View.GONE);
        R4.setVisibility(View.GONE);
        R5.setVisibility(View.GONE);
        R6.setVisibility(View.GONE);
        StatusStok.setVisibility(View.GONE);

        if (Suggestion.size()<=0){
            StatusStok.setVisibility(View.VISIBLE);
            BtnSubmit.setVisibility(View.GONE);
        }

        //Processing HashMap
        for (int i = 0; i < Suggestion.size(); i++) {
            BtnSubmit.setVisibility(View.VISIBLE);
            switch (i){
                case 0:{
                    R1.setVisibility(View.VISIBLE);

                    txtRak_1.setVisibility(View.VISIBLE);
                    txtRakLvl_1.setVisibility(View.VISIBLE);
                    txtExpDate_1.setVisibility(View.VISIBLE);
                    txtJumlah_1.setVisibility(View.VISIBLE);
                    
                    txtRak_1.setText(Suggestion.get(i).get(TAG_RACKSLOTNAME));
                    txtRakLvl_1.setText(Suggestion.get(i).get(TAG_RACKSLOTLVL));
                    txtExpDate_1.setText(Suggestion.get(i).get(TAG_EXPDATE));
                    txtJumlah_1.setText(Suggestion.get(i).get(TAG_QTYTEXT));
                    break;
                }
                case 1:{
                    R2.setVisibility(View.VISIBLE);

                    txtRak_2.setVisibility(View.VISIBLE);
                    txtRakLvl_2.setVisibility(View.VISIBLE);
                    txtExpDate_2.setVisibility(View.VISIBLE);
                    txtJumlah_2.setVisibility(View.VISIBLE);
                    
                    txtRak_2.setText(Suggestion.get(i).get(TAG_RACKSLOTNAME));
                    txtRakLvl_2.setText(Suggestion.get(i).get(TAG_RACKSLOTLVL));
                    txtExpDate_2.setText(Suggestion.get(i).get(TAG_EXPDATE));
                    txtJumlah_2.setText(Suggestion.get(i).get(TAG_QTYTEXT));
                    break;
                }
                case 2:{
                    R3.setVisibility(View.VISIBLE);

                    txtRak_3.setVisibility(View.VISIBLE);
                    txtRakLvl_3.setVisibility(View.VISIBLE);
                    txtExpDate_3.setVisibility(View.VISIBLE);
                    txtJumlah_3.setVisibility(View.VISIBLE);
                    
                    txtRak_3.setText(Suggestion.get(i).get(TAG_RACKSLOTNAME));
                    txtRakLvl_3.setText(Suggestion.get(i).get(TAG_RACKSLOTLVL));
                    txtExpDate_3.setText(Suggestion.get(i).get(TAG_EXPDATE));
                    txtJumlah_3.setText(Suggestion.get(i).get(TAG_QTYTEXT));
                    break;
                }
                case 3:{
                    R4.setVisibility(View.VISIBLE);
                    txtRak_4.setVisibility(View.VISIBLE);
                    txtRakLvl_4.setVisibility(View.VISIBLE);
                    txtExpDate_4.setVisibility(View.VISIBLE);
                    txtJumlah_4.setVisibility(View.VISIBLE);
                    
                    txtRak_4.setText(Suggestion.get(i).get(TAG_RACKSLOTNAME));
                    txtRakLvl_4.setText(Suggestion.get(i).get(TAG_RACKSLOTLVL));
                    txtExpDate_4.setText(Suggestion.get(i).get(TAG_EXPDATE));
                    txtJumlah_4.setText(Suggestion.get(i).get(TAG_QTYTEXT));
                    break;
                }
                case 4:{
                    R5.setVisibility(View.VISIBLE);
                    txtRak_5.setVisibility(View.VISIBLE);
                    txtRakLvl_5.setVisibility(View.VISIBLE);
                    txtExpDate_5.setVisibility(View.VISIBLE);
                    txtJumlah_5.setVisibility(View.VISIBLE);
                    
                    txtRak_5.setText(Suggestion.get(i).get(TAG_RACKSLOTNAME));
                    txtRakLvl_5.setText(Suggestion.get(i).get(TAG_RACKSLOTLVL));
                    txtExpDate_5.setText(Suggestion.get(i).get(TAG_EXPDATE));
                    txtJumlah_5.setText(Suggestion.get(i).get(TAG_QTYTEXT));
                    break;
                }
                case 5:{
                    R6.setVisibility(View.VISIBLE);
                    txtRak_6.setVisibility(View.VISIBLE);
                    txtRakLvl_6.setVisibility(View.VISIBLE);
                    txtExpDate_6.setVisibility(View.VISIBLE);
                    txtJumlah_6.setVisibility(View.VISIBLE);
                    
                    txtRak_6.setText(Suggestion.get(i).get(TAG_RACKSLOTNAME));
                    txtRakLvl_6.setText(Suggestion.get(i).get(TAG_RACKSLOTLVL));
                    txtExpDate_6.setText(Suggestion.get(i).get(TAG_EXPDATE));
                    txtJumlah_6.setText(Suggestion.get(i).get(TAG_QTYTEXT));
                    break;
                }
                default:break;
            };
            //SuggestionPPCK.get(i).get(TAG_QTYTEXT);
            //SuggestionPPCK.get(i).get(TAG_PRIORITY);
        }
        dialog.show();
    }

}