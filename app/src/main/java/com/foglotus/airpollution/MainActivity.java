package com.foglotus.airpollution;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foglotus.airpollution.adapter.StationAdapter;
import com.foglotus.airpollution.api.QueryAirQualityApi;
import com.foglotus.airpollution.entity.City;
import com.foglotus.airpollution.entity.CityAirQuality;
import com.foglotus.airpollution.handler.CityInitResultHandler;
import com.foglotus.airpollution.handler.QueryResultHandler;
import com.foglotus.airpollution.utils.Loading;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends Activity {

    //关于界面
    private Button btnOk;
    private AlertDialog alertDialog;

    //必要成员数据
    private int REQUEST_ID = 1314;
    private static ArrayList<City> cityArrayList;//城市信息
    private static ArrayList<CityAirQuality> cityAirQualityArrayList;//城市空气质量
    private static Boolean cityDataReady;
    private Intent selectCityIntent;

    //主界面必要数据
    private String cityName;
    private String cityPinyin;

    //主界面元素成员
    private static TextView selectedCityName,primaryPollution,airQuality,airQualityNumber,pointTime;
    private static ListView station_list;
    private ImageButton refreshButton;
    private LinearLayout mainContent;

    //进度提示框
    private static Loading loading;

    //处理器
    private static CityInitResultHandler cityInitResultHandler;
    private static QueryResultHandler queryResultHandler;

    //成员初始化代码块
    static {
        cityArrayList = new ArrayList<>();
        cityAirQualityArrayList = new ArrayList<>();
        cityDataReady = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStatusBar();
        setContentView(R.layout.activity_main);

        initCity();

        initMainContent();

        prepareLastSelected();

        initQueryButton();
    }

    /**
     * 查询按钮初始化
     */

    public void initQueryButton(){
        //点击事件
        queryResultHandler = new QueryResultHandler(this);
        refreshButton = findViewById(R.id.img_btn_query);
        loading = new Loading(this,"查询中...");
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cityDataReady == false){
                    return;
                }
                loading.setTips("查询中");
                loading.start();
                QueryAirQualityApi airQualityApi = new QueryAirQualityApi(queryResultHandler,new City(cityPinyin,cityName));
                airQualityApi.search();
            }
        });
    }

    /***
     * 菜单创建
     * @param menu
     * @return boolean
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /***
     * 菜单点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_select_city:
                if(cityDataReady){
                    if(selectCityIntent == null){
                        selectCityIntent = new Intent(MainActivity.this,CitySelectActivity.class);
                        selectCityIntent.putExtra("cityArrayList",cityArrayList);
                    }
                    startActivityForResult(selectCityIntent,REQUEST_ID);
                }else{
                    Toast.makeText(MainActivity.this,R.string.tips_city_load_not_ready,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_about_me:
                if(alertDialog != null){
                    alertDialog.show();
                }else{
                    View about = getLayoutInflater().inflate(R.layout.about,null);
                    alertDialog = new AlertDialog.Builder(this).setView(about).create();
                    alertDialog.show();
                    btnOk = alertDialog.getWindow().findViewById(R.id.btn_accept);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setCancelable(false);
                }
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * 初始化主界面元素
     */
    public void initMainContent(){
        selectedCityName = findViewById(R.id.selected_city);
        primaryPollution = findViewById(R.id.value_main_pollution);
        pointTime = findViewById(R.id.value_time);
        airQuality = findViewById(R.id.value_air_quality);
        airQualityNumber = findViewById(R.id.value_air_quality_number);
        station_list = findViewById(R.id.station_pollution_list);
        mainContent = findViewById(R.id.main_content);
    }

    /**
     * 读取上一次选择城市数据
     */

    public void prepareLastSelected(){
        //读取保存的城市数据
        SharedPreferences sp = MainActivity.this.getPreferences(MainActivity.MODE_PRIVATE);
        selectedCityName.setText(sp.getString("city_name",getResources().getString(R.string.item_name_no_option)));

        cityPinyin = sp.getString("city_pinyin","");
        cityName = sp.getString("city_name",getResources().getString(R.string.item_name_no_option));

        if(!cityPinyin.isEmpty())cityDataReady = true;
    }

    /***
     * 初始化城市数据
     */
    private void initCity(){
        cityInitResultHandler = new CityInitResultHandler(MainActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStreamReader = getResources().openRawResource(R.raw.city_pinyin);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamReader));
                StringBuilder stringBuilder = new StringBuilder();
                char[] cityPinyin = new char[1024];
                int length = 0;
                try {
                    while((length = bufferedReader.read(cityPinyin,0,cityPinyin.length))>0){
                        stringBuilder.append(new String(cityPinyin,0,length));
                    }

                    String temp = stringBuilder.toString();
                    String[] cityTemp = temp.split("\n");
                    String[] cityData;
                    for (String str:cityTemp){
                        cityData = str.split(",");
                        cityArrayList.add(new City(cityData[0],cityData[1]));
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data",cityArrayList);
                    sendHandlerMessage(cityInitResultHandler,1,getResources().getString(R.string.tips_city_load_success),bundle);
                    cityDataReady = true;
                    return;
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                sendHandlerMessage(cityInitResultHandler,0,getResources().getString(R.string.tips_city_load_failed),new Bundle());
            }
        }).start();
    }

    /***
     * 多线程消息发送
     * @param handler 处理
     * @param what 状态信息
     * @param msg 提示消息
     * @param dataBundle 数据
     */
    public void sendHandlerMessage(Handler handler,int what ,String msg,Bundle dataBundle){

        Message message = new Message();
        dataBundle.putString("msg",msg);
        message.setData(dataBundle);
        message.what = what;
        handler.sendMessage(message);
    }

    /***
     * 选中城市后数据处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ID&&resultCode==RESULT_OK){
            Toast.makeText(MainActivity.this,data.getStringExtra("city_name"),Toast.LENGTH_SHORT).show();
            cityName = data.getStringExtra("city_name");
            cityPinyin = data.getStringExtra("city_pinyin");
            selectedCityName = findViewById(R.id.selected_city);
            selectedCityName.setText(cityName);
            SharedPreferences sp = this.getPreferences(MainActivity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("city_name", data.getStringExtra("city_name"));
            editor.putString("city_pinyin",data.getStringExtra("city_pinyin"));
            editor.commit();
        }
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

    /**
     * 清除过期查询数据
     */
    public void clearData(){
        cityAirQualityArrayList.clear();
    }

    /**
     * 添加空气质量数据
     * @param cityAirQuality
     */
    public void addCityAirQualityArrayList(CityAirQuality cityAirQuality){
        cityAirQualityArrayList.add(cityAirQuality);
    }

    /**
     * 展示查询的监测点空气质量数据
     */
    public void setStationAdapter(){
        station_list.setAdapter(new StationAdapter(cityAirQualityArrayList,this));
    }

    /**
     * 设置主监测点数据
     * @param cityAirQuality
     */
    public void setMainCityData(CityAirQuality cityAirQuality){
        pointTime.setText(cityAirQuality.getTimePoint());
        airQualityNumber.setText(cityAirQuality.getAqi());
        airQuality.setText(cityAirQuality.getQuality());
        primaryPollution.setText(cityAirQuality.getPrimaryPollution());
    }

    /**
     * 显示查询的数据
     */
    public void showMainContent(){
        mainContent.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭加载动画
     */
    public void closeLoading(){
        loading.close();
    }
}
