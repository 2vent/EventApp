package com.example.win.a2vent;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.win.a2vent.databinding.OwnerEventMainBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static com.example.win.a2vent.activity_User_Login.savedID;

/**
 * Created by win on 2017-07-06.
 */

public class owner_Event_Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "owner_Event_Main";
    private static final String TAG_JSON = "Company";
    String mJsonString;
    private int nType = 1;

    Context mContext;
    FloatingActionButton fab;

    owner_companyDB_test owner_companyDB_test;
    OwnerEventMainBinding binding_OwnerMain;

    RecyclerView.Adapter com_rAdapter;
    ArrayList my_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_OwnerMain = DataBindingUtil.setContentView(this, R.layout.owner_event_main);

        Log.d("savedID : ",savedID);

        mContext = getApplicationContext();

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(owner_Event_Main.this, "Permission Denied\n"
                        + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this).setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA).check();

        Toolbar toolbar = (Toolbar) findViewById(R.id.owner_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nType == 1) {
                    Intent intent_eventform = new Intent(getBaseContext(), owner_Event_Form.class);
                    startActivity(intent_eventform);
                } else if (nType == 0) {
                    Intent intent_addstore = new Intent(getBaseContext(), owner_AddStore.class);
                    startActivity(intent_addstore);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_company) {
            nType = 0;
            owner_companyDB_test = new owner_companyDB_test();
            owner_companyDB_test.execute(GlobalData.getURL() + "2ventGetCompany.php");

        } else if (id == R.id.on_event) {
            nType = 1;

        } else if (id == R.id.temp_event) {
            nType = 1;

        } else if (id == R.id.end_event) {
            nType = 1;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class owner_companyDB_test extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(owner_Event_Main.this,
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
                Log.d(TAG, "owner_companyDB_test Error : ", e);
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
        my_company = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String com_number = item.getString("com_number");
                String com_name = item.getString("com_name");
                String com_addr = item.getString("com_addr");
                String com_category = item.getString("com_category");
                String com_manager = item.getString("com_manager");
                String com_URI = item.getString("com_URI");
                String id = item.getString("id");

                Log.d(savedID,id);

                if (savedID.equals(id)) { // equals 객체의 내용 자체를 비교하지만 == 연산자는 대상의 주소값을 비교한다
                    if (com_category.equals("0")) {
                        my_company.add(new owner_Addstore_Item(com_number, com_name, com_addr,
                                com_manager, "문화", com_URI, id));
                    } else if (com_category.equals("1")) {
                        my_company.add(new owner_Addstore_Item(com_number, com_name, com_addr,
                                com_manager, "외식", com_URI, id));
                    } else if (com_category.equals("2")) {
                        my_company.add(new owner_Addstore_Item(com_number, com_name, com_addr,
                                com_manager, "뷰티", com_URI, id));
                    } else if (com_category.equals("3")) {
                        my_company.add(new owner_Addstore_Item(com_number, com_name, com_addr,
                                com_manager, "패션", com_URI, id));
                    }
                }

            }

            com_rAdapter = new owner_Addstore_Adapter(my_company, mContext);

            binding_OwnerMain.eventOwnerMain.setAdapter(com_rAdapter);

            com_rAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.d(TAG, "addItemInCategory Error : ", e);
        }
    } // JSON 데이터를 카테고리에 저장

    @Override
    protected void onDestroy() {
        if (owner_companyDB_test != null) {
            owner_companyDB_test.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (owner_companyDB_test != null) {
            owner_companyDB_test.cancel(true);
        }
        super.onPause();
    }
}
