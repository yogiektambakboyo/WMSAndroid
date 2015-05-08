package com.bcp.WMSAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OperatorTaskRBPB_2 extends Activity {

    EditText InputKode,InputRackSlot,InputExpDate,InputBin,InputQty;
    TextView TxtKodeBrg,TxtNamaBrg,TxtKodeNota,TxtPerusahaan,TxtNote,TxtRackSlot;
    Spinner  CmbSatuan;
    Button   BtnCariManual;
    Button   BtnSubmit;
    Button   BtnDaftarBrg;

    ImageView ImgKodeBrg;
    ImageView ImgNamaBrg;
    ImageView ImgQty;
    ImageView ImgExpDate;
    ImageView ImgBin;
    ImageView ImgRakSlot;

    private final String TAG_PREF= "WMSAndroid";
    private final String TAG_RECEIVESOURCE = "ReceiveSource";
    private final String TAG_OPERATOR = "OperatorCode";
    private final String TAG_KODE = "Kode";
    private final String TAG_KETERANGAN = "Keterangan";
    private final String TAG_CARIBARCODE = "operatortaskreceivebpb2caribarangbarcode";
    private final String TAG_TRANSACTIONCODE = "TransactionCode";
    private final String TAG_KODENOTA = "KodeNota";
    private final String TAG_PERUSAHAAN = "Perusahaan";
    private final String TAG_NOTE = "Note";
    private final String TAG_CARISATUAN = "operatortaskreceivebpb2getsatuan";
    private final String TAG_CARIRAK = "operatortaskreceivebpb2getrakslot";
    private final String TAG_CARIBRG = "operatortaskreceivebpb2caribarangmanual";
    private final String TAG_SATUAN = "Satuan";
    private final String TAG_RASIO = "Rasio";
    private final String TAG_CARIISUSED = "operatortaskreceivebpb2cekbin";
    private final String TAG_CARIRAKSLOT = "operatortaskreceivebpb2cekrakslot";
    private final String TAG_CARICEKJUMLAH = "operatortaskreceivebpb2cekjumlah";
    private final String TAG_STATUSINSERT = "operatortaskreceivebpb2insert";

    JSONArray  barangbarcode=null;
    JSONArray  satuan=null;
    JSONArray  rak=null;
    JSONArray  brg=null;
    JSONArray  isused=null;
    JSONArray  israkslot=null;
    JSONArray  isjumlahok=null;
    JSONArray  isInsert=null;

    String TransactionCode;
    String Barcode;
    String isUsed="1";
    String isRakSlot="0";
    String StatusJumlah="0";
    String JumlahOK="0";
    String StatusInsert="0";
    String OperatorCode="";

    private int year;
    private int month;
    private int day;
    private int RasioBrg=0;

    List SatuanList;
    ArrayList<HashMap<String, String>> OperatorTaskBPB2ListSatuan = new ArrayList<HashMap<String, String>>();

    Intent in;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskreceivebpb2);

        SatuanList = new ArrayList();

        in = getIntent();
        TransactionCode = in.getStringExtra(TAG_TRANSACTIONCODE);
        OperatorCode = in.getStringExtra(TAG_OPERATOR);

        InputKode = (EditText) findViewById(R.id.OPTRBPB2_CariKode);
        InputRackSlot = (EditText) findViewById(R.id.OPTRBPB2_RackSrc);
        InputExpDate = (EditText) findViewById(R.id.OPTRBPB2_Exp);
        InputBin = (EditText) findViewById(R.id.OPTRBPB2_Bin);
        InputQty = (EditText) findViewById(R.id.OPTRBPB2_Qty);
        TxtKodeBrg = (TextView) findViewById(R.id.OPTRBPB2_KodeBrg);
        TxtNamaBrg = (TextView) findViewById(R.id.OPTRBPB2_NamaBrg);
        TxtKodeNota = (TextView) findViewById(R.id.OPTRBPB2_kodenota);
        TxtPerusahaan = (TextView) findViewById(R.id.OPTRBPB2_Perusahaan);
        TxtNote = (TextView) findViewById(R.id.OPTRBPB2_Note);
        TxtRackSlot = (TextView) findViewById(R.id.OPTRBPB2_RackTxt);
        TxtRackSlot.setText("");


        ImgKodeBrg = (ImageView) findViewById(R.id.KodeBrgMsg);
        ImgNamaBrg = (ImageView) findViewById(R.id.NamaBrgMsg);
        ImgQty = (ImageView) findViewById(R.id.QtyMsg);
        ImgExpDate = (ImageView) findViewById(R.id.ExpDateMsg);
        ImgBin = (ImageView) findViewById(R.id.BinMsg);
        ImgRakSlot = (ImageView) findViewById(R.id.RackMsg);

        BtnCariManual = (Button) findViewById(R.id.OPTRBPB2_CariManual);
        BtnSubmit = (Button) findViewById(R.id.OPTRBPB2_Submit);
        BtnDaftarBrg = (Button) findViewById(R.id.OPTRBPB2_ListBrg);



        TxtKodeNota.setText(in.getStringExtra(TAG_KODENOTA));
        TxtPerusahaan.setText(in.getStringExtra(TAG_PERUSAHAAN));
        TxtNote.setText(in.getStringExtra(TAG_NOTE));


        ImgKodeBrg.setVisibility(View.GONE);
        ImgNamaBrg.setVisibility(View.GONE);
        ImgQty.setVisibility(View.GONE);
        ImgExpDate.setVisibility(View.GONE);
        ImgBin.setVisibility(View.GONE);
        ImgRakSlot.setVisibility(View.GONE);

        BtnDaftarBrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), OperatorTaskRBPB_2_ListBrg.class);
                in.putExtra(TAG_TRANSACTIONCODE,TransactionCode);
                in.putExtra(TAG_OPERATOR,OperatorCode);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });

        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validation Input
                if(TxtKodeBrg.getText().toString().equals("0")){
                    ImgKodeBrg.setVisibility(View.VISIBLE);
                    ImgNamaBrg.setVisibility(View.GONE);
                    ImgQty.setVisibility(View.GONE);
                    ImgExpDate.setVisibility(View.GONE);
                    ImgBin.setVisibility(View.GONE);
                    ImgRakSlot.setVisibility(View.GONE);
                }else if(TxtNamaBrg.getText().toString().equals("0")){
                    ImgNamaBrg.setVisibility(View.VISIBLE);
                    ImgKodeBrg.setVisibility(View.GONE);
                    ImgQty.setVisibility(View.GONE);
                    ImgExpDate.setVisibility(View.GONE);
                    ImgBin.setVisibility(View.GONE);
                    ImgRakSlot.setVisibility(View.GONE);
                }else if((InputQty.getText().toString().length()==0)||(InputQty.getText().toString().equals("0"))){
                    InputQty.setText("0");
                    if(Integer.parseInt(InputQty.getText().toString())<=0){
                        ImgQty.setVisibility(View.VISIBLE);
                        ImgKodeBrg.setVisibility(View.GONE);
                        ImgNamaBrg.setVisibility(View.GONE);
                        ImgExpDate.setVisibility(View.GONE);
                        ImgBin.setVisibility(View.GONE);
                        ImgRakSlot.setVisibility(View.GONE);
                    }
                }else if(InputExpDate.getText().toString().length()==0){
                    ImgExpDate.setVisibility(View.VISIBLE);
                    ImgKodeBrg.setVisibility(View.GONE);
                    ImgNamaBrg.setVisibility(View.GONE);
                    ImgQty.setVisibility(View.GONE);
                    ImgBin.setVisibility(View.GONE);
                    ImgRakSlot.setVisibility(View.GONE);
                }else if(InputBin.getText().toString().length()!=7){
                    ImgBin.setVisibility(View.VISIBLE);
                    ImgKodeBrg.setVisibility(View.GONE);
                    ImgNamaBrg.setVisibility(View.GONE);
                    ImgQty.setVisibility(View.GONE);
                    ImgExpDate.setVisibility(View.GONE);
                    ImgRakSlot.setVisibility(View.GONE);
                }else if(InputRackSlot.getText().toString().length()!=8){
                    ImgRakSlot.setVisibility(View.VISIBLE);
                    ImgKodeBrg.setVisibility(View.GONE);
                    ImgNamaBrg.setVisibility(View.GONE);
                    ImgQty.setVisibility(View.GONE);
                    ImgExpDate.setVisibility(View.GONE);
                    ImgBin.setVisibility(View.GONE);
                }else{
                    // Creating JSON Parser instance
                    JSONParser jParser = new JSONParser();

                    // 4987176006783 10/14/R/000001";
                    String url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2cekbin.php?kodebin="+InputBin.getText().toString();

                    // getting JSON string from URL
                    JSONObject json = jParser.getJSONFromUrl(url);

                    try {
                        // Getting Array of Barang
                        isused = json.getJSONArray(TAG_CARIISUSED);

                        // looping through All Barang
                        for (int i = 0; i < isused.length(); i++) {
                            JSONObject c = isused.getJSONObject(i);

                            // Storing each json item in variable
                            isUsed = c.getString("isUsed");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(isUsed.equals("1")){
                        Toast.makeText(OperatorTaskRBPB_2.this,InputBin.getText().toString()+" Tidak valid atau telah dipakai",Toast.LENGTH_SHORT).show();
                        ImgBin.setVisibility(View.VISIBLE);
                        ImgKodeBrg.setVisibility(View.GONE);
                        ImgNamaBrg.setVisibility(View.GONE);
                        ImgQty.setVisibility(View.GONE);
                        ImgExpDate.setVisibility(View.GONE);
                        ImgRakSlot.setVisibility(View.GONE);
                    }else{
                        url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2cekrakslot.php?koderakslot="+InputRackSlot.getText().toString();
                        json = jParser.getJSONFromUrl(url);
                        try {
                            israkslot = json.getJSONArray(TAG_CARIRAKSLOT);
                            for (int i = 0; i < israkslot.length(); i++) {
                                JSONObject c = israkslot.getJSONObject(i);

                                // Storing each json item in variable
                                isRakSlot = c.getString("RackSlotCode");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if((isRakSlot.length()!=8)&&(isRakSlot.equals("0"))){
                            Toast.makeText(OperatorTaskRBPB_2.this,InputRackSlot.getText().toString()+" Rak Tidak Valid",Toast.LENGTH_SHORT).show();
                            ImgRakSlot.setVisibility(View.VISIBLE);
                            ImgKodeBrg.setVisibility(View.GONE);
                            ImgNamaBrg.setVisibility(View.GONE);
                            ImgQty.setVisibility(View.GONE);
                            ImgExpDate.setVisibility(View.GONE);
                            ImgBin.setVisibility(View.GONE);
                        }else{

                            ImgRakSlot.setVisibility(View.GONE);

                            jParser = new JSONParser();

                            int jumlah = Integer.parseInt(InputQty.getText().toString()) * RasioBrg;

                            url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2cekjumlah.php?barcode="+TxtKodeBrg.getText()+"&transactioncode="+TransactionCode+"&jumlahin="+jumlah+"&bincode="+InputBin.getText().toString();

                            json = jParser.getJSONFromUrl(url);

                            try {
                                isjumlahok = json.getJSONArray(TAG_CARICEKJUMLAH);
                                for (int i = 0; i < isjumlahok.length(); i++) {
                                    JSONObject d = isjumlahok.getJSONObject(i);
                                    StatusJumlah = d.getString("Status");
                                    JumlahOK = d.getString("Jumlah");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(StatusJumlah.equals("0")){
                                Toast.makeText(OperatorTaskRBPB_2.this,JumlahOK,Toast.LENGTH_LONG).show();
                                ImgQty.setVisibility(View.VISIBLE);
                            }else{
                                ImgQty.setVisibility(View.GONE);

                                jParser = new JSONParser();

                                url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2insert.php?transactioncode="+TransactionCode+"&bincode="+InputBin.getText().toString()+"&skucode="+TxtKodeBrg.getText()+"&expdate="+InputExpDate.getText().toString()+"&qty="+jumlah+"&currrackslot="+InputRackSlot.getText().toString()+"&destrackslot="+InputRackSlot.getText().toString()+"&receivesource="+getPref(TAG_RECEIVESOURCE)+"&operatorcode="+OperatorCode;

                                json = jParser.getJSONFromUrl(url);

                                try {
                                    isInsert = json.getJSONArray(TAG_STATUSINSERT);
                                    for (int i = 0; i < isInsert.length(); i++) {
                                        JSONObject d = isInsert.getJSONObject(i);
                                        StatusInsert = d.getString("Status");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if(StatusInsert.equals("0")){
                                    Toast.makeText(OperatorTaskRBPB_2.this,StatusInsert+" Insert Gagal",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(OperatorTaskRBPB_2.this,StatusJumlah+" Insert Berhasil",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(),OperatorTaskRBPB_2.class);
                                    intent.putExtra(TAG_PERUSAHAAN, in.getStringExtra(TAG_PERUSAHAAN));
                                    intent.putExtra(TAG_KODENOTA,in.getStringExtra(TAG_KODENOTA));
                                    intent.putExtra(TAG_TRANSACTIONCODE,in.getStringExtra(TAG_TRANSACTIONCODE));
                                    intent.putExtra(TAG_NOTE,in.getStringExtra(TAG_NOTE));
                                    intent.putExtra(TAG_OPERATOR,OperatorCode);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                }
            }
        });

        BtnCariManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDaftarBrgManual(InputKode.getText().toString(),TransactionCode);
            }
        });

        InputKode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Barcode = InputKode.getText().toString();
                Barcode = Barcode.trim();
                Barcode = Barcode.replace(" ", "");
                if ((Barcode.length() > 0)) {
                    // Creating JSON Parser instance
                    JSONParser jParser = new JSONParser();

                    // 4987176006783 10/00/R/000001";
                    String url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2caribarangbarcode.php?barcode=" + Barcode + "&transactioncode=" + TransactionCode;

                    // getting JSON string from URL
                    JSONObject json = jParser.getJSONFromUrl(url);

                    try {
                        barangbarcode = json.getJSONArray(TAG_CARIBARCODE);
                        for (int i = 0; i < barangbarcode.length(); i++) {
                            JSONObject c = barangbarcode.getJSONObject(i);
                            String kode = c.getString(TAG_KODE);
                            String keterangan = c.getString(TAG_KETERANGAN);

                            TxtKodeBrg.setText(kode);
                            TxtNamaBrg.setText(keterangan);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                getDataListSatuan();
            }
        });

        InputExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(OperatorTaskRBPB_2.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                InputExpDate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                dpd.show();
            }
        });
        addListenerOnSpinnerItemSelection();

        //Add listener to cek Rak
        InputRackSlot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                            TxtRackSlot.setText(c.getString("Name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    TxtRackSlot.setText("Rak Tidak Ada");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void getDataListSatuan(){
        if(!TxtKodeBrg.getText().toString().equals("0")){

            // Creating JSON Parser instance
            JSONParser jParser = new JSONParser();

            // 4987176006783 10/00/R/000001";
            String url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2getsatuan.php?kodebarang="+TxtKodeBrg.getText();

            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url);

            SatuanList.clear();

            try {
                // Getting Array of Satuan
                satuan = json.getJSONArray(TAG_CARISATUAN);
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

    // add items into spinner dynamically
    public void addItemsOnSpinner(List satuanList) {
        CmbSatuan = (Spinner) findViewById(R.id.OPTRBPB2_Satuan);
        List list = satuanList;
        ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CmbSatuan.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        CmbSatuan = (Spinner) findViewById(R.id.OPTRBPB2_Satuan);
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

    public void openDialogDaftarBrgManual(String cari, String trCode){
        if(cari.length()>0){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            // Creating JSON Parser instance
            JSONParser jParser = new JSONParser();

            // 4987176006783 10/00/R/000001";
            String url = "http://192.168.31.10:9020/ws/operatortaskreceivebpb2caribarangmanual.php?katakunci="+cari+"&transactioncode="+trCode;

            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url);

            List<String> listItems = new ArrayList<String>();
            try {
                // Getting Array of Satuan
                brg = json.getJSONArray(TAG_CARIBRG);
                // looping through All Barang
                for (int i = 0; i < brg.length(); i++) {
                    JSONObject c = brg.getJSONObject(i);
                    listItems.add(c.getString(TAG_KETERANGAN)+"--"+c.getString(TAG_KODE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

            alert.setTitle("Daftar Barang");
            alert.setItems(items,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String container = items[which].toString();
                    String kodeBrg = container.substring(container.indexOf("--")+2,container.length());
                    String NamaBrg = container.substring(0,container.indexOf("--"));

                    TxtKodeBrg.setText(kodeBrg);
                    TxtNamaBrg.setText(NamaBrg);

                    getDataListSatuan();

                    InputQty.requestFocus();
                }
            });

            alert.show();
        }else{
            Toast.makeText(OperatorTaskRBPB_2.this,"Inputan tidak boleh kosong",Toast.LENGTH_SHORT).show();
        }
    }

    public String getPref(String KEY){
        SharedPreferences LoginPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        String Value=LoginPref.getString(KEY,"0");
        return  Value;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent in = new Intent(getApplicationContext(), OperatorTaskRBPB_1.class);
            in.putExtra(TAG_OPERATOR, OperatorCode);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
        }
        return false;
    }
}