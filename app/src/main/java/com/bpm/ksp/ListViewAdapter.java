package com.bpm.ksp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public ListViewAdapter(Context context, int resourceId, List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView tanggal;
        TextView nominal;
        TextView angsuran;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.tanggal = (TextView) convertView.findViewById(R.id.tanggal);
            holder.nominal = (TextView) convertView.findViewById(R.id.nominal);
            holder.angsuran = (TextView) convertView.findViewById(R.id.angsuran);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tanggal.setText(rowItem.getTanggal());
        holder.nominal.setText(rowItem.getNominal());
        holder.angsuran.setText(rowItem.getAngsuran());

        return convertView;
    }
}
