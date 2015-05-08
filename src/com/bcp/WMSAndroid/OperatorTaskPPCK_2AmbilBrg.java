package com.bcp.WMSAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OperatorTaskPPCK_2AmbilBrg extends Activity {
    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";
    private String DestBin = "";
    private String WHCode = "";
    private String EDPanjang = "";
    private String TransactionDate = "";
    private String TransactionCode = "";
    private String ERPCode = "";
    private String Keterangan = "";
    private String SKUCode = "0";
    private String Jumlah = "";
    private String JumlahKonversi = "";
    private String AddTask = "";
    private String Needed = "";
    private String Picked = "";
    private String NoUrut = "";

    // url to make request
    private static String url = "http://192.168.31.10:9020/ws/operatortaskpickingpck2.php?transactioncode=1";


    // JSON Node names
    private static final String TAG_OPERATORTASKPPCK2= "operatortaskpickingpck2";
    private static final String TAG_TRANSACTIONCODE = "TransactionCode";
    private static final String TAG_ERPCODE = "ERPCode";
    private static final String TAG_SKUCODE = "SKUCode";
    private static final String TAG_NOURUT = "NoUrut";
    private static final String TAG_KETERANGAN = "Keterangan";
    private static final String TAG_QTY = "Qty";
    private static final String TAG_SATUAN = "Satuan";
    private final        String TAG_RASIO = "Rasio";
    private static final String TAG_NEEDED = "Needed";
    private static final String TAG_PICKED = "Picked";
    private static final String TAG_KONVESI = "Konversi";
    private static final String TAG_ADDTASK = "AddTask";
    private static final String TAG_STATADDTASK = "NormalTask";
    private static final String TAG_DESTBIN = "DestBin";

    private static final String TAG_TRANSACTIONDATE = "TransactionDate";
    private static final String TAG_WHCODE = "WHCode";
    private static final String TAG_EDPANJANG = "EDPanjang";
    private static final String TAG_ASSIGNED = "Assigned";

    private static final String TAG_RACKSLOTNAME = "RackSlotName";
    private static final String TAG_RACKSLOTLVL = "RackSlotLvl";
    private static final String TAG_EXPDATE = "ExpDate";
    private static final String TAG_QTYTEXT = "QtyText";
    private static final String TAG_PRIORITY = "Priority";

    private final String TAG_CARIRAK = "operatortaskreceivebpb2getrakslot";

    // Form
    EditText InputBinSrc,InputRackSrc,InputQty;
    TextView TxtKeterangan, TxtJumlah, TxtKodeBin,TxtRackName;
    Spinner  CmbSatuan;
    ImageView RakSrcMsg,BinSrcMsg,QtyMsg;
    Button    BtnSubmit;

    JSONArray satuan=null;
    List SatuanList;

    private int RasioBrg=0;

    ArrayList<HashMap<String, String>> OperatorTaskBPB2ListSatuan = new ArrayList<HashMap<String, String>>();

    JSONArray operatortaskppckBin= null;
    JSONArray operatortaskppcksetBin= null;
    JSONArray  rak=null;

    private String Status = "0";
    private String StatusSet = "0";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskpickingpck2ambilbrg);

        Intent in = getIntent();
        DestBin = in.getStringExtra(TAG_DESTBIN);
        Keterangan = in.getStringExtra(TAG_KETERANGAN);
        SKUCode = in.getStringExtra(TAG_SKUCODE);
        Jumlah = in.getStringExtra(TAG_QTY);
        JumlahKonversi = in.getStringExtra(TAG_QTYTEXT);
        Needed = in.getStringExtra(TAG_NEEDED);
        AddTask = in.getStringExtra(TAG_ADDTASK);
        NoUrut = in.getStringExtra(TAG_NOURUT);
        Picked = in.getStringExtra(TAG_PICKED);

        ERPCode = in.getStringExtra(TAG_ERPCODE);
        TransactionDate = in.getStringExtra(TAG_TRANSACTIONDATE);
        WHCode = in.getStringExtra(TAG_WHCODE);
        TransactionCode = in.getStringExtra(TAG_TRANSACTIONCODE);

        TxtKodeBin = (TextView) findViewById(R.id.OPTPPCK2Ambil_BinCode);
        TxtKodeBin.setText(DestBin);

        TxtKeterangan = (TextView) findViewById(R.id.OPTPPCK2Ambil_NamaBrg);
        TxtKeterangan.setText(Keterangan);

        TxtJumlah = (TextView) findViewById(R.id.OPTPPCK2Ambil_Jumlah);
        TxtJumlah.setText(JumlahKonversi);

        TxtRackName = (TextView) findViewById(R.id.OPTPPCK2Ambil_TxtRack);

        InputBinSrc = (EditText) findViewById(R.id.OPTPPCK2Ambil_InputBinAsal);
        InputRackSrc = (EditText) findViewById(R.id.OPTPPCK2Ambil_InputRakAsal);
        InputQty = (EditText) findViewById(R.id.OPTPPCK2Ambil_InputJumlah);

        TxtRackName.setText("");

        //Add listener to cek Rak
        InputRackSrc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (InputRackSrc.getText().toString().length() == 8) {

                    // Creating JSON Parser instance
                    JSONParser jParser = new JSONParser();

                    // 4987176006783 10/00/R/000001";
                    String url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2getrakslot.php?koderak=" + InputRackSrc.getText().toString().replace(" ", "");

                    // getting JSON string from URL
                    JSONObject json = jParser.getJSONFromUrl(url);

                    try {
                        // Getting Array of Satuan
                        rak = json.getJSONArray(TAG_CARIRAK);
                        // looping through All Barang
                        for (int i = 0; i < rak.length(); i++) {
                            JSONObject c = rak.getJSONObject(i);
                            TxtRackName.setText(c.getString("Name"));
                            RakSrcMsg.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    RakSrcMsg.setVisibility(View.VISIBLE);
                    TxtRackName.setText("Rak Tidak Ada");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        RakSrcMsg = (ImageView) findViewById(R.id.OPTPPCKAmbil_InputRakAsalMsg);
        BinSrcMsg = (ImageView) findViewById(R.id.OPTPPCKAmbil_InputBinAsalMsg);
        QtyMsg = (ImageView) findViewById(R.id.OPTPPCKAmbil_SatuanMsg);

        RakSrcMsg.setVisibility(View.GONE);
        BinSrcMsg.setVisibility(View.GONE);
        QtyMsg.setVisibility(View.GONE);

        BtnSubmit = (Button) findViewById(R.id.OPTPPCK2Ambil_BtnSubmit);
        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InputRackSrc.getText().toString().length()==0){
                    RakSrcMsg.setVisibility(View.VISIBLE);
                    BinSrcMsg.setVisibility(View.GONE);
                    QtyMsg.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Rak Asal Harus Diisi!!",Toast.LENGTH_SHORT).show();
                }else if(InputBinSrc.getText().toString().length()==0){
                    RakSrcMsg.setVisibility(View.GONE);
                    BinSrcMsg.setVisibility(View.VISIBLE);
                    QtyMsg.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Bin Asal Harus Diisi!!",Toast.LENGTH_SHORT).show();
                }else if(InputQty.getText().toString().length()==0){
                    RakSrcMsg.setVisibility(View.GONE);
                    BinSrcMsg.setVisibility(View.GONE);
                    QtyMsg.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Jumlah Barang Harus Diisi!!",Toast.LENGTH_SHORT).show();
                }else if(InputQty.getText().toString().equals("0")){
                    RakSrcMsg.setVisibility(View.GONE);
                    BinSrcMsg.setVisibility(View.GONE);
                    QtyMsg.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Jumlah Barang Tidak Boleh 0!!",Toast.LENGTH_SHORT).show();
                }else if((Integer.parseInt(InputQty.getText().toString())*RasioBrg)>Integer.parseInt(Jumlah)){
                    RakSrcMsg.setVisibility(View.GONE);
                    BinSrcMsg.setVisibility(View.GONE);
                    QtyMsg.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Jumlah Barang Berlebih!!",Toast.LENGTH_SHORT).show();
                }else if((InputRackSrc.getText().toString().length()!=8)||(InputRackSrc.getText().toString().equals("Rak Tidak Ada"))){
                    RakSrcMsg.setVisibility(View.VISIBLE);
                    BinSrcMsg.setVisibility(View.GONE);
                    QtyMsg.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Rak Asal Salah!!",Toast.LENGTH_SHORT).show();
                }else if(InputBinSrc.getText().toString().length()!=7){
                    RakSrcMsg.setVisibility(View.GONE);
                    BinSrcMsg.setVisibility(View.VISIBLE);
                    QtyMsg.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Bin Asal Salah!!",Toast.LENGTH_SHORT).show();
                }else{
                    // Creating JSON Parser instance
                    JSONParser jParser = new JSONParser();
                    url = "http://192.168.31.10:9020/ws/operatortaskpickingpckambilcekbin.php?bincode="+InputBinSrc.getText().toString()+"&operatorcode="+OperatorCode+"&rackcode="+InputRackSrc.getText().toString()+"&skucode="+SKUCode;

                    // getting JSON string from URL
                    JSONObject json = jParser.getJSONFromUrl(url);


                    try {
                        // Getting Array of Barang
                        operatortaskppckBin = json.getJSONArray("operatortaskpickingpckambilcekbin");

                        // looping through All Barang
                        for(int i = 0; i < operatortaskppckBin.length(); i++){
                            JSONObject c = operatortaskppckBin.getJSONObject(i);

                            // Storing each json item in variable
                            Status = c.getString("Jumlah");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(Status.equals("1")){
                        // Creating JSON Parser instance
                        jParser = new JSONParser();
                        url = "http://192.168.31.10:9020/ws/operatortaskpickingpcksetambilbarang.php?skucode="+SKUCode+"&bincode="+InputBinSrc.getText().toString()+"&rackcode="+InputRackSrc.getText().toString()+"&transactioncode="+TransactionCode+"&bintemp="+DestBin+"&qtyrasio="+(RasioBrg*Integer.parseInt(Jumlah))+"&nourut="+NoUrut+"&addtask="+AddTask+"&operatorcode="+OperatorCode+"&needed="+Needed+"&picked="+Picked;

                        // getting JSON string from URL
                        json = jParser.getJSONFromUrl(url);


                        try {
                            // Getting Array of Barang
                            operatortaskppcksetBin = json.getJSONArray("operatortaskpickingpcksetambilbarang");

                            // looping through All Barang
                            for(int i = 0; i < operatortaskppcksetBin.length(); i++){
                                JSONObject c = operatortaskppcksetBin.getJSONObject(i);

                                // Storing each json item in variable
                                StatusSet = c.getString("Status");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(StatusSet.equals("1")){
                            RakSrcMsg.setVisibility(View.GONE);
                            BinSrcMsg.setVisibility(View.GONE);
                            QtyMsg.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Berhasil!!!",Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(getApplicationContext(),OperatorTaskPPCK_2.class);
                            in.putExtra(TAG_ERPCODE,ERPCode);
                            in.putExtra(TAG_TRANSACTIONDATE, TransactionDate);
                            in.putExtra(TAG_EDPANJANG,EDPanjang);
                            in.putExtra(TAG_WHCODE,WHCode);
                            in.putExtra(TAG_TRANSACTIONCODE,TransactionCode);
                            in.putExtra(TAG_DESTBIN,DestBin);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);
                        }else{
                            Toast.makeText(getApplicationContext(),StatusSet,Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        RakSrcMsg.setVisibility(View.VISIBLE);
                        BinSrcMsg.setVisibility(View.VISIBLE);
                        QtyMsg.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),Status,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        SatuanList = new ArrayList();

        getDataListSatuan();
        addListenerOnSpinnerItemSelection();
    }

    public void getDataListSatuan(){
        if(!SKUCode.equals("0")){

            // Creating JSON Parser instance
            JSONParser jParser = new JSONParser();

            String url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2getsatuan.php?kodebarang="+SKUCode;

            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url);

            SatuanList.clear();

            try {
                // Getting Array of Satuan
                satuan = json.getJSONArray("operatortaskreceivebpb2getsatuan");
                // looping through All Barang
                for (int i = 0; i < satuan.length(); i++) {
                    JSONObject c = satuan.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value

                    map.put(TAG_SATUAN, c.getString(TAG_SATUAN));
                    map.put(TAG_RASIO, c.getString(TAG_RASIO));

                    OperatorTaskBPB2ListSatuan.add(map);

                    SatuanList.add(c.getString(TAG_SATUAN));
                }
                addItemsOnSpinner(SatuanList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void addItemsOnSpinner(List satuanList) {
        CmbSatuan = (Spinner) findViewById(R.id.OPTPPCK2Ambil_Satuan);
        List list = satuanList;
        ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CmbSatuan.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        CmbSatuan = (Spinner) findViewById(R.id.OPTPPCK2Ambil_Satuan);
        CmbSatuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RasioBrg = Integer.parseInt(OperatorTaskBPB2ListSatuan.get(position).get(TAG_RASIO).toString());
                Toast.makeText(getApplicationContext(),"Rasio Brg - "+RasioBrg,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
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
        return false;
    }

}