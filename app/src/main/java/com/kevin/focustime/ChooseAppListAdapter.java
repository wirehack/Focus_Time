package com.kevin.focustime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lenovo on 2015-08-17.
 */
public class ChooseAppListAdapter extends BaseAdapter {
    private Context context;
    private List<MyPackageInfo> piList;
    private LayoutInflater mInflater;
    private static HashMap<Integer, Boolean> isSelected;
    public ChooseAppListAdapter(Context context, List<MyPackageInfo> piList)
    {
        this.context = context;
        this.piList = piList;
        this.mInflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }

    private void initDate() {
        for (int i = 0; i < piList.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount()
    {
        return piList.size();
    }


    @Override
    public Object getItem(int position)
    {
        return piList.get(position);
    }


    @Override

    public long getItemId(int position)
    {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.choose_app_list, null);
            viewHolder = new ViewHolder();
            viewHolder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
            viewHolder.appName = (TextView) convertView.findViewById(R.id.app_name);
            viewHolder.checkBox= (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.appIcon.setBackground(piList.get(position).getPackageIcon());
        viewHolder.appName.setText(piList.get(position).getPackageLabel());
        viewHolder.checkBox.setChecked(getIsSelected().get(position));
        return convertView;
    }
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        ChooseAppListAdapter.isSelected = isSelected;
    }

    class ViewHolder
    {
        public ImageView appIcon;
        public TextView appName;
        public CheckBox checkBox;
    }
}
