package com.example.win.a2vent;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

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
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017-07-03.
 */

public class user_map extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener {

    private int selectedNumberinMarker = 0;

    private getMapData getMapData;
    private MapView mapView;
    private RelativeLayout simple_info;
    private FloatingActionButton map_FAB;
    private JSONObject data = null;
    private Geocoder geocoder = null;
    private List<Address> list = null;

    private TextView tv_price = null;
    private TextView tv_discount = null;
    private ImageView iv_image = null;

    private LocationListener locationListener = null;
    private LocationManager locationManager = null;

    private Double myLatitude = 37.0;
    private Double myLongitude = 127.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        //권한 부여
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(user_map.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this).setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE).check();

        mapView = new MapView(this);
        mapView.setDaumMapApiKey("65414811e931909ad72eda29477bcf4f");
        RelativeLayout container = (RelativeLayout) findViewById(R.id.map_view);

        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setCurrentLocationTrackingMode
                (MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
        mapView.setCurrentLocationEventListener(this);

        // 구현한 CalloutBalloonAdapter 등록
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        container.addView(mapView);

        simpleinfo_listener simpleinfo_listener = new simpleinfo_listener();

        simple_info = (RelativeLayout) findViewById(R.id.llTestView);
        simple_info.setOnClickListener(simpleinfo_listener);
        simple_info.setVisibility(View.GONE);

        map_FAB = (FloatingActionButton) findViewById(R.id.map_FAB);
        map_FAB.setOnClickListener(simpleinfo_listener);
        map_FAB.bringToFront();


        //로케이션 매니저 받아오기
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        //리스너생성
//        createLocationListener();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, locationListener);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10,1,locationListener);

        tv_discount = (TextView) findViewById(R.id.simpleinfo_discount);
        tv_price = (TextView) findViewById(R.id.simpleinfo_price);
        iv_image = (ImageView) findViewById(R.id.simpleinfo_image);

    }

    // CalloutBalloonAdapter 인터페이스 구현
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
//            ((ImageView) mCalloutBalloon.findViewById(R.id.badge)).setImageResource(R.drawable.ic_launcher);

            ((TextView) mCalloutBalloon.findViewById(R.id.balloon_title)).setText(poiItem.getItemName());
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onMapViewInitialized(MapView mapView) {
        getMapData = new getMapData();
        getMapData.execute();
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        setFadeOutDownAnimation();
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        slideViewUpdate((int) mapPOIItem.getUserObject());
        selectedNumberinMarker = (int) mapPOIItem.getUserObject();

        setFadeInUpAnimation();
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView,
                                                 MapPOIItem mapPOIItem,
                                                 MapPOIItem.CalloutBalloonButtonType cBalloonBType) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }

    //애니메이션
    private void setFadeInUpAnimation() {
        simple_info.setVisibility(View.VISIBLE);
        Animation animFadeInUp = AnimationUtils.loadAnimation
                (getApplicationContext(), R.anim.anim_fade_in_up);
        simple_info.setAnimation(animFadeInUp);
        simple_info.bringToFront();

    }

    private void setFadeOutDownAnimation() {
        Animation animFadeOutDown = AnimationUtils.loadAnimation
                (getApplicationContext(), R.anim.anim_fade_out_down);
        simple_info.setAnimation(animFadeOutDown);
        simple_info.setVisibility(View.GONE);
        mapView.bringToFront();
        map_FAB.bringToFront();

    }

    // 하단뷰 플로팅버튼 리스너
    class simpleinfo_listener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.llTestView:
                    // 하단뷰가 보일때만
                    if (simple_info.getVisibility() == View.VISIBLE) {
                        intent = new Intent(getBaseContext(), user_iteminfo.class);
                        intent.putExtra("event_number", selectedNumberinMarker);
                        startActivity(intent);
                    }

                    break;
                case R.id.map_FAB:
                    intent = new Intent(user_map.this, user_Event_Main.class);
                    startActivity(intent);
            }

        }
    }

    //마커에 표시할 데이터를 다운받음
    private class getMapData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(String... args) {
            InputStream inputStream = null;
            StringBuilder sb = null;
            try {
                URL url = new URL(GlobalData.getURL() + "2ventGetEventOnMap.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF8"));
                writer.write("");
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
            try {
                data = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonParser(s);


        }

    }

    //마커찍기
    public void makeMarker(String addr, String com_name, MapView mapView, int event_no) {
        double latitude;
        double longitude;
        geocoder = new Geocoder(this);
        try {
            list = geocoder.getFromLocationName(addr, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latitude = list.get(0).getLatitude();
        longitude = list.get(0).getLongitude();

        MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(latitude, longitude);

        MapPOIItem mDefaultMarker = new MapPOIItem();
        mDefaultMarker.setItemName(com_name);
        mDefaultMarker.setTag(1);
        mDefaultMarker.setMapPoint(DEFAULT_MARKER_POINT);

        mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        mDefaultMarker.setCustomImageResourceId(R.drawable.map_pin);
        mDefaultMarker.setCustomImageAutoscale(true);
        mDefaultMarker.setCustomImageAnchor(0.5f, 1.0f);
        mDefaultMarker.setUserObject(event_no);

        Log.i("거리", String.valueOf(distanceCheck(latitude, longitude)));
        if (distanceCheck(latitude, longitude) < 2000) {
            mapView.addPOIItem(mDefaultMarker);
        }


//        mapView.selectPOIItem(mDefaultMarker, true);
//        mapView.setMapCenterPoint(DEFAULT_MARKER_POINT, false);
    }

    // json 객체 값 추출
    public void jsonParser(String s) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("eventInfo");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String com_addr = item.getString("com_addr");
                String com_name = item.getString("com_name");
                int event_no = item.getInt("event_number");
                Log.i("가게이름", com_name);
                makeMarker(com_addr, com_name, mapView, event_no);
            }
            mapView.fitMapViewAreaToShowAllPOIItems();   //추가된 마커가 모두보이게 조절
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 하단 view 업데이트
    public void slideViewUpdate(int event_number) {
        JSONObject jsonObject = null;
        try {
            jsonObject = data;
            JSONArray jsonArray = jsonObject.getJSONArray("eventInfo");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                if (event_number == item.getInt("event_number")) {
                    String price = item.getString("event_price");
                    String discount = item.getString("event_dis_price");
                    String imageURI = item.getString("event_URI");

                    tv_price.setText(price);
                    tv_discount.setText(discount);
                    LoadImage loadImage = new LoadImage();
                    loadImage.execute(imageURI);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //사진 다운로드
    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        Bitmap mBitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... args) {
            InputStream inputStream = null;
            try {
                URL url = new URL("http://192.168.0.106/EventApp/php1.php");
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

            }
        }

    }

    //로케이션 리스너 생성
    public void createLocationListener() {
        locationListener = new LocationListener() {
            //로케이션 변동시 이벤트 발생
            @Override
            public void onLocationChanged(Location location) {

                myLongitude = location.getLongitude(); //경도
                myLatitude = location.getLatitude();   //위도
                Log.i("mylati", String.valueOf(myLatitude));
                Log.i("mylongi", String.valueOf(myLongitude));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    //현재 위치값 받아오기
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        Log.i("좌표", mapPoint.getMapPointGeoCoord().latitude + " " + mapPoint.getMapPointGeoCoord().longitude);
        myLatitude = mapPoint.getMapPointGeoCoord().latitude;
        myLongitude = mapPoint.getMapPointGeoCoord().longitude;
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
    }

    //현재 자기 위치와 매장과의 거리 계산
    public double distanceCheck(double latitude, double longitude) {
        LatLng to = new LatLng(latitude, longitude);

//        while(myLongitude==null&&myLatitude==null){
//            Log.i("경도 위도",String.valueOf(myLatitude) + " "+String.valueOf(myLongitude));
//        }
//        LatLng from = new LatLng(myLatitude,myLongitude);
        LatLng from = new LatLng(latitude, longitude);

        return SphericalUtil.computeDistanceBetween(to, from);
    }

    @Override
    protected void onDestroy() {
        if (getMapData != null) {
            getMapData.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (getMapData != null) {
            getMapData.cancel(true);
        }
        super.onPause();
    }

}