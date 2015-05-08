package com.bcp.WMSAndroid;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
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


public class OperatorTask extends ListActivity {

    private final String TAG_WHROLECODE_1 = "10/WHR/001";
    private final String TAG_WHROLECODE_2 = "10/WHR/002";
    private final String TAG_WHROLECODE_3 = "10/WHR/003";
    private final String TAG_WHROLECODE_4 = "10/WHR/004";

    private final String TAG_PROJECTCODE_1 = "ABB";
    private final String TAG_PROJECTCODE_2 = "BPB";
    private final String TAG_PROJECTCODE_3 = "PCK";
    private final String TAG_PROJECTCODE_4 = "RJT";
    private final String TAG_PROJECTCODE_5 = "RPL";

    private final String TAG_OPERATOR = "OperatorCode";
    private String OperatorCode = "";
    private String WHRoleCode = "";
    private String WHRoleName = "";


    // url to make request
    private static String url = "http://192.168.31.10:9020/ws/operatortask.php?task=1";

    // JSON Node names
    private static final String TAG_OPERATORTASK= "operatortask";
    private static final String TAG_PROJECTCODE = "ProjectCode";
    private static final String TAG_WHROLECODE = "WHRoleCode";
    private static final String TAG_NAME = "Name";
    private static final String TAG_NAMELINKADDRESS = "NameLinkAddress";
    private static final String TAG_ICON = "1";

    // Cabang JSONArray
    JSONArray operatortask = null;

    int[] flags = new int[]{
            R.drawable.ic_launcher,
            R.drawable.ic_launcher2,
            R.drawable.ic_launcher,
    };

    int[] icon_peran = new int[]{
            R.drawable.wh_receiver,
            R.drawable.wh_mover,
            R.drawable.wh_picking,
            R.drawable.wh_shipping
    };

    TextView OPTNameTxt;
    ImageView OPTIconImg;

    public void showDialog(String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title != null)
            builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Admin", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getBaseContext(),"Admin",Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(),OperatorTaskMRPLADMIN_1.class);
                in.putExtra(TAG_WHROLECODE,WHRoleCode);
                in.putExtra(TAG_OPERATOR,OperatorCode);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        builder.setNegativeButton("Manual", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getBaseContext(),"Manual",Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatortask);

        Intent in = getIntent();
        WHRoleCode = in.getStringExtra(TAG_WHROLECODE);
        WHRoleName = in.getStringExtra(TAG_NAME);
        OperatorCode = in.getStringExtra(TAG_OPERATOR);

        OPTNameTxt = (TextView) findViewById(R.id.OPTName);
        OPTNameTxt.setText((WHRoleName+" TASK"));

        OPTIconImg = (ImageView) findViewById(R.id.OPT_Icon);

        if((WHRoleCode.equals(TAG_WHROLECODE_1))){
            OPTIconImg.setBackgroundResource(icon_peran[0]);
        }
        else if((WHRoleCode.equals(TAG_WHROLECODE_2))){
            OPTIconImg.setBackgroundResource(icon_peran[1]);
        }
        else if((WHRoleCode.equals(TAG_WHROLECODE_3))){
            OPTIconImg.setBackgroundResource(icon_peran[2]);
        }
        else{
            OPTIconImg.setBackgroundResource(icon_peran[3]);
        }

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> OperatorMenuList = new ArrayList<HashMap<String, String>>();

        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        url = "http://192.168.31.10:9020/ws/operatortask.php?task="+WHRoleCode;

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);


        try {
            // Getting Array of Barang
            operatortask = json.getJSONArray(TAG_OPERATORTASK);

            // looping through All Barang
            for(int i = 0; i < operatortask.length(); i++){
                JSONObject c = operatortask.getJSONObject(i);

                // Storing each json item in variable
                String whrolecode = c.getString(TAG_WHROLECODE);
                String namelinkaddress = c.getString(TAG_NAMELINKADDRESS);
                String projectcode = c.getString(TAG_PROJECTCODE);
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value

                if (i%2==0){
                    map.put(TAG_ICON,Integer.toString(flags[0]));
                }else{
                    map.put(TAG_ICON,Integer.toString(flags[1]));
                }

                map.put(TAG_PROJECTCODE,projectcode);
                map.put(TAG_WHROLECODE, whrolecode);
                map.put(TAG_NAMELINKADDRESS, namelinkaddress);

                OperatorMenuList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(this, OperatorMenuList,
                R.layout.listoperatortask,
                new String[] { TAG_ICON, TAG_NAMELINKADDRESS, TAG_PROJECTCODE },
                new int[] { R.id.imageViewOPT, R.id.namaOPT, R.id.ProjectCodeOPT});

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String projectcode = ((TextView) view.findViewById(R.id.ProjectCodeOPT)).getText().toString();

                /*
                *     TAG_WHROLECODE_1 = "10/WHR/001";
                *     TAG_WHROLECODE_2 = "10/WHR/002";
                *     TAG_WHROLECODE_3 = "10/WHR/003";
                *     TAG_WHROLECODE_4 = "10/WHR/004";
                *     TAG_PROJECTCODE_1 = "ABB";
                *     TAG_PROJECTCODE_2 = "BPB";
                *     TAG_PROJECTCODE_3 = "PCK";
                *     TAG_PROJECTCODE_4 = "RJT";
                *     TAG_PROJECTCODE_5 = "RPL";
                * */


                // Receive
                if((projectcode.equals(TAG_PROJECTCODE_2))&&(WHRoleCode.equals(TAG_WHROLECODE_1))){
                    Intent in = new Intent(getApplicationContext(),OperatorTaskRBPB_1.class);
                    in.putExtra(TAG_WHROLECODE,WHRoleCode);
                    in.putExtra(TAG_OPERATOR,OperatorCode);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }

                if((projectcode.equals(TAG_PROJECTCODE_4))&&(WHRoleCode.equals(TAG_WHROLECODE_1))){
                    Intent in = new Intent(getApplicationContext(),OperatorTaskRR_1.class);
                    in.putExtra(TAG_WHROLECODE,WHRoleCode);
                    in.putExtra(TAG_OPERATOR,OperatorCode);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }

                // Mover
                if((projectcode.equals(TAG_PROJECTCODE_1))&&(WHRoleCode.equals(TAG_WHROLECODE_2))){
                    Intent in = new Intent(getApplicationContext(),OperatorTaskMABB_1.class);
                    in.putExtra(TAG_WHROLECODE,WHRoleCode);
                    in.putExtra(TAG_OPERATOR,OperatorCode);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }

                if((projectcode.equals(TAG_PROJECTCODE_2))&&(WHRoleCode.equals(TAG_WHROLECODE_2))){
                    Intent in = new Intent(getApplicationContext(),OperatorTaskMBPB_1.class);
                    in.putExtra(TAG_WHROLECODE,WHRoleCode);
                    in.putExtra(TAG_OPERATOR,OperatorCode);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }

                if((projectcode.equals(TAG_PROJECTCODE_4))&&(WHRoleCode.equals(TAG_WHROLECODE_2))){
                    Intent in = new Intent(getApplicationContext(),OperatorTaskMRJT_1.class);
                    in.putExtra(TAG_WHROLECODE,WHRoleCode);
                    in.putExtra(TAG_OPERATOR,OperatorCode);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }

                if((projectcode.equals(TAG_PROJECTCODE_5))&&(WHRoleCode.equals(TAG_WHROLECODE_2))){
                    showDialog("Jenis Replenish","Pilih salah satu replenish");
                }

                //Picking

                if((projectcode.equals(TAG_PROJECTCODE_3))&&(WHRoleCode.equals(TAG_WHROLECODE_3))){
                    Intent in = new Intent(getApplicationContext(),OperatorTaskPPCK_1.class);
                    in.putExtra(TAG_WHROLECODE,WHRoleCode);
                    in.putExtra(TAG_OPERATOR,OperatorCode);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }

                //Shipping

                if((projectcode.equals(TAG_PROJECTCODE_3))&&(WHRoleCode.equals(TAG_WHROLECODE_4))){
                    Intent in = new Intent(getApplicationContext(),OperatorTaskSPCK_1.class);
                    in.putExtra(TAG_WHROLECODE,WHRoleCode);
                    in.putExtra(TAG_OPERATOR,OperatorCode);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }
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