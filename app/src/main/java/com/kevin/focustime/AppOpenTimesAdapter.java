package com.kevin.focustime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppOpenTimesAdapter extends BaseAdapter {
    private Context context;
    private List<MyPackageInfo> piList;
    private LayoutInflater mInflater;

    public AppOpenTimesAdapter(Context context, List<MyPackageInfo> piList) {
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
            convertView = mInflater.inflate(R.layout.app_open_times_list, null);
            viewHolder = new ViewHolder();
            viewHolder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
            viewHolder.appName = (TextView) convertView.findViewById(R.id.app_name);
            viewHolder.appTimes = (TextView) convertView.findViewById(R.id.times);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.appIcon.setBackground(piList.get(position).getPackageIcon());
        viewHolder.appName.setText(piList.get(position).getPackageLabel());
        viewHolder.appTimes.setText(piList.get(position).getUsedTimes()+" times");
        return convertView;
    }

    class ViewHolder {
        public ImageView appIcon;
        public TextView appName;
        public TextView appTimes;
    }
}
