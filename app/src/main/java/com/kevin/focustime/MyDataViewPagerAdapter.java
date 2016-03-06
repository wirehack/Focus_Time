package com.kevin.focustime;

import android.content.Context;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyDataViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<View> views;
    private List<String> titles;
    public MyDataViewPagerAdapter(Context context, List<View> views, List<String> titles) {
        this.context = context;
        this.views = views;
        this.titles=titles;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}