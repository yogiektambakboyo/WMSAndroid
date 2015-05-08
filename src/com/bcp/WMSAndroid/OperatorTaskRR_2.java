package com.bcp.WMSAndroid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ResourceAsColor")

public class OperatorTaskRR_2 extends ListActivity {
    private final String TAG_AKSI = "Action";
    private final String TAG_EXPDATE = "ExpDate";
    private final String TAG_QTY = "Qty";
    private final String TAG_BINCODE = "BinCode";
    private final String TAG_KETERANGAN = "Keterangan";
    private final String TAG_CARIBRG = "operatortaskreceivebpb2caribarangmanual";
    private final String TAG_KODE = "Kode";
    private final String TAG_TRANSACTIONCODE = "TransactionCode";
    private final String TAG_CARIBARCODE = "operatortaskreceivebpb2caribarangbarcode";
    private final String TAG_CARISATUAN = "operatortaskreceivebpb2getsatuan";
    private final String TAG_SATUAN = "Satuan";
    private final String TAG_RASIO = "Rasio";

    private final String TAG_RACKCODE = "RackCode";

    TextView TxtRack,TxtKodeBrg,TxtNamaBrg;
    EditText InputKode,InputQty,InputBin;
    ImageView SKUMsg,NamaBrgMsg,QtyMsg,ExpDateMsg,BinMsg;
    Spinner CmbSatuan;

    ImageView ImgKodeBrg,ImgNamaBrg,ImgQty,ImgExpDate,ImgBin;

    Button BtnTambahRetur,BtnSimpanRetur,BtnCariManual;

    EditText  InputExpDate;

    ListView lv;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> OperatorTaskRR2List = new ArrayList<HashMap<String, String>>();

    ArrayList<HashMap<String, String>> OperatorTaskRR2ListSatuan = new ArrayList<HashMap<String, String>>();

    ListAdapter adapter;

    HashMap<String, String> map;

    ScrollView ScrollList;


    JSONArray brg=null;
    JSONArray satuan=null;
    JSONArray  barangbarcode=null;
    String RackCode="";
    String Barcode="";
    String TransactionCode="";

    private int year;
    private int month;
    private int day;
    int RasioBrg=0;

    List SatuanList;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskreceiveretur2);

        SatuanList = new ArrayList();

        Intent in = getIntent();
        RackCode = in.getStringExtra(TAG_RACKCODE);
        TransactionCode = in.getStringExtra(TAG_TRANSACTIONCODE);

        TxtRack = (TextView) findViewById(R.id.OPTRR2_Rak);
        TxtRack.setText(in.getStringExtra(TAG_RACKCODE));

        TxtNamaBrg = (TextView) findViewById(R.id.OPTRR2_NamaBrg);
        TxtKodeBrg = (TextView) findViewById(R.id.OPTRR2_KodeBrg);

        ImgKodeBrg = (ImageView) findViewById(R.id.OPTRR2_KodeBrgMsg);
        ImgNamaBrg = (ImageView) findViewById(R.id.OPTRR2_NamaBrgMsg);
        ImgQty = (ImageView) findViewById(R.id.OPTRR2_QtyMsg);
        ImgExpDate = (ImageView) findViewById(R.id.OPTRR2_ExpDateMsg);
        ImgBin = (ImageView) findViewById(R.id.OPTRR2_BinMsg);

        ImgKodeBrg.setVisibility(View.GONE);
        ImgNamaBrg.setVisibility(View.GONE);
        ImgQty.setVisibility(View.GONE);
        ImgExpDate.setVisibility(View.GONE);
        ImgBin.setVisibility(View.GONE);

        InputQty = (EditText) findViewById(R.id.OPTRR2_Qty);
        InputBin = (EditText) findViewById(R.id.OPTRR2_Bin);
        InputExpDate = (EditText) findViewById(R.id.OPTRR2_Exp);
        InputExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(OperatorTaskRR_2.this,
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
        InputKode = (EditText) findViewById(R.id.OPTRR2_CariKode);
        InputKode.requestFocus();


        SKUMsg = (ImageView) findViewById(R.id.OPTRR2_KodeBrgMsg);
        NamaBrgMsg = (ImageView) findViewById(R.id.OPTRR2_NamaBrgMsg);
        QtyMsg = (ImageView) findViewById(R.id.OPTRR2_QtyMsg);
        ExpDateMsg = (ImageView) findViewById(R.id.OPTRR2_ExpDateMsg);
        BinMsg = (ImageView) findViewById(R.id.OPTRR2_BinMsg);

        ScrollList = (ScrollView) findViewById(R.id.ScrollList);


        // Hide it
        SKUMsg.setVisibility(View.GONE);
        NamaBrgMsg.setVisibility(View.GONE);
        QtyMsg.setVisibility(View.GONE);
        ExpDateMsg.setVisibility(View.GONE);
        BinMsg.setVisibility(View.GONE);

        // creating new HashMap
        //map = new HashMap<String, String>();
        //map.put(TAG_AKSI,"0");
        //map.put(TAG_EXPDATE,"0");
        //map.put(TAG_QTY,"0");
        //map.put(TAG_BINCODE,"0");
        //map.put(TAG_KETERANGAN,"Data Kosong");

        //OperatorTaskRR2List.add(map);

        /**
         * Updating parsed JSON data into ListView
         * */
        adapter = new SimpleAdapter(this, OperatorTaskRR2List,
                R.layout.listoperatortaskreceiveretur2,
                new String[] { TAG_AKSI, TAG_EXPDATE, TAG_QTY, TAG_BINCODE, TAG_KETERANGAN},
                new int[] { R.id.OPTRR2_ListAksi, R.id.OPTRR2_ListExpdate, R.id.OPTRR2_ListJumlah, R.id.OPTRR2_ListKodeBin, R.id.OPTRR2_ListNamaBrg });

        setListAdapter(adapter);


        // selecting single ListView item
        lv = getListView();
        setListViewHeightBasedOnChildren(lv);

                // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String keterangan = ((TextView) view.findViewById(R.id.OPTRR2_ListNamaBrg)).getText().toString();
                Toast.makeText(getApplicationContext(),keterangan,Toast.LENGTH_SHORT).show();

                view.setSelected(true);
                view.setBackgroundColor(Color.RED);
                //Intent in = new Intent(getApplicationContext(),OperatorTask.class);
                //in.putExtra(TAG_PERUSAHAAN,perusahaan);
                //in.putExtra(TAG_KODENOTA,kodenota);
                //in.putExtra(TAG_NOTE,note);
                //startActivity(in);

            }
        });


        BtnTambahRetur = (Button) findViewById(R.id.OPTRR2_Tambah);
        BtnTambahRetur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TxtKodeBrg.getText().toString().equals("0")){
                    ImgKodeBrg.setVisibility(View.VISIBLE);
                    ImgNamaBrg.setVisibility(View.GONE);
                    ImgQty.setVisibility(View.GONE);
                    ImgExpDate.setVisibility(View.GONE);
                    ImgBin.setVisibility(View.GONE);
                }else if(TxtNamaBrg.getText().toString().equals("0")){
                    ImgNamaBrg.setVisibility(View.VISIBLE);
                    ImgKodeBrg.setVisibility(View.GONE);
                    ImgQty.setVisibility(View.GONE);
                    ImgExpDate.setVisibility(View.GONE);
                    ImgBin.setVisibility(View.GONE);
                }else if((InputQty.getText().toString().length()==0)||(InputQty.getText().toString().equals("0"))){
                    InputQty.setText("0");
                    if(Integer.parseInt(InputQty.getText().toString())<=0){
                        ImgQty.setVisibility(View.VISIBLE);
                        ImgKodeBrg.setVisibility(View.GONE);
                        ImgNamaBrg.setVisibility(View.GONE);
                        ImgExpDate.setVisibility(View.GONE);
                        ImgBin.setVisibility(View.GONE);
                    }
                }else if(InputExpDate.getText().toString().length()==0){
                    ImgExpDate.setVisibility(View.VISIBLE);
                    ImgKodeBrg.setVisibility(View.GONE);
                    ImgNamaBrg.setVisibility(View.GONE);
                    ImgQty.setVisibility(View.GONE);
                    ImgBin.setVisibility(View.GONE);
                }else if(InputBin.getText().toString().length()!=7){
                    ImgBin.setVisibility(View.VISIBLE);
                    ImgKodeBrg.setVisibility(View.GONE);
                    ImgNamaBrg.setVisibility(View.GONE);
                    ImgQty.setVisibility(View.GONE);
                    ImgExpDate.setVisibility(View.GONE);
                }else if(TxtRack.getText().length()!=8){
                    ImgKodeBrg.setVisibility(View.GONE);
                    ImgNamaBrg.setVisibility(View.GONE);
                    ImgQty.setVisibility(View.GONE);
                    ImgExpDate.setVisibility(View.GONE);
                    ImgBin.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getApplicationContext(),"Updated "+OperatorTaskRR2List.size()+" -  "+ScrollList.getHeight(),Toast.LENGTH_SHORT).show();

                    // creating new HashMap
                    map = new HashMap<String, String>();
                    map.put(TAG_AKSI,"0");
                    map.put(TAG_EXPDATE,InputExpDate.getText().toString());
                    map.put(TAG_QTY,InputQty.getText().toString());
                    map.put(TAG_BINCODE,InputBin.getText().toString());
                    map.put(TAG_KETERANGAN,TxtNamaBrg.getText().toString());

                    OperatorTaskRR2List.add(map);

                    adapter = new SimpleAdapter(getApplicationContext(), OperatorTaskRR2List,
                            R.layout.listoperatortaskreceiveretur2,
                            new String[] { TAG_AKSI, TAG_EXPDATE, TAG_QTY, TAG_BINCODE, TAG_KETERANGAN},
                            new int[] { R.id.OPTRR2_ListAksi, R.id.OPTRR2_ListExpdate, R.id.OPTRR2_ListJumlah, R.id.OPTRR2_ListKodeBin, R.id.OPTRR2_ListNamaBrg });

                    setListAdapter(adapter);
                    setListViewHeightBasedOnChildren(lv);

                    ImgKodeBrg.setVisibility(View.GONE);
                    ImgNamaBrg.setVisibility(View.GONE);
                    ImgQty.setVisibility(View.GONE);
                    ImgExpDate.setVisibility(View.GONE);
                    ImgBin.setVisibility(View.GONE);

                    InputKode.setText("");
                    InputQty.setText("");
                    InputBin.setText("");
                    InputExpDate.setText("");

                    TxtNamaBrg.setText("0");
                    TxtKodeBrg.setText("0");
                    InputKode.requestFocus();
                }
            }
        });

        BtnSimpanRetur = (Button) findViewById(R.id.OPTRR2_Simpan);


        BtnCariManual = (Button) findViewById(R.id.OPTRR2_CariManual);

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

        addListenerOnSpinnerItemSelection();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
        listView.requestLayout();
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

                    OperatorTaskRR2ListSatuan.add(map);

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
        CmbSatuan = (Spinner) findViewById(R.id.OPTRR2_Satuan);
        List list = satuanList;
        ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CmbSatuan.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        CmbSatuan = (Spinner) findViewById(R.id.OPTRR2_Satuan);
        CmbSatuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RasioBrg = Integer.parseInt(OperatorTaskRR2ListSatuan.get(position).get(TAG_RASIO).toString());
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
            Toast.makeText(OperatorTaskRR_2.this,"Inputan tidak boleh kosong",Toast.LENGTH_SHORT).show();
        }
    }
}