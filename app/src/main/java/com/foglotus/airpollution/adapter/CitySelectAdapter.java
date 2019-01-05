package com.foglotus.airpollution.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foglotus.airpollution.R;
import com.foglotus.airpollution.entity.City;

import java.util.ArrayList;

public class CitySelectAdapter extends BaseAdapter {
    private ArrayList<City> cityArrayList;
    private Context context;


    public CitySelectAdapter(ArrayList<City> cityArrayList, Context context) {
        this.cityArrayList = cityArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cityArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null;
        ViewHolder viewHolder = null;
        if(convertView!=null){
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }else{
            view = View.inflate(context, R.layout.select_city_item, null);
            viewHolder = new ViewHolder();
            viewHolder.city = view.findViewById(R.id.city_name);
            view.setTag(viewHolder);
        }
        viewHolder.city.setText(cityArrayList.get(position).getCityName());
        return view;
    }
    static class ViewHolder{
        TextView city;
    }
}
