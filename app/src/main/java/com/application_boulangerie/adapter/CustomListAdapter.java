package com.application_boulangerie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.application_boulangerie.R;
import com.application_boulangerie.data.Categorie;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    private List<Categorie> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context aContext,  List<Categorie> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_custom_list_item, null);
            holder = new ViewHolder();
            holder.categorieView = (TextView) convertView.findViewById(R.id.textView_categorie);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Categorie categorie = this.listData.get(position);
        holder.categorieView.setText(categorie.toString());

        return convertView;
    }

    static class ViewHolder {
        TextView categorieView;
    }


}
