package com.example.win.a2vent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-07-03.
 */

public class user_info extends AppCompatActivity {
    private TextView et_name;
    private ImageView iv_mypic;
    private Button bt_back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);
        et_name=(TextView) findViewById(R.id.userinfo_name);
        iv_mypic=(ImageView)findViewById(R.id.userinfo_image);
        bt_back=(Button)findViewById(R.id.userinfo_back);


    }
}
