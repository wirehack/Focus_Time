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
public class AppUsageListAdapter extends BaseAdapter {
    private Context context;
    private List<MyPackageInfo> piList;
    private LayoutInflater mInflater;

    public AppUsageListAdapter(Context context, List<MyPackageInfo> piList) {
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
            convertView = mInflater.inflate(R.layout.app_usage_list, null);
            viewHolder = new ViewHolder();
            viewHolder.ivShowIcon = (ImageView) convertView.findViewById(R.id.activity_main_adapter_iv_icon);
            viewHolder.tvShowLabel = (TextView) convertView.findViewById(R.id.activity_main_adapter_tv_label);
            viewHolder.tvShowTime = (TextView) convertView.findViewById(R.id.packageusagetime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivShowIcon.setBackground(piList.get(position).getPackageIcon());
        viewHolder.tvShowLabel.setText(piList.get(position).getPackageLabel());
        viewHolder.tvShowTime.setText(piList.get(position).getPackageUsageTimeString());
        return convertView;
    }

    class ViewHolder {
        public ImageView ivShowIcon;
        public TextView tvShowLabel;
        public TextView tvShowTime;
    }
}
