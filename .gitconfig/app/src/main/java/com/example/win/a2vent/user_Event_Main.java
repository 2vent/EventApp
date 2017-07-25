package com.example.win.a2vent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.win.a2vent.databinding.UserEventMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 사용자 메인 화면
 * <p>
 * Android & PHP & MySQL 예제
 * http://webnautes.tistory.com/category/Android/DATABASE
 * 데이터 바인딩 라이브러리
 * https://developer.android.com/topic/libraries/data-binding/index.html?hl=ko
 */

public class user_Event_Main extends AppCompatActivity {
    private static String TAG = "JSON으로 데이터 가져오기";
    private static final String TAG_JSON = "Event";
    private static final String TAG_NAME = "event_name";
    private static final String TAG_TYPE = "event_type";
    private static final String TAG_URI = "event_URI";
    private static final String TAG_PRICE = "event_price";
    private static final String TAG_DISPRICE = "event_dis_price";
    private static final String TAG_STARTDAY = "event_startday";
    private static final String TAG_ENDDAY = "event_endday";

    static RecyclerView mRecyclerView; // 어댑터에서 쓸 인스턴스

    UserEventMainBinding binding_UserMain;
    Context mContext;
    String mJsonString;
    RecyclerView.Adapter rAdapter1, rAdapter2, rAdapter3, rAdapter4, rAdapter5;
    ArrayList category_All, category_Culture, category_Meal, category_Beauty, category_Fashion;
    getEventDB getEventDB;
    int event_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_UserMain = DataBindingUtil.setContentView(this, R.layout.user_event_main);
        set_TabHost(this);

        mContext = getApplicationContext();
        mRecyclerView = binding_UserMain.rviewContent1;

        getEventDB = new getEventDB();
        getEventDB.execute(GlobalData.getURL() + "2ventGetEventAll.php"); // AsyncTask 실행
    }

    public void onClick_Accountinfo(View v) {
        Toast.makeText(user_Event_Main.this, "이거 누르면 계정정보로", Toast.LENGTH_SHORT).show();
    }

    public void onClick_goMap(View v) {
        Intent intent = new Intent(this, user_map.class);
        startActivity(intent);
    }

    public void set_TabHost(Activity a) {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost_usermain);
        tabHost.setup();

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("전체");
        tabSpec1.setContent(R.id.content1);
        tabSpec1.setIndicator("전체");
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("문화");
        tabSpec2.setContent(R.id.content2);
        tabSpec2.setIndicator("문화");
        tabHost.addTab(tabSpec2);

        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("외식");
        tabSpec3.setContent(R.id.content3);
        tabSpec3.setIndicator("외식");
        tabHost.addTab(tabSpec3);

        TabHost.TabSpec tabSpec4 = tabHost.newTabSpec("뷰티");
        tabSpec4.setContent(R.id.content4);
        tabSpec4.setIndicator("뷰티");
        tabHost.addTab(tabSpec4);

        TabHost.TabSpec tabSpec5 = tabHost.newTabSpec("패션");
        tabSpec5.setContent(R.id.content5);
        tabSpec5.setIndicator("패션");
        tabHost.addTab(tabSpec5);

        // 탭 이동시 리스너
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId == "전체") {
                    rAdapter1.notifyDataSetChanged();
                } else if (tabId == "문화") {
                    rAdapter2.notifyDataSetChanged();
                } else if (tabId == "외식") {
                    rAdapter3.notifyDataSetChanged();
                } else if (tabId == "뷰티") {
                    rAdapter4.notifyDataSetChanged();
                } else if (tabId == "패션") {
                    rAdapter5.notifyDataSetChanged();
                }
            }
        });
    } // TabWidget 설정

    private class getEventDB extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(user_Event_Main.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "getEventDB Error : ", e);
                errorString = e.toString();

                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            if (result == null) {

            } else {
                mJsonString = result;
                addItemInCategory();
            }
        }

    } // EventDB 받는 AsyncTask

    private void addItemInCategory() {
        category_All = new ArrayList<>();
        category_Culture = new ArrayList<>();
        category_Meal = new ArrayList<>();
        category_Beauty = new ArrayList<>();
        category_Fashion = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String event_name = item.getString(TAG_NAME);
                event_type = item.getInt(TAG_TYPE);
                String event_URI = item.getString(TAG_URI);
                String event_price = item.getString(TAG_PRICE);
                String event_dis_price = item.getString(TAG_DISPRICE);
                String event_startday = item.getString(TAG_STARTDAY);
                String event_endday = item.getString(TAG_ENDDAY);

                // 전체 항목에는 모두 저장
                category_All.add(new user_Event_Item(event_name, event_URI,
                        event_price, event_dis_price, event_startday, event_endday));

                // 카테고리 분류해서 각각 저장
                if (event_type == 0) {
                    category_Culture.add(new user_Event_Item(event_name, event_URI,
                            event_price, event_dis_price, event_startday, event_endday));
                } else if (event_type == 1) {
                    category_Meal.add(new user_Event_Item(event_name, event_URI,
                            event_price, event_dis_price, event_startday, event_endday));
                } else if (event_type == 2) {
                    category_Beauty.add(new user_Event_Item(event_name, event_URI,
                            event_price, event_dis_price, event_startday, event_endday));
                } else if (event_type == 3) {
                    category_Fashion.add(new user_Event_Item(event_name, event_URI,
                            event_price, event_dis_price, event_startday, event_endday));
                }
            }

            rAdapter1 = new user_Event_Adapter(category_All, mContext);
            rAdapter2 = new user_Event_Adapter(category_Culture, mContext);
            rAdapter3 = new user_Event_Adapter(category_Meal, mContext);
            rAdapter4 = new user_Event_Adapter(category_Beauty, mContext);
            rAdapter5 = new user_Event_Adapter(category_Fashion, mContext);

            binding_UserMain.rviewContent1.setAdapter(rAdapter1);
            binding_UserMain.rviewContent2.setAdapter(rAdapter2);
            binding_UserMain.rviewContent3.setAdapter(rAdapter3);
            binding_UserMain.rviewContent4.setAdapter(rAdapter4);
            binding_UserMain.rviewContent5.setAdapter(rAdapter5);

            rAdapter1.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.d(TAG, "addItemInCategory Error : ", e);
        }
    } // JSON 데이터를 카테고리에 저장

    @Override
    protected void onDestroy() {
        if (getEventDB != null) {
            getEventDB.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (getEventDB != null) {
            getEventDB.cancel(true);
        }
        super.onPause();
    }

}
