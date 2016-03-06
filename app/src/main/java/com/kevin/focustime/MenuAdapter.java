package com.kevin.focustime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lenovo on 2015-07-27.
 */
public class MenuAdapter extends ArrayAdapter<MainMenu> {
    private int resource;
    public MenuAdapter(Context context, int resource, List<MainMenu> objects) {
        super(context, resource, objects);
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainMenu mainMenu =getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resource, null);
        ImageView menuImage= (ImageView) view.findViewById(R.id.menu_image);
        TextView menuName= (TextView) view.findViewById(R.id.menu_name);
        //menuImage.setImageResource(mainMenu.getImageId());
        menuName.setText(mainMenu.getName());
        return view;
    }
}
