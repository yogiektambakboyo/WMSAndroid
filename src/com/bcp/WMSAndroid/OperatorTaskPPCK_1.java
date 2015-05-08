package com.bcp.WMSAndroid;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class OperatorTaskPPCK_1 extends ListActivity {

    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";
    private String Status = "";

    // url to make request
    private static String url = "http://192.168.31.10:9020/ws/operatortaskpickingpck1.php?operatorcode=1";

    // JSON Node names
    private static final String TAG_OPERATORTASKPPCK1= "operatortaskpickingpck1";
    private static final String TAG_TRANSACTIONCODE = "TransactionCode";
    private static final String TAG_TRANSACTIONDATE = "TransactionDate";
    private static final String TAG_ERPCODE = "ERPCode";
    private static final String TAG_WHCODE = "WHCode";
    private static final String TAG_EDPANJANG = "EDPanjang";
    private static final String TAG_ASSIGNED = "Assigned";
    private static final String TAG_ICON = "1";

    private static final String TAG_DESTBIN = "DestBin";

    // Cabang JSONArray
    JSONArray operatortaskppck1 = null;
    JSONArray operatortaskppckBin= null;

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] flags = new int[]{
            R.drawable.ic_launcher,
            R.drawable.ic_launcher2,
            R.drawable.ic_launcher,
    };

    public Comparator<Map<String, String>> mapComparator = new Comparator<Map<String, String>>() {
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            return m1.get(TAG_TRANSACTIONCODE).compareTo(m2.get(TAG_TRANSACTIONCODE));
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortaskpickerpck1);

        Intent in = getIntent();
        OperatorCode = in.getStringExtra(TAG_OPERATOR);

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> OperatorTaskPPCK = new ArrayList<HashMap<String, String>>();

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        url = "http://192.168.31.10:9020/ws/operatortaskpickingpck1.php?operatorcode="+OperatorCode;

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);


        try {
            // Getting Array of Barang
            operatortaskppck1 = json.getJSONArray(TAG_OPERATORTASKPPCK1);

            // looping through All Barang
            for(int i = 0; i < operatortaskppck1.length(); i++){
                JSONObject c = operatortaskppck1.getJSONObject(i);

                // Storing each json item in variable
                String assigned = c.getString(TAG_ASSIGNED);
                String erpcode = c.getString(TAG_ERPCODE);
                String tgl = c.getString(TAG_TRANSACTIONDATE);
                String transcationcode = c.getString(TAG_TRANSACTIONCODE);
                String whcode = c.getString(TAG_WHCODE);
                String edpanjang = c.getString(TAG_EDPANJANG);
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value

                if (i%2==0){
                    map.put(TAG_ICON,Integer.toString(flags[0]));
                }else{
                    map.put(TAG_ICON,Integer.toString(flags[1]));
                }

                map.put(TAG_ASSIGNED, assigned);
                map.put(TAG_ERPCODE, erpcode);
                map.put(TAG_WHCODE, whcode);
                map.put(TAG_EDPANJANG, edpanjang);
                map.put(TAG_TRANSACTIONCODE, transcationcode);
                map.put(TAG_TRANSACTIONDATE, tgl);

                OperatorTaskPPCK.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(OperatorTaskPPCK, mapComparator);

        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(this, OperatorTaskPPCK,
                R.layout.listoperatortaskpickingpck1,
                new String[] { TAG_TRANSACTIONDATE, TAG_TRANSACTIONCODE, TAG_ERPCODE, TAG_WHCODE, TAG_EDPANJANG},
                new int[] { R.id.OPTPPCK1_tgl, R.id.OPTPPCK1_TransactionCode,R.id.OPTPPCK1_ERPCode,R.id.OPTPPCK1_WHCode,R.id.OPTPPCK1_EDPanjang});

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String kodenota = ((TextView) view.findViewById(R.id.OPTPPCK1_ERPCode)).getText().toString();
                String tgl = ((TextView) view.findViewById(R.id.OPTPPCK1_tgl)).getText().toString();
                String edpanjang = ((TextView) view.findViewById(R.id.OPTPPCK1_EDPanjang)).getText().toString();
                String whcode = ((TextView) view.findViewById(R.id.OPTPPCK1_WHCode)).getText().toString();
                String transactioncode = ((TextView) view.findViewById(R.id.OPTPPCK1_TransactionCode)).getText().toString();

                InputBin(kodenota,tgl,edpanjang,transactioncode,whcode);
            }
        });


    }

    public void InputBin(final String Kodenota,final String Tgl,final String EDPanjang,final String TransactionCode,final String WHCode){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Masukkan Bin Pallete Untuk Picking");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setSingleLine();
        input.setHint("Masukkan Bin");
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(7);
        input.setFilters(FilterArray);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(),"Bin Tidak Boleh Kosong!!!",Toast.LENGTH_SHORT).show();
                }else if(input.getText().toString().length()<7){
                    Toast.makeText(getApplicationContext(),"Bin Salah!!!",Toast.LENGTH_SHORT).show();
                }else{
                    // Creating JSON Parser instance
                    JSONParser jParser = new JSONParser();

                    url = "http://192.168.31.10:9020/ws/operatortaskpickingpck1cekbin.php?bincode="+input.getText().toString()+"&operatorcode="+OperatorCode+"&transactioncode="+TransactionCode;

                    // getting JSON string from URL
                    JSONObject json = jParser.getJSONFromUrl(url);


                    try {
                        // Getting Array of Barang
                        operatortaskppckBin = json.getJSONArray("operatortaskpickingpck1cekbin");

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
                        dialog.dismiss();
                        Intent in = new Intent(getApplicationContext(),OperatorTaskPPCK_2.class);
                        in.putExtra(TAG_ERPCODE,Kodenota);
                        in.putExtra(TAG_TRANSACTIONDATE, Tgl);
                        in.putExtra(TAG_EDPANJANG,EDPanjang);
                        in.putExtra(TAG_WHCODE,WHCode);
                        in.putExtra(TAG_TRANSACTIONCODE,TransactionCode);
                        in.putExtra(TAG_DESTBIN,input.getText().toString());
                        startActivity(in);
                    }else {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Bin Salah!!!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.show();
    }


}