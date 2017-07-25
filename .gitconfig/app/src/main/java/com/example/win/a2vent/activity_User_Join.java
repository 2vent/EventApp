package com.example.win.a2vent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.win.a2vent.databinding.ActivityUserJoinBinding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 회원가입 부분
 */

public class activity_User_Join extends AppCompatActivity {

    ActivityUserJoinBinding binding_userJoin;
    String sex, user_type;
    String id, pw, name, addr, birth, phone, accountnumber; // 회원가입 시 사용될 String 변수
    JoinDB joinDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_userJoin = DataBindingUtil.setContentView(this, R.layout.activity_user_join);

//        라디오버튼 체크리스너
        binding_userJoin.rGroupSex.setOnCheckedChangeListener
                (new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        switch (checkedId) {
                            case R.id.rBt_sex0:
                                sex = "0";
                                break;
                            case R.id.rBt_sex1:
                                sex = "1";
                                break;
                        }
                    }
                });
        binding_userJoin.rGroupType.setOnCheckedChangeListener
                (new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        switch (checkedId) {
                            case R.id.rBt_user0:
                                user_type = "1";
                                break;
                            case R.id.rBt_user1:
                                user_type = "2";
                                break;
                            case R.id.rBt_user2:
                                user_type = "2";
                                break;
                        }
                    }
                });

    }


    public void onClick_joinOK(View view) {
        try {
            id = binding_userJoin.eTextJoinId.getText().toString();
            pw = binding_userJoin.eTextJoinPw.getText().toString();
            name = binding_userJoin.eTextJoinName.getText().toString();
            addr = binding_userJoin.eTextJoinAddr.getText().toString();
            birth = binding_userJoin.eTextJoinBirth.getText().toString();
            phone = binding_userJoin.eTextJoinPhone.getText().toString();
            accountnumber = binding_userJoin.eTextJoinAccountnumber.getText().toString();
        } catch (NullPointerException e) {
            Log.e("Join Error : ", e.getMessage());
        }

        joinDB = new JoinDB();
        joinDB.execute(id, pw, name, addr, birth, sex, phone, user_type, accountnumber);
    } // 회원가입 버튼

    class JoinDB extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(activity_User_Join.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            String id = (String) params[0];
            String pw = (String) params[1];
            String name = (String) params[2];
            String addr = (String) params[3];
            String birth = (String) params[4];
            String sex = (String) params[5];
            String phone = (String) params[6];
            String user_type = (String) params[7];
            String account_number = (String) params[8];

            String serverURL = GlobalData.getURL() + "2ventRegister.php";
            String postParameters = "&id=" + id + "&pw=" + pw + "&name=" + name +
                    "&addr=" + addr + "&birthday=" + birth + "&sex=" + sex + "&phone=" + phone +
                    "&user_type=" + user_type + "&account_number=" + account_number;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("DB", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString();

            } catch (Exception e) {
                Log.d("DB", "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

//            2ventRegister.php의 echo(result)와 비교하여 회원가입 성공 및 실패
            if (result.equals("회원가입 성공!")) {
                Intent intent_Joindone = new Intent(activity_User_Join.this, activity_User_Login.class);
                startActivity(intent_Joindone);
            } else if (result.equals("아이디가 존재합니다")) {
                Toast.makeText(activity_User_Join.this, "아이디가 존재합니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity_User_Join.this, "Register Error", Toast.LENGTH_SHORT).show();
            }
            Log.d("DB", "POST response  - " + result);
        }

    } // 회원 DB입력 AsynTask

    @Override
    protected void onDestroy() {
        if (joinDB != null) {
            joinDB.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (joinDB != null) {
            joinDB.cancel(true);
        }
        super.onPause();
    }
}

