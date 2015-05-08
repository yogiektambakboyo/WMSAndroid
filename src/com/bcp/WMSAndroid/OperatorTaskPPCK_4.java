package com.bcp.WMSAndroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OperatorTaskPPCK_4 extends Activity {
    private static final String TAG_DESTBIN = "DestBin";
    private final String TAG_OPERATOR = "OperatorCode";
    private final String TAG_TRANSACTIONCODE = "TransactionCode";
    private String OperatorCode = "";

    TextView TxtBinCode,TxtRackName;
    EditText InputTempBin,InputDestRack;
    Button   BtnTaruhBin;
    ImageView  MsgTempBin,MsgDestRack;

    JSONArray  bin,rak,updatebin;
    String     statusBin="";
    String     statusUpdate="0";
    String     statusRack="Salah";
    String     TransactionCode="";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskpickingpck4);

        Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);
        TransactionCode = in.getStringExtra(TAG_TRANSACTIONCODE);

        MsgTempBin = (ImageView) findViewById(R.id.OPTPPCK4_BinTempMsg);
        MsgTempBin.setVisibility(View.GONE);
        MsgDestRack = (ImageView) findViewById(R.id.OPTPPCK4_DestRackMsg);
        MsgDestRack.setVisibility(View.GONE);

        TxtBinCode = (TextView) findViewById(R.id.OPTPPCK4_BinCode);
        TxtBinCode.setText(in.getStringExtra(TAG_DESTBIN));
        TxtRackName = (TextView) findViewById(R.id.OPTPPCK4_TxtRack);

        InputTempBin = (EditText) findViewById(R.id.OPTPPCK4_BinTemp);
        InputDestRack = (EditText) findViewById(R.id.OPTPPCK4_DestRack);

        InputDestRack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(InputDestRack.getText().toString().length()==8){
                    // Creating JSON Parser instance
                    JSONParser jParser = new JSONParser();

                    // 4987176006783 10/00/R/000001";
                    String url = "http://192.168.31.10:9020/ws/operatortaskpickingpck4cekrak.php?koderak="+InputDestRack.getText().toString();
                    // getting JSON string from URL
                    JSONObject json = jParser.getJSONFromUrl(url);

                    try {
                        // Getting Array of Satuan
                        rak = json.getJSONArray("operatortaskpickingpck4cekrak");
                        // looping through All Barang
                        for (int i = 0; i < rak.length(); i++) {
                            JSONObject c = rak.getJSONObject(i);
                            statusRack = c.getString("Name");
                        }
                        TxtRackName.setText(statusRack);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    statusRack="Salah";
                    TxtRackName.setText(statusRack);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        BtnTaruhBin = (Button) findViewById(R.id.OPTPPCK4_BtnTaruhBin);
        BtnTaruhBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InputTempBin.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(),"Bin Temp Harus Diisi",Toast.LENGTH_SHORT).show();
                    MsgTempBin.setVisibility(View.VISIBLE);
                    MsgDestRack.setVisibility(View.GONE);
                }else if(InputDestRack.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(),"Rak Tujuan Harus Diisi",Toast.LENGTH_SHORT).show();
                    MsgDestRack.setVisibility(View.VISIBLE);
                    MsgTempBin.setVisibility(View.GONE);
                }else{
                    // Cek Bin
                    // Creating JSON Parser instance
                    JSONParser jParser = new JSONParser();

                    // 4987176006783 10/00/R/000001";
                    String url = "http://192.168.31.10:9020/ws/operatortaskpickingpck4cekbin.php?bincode="+InputTempBin.getText().toString()+"&operatorcode="+OperatorCode;
                    // getting JSON string from URL
                    JSONObject json = jParser.getJSONFromUrl(url);

                    try {
                        // Getting Array of Satuan
                        bin = json.getJSONArray("operatortaskpickingpck4cekbin");
                        // looping through All Barang
                        for (int i = 0; i < bin.length(); i++) {
                            JSONObject c = bin.getJSONObject(i);
                            statusBin = c.getString("Jumlah");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(statusBin.equals("1")){
                        if(!TxtRackName.getText().toString().equals("Salah")){
                            // Cek Bin
                            // Creating JSON Parser instance
                            jParser = new JSONParser();

                            // 4987176006783 10/00/R/000001";
                            url = "http://192.168.31.10:9020/ws/operatortaskpickingpck4updatetaruhbin.php?kodebin="+InputTempBin.getText().toString()+"&operatorcode="+OperatorCode+"&koderak="+InputDestRack.getText().toString()+"&transactioncode="+TransactionCode;
                            // getting JSON string from URL
                            json = jParser.getJSONFromUrl(url);

                            try {
                                // Getting Array of Satuan
                                updatebin = json.getJSONArray("operatortaskpickingpck4updatetaruhbin");
                                // looping through All Barang
                                for (int i = 0; i < updatebin.length(); i++) {
                                    JSONObject c = updatebin.getJSONObject(i);
                                    statusUpdate = c.getString("Status");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (statusUpdate.equals("1")){
                                Toast.makeText(getApplicationContext(),"Update Berhasil",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(),"Gagal Update",Toast.LENGTH_SHORT).show();
                            }

                            MsgDestRack.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(getApplicationContext(),"Rak Tidak Ada",Toast.LENGTH_SHORT).show();
                            MsgTempBin.setVisibility(View.GONE);
                            MsgDestRack.setVisibility(View.VISIBLE);
                        }
                    }else{
                        MsgTempBin.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),statusBin,Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}