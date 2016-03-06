package com.kevin.focustime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lenovo on 2015-08-17.
 */
public class AppListInAlertDialogAdapter extends BaseAdapter {
    Context context;
    List<MyPackageInfo> piList;
    LayoutInflater mInflater;

    public AppListInAlertDialogAdapter(Context context, List<MyPackageInfo> piList) {
        this.context = context;
        this.piList = piList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return piList.size();
    }

    @Override
    public Object getItem(int position) {
        return piList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.app_list_in_alertdialog, null);
            viewHolder = new ViewHolder();
            viewHolder.ivShowIcon = (ImageView) convertView.findViewById(R.id.app_icon);
            viewHolder.tvShowLabel = (TextView) convertView.findViewById(R.id.app_name);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivShowIcon.setBackground(piList.get(position).getPackageIcon());
        viewHolder.tvShowLabel.setText(piList.get(position).getPackageLabel());
        return convertView;

    }
    class ViewHolder
    {
        public ImageView ivShowIcon;
        public TextView tvShowLabel;
    }
}
