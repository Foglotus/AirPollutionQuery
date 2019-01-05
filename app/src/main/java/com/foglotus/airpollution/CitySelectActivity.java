package com.foglotus.airpollution;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.foglotus.airpollution.adapter.CitySelectAdapter;
import com.foglotus.airpollution.entity.City;

import java.util.ArrayList;

public class CitySelectActivity extends Activity {
    private ArrayList<City> cityArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStatusBar();
        setContentView(R.layout.activity_city_select);
        //获取cityArrayList数据
        cityArrayList = (ArrayList<City>)getIntent().getExtras().getSerializable("cityArrayList");

        //设置适配器
        CitySelectAdapter cityAdapter = new CitySelectAdapter(cityArrayList,this);
        ListView listView = findViewById(R.id.city_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("city_pinyin",cityArrayList.get(position).getCityPinyin());
                intent.putExtra("city_name",cityArrayList.get(position).getCityName());
                setResult(RESULT_OK,intent);
                finish();
                return;
            }
        });

        listView.setAdapter(cityAdapter);
    }

    /***
     * actionbar 图标显示
     */
    private void showStatusBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setLogo(R.drawable.ic_action_name);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
    }
}
