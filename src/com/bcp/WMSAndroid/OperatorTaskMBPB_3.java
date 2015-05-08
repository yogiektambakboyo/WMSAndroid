package com.bcp.WMSAndroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OperatorTaskMBPB_3 extends Activity {
    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";
    private String TransactionCode = "";
    private String NoUrut = "";
    private String QueueNumber = "";
    private String SKUCode = "";

    // url to make request
    private static String url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb2prosesambil.php?operatorcode=1";

    // JSON Node names
    private final String TAG_CARISATUAN = "operatortaskreceivebpb2getsatuan";
    private static final String TAG_OPERATORTASKBPB3= "operatortaskmoverbpb2prosesambil";
    private static final String TAG_NOURUT = "NoUrut";
    private static final String TAG_QUEUENUMBER = "QueueNumber";
    private static final String TAG_ERPCODE = "ERPCode";
    private static final String TAG_TRANSACTIONCODE = "TransactionCode";
    private static final String TAG_BINCODE = "BinCode";
    private static final String TAG_SKUCODE = "SKUCode";
    private static final String TAG_KETERANGAN = "Keterangan";
    private static final String TAG_QTY = "Qty";
    private static final String TAG_QTYKONVERSI = "QtyKonversi";
    private static final String TAG_RATIO = "Ratio";
    private static final String TAG_RATIONAME = "RatioName";
    private static final String TAG_DESTRACKSLOT= "DestRackSlot";
    private static final String TAG_CURRRACKSLOT = "CurrRackSlot";
    private static final String TAG_STATUS = "Status";
    private final String TAG_SATUAN = "Satuan";
    private final String TAG_RASIO = "Rasio";

    private String JumlahBrg="0";
    private String RasioBrg="0";
    private String RasioNameBrg="0";

    private String status = "0";

    // Cabang JSONArray
    JSONArray operatortaskmoverbpb2prosesambil = null;

    //Form
    TextView TxtBPB,TxtKodeBin, TxtBrg, TxtJumlah,TxtDestRack;
    Button   InputProsesAmbil,InputProses;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskmoverbpb3);

        Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);
        TransactionCode = in.getStringExtra(TAG_TRANSACTIONCODE);
        NoUrut = in.getStringExtra(TAG_NOURUT);
        QueueNumber = in.getStringExtra(TAG_QUEUENUMBER);
        SKUCode = in.getStringExtra(TAG_SKUCODE);
        JumlahBrg = in.getStringExtra(TAG_QTY);
        RasioBrg = in.getStringExtra(TAG_RATIO);
        RasioNameBrg = in.getStringExtra(TAG_RATIONAME);

        TxtBPB = (TextView) findViewById(R.id.OPTMBPB3_BPB);
        TxtBPB.setText(in.getStringExtra(TAG_ERPCODE));
        TxtKodeBin = (TextView) findViewById(R.id.OPTMBPB3_KodeBin);
        TxtKodeBin.setText(in.getStringExtra(TAG_BINCODE));
        TxtBrg = (TextView) findViewById(R.id.OPTMBPB3_NamaBrg);
        TxtBrg.setText(in.getStringExtra(TAG_KETERANGAN));
        TxtJumlah = (TextView) findViewById(R.id.OPTMBPB3_Jumlah);
        TxtJumlah.setText(in.getStringExtra(TAG_QTYKONVERSI));
        TxtDestRack = (TextView) findViewById(R.id.OPTMBPB3_DestRack);
        TxtDestRack.setText(in.getStringExtra(TAG_DESTRACKSLOT));

        // Creating JSON Parser instance
        final JSONParser jParser = new JSONParser();

        InputProsesAmbil = (Button) findViewById(R.id.OPTMBPB3_ProsesAmbil);
        InputProsesAmbil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb2prosesambil.php?queuenumber="+QueueNumber+"&operatorcode="+OperatorCode+"&nourut="+NoUrut+"&transactioncode="+TransactionCode;

                // getting JSON string from URL
                JSONObject json = jParser.getJSONFromUrl(url);


                try {
                    // Getting Array of Barang
                    operatortaskmoverbpb2prosesambil = json.getJSONArray(TAG_OPERATORTASKBPB3);

                    // looping through All Barang
                    for(int i = 0; i < operatortaskmoverbpb2prosesambil.length(); i++){
                        JSONObject c = operatortaskmoverbpb2prosesambil.getJSONObject(i);

                        // Storing each json item in variable
                        status = c.getString(TAG_STATUS);

                        if (status.equals("0")){
                            Toast.makeText(getApplicationContext(),"Proses Gagal",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Proses Berhasil",Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(getApplicationContext(),OperatorTaskMBPB_1.class);
                            in.putExtra(TAG_OPERATOR,OperatorCode);
                            startActivity(in);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        InputProses = (Button) findViewById(R.id.OPTMBPB3_Proses);
        InputProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb2prosesambil.php?queuenumber="+QueueNumber+"&operatorcode="+OperatorCode+"&nourut="+NoUrut+"&transactioncode="+TransactionCode;

                // getting JSON string from URL
                JSONObject json = jParser.getJSONFromUrl(url);


                try {
                    // Getting Array of Barang
                    operatortaskmoverbpb2prosesambil = json.getJSONArray(TAG_OPERATORTASKBPB3);

                    // looping through All Barang
                    for(int i = 0; i < operatortaskmoverbpb2prosesambil.length(); i++){
                        JSONObject c = operatortaskmoverbpb2prosesambil.getJSONObject(i);

                        // Storing each json item in variable
                        status = c.getString(TAG_STATUS);

                        if (status.equals("0")){
                            Toast.makeText(getApplicationContext(),"Proses Gagal",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Proses Berhasil",Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(getApplicationContext(),OperatorTaskMBPB_4.class);
                            in.putExtra(TAG_OPERATOR,OperatorCode);
                            in.putExtra(TAG_SKUCODE,SKUCode);
                            in.putExtra(TAG_TRANSACTIONCODE,TransactionCode);
                            in.putExtra(TAG_BINCODE,TxtKodeBin.getText());
                            in.putExtra(TAG_KETERANGAN,TxtBrg.getText());
                            in.putExtra(TAG_DESTRACKSLOT,TxtDestRack.getText());
                            in.putExtra(TAG_ERPCODE,TxtBPB.getText());
                            in.putExtra(TAG_QTYKONVERSI,TxtJumlah.getText());
                            in.putExtra(TAG_QTY,JumlahBrg);
                            in.putExtra(TAG_RATIO,RasioBrg);
                            in.putExtra(TAG_RATIONAME,RasioNameBrg);
                            startActivity(in);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}