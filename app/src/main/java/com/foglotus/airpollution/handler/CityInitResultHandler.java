package com.foglotus.airpollution.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class CityInitResultHandler extends Handler {
    private Context context;

    public CityInitResultHandler(Context context) {
        this.context = context;
    }
    @Override
    public void handleMessage(Message msg) {
        if(msg.what == 1){
            Toast.makeText(context,msg.getData().getString("msg"),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,msg.getData().getString("msg"),Toast.LENGTH_SHORT).show();
        }
    }
}
