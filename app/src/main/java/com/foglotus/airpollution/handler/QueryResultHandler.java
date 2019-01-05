package com.foglotus.airpollution.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.foglotus.airpollution.MainActivity;
import com.foglotus.airpollution.entity.CityAirQuality;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryResultHandler extends Handler{

    private Context context;

    public QueryResultHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        MainActivity mainActivity = (MainActivity)context;
        mainActivity.closeLoading();
        if(msg.what == 1){
            Bundle bundle = msg.getData();
            Toast.makeText(context,bundle.getString("msg"),Toast.LENGTH_SHORT).show();
            String jsonString = bundle.getString("data","");
            if(!jsonString.isEmpty()){
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    JSONObject jsonObject = null;
                    mainActivity.clearData();
                    for(int i =0;i<jsonArray.length();i++) {
                        //int aqi, String area, String primaryPollutant, String quality, String stationCode, String timePoint, String pinYin
                        jsonObject = jsonArray.getJSONObject(i);
                        int aqi = jsonObject.getInt("aqi");
                        String area = jsonObject.getString("area");
                        String primaryPollution = jsonObject.getString("primary_pollutant");
                        String positionName = jsonObject.getString("position_name");
                        String quality =jsonObject.getString("quality");;
                        String stationCode =jsonObject.getString("station_code");
                        String timePoint = jsonObject.getString("time_point");
                        if(stationCode.equals("null")){
                            mainActivity.setMainCityData(new CityAirQuality(aqi, area, primaryPollution, positionName, quality, stationCode, timePoint));

                        }else {
                           mainActivity.addCityAirQualityArrayList(new CityAirQuality(aqi, area, primaryPollution, positionName, quality, stationCode, timePoint));
                        }
                    }
                    mainActivity.setStationAdapter();
                    mainActivity.showMainContent();
                }catch (JSONException e){

                }
            }

        }else{
            Toast.makeText(context,msg.getData().getString("msg"),Toast.LENGTH_SHORT).show();
        }
    }
}