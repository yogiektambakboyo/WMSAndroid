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

public class OperatorTaskMBPB_4 extends Activity {
    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";
    private String TransactionCode = "";
    private String NoUrut = "";
    private String QueueNumber = "";
    private String SKUCode = "";
    private String BinCode = "";
    private String ERPCode = "";
    private String Keterangan = "";
    private String DestRack = "";

    // url to make request
    private String url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb2prosesambil.php?operatorcode=1";

    // JSON Node names
    private final String TAG_CARISATUAN = "operatortaskreceivebpb2getsatuan";
    private final String TAG_OPERATORTASKBPB3= "operatortaskmoverbpb2prosesambil";
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
    private final String TAG_STATUS = "Status";
    private final String TAG_CARIRAK = "operatortaskreceivebpb2getrakslot";
    private final String TAG_CEKRAK = "operatortaskreceivebpb2cekrak";

    private int    Ratio=0;
    private int    Qty=0;
    private String Gang="";

    String[] GangArray;
    JSONArray  rak=null;
    JSONArray  cekrak=null;

    //Form
    TextView TxtBPB,TxtKodeBin,TxtJumlah,TxtRasioName, TxtBrg,TxtDestRack, TxtRackSlot;
    EditText InputKodeBin,InputRackDest,InputBinDest;
    Spinner  SpnGang;
    ImageView KodeBinMsg,DestRackMsg,JumlahMsg,GangMsg,DestBinMsg;
    Button    BtnSubmit;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskmoverbpb4);

        Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);
        TransactionCode = in.getStringExtra(TAG_TRANSACTIONCODE);
        NoUrut = in.getStringExtra(TAG_NOURUT);
        QueueNumber = in.getStringExtra(TAG_QUEUENUMBER);
        SKUCode = in.getStringExtra(TAG_SKUCODE);
        BinCode = in.getStringExtra(TAG_BINCODE);
        Keterangan = in.getStringExtra(TAG_KETERANGAN);
        DestRack = in.getStringExtra(TAG_DESTRACKSLOT);
        ERPCode = in.getStringExtra(TAG_ERPCODE);
        Qty = Integer.parseInt(in.getStringExtra(TAG_QTY));
        Ratio = Integer.parseInt(in.getStringExtra(TAG_RATIO));

        TxtBPB = (TextView) findViewById(R.id.OPTMBPB4_BPB);
        TxtBPB.setText(ERPCode);

        TxtJumlah = (TextView) findViewById(R.id.OPTMBPB4_Qty);
        float QtyRasio = Qty/Ratio;
        TxtJumlah.setText(Float.toString(QtyRasio));

        TxtRasioName = (TextView) findViewById(R.id.OPTMBPB4_RasioName);
        TxtRasioName.setText(in.getStringExtra(TAG_RATIONAME));

        TxtKodeBin = (TextView) findViewById(R.id.OPTMBPB4_TxtKodeBin);
        TxtKodeBin.setText(BinCode);

        TxtBrg = (TextView) findViewById(R.id.OPTMBPB4_Brg);
        TxtBrg.setText(Keterangan);

        TxtDestRack = (TextView) findViewById(R.id.OPTMBPB4_DestRack);
        TxtDestRack.setText(DestRack);

        TxtRackSlot = (TextView) findViewById(R.id.OPTMBPB4_TxtRack);

        //Hide Error Message
        KodeBinMsg = (ImageView) findViewById(R.id.OPTMBPB4_KodeBinMsg);
        KodeBinMsg.setVisibility(View.GONE);

        DestRackMsg = (ImageView) findViewById(R.id.OPTMBPB4_RackDestMsg);
        DestRackMsg.setVisibility(View.GONE);

        JumlahMsg = (ImageView) findViewById(R.id.OPTMBPB4_QtyMsg);
        JumlahMsg.setVisibility(View.GONE);

        GangMsg = (ImageView) findViewById(R.id.OPTMBPB4_GangMsg);
        GangMsg.setVisibility(View.GONE);

        DestBinMsg = (ImageView) findViewById(R.id.OPTMBPB4_BinDestMsg);
        DestBinMsg.setVisibility(View.GONE);

        SpnGang = (Spinner) findViewById(R.id.OPTMBPB4_SpinnerGang);
        GangArray=new String[2];
        GangArray[0]="Tidak";
        GangArray[1]="Ya";

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, GangArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpnGang.setAdapter(adapter);

        SpnGang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Gang = GangArray[position];
                Toast.makeText(getApplicationContext(),"Gang - "+Gang,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        InputKodeBin = (EditText) findViewById(R.id.OPTMBPB4_KodeBin);
        InputRackDest = (EditText) findViewById(R.id.OPTMBPB4_RackDest);
        InputBinDest = (EditText) findViewById(R.id.OPTMBPB4_BinDest);


        //Add listener to cek Rak
        InputRackDest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(InputRackDest.getText().toString().length()==8){

                    // Creating JSON Parser instance
                    JSONParser jParser = new JSONParser();

                    // 4987176006783 10/00/R/000001";
                    String url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2getrakslot.php?koderak="+InputRackDest.getText().toString().replace(" ","");

                    // getting JSON string from URL
                    JSONObject json = jParser.getJSONFromUrl(url);

                    try {
                        // Getting Array of Satuan
                        rak = json.getJSONArray(TAG_CARIRAK);
                        // looping through All Barang
                        for (int i = 0; i < rak.length(); i++) {
                            JSONObject c = rak.getJSONObject(i);
                            TxtRackSlot.setText(c.getString("Name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    TxtRackSlot.setText("Rak Salah");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        BtnSubmit = (Button) findViewById(R.id.OPTMBPB4_Submit);
        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InputKodeBin.getText().length()<=0){
                    Toast.makeText(getApplicationContext(),"Kode Bin Harus Diisi!!!",Toast.LENGTH_SHORT).show();
                    KodeBinMsg.setVisibility(View.VISIBLE);
                    DestRackMsg.setVisibility(View.GONE);
                    DestBinMsg.setVisibility(View.GONE);
                }else if(InputRackDest.getText().length()<=0){
                    Toast.makeText(getApplicationContext(),"Rak Tujuan Harus Diisi!!!",Toast.LENGTH_SHORT).show();
                    KodeBinMsg.setVisibility(View.GONE);
                    DestRackMsg.setVisibility(View.VISIBLE);
                    DestBinMsg.setVisibility(View.GONE);
                }else if(InputBinDest.getText().length()<=0){
                    Toast.makeText(getApplicationContext(),"Bin Tujuan Harus Diisi!!!",Toast.LENGTH_SHORT).show();
                    KodeBinMsg.setVisibility(View.GONE);
                    DestRackMsg.setVisibility(View.GONE);
                    DestBinMsg.setVisibility(View.VISIBLE);
                }else if((InputKodeBin.getText().length()!=7)||(!InputKodeBin.getText().toString().equals(BinCode))){
                    Toast.makeText(getApplicationContext(),"Kode Bin Salah!!!",Toast.LENGTH_SHORT).show();
                    KodeBinMsg.setVisibility(View.VISIBLE);
                    DestRackMsg.setVisibility(View.GONE);
                    DestBinMsg.setVisibility(View.GONE);
                }else if(TxtRackSlot.getText().toString().equals("Rak Salah")){
                    Toast.makeText(getApplicationContext(),"Rak Tujuan Salah!!!",Toast.LENGTH_SHORT).show();
                    KodeBinMsg.setVisibility(View.GONE);
                    DestRackMsg.setVisibility(View.VISIBLE);
                    DestBinMsg.setVisibility(View.GONE);
                }else{
                    if((InputBinDest.getText().toString().equals(InputKodeBin.getText().toString()))||(InputBinDest.getText().length()<=0)){
                        String CekValid = CekValidasiRack(InputRackDest.getText().toString(),InputKodeBin.getText().toString());
                        Toast.makeText(getApplicationContext(),CekValid,Toast.LENGTH_SHORT).show();
                        if((!CekValid.equals("1"))&&(Gang.equals("Tidak"))){
                            Toast.makeText(getApplicationContext(),"Rak Tidak MultipleBin/Bin Salah!!!",Toast.LENGTH_SHORT).show();
                            KodeBinMsg.setVisibility(View.GONE);
                            DestRackMsg.setVisibility(View.GONE);
                            DestBinMsg.setVisibility(View.VISIBLE);
                        }else{
                            if((getCountSKUBarangInOneBin(InputKodeBin.getText().toString()).equals("0"))&&(Gang.equals("Tidak"))){
                                Toast.makeText(getApplicationContext(),"Bin Tujuan Harus Diisi Dengan Benar!!!",Toast.LENGTH_SHORT).show();
                                KodeBinMsg.setVisibility(View.GONE);
                                DestRackMsg.setVisibility(View.GONE);
                                DestBinMsg.setVisibility(View.VISIBLE);
                            }else{
                                InputBinDest.setText(TxtKodeBin.getText().toString());
                            }
                        }
                    }else{
                        // Cek SKU ED
                        if(CekSKUED(InputBinDest.getText().toString(),SKUCode,InputKodeBin.getText().toString(),TransactionCode,NoUrut).equals("0")){
                            Toast.makeText(getApplicationContext(),"Bin Tujuan SKU dan ED Tidak Sesuai!!!",Toast.LENGTH_SHORT).show();
                            KodeBinMsg.setVisibility(View.GONE);
                            DestRackMsg.setVisibility(View.GONE);
                            DestBinMsg.setVisibility(View.VISIBLE);
                        }
                    }

                    // Sampe Sini
                    if((Gang.equals("Ya"))||((CekValidasiRack(InputRackDest.getText().toString(),InputKodeBin.getText().toString()).equals("1"))&&(Gang.equals("Tidak")))||((CekValidasiRack(InputRackDest.getText().toString(),InputKodeBin.getText().toString()).equals("0"))&&(Gang.equals("Tidak"))&&(CekSKUED(InputBinDest.getText().toString(),SKUCode,InputKodeBin.getText().toString(),TransactionCode,NoUrut).equals("1")))){
                        if(TaruhBin(InputKodeBin.getText().toString(),InputBinDest.getText().toString(),InputRackDest.getText().toString(),Qty,Gang,OperatorCode,TransactionCode,NoUrut,QueueNumber).equals("1")){
                            Toast.makeText(getApplicationContext(),"Input Berhasil",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), OperatorMenu.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),"Input Gagal!!!",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Area Rack Tidak Bisa Dipakai!!!",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public String CekValidasiRack(String KodeRack,String KodeBin){
        String status="0";

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        // 4987176006783 10/00/R/000001";
        String url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb2cekrak.php?kodebin="+KodeBin+"&koderack="+KodeRack;

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        try {
            // Getting Array of Satuan
            cekrak = json.getJSONArray(TAG_CEKRAK);
            // looping through All Barang
            for (int i = 0; i < cekrak.length(); i++) {
                JSONObject c = cekrak.getJSONObject(i);
                status = c.getString(TAG_STATUS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public String getCountSKUBarangInOneBin(String KodeBin){
        String Jumlah="0";

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        // 4987176006783 10/00/R/000001";
        String url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb2getcountskubaranginonebin.php?kodebin="+KodeBin;

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        try {
            // Getting Array of Satuan
            cekrak = json.getJSONArray("operatortaskmoverbpb2getcountskubaranginonebin");
            // looping through All Barang
            for (int i = 0; i < cekrak.length(); i++) {
                JSONObject c = cekrak.getJSONObject(i);
                Jumlah = c.getString("Jumlah");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Jumlah;
    }

    public String CekSKUED(String KodeBinDest, String SKUCode, String KodeBin, String TransactionCode, String NoUrut){
        String status="0";

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        // 4987176006783 10/00/R/000001";
        String url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb2cekskued.php?kodebindest="+KodeBinDest+"&kodebin="+KodeBin+"&skucode="+SKUCode+"&transactioncode="+TransactionCode+"&nourut="+NoUrut;

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        try {
            // Getting Array of Satuan
            cekrak = json.getJSONArray("operatortaskmoverbpb2cekskued");
            // looping through All Barang
            for (int i = 0; i < cekrak.length(); i++) {
                JSONObject c = cekrak.getJSONObject(i);
                status = c.getString(TAG_STATUS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public String TaruhBin(String KodeBin, String KodeBinDest, String KodeRack,int Qty, String Gang, String OperatorCode, String TransactionCode, String NoUrut, String QueueNumber){
        String status="0";

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        // 4987176006783 10/00/R/000001";
        String url = "http://192.168.31.10:9020/ws/operatortaskmoverbpb2updatebinsku.php?kodebindest="+KodeBinDest+"&kodebin="+KodeBin+"&transactioncode="+TransactionCode+"&nourut="+NoUrut+"&koderack="+KodeRack+"&qty="+Qty+"&gang="+Gang+"&operatorcode="+OperatorCode+"&queuenumber="+QueueNumber;

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        try {
            // Getting Array of Satuan
            cekrak = json.getJSONArray("operatortaskmoverbpb2updatebinsku");
            // looping through All Barang
            for (int i = 0; i < cekrak.length(); i++) {
                JSONObject c = cekrak.getJSONObject(i);
                status = c.getString(TAG_STATUS);
                Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }
}