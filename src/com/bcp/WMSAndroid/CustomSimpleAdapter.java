package com.bcp.WMSAndroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;


public class CustomSimpleAdapter extends SimpleAdapter {

    private List<Map<String, String>> itemList;
    private Context mContext;
    private final String TAG_COLOR = "color";
    private final String TAG_WHROLECODE = "WHRoleCode";
    private final String TAG_NAME = "Name";
    private final String TAG_ICON = "1";

    public CustomSimpleAdapter(Context context, List<? extends Map<String, ?>> data,
                               int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        this.itemList = (List<Map<String, String>>) data;
        this.mContext = context;
    }

    /* A Static class for holding the elements of each List View Item
     * This is created as per Google UI Guideline for faster performance */
    class ViewHolder {
        TextView ListHeader;
        TextView ListDesc;
        LinearLayout ListBG;
        ImageView Listicon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //View v = super.getView(position, convertView,   parent);
        //if the position exists in that list the you must set the background to BLUE
        //v.setBackgroundColor(Color.BLUE);

        ViewHolder holder = null;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listoperatormenu, null);
            holder = new ViewHolder();

            // get the textview's from the convertView
            holder.ListHeader = (TextView) convertView.findViewById(R.id.Keterangan);
            holder.ListDesc = (TextView) convertView.findViewById(R.id.WHRoleCode);
            holder.ListBG = (LinearLayout) convertView.findViewById(R.id.ListLayout);
            holder.Listicon = (ImageView) convertView.findViewById(R.id.imageViewOP);

            // store it in a Tag as its the first time this view is generated
            convertView.setTag(holder);
        } else {
            /* get the View from the existing Tag */
            holder = (ViewHolder) convertView.getTag();
        }

        /* update the textView's text and color of list item */
        holder.ListHeader.setText(itemList.get(position).get(TAG_NAME));
        holder.ListDesc.setText(itemList.get(position).get(TAG_WHROLECODE));
        holder.Listicon.setBackgroundResource(Integer.parseInt(itemList.get(position).get(TAG_ICON)));

        if((itemList.get(position).get(TAG_COLOR)).equals("0")){
            holder.ListBG.setBackgroundResource(R.drawable.wh_00);
        }else if((itemList.get(position).get(TAG_COLOR)).equals("1")){
            holder.ListBG.setBackgroundResource(R.drawable.wh_01);
        }else if((itemList.get(position).get(TAG_COLOR)).equals("2")){
            holder.ListBG.setBackgroundResource(R.drawable.wh_02);
        }else{
            holder.ListBG.setBackgroundResource(R.drawable.wh_03);
        }
        return convertView;
    }

}