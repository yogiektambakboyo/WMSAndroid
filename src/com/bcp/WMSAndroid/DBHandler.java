package com.bcp.WMSAndroid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class DBHandler extends SQLiteOpenHelper {

    private final String TAG_OPERATORCODE = "OperatorCode";
    private final String TAG_WHROLECODE = "WHRoleCode";
    private final String TAG_NAME = "Name";
    private final String TAG_OPERATORMENU= "operatormenu";
    private final String TAG_STATUS = "status";

    public DBHandler(Context context,String DB_PATH,String DB_NAME) {
        super(context, DB_PATH+ File.separator +DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void CreateMenu(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS MainMenu");
        db.execSQL("CREATE TABLE IF NOT EXISTS MainMenu (OperatorCode TEXT,WHRoleCode TEXT,Name TEXT)");
    }

    public void InsertMenu(String OperatorCode,String WHRoleCode, String Name){
       SQLiteDatabase db = this.getWritableDatabase();
       db.execSQL("INSERT INTO MainMenu(OperatorCode,WHRoleCode,Name) VALUES('"+OperatorCode+"','"+WHRoleCode+"','"+Name+"')");
    }

    public JSONObject GetMenu(String OperatorCode) throws JSONException{
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="SELECT * FROM MainMenu WHERE OperatorCode='"+OperatorCode+"'";
        Cursor cursor = db.rawQuery(sql,null);

        JSONObject jResult = new JSONObject();
        JSONArray  jArray  = new JSONArray();

        if(cursor.moveToFirst()){
            jResult.put(TAG_STATUS,1);
            do {
                JSONObject JData = new JSONObject();
                JData.put(TAG_OPERATORCODE, cursor.getString(cursor.getColumnIndex(TAG_OPERATORCODE)));
                JData.put(TAG_WHROLECODE, cursor.getString(cursor.getColumnIndex(TAG_WHROLECODE)));
                JData.put(TAG_NAME, cursor.getString(cursor.getColumnIndex(TAG_NAME)));
                jArray.put(JData);
            }while (cursor.moveToNext());
            jResult.put(TAG_OPERATORMENU,jArray);
        }else{
            jResult.put(TAG_STATUS,0);
        }
        return jResult;
    }

}
