package com.pl3x.rominstaller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater=null;
    
    public ListAdapter(Activity a, String[] d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public static class ViewHolder{
        public TextView text;
        public ImageView icon;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;
        if(convertView==null){
            vi = inflater.inflate(R.layout.item, null);
            holder=new ViewHolder();
            holder.text = (TextView)vi.findViewById(R.id.dirtext);
            holder.icon = (ImageView)vi.findViewById(R.id.dirimage);
            vi.setTag(holder);
        } else {
            holder=(ViewHolder)vi.getTag();
        }

        // Strip out directories, just list the filename;
        String data_filename = null;
        if (data[position] == "/") {
            data_filename = "/";
        } else {
            String[] r = data[position].split("/");
            data_filename = (r[r.length - 1] == null) ? r[r.length - 2] : r[r.length - 1];
        }

        // Remove folder icon if ZIP file
        if (Helper.isZip(data[position])) {
            holder.icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_zip));
        } else {
            holder.icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_folder));
        }

        holder.text.setText(data_filename);
        return vi;
    }
}