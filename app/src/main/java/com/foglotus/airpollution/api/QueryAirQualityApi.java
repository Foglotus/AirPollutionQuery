package com.foglotus.airpollution.api;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.foglotus.airpollution.entity.City;
import com.foglotus.airpollution.entity.CityAirQuality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QueryAirQualityApi {
    private Handler resultHandler;
    private CityAirQuality cityAirQuality;
    private City city;

    public QueryAirQualityApi(Handler resultHandler, City city) {
        this.resultHandler = resultHandler;
        this.city = city;
    }

    public Handler getResultHandler() {
        return resultHandler;
    }

    public void setResultHandler(Handler resultHandler) {
        this.resultHandler = resultHandler;
    }

    public CityAirQuality getCityQuality() {
        return cityAirQuality;
    }

    public void setCityQuality(CityAirQuality cityQuality) {
        this.cityAirQuality = cityQuality;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void search(){

        //开辟线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cityNamePinYin = city.getCityPinyin();
                if(cityNamePinYin == null || cityNamePinYin.isEmpty()){
                    sendHandlerMessage(0,"城市名不存在，请重新选择城市!",null);
                    return;
                }

                String api = "http://www.pm25.in/api/querys/only_aqi.json?city=[PINYIN]&token=5j1znBVAsnSf5xQyNQyq";
                String url = api.replace("[PINYIN]",cityNamePinYin);
                //处理数据

                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.connect();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        Log.e("TGA","奏劾里");
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        String line;
                        StringBuffer buffer = new StringBuffer();
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();
                        sendHandlerMessage(1,"查询成功!",buffer.toString());
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("TGA","111");
                sendHandlerMessage(0,"查询失败!","");
            }
        }).start();

    }

    /***
     * 发送消息
     * @param what 正常：1  异常：0
     * @param msg 提示信息
     * @param data 数据信息
     */
    private void sendHandlerMessage(int what,String msg,String data){
        Message message = new Message();
        Bundle dataBundle = new Bundle();

        //装载数据
        dataBundle.putString("msg",msg);
        dataBundle.putString("data",data);

        //装在信息
        message.what = what;
        message.setData(dataBundle);
        resultHandler.sendMessage(message);
    }
}
