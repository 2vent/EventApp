package com.example.win.a2vent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017-07-03.
 */

public class user_iteminfo extends AppCompatActivity {
    private TextView tv_name;
    private TextView tv_discount;
    private TextView tv_money;
    private TextView tv_start;
    private TextView tv_end;
    private TextView tv_people;
    private ImageView iv_image;
    private Button bt_do;
    private Button bt_back;

    private int event_type;

    private Bitmap mBitmap;

    private int event_number = 0;
    private String com_number;

    putEntry putEntry;
    getEventInfo getEventInfo;
    infoImageDown infoImageDown;


    //테스트 용 변수
    private String testid = "2";
    private String testname = "2";
    private String testaddr = "동대구로";
    private String testbirthday = "1993-02-14";
    private String testsex = "1";
    private String testphone = "01035208543";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        Intent intent = getIntent();
        event_number = intent.getIntExtra("event_number", 0);
        Log.i("이벤트 넘버", String.valueOf(event_number));

        tv_name = (TextView) findViewById(R.id.info_name);
        tv_discount = (TextView) findViewById(R.id.info_discount);
        tv_money = (TextView) findViewById(R.id.info_money);
        tv_start = (TextView) findViewById(R.id.info_start);
        tv_end = (TextView) findViewById(R.id.info_end);
        tv_people = (TextView) findViewById(R.id.info_people);
        iv_image = (ImageView) findViewById(R.id.info_image);
        bt_do = (Button) findViewById(R.id.info_do);
        bt_back = (Button) findViewById(R.id.info_back);

        ButtonListener buttonListener = new ButtonListener();

        bt_back.setOnClickListener(buttonListener);
        bt_do.setOnClickListener(buttonListener);


        getEventInfo = new getEventInfo();
        getEventInfo.execute(String.valueOf(event_number));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    //버튼 리스너
    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.info_back:
                    finish();
                    break;
                case R.id.info_do:
                    if (event_type == 0) {

                        putEntry = new putEntry();
                        Log.i("이벤트값", Integer.toString(event_number));
                        putEntry.execute(Integer.toString(event_number), testid, testname, testaddr,
                                testbirthday, testsex, testphone, Integer.toString(event_type), com_number);
                    } else {
                        Intent intent = new Intent(getBaseContext(), user_payment.class);
                        startActivity(intent);
                    }

                    break;
            }
        }
    }


    //이벤트 상세정보 받아오기
    private class getEventInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(String... args) {
            StringBuilder sb = null;
            try {


                URL url = new URL(GlobalData.getURL() + "2ventGetEventOnItemInfo.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF8"));
                writer.write("event_number=" + args[0]);
                writer.flush();
                writer.close();
                os.close();

                httpURLConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF8")); //캐릭터셋 설정

                sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.i("받은 json ",s);
            jsonParser(s);


        }

    }

    public void jsonParser(String s) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);

            JSONArray jsonArray = jsonObject.getJSONArray("eventInfo");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String event_name = item.getString("event_name");
                String event_URI = item.getString("event_URI");
                String event_price = item.getString("event_price");
                String event_dis_price = item.getString("event_dis_price");
                String event_people = item.getString("event_people");
                String event_startday = item.getString("event_startday");
                String event_endday = item.getString("event_endday");
                String event_starttime = item.getString("event_starttime");
                String event_endtime = item.getString("event_endtime");
//                String event_payment = item.getString("event_payment");
                com_number = item.getString("com_number");
                event_type = item.getInt("event_type");


                tv_name.setText(event_name);
                tv_money.setText(event_price);
                tv_discount.setText(event_dis_price);
                tv_start.setText(event_startday + " " + event_starttime);
                tv_end.setText(event_endday + " " + event_endtime);
                tv_people.setText(event_people);

                if (event_type == 0) {
                    bt_do.setText("응모하기");
                } else {
                    bt_do.setText("결제하기");
                }

                infoImageDown = new infoImageDown();
                infoImageDown.execute(event_URI);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //이미지 다운로드
    private class infoImageDown extends AsyncTask<String, String, Bitmap> {
        Bitmap mBitmap;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected Bitmap doInBackground(String... args) {
            InputStream inputStream = null;
            try {


                URL url = new URL(GlobalData.getURL() + "php1.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF8"));
                writer.write("event_URI=" + args[0]);
                writer.flush();
                writer.close();
                os.close();

                httpURLConnection.connect();


                mBitmap = BitmapFactory
                        .decodeStream(httpURLConnection.getInputStream());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mBitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if (image != null) {

                iv_image.setImageBitmap(image);
                iv_image.setAdjustViewBounds(true);

            }


        }
    }

    //응모형일때 이벤트 참여시 사용자 데이터 보냄
    private class putEntry extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(String... args) {
            StringBuilder sb = null;

            try {
                URL url = new URL(GlobalData.getURL() + "2ventAddEntry.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF8"));
                writer.write("&event_number=" + args[0] + "&id=" + args[1] + "&entry_name=" + args[2]
                        + "&entry_addr=" + args[3] + "&entry_birthday=" + args[4] + "&entry_sex="
                        + args[5] + "&entry_phone=" + args[6] + "&entry_type=" + args[7]
                        + "&com_number=" + args[8]);
                writer.flush();
                writer.close();
                os.close();

                httpURLConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF8")); //캐릭터셋 설정

                sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("echo", s);
            if (s.equals("응모 성공")) {
                Toast.makeText(user_iteminfo.this, "응모 완료", Toast.LENGTH_SHORT).show();
            } else if (s.equals("이미 응모함")) {
                Toast.makeText(user_iteminfo.this, "이미 응모한 이벤트입니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(user_iteminfo.this, "응모 실패", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onDestroy() {
        if (putEntry != null) {
            putEntry.cancel(true);
        } else if (infoImageDown != null) {
            infoImageDown.cancel(true);
        } else if (getEventInfo != null) {
            getEventInfo.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (putEntry != null) {
            putEntry.cancel(true);
        } else if (infoImageDown != null) {
            infoImageDown.cancel(true);
        } else if (getEventInfo != null) {
            getEventInfo.cancel(true);
        }
        super.onPause();
    }


}