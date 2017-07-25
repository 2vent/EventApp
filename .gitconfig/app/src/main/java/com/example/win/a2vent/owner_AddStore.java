package com.example.win.a2vent;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class owner_AddStore extends AppCompatActivity implements View.OnClickListener {

    TextView v_com_name;
    TextView v_com_addr;
    RadioButton v_com_radio_culture;
    RadioButton v_com_radio_food;
    RadioButton v_com_radio_beauty;
    RadioButton v_com_radio_fashion;
    RadioButton v_com_radio_travel;
    Spinner v_com_manager;
    TextView v_com_number;
    URI v_com_URI;
    ImageView v_com_iv;
    Button v_com_button;
    RadioGroup v_com_radio;
    Button btn_search_addr;
    TextView v_com_addr2;


    String com_name = null;
    String com_addr = null;
    String com_category;
    String com_manager = null;
    String com_URI = null;
    //유저목록 불러와야됨
    //이미지 삽입 해야됨
    String ID;
    String com_number;
    ArrayList<String> managerList = null;

    //주소 검색
    WebView webview;
    private Handler handler;


    //사진 관련
    Button btn_gall;
    Button btn_picture;
    Uri photoUri;
    String file_name;
    String file_dir;
    String[] city = null;
    private static final int PICK_FROM_CAMERA = 1; //카메라 촬영으로 사진 가져오기
    private static final int PICK_FROM_ALBUM = 2; //앨범에서 사진 가져오기
    private static final int CROP_FROM_CAMERA = 3; //가져온 사진을 자르기 위한 변수
    private static final int SEARCH_ADDR = 4; //가져온 사진을 자르기 위한 변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_addstore);

        v_com_name = (TextView) findViewById(R.id.com_form_name);
        v_com_addr = (TextView) findViewById(R.id.com_form_addr);
        v_com_addr2 = (TextView) findViewById(R.id.com_form_addr2);
        v_com_radio_culture = (RadioButton) findViewById(R.id.com_form_radio_culture);
        v_com_radio_food = (RadioButton) findViewById(R.id.com_form_radio_meal);
        v_com_radio_beauty = (RadioButton) findViewById(R.id.com_form_radio_beauty);
        v_com_radio_fashion = (RadioButton) findViewById(R.id.com_form_radio_fashion);
        v_com_radio_travel = (RadioButton) findViewById(R.id.com_form_radio_meal);
        v_com_manager = (Spinner) findViewById(R.id.com_form_manager);
        v_com_number = (TextView) findViewById(R.id.com_form_number);
        v_com_radio = (RadioGroup) findViewById(R.id.com_form_radio);
        btn_gall = (Button) findViewById(R.id.com_btn_gall);
        btn_picture = (Button) findViewById(R.id.com_btn_picture);
        btn_search_addr = (Button) findViewById(R.id.com_btn_search_addr);


//        v_com_URI
        v_com_iv = (ImageView) findViewById(R.id.com_form_iv);
        v_com_button = (Button) findViewById(R.id.com_button);

        clicked clicked = new clicked();
        v_com_button.setOnClickListener(clicked);

        v_com_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.com_form_radio_culture:
                        com_category = "0";
                        break;
                    case R.id.com_form_radio_meal:
                        com_category = "1";
                        break;
                    case R.id.com_form_radio_beauty:
                        com_category = "2";
                        break;
                    case R.id.com_form_radio_fashion:
                        com_category = "3";
                        break;
                }
            }
        });

        btn_gall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAlbum();
            }
        });

        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        btn_search_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(owner_AddStore.this, owner_AddStore_webView.class);
                startActivityForResult(i, SEARCH_ADDR);
//                // WebView 초기화
//                init_webView();
//
//                // 핸들러를 통한 JavaScript 이벤트 반응
//                handler = new Handler();
            }
        });


        managerList = new ArrayList<String>();


//        Log.e("시티",city.toString());
        getManager getManager = new getManager();
        getManager.execute();


//        Thread workingTread = new Thread(){
//            public void run(){
//
//
//
//                }
//
//        };
//
//        workingTread.start();
//        try {
//            workingTread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }

    public void asdf() {
        city = new String[managerList.size()];

        for (int i = 0; i < city.length; i++) {
            city[i] = managerList.get(i);

        }


//        Log.e("왜", String.valueOf(managerList.get(4)));

        Log.e("새로 만든거", String.valueOf(city.length));


        ArrayAdapter spin_adapter = new ArrayAdapter(getApplicationContext(), R.layout.owner_addstore_spin, city);
        spin_adapter.setDropDownViewResource(R.layout.owner_addstore_spin_drop);

        v_com_manager.setAdapter(spin_adapter);
        v_com_manager.setSelection(0);
//        Log.e("매니저 값",v_com_manager.getSelectedItem());
        v_com_manager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                v_com_manager.setSelection(Integer.parseInt(managerList.get(position).toString()));
//                Log.e("매니저",v_com_manager.getSelectedItem().toString());
//                Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    class clicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.com_button:
                    com_number = v_com_number.getText().toString();
                    com_name = v_com_name.getText().toString();
                    if (v_com_addr2 == null) {
                        com_addr = null;
                        com_addr = v_com_addr.getText().toString();
                    } else {
                        com_addr = null;
                        com_addr = v_com_addr.getText().toString() + v_com_addr2.getText().toString();
                    }

                    Log.e("맞나?", com_addr);


                    ID = GlobalData.getLogin_ID();
                    com_manager = v_com_manager.getSelectedItem().toString();
                    com_URI = file_name;

                    Log.i("asyntask", "됨");
                    InsertData_com com_Task = new InsertData_com();
                    Log.i("asyntask", "됨");
                    com_Task.execute(com_number, com_name, com_addr, com_category, com_manager, com_URI, ID, file_dir + "" + file_name, file_dir, file_name);

                    break;

            }
        }
    }

    @Override
    public void onClick(View v) {


    }

    class InsertData_com extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.i("asyntask", "됨");
            progressDialog = ProgressDialog.show(owner_AddStore.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            String com_number = (String) params[0];
            String com_name = (String) params[1];
            String com_addr = (String) params[2];
            String com_category = (String) params[3];
            String com_manager = (String) params[4];
            String com_URI = (String) params[5];
            String id = (String) params[6];
            String sourceFileUri = (String) params[7];
            String file_dir = (String) params[8];
            String file_name = (String) params[9];


            String fileName = sourceFileUri;
            String uploadFilePath = file_dir;
            String uploadFileName = file_name;
            File sourceFile = new File(sourceFileUri);

            String boundary = "^******^";
            DataOutputStream dos = null;
            String delimiter = "\r\n--" + boundary + "\r\n";
            int bytesRead, bytesAvailable, bufferSize;
            int maxBufferSize = 1 * 1024 * 1024;
            byte[] buffer;

            String serverURL = GlobalData.getURL() + "YTest.php";
            String postParameters = "&com_number=" + com_number + "&com_name=" + com_name
                    + "&com_addr=" + com_addr + "&com_category=" + com_category
                    + "&com_manager=" + com_manager + "&com_URI=" + com_URI + "&id=" + id;


            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true); // Allow Inputs
                httpURLConnection.setDoOutput(true); // Allow Outputs
                httpURLConnection.setUseCaches(false); // Don't use a Cached Copy
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                StringBuffer postDataBuilder = new StringBuffer();

                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_number", com_number));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_name", com_name));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_addr", com_addr));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_category", com_category));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_manager", com_manager));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_URI", com_URI));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("id", id));
                postDataBuilder.append(delimiter);

                dos = new DataOutputStream(httpURLConnection.getOutputStream());

                postDataBuilder.append(setFile("uploaded_file", fileName));
                postDataBuilder.append("\r\n");

                dos.writeUTF(postDataBuilder.toString());

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(delimiter);
                dos.flush();
                dos.close();
                fileInputStream.close();


//                outputStream.flush();
//                outputStream.close();

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

            Log.d("DB", "POST response  - " + result);
        }

        public String setValue(String key, String value) {
            return "Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n" + value;
        }

        public String setFile(String key, String fileName) {
            return "Content-Disposition: form-data; name=\"" + key + "\";filename=\"" + fileName + "\"\r\n";
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍기 위하여 설정합니다.
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(owner_AddStore.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(owner_AddStore.this,
                    "com.example.win.a2vent.provider", photoFile); //FileProvider의 경우 이전 포스트를 참고하세요.
            Toast.makeText(this, photoUri.toString(), Toast.LENGTH_SHORT).show();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    // Android M에서는 Uri.fromFile 함수를 사용하였으나 7.0부터는 이 함수를 사용할 시 FileUriExposedException이
    // 발생하므로 아래와 같이 함수를 작성합니다. 이전 포스트에 참고한 영문 사이트를 들어가시면 자세한 설명을 볼 수 있습니다.
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "2vent" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/test/"); //test라는 경로에 이미지를 저장하기 위함
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        file_dir = storageDir.toString() + "/";
        file_name = image.getName();

        return image;
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PICK 즉 사진을 고르겠다!
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(owner_AddStore.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return;
            }

            photoUri = data.getData();
            cropImage();


        } else if (requestCode == PICK_FROM_CAMERA) {
            cropImage();
            MediaScannerConnection.scanFile(owner_AddStore.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {
            try { //저는 bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출하였습니다.
//                this.grantUriPermission("com", photoUri,
//                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 128, 128);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축


                //여기서는 ImageView에 setImageBitmap을 활용하여 해당 이미지에 그림을 띄우시면 됩니다.
                v_com_iv.setImageBitmap(bitmap);

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());

            }
        } else if (requestCode == SEARCH_ADDR) {
            try {
                v_com_addr.setText(data.getStringExtra("addr"));
                String test = data.getStringExtra("addr");
                Log.e("맞나?", test);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

    }

    //Android N crop image (이 부분에서 몇일동안 정신 못차렸습니다 ㅜ)
//모든 작업에 있어 사전에 FALG_GRANT_WRITE_URI_PERMISSION과 READ 퍼미션을 줘야 uri를 활용한 작업에 지장을 받지 않는다는 것이 핵심입니다.
    public void cropImage() {
        this.grantUriPermission("com.android.camera", photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/test/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(owner_AddStore.this,
                    "com.example.win.a2vent.provider", tempFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            grantUriPermission(res.activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_CAMERA);


        }

    }


    private class getManager extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(String... args) {
            StringBuilder sb = null;
            try {


                URL url = new URL(GlobalData.getURL() + "2ventAddstoreGetManager.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF8"));
//                writer.write("?event_number="+args[0]);
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
            Log.i("받은 json ", s);
            jsonParser(s);
            asdf();


        }

    }

    public void jsonParser(String s) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);

            JSONArray jsonArray = jsonObject.getJSONArray("manager");


            for (int i = 0; i < jsonArray.length(); i++) {


                JSONObject item = jsonArray.getJSONObject(i);

//                managerList.
                managerList.add(item.getString("id"));
//                managerList.add(item.getgetString("id"));
//            managerList.add(item.getString());
                Log.e("item", item.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
