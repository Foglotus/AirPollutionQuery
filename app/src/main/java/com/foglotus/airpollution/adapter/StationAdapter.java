package com.foglotus.airpollution.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foglotus.airpollution.R;
import com.foglotus.airpollution.entity.CityAirQuality;

import java.util.ArrayList;

public class StationAdapter extends BaseAdapter {
    private ArrayList<CityAirQuality> qualityArrayList;
    private Context context;

    public StationAdapter(ArrayList<CityAirQuality> qualityArrayList, Context context) {
        this.qualityArrayList = qualityArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return qualityArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return qualityArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null;
        StationAdapter.ViewHolder viewHolder = null;
        if(convertView!=null){
            view = convertView;
            viewHolder = (StationAdapter.ViewHolder) view.getTag();
        }else{
            view = View.inflate(context, R.layout.station, null);
            viewHolder = new StationAdapter.ViewHolder();
            viewHolder.stationName = view.findViewById(R.id.station_name);
            viewHolder.stationAqi = view.findViewById(R.id.station_aqi);
            viewHolder.stationPollution = view.findViewById(R.id.station_pollution);
            view.setTag(viewHolder);
        }
        viewHolder.stationName.setText(qualityArrayList.get(position).getPositionName());
        viewHolder.stationAqi.setText(qualityArrayList.get(position).getAqi());
        viewHolder.stationPollution.setText(qualityArrayList.get(position).getPrimaryPollution());
        return view;
    }
    static class ViewHolder{
        TextView stationName;
        TextView stationAqi;
        TextView stationPollution;
    }
}
