package com.example.win.a2vent;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by win on 2017-07-10.
 */

public class owner_Event_Form2 extends AppCompatActivity {

    String test;
    private final static String TAG = "테스트";

    private RelativeLayout rlEventForm;

    private RadioGroup rgType;
    private RadioButton rbTypeEnter, rbTypePay;
    private String strType = "0";

    private Spinner spinStore;
    private EditText etFixedPrice, etDiscount, etLimitPersons;

    private EditText etStartDateYear, etStartDateMonth, etStartDateDay, etStartHour, etStartMin, etEndDateYear, etEndDateMonth, etEndDateDay, etEndHour, etEndMin;

    private RadioGroup rgPayment;
    private RadioButton rbCashPayment, rbCardPayment;
    private String strPayment = "0";

    private EditText etEventName, etMinimumAge, etMaximumAge;

    private static final int REQ_CODE_SELECT_IMAGE = 100;
    private String absolutePath, imgName;
    private static String uploadImgPath;
    private static File filePath;
    private ImageView imgContents;

    private Switch swConditions;
    private String strConditions = "0";

    private RadioGroup rgSex;
    private RadioButton rbMale, rbFemale, rbAllSex;
    private String strSex = "1";

    private Spinner spinLocation;

    private Button btnAddSubmit, btnAddTemp, btnAddCancel;

    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_event_form);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog().build());

        verifyStoragePermissions(owner_Event_Form2.this);

        rlEventForm = (RelativeLayout) findViewById(R.id.rlEventForm);
        rlEventForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        rbTypeEnter = (RadioButton) findViewById(R.id.rbTypeEnter);
        rbTypePay = (RadioButton) findViewById(R.id.rbTypePay);
        rgType = (RadioGroup) findViewById(R.id.rgType);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbTypeEnter:
                        invisiblePayment();
                        strType = "0";
                        break;
                    case R.id.rbTypePay:
                        visiblePayment();
                        strType = "1";
                        break;
                }
            }
        });

        ArrayList<CharSequence> arrListStore = new ArrayList<>();
        arrListStore.add(0, "AAA");
        arrListStore.add(1, "BBB");
        ArrayAdapter<CharSequence> arrAdtSpinnerStore = new ArrayAdapter<>(owner_Event_Form2.this, R.layout.support_simple_spinner_dropdown_item, arrListStore);
        spinStore = (Spinner) findViewById(R.id.spinStore);
        spinStore.setAdapter(arrAdtSpinnerStore);
        spinStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etFixedPrice = (EditText) findViewById(R.id.etFixedPrice);
        etDiscount = (EditText) findViewById(R.id.etDiscount);
        etFixedPrice.addTextChangedListener(new TextWatcher_MoneyToComma(etFixedPrice));
        etDiscount.addTextChangedListener(new TextWatcher_MoneyToComma(etDiscount));

        etLimitPersons = (EditText) findViewById(R.id.etLimitPersons);
        etLimitPersons.addTextChangedListener(new TextWatcher_MoneyToComma(etLimitPersons));

        etStartDateYear = (EditText) findViewById(R.id.etStartDateYear);
        etStartDateMonth = (EditText) findViewById(R.id.etStartDateMonth);
        etStartDateDay = (EditText) findViewById(R.id.etStartDateDay);
        etStartHour = (EditText) findViewById(R.id.etStartHour);
        etStartMin = (EditText) findViewById(R.id.etStartMin);

        etEndDateYear = (EditText) findViewById(R.id.etEndDateYear);
        etEndDateMonth = (EditText) findViewById(R.id.etEndDateMonth);
        etEndDateDay = (EditText) findViewById(R.id.etEndDateDay);
        etEndHour = (EditText) findViewById(R.id.etEndHour);
        etEndMin = (EditText) findViewById(R.id.etEndMin);

        rbCashPayment = (RadioButton) findViewById(R.id.rbCashPayment);
        rbCardPayment = (RadioButton) findViewById(R.id.rbCardPayment);
        rgPayment = (RadioGroup) findViewById(R.id.rgPayment);
        rgPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbCashPayment:
                        //Toast.makeText(owner_event_form.this, rbCashPayment.getText().toString(), Toast.LENGTH_SHORT).show();
                        strPayment = "0";
                        break;
                    case R.id.rbCardPayment:
                        //Toast.makeText(owner_event_form.this, rbCardPayment.getText().toString(), Toast.LENGTH_SHORT).show();
                        strPayment = "1";
                        break;
                }
            }
        });

        etEventName = (EditText) findViewById(R.id.etEventName);

        imgContents = (ImageView) findViewById(R.id.imgContents);
        imgContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _intent = new Intent(Intent.ACTION_PICK);
                _intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                _intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(_intent, REQ_CODE_SELECT_IMAGE);
                Log.d(TAG, "터치");
            }
        });

        swConditions = (Switch) findViewById(R.id.swConditions);
        swConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedConditions(isChecked);
                if (isChecked) {
                    strConditions = "1";
                } else {
                    strConditions = "0";
                }
            }
        });

        etMinimumAge = (EditText) findViewById(R.id.etMinimumAge);
        etMaximumAge = (EditText) findViewById(R.id.etMaximumAge);

        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        rbAllSex = (RadioButton) findViewById(R.id.rbAllSex);
        rgSex = (RadioGroup) findViewById(R.id.rgSex);
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbMale:
                        Toast.makeText(owner_Event_Form2.this, rbMale.getText().toString(), Toast.LENGTH_SHORT).show();
                        strSex = "1";
                        break;
                    case R.id.rbFemale:
                        Toast.makeText(owner_Event_Form2.this, rbFemale.getText().toString(), Toast.LENGTH_SHORT).show();
                        strSex = "0";
                        break;
                    case R.id.rbAllSex:
                        Toast.makeText(owner_Event_Form2.this, rbAllSex.getText().toString(), Toast.LENGTH_SHORT).show();
                        strSex = "2";
                        break;
                }
            }
        });

        ArrayAdapter<CharSequence> arrAdtLocations = ArrayAdapter.createFromResource(owner_Event_Form2.this, R.array.locations, R.layout.support_simple_spinner_dropdown_item);
        spinLocation = (Spinner) findViewById(R.id.spinLocation);
        spinLocation.setAdapter(arrAdtLocations);
        spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinLocation.setEnabled(false);

        btnAddSubmit = (Button) findViewById(R.id.btnAddSubmit);
        btnAddSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_AddSubmit();
            }
        });
        btnAddTemp = (Button) findViewById(R.id.btnAddTemp);
        btnAddTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_AddTemp();
            }
        });
        btnAddCancel = (Button) findViewById(R.id.btnAddCancel);
        btnAddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        invisiblePayment();
    }

    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(owner_Event_Form2.this, "'뒤로' 버튼을 한버 더 누르시면 종료됩니다", Toast.LENGTH_SHORT);

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "ActivityResult");
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            Log.d(TAG, "REQ_CODE_SELECT_IMAGE");
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Log.d(TAG, "RESULT_OK");
                    // Uri에서 이미지 이름을 얻어온다.
                    String strName = getImageNameToUri(data.getData());
                    Uri selPhotoUri = data.getData();
                    Log.d(TAG, "strName: " + strName);
                    // 절대경로 획득
                    Cursor cursor = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null, null, null, null);
                    cursor.moveToNext();
                    absolutePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));

                    // 이미지 데이터를 비트맵으로 받아옴
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    // 이미지 리사이징
                    int height = image_bitmap.getHeight();
                    int width = image_bitmap.getWidth();

                    Bitmap src = BitmapFactory.decodeFile(absolutePath);
                    Bitmap resized = Bitmap.createScaledBitmap(src, width / 2, height / 2, true);

                    saveBitmapToJPEG(resized, "2ventApp", strName);

                    Log.d(TAG, "불러오기");

                    imgContents.setImageBitmap(resized);
                    imgContents.setTag("exist");

                    imgName = strName;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkedConditions(boolean isChecked) {
        if (isChecked) {
            activeContents();
        } else {
            inActiveContents();
        }
    }

    private void activeContents() {
        int darkGray = Color.argb(255, 102, 102, 102);
        TextView tvAge = (TextView) findViewById(R.id.tvAge);
        TextView tvAge1 = (TextView) findViewById(R.id.tvAge1);
        TextView tvSex = (TextView) findViewById(R.id.tvSex);
        TextView tvLocations = (TextView) findViewById(R.id.tvLocation);
        tvAge.setTextColor(darkGray);
        tvAge1.setTextColor(darkGray);
        tvSex.setTextColor(darkGray);
        tvLocations.setTextColor(darkGray);
        etMinimumAge.setEnabled(true);
        etMinimumAge.setTextColor(darkGray);
        etMaximumAge.setEnabled(true);
        etMaximumAge.setTextColor(darkGray);
        rbMale.setEnabled(true);
        rbMale.setTextColor(darkGray);
        rbFemale.setEnabled(true);
        rbFemale.setTextColor(darkGray);
        rbAllSex.setEnabled(true);
        rbAllSex.setTextColor(darkGray);
        spinLocation.setEnabled(true);
    }

    private void inActiveContents() {
        TextView tvAge = (TextView) findViewById(R.id.tvAge);
        TextView tvAge1 = (TextView) findViewById(R.id.tvAge1);
        TextView tvSex = (TextView) findViewById(R.id.tvSex);
        TextView tvLocations = (TextView) findViewById(R.id.tvLocation);
        tvAge.setTextColor(Color.LTGRAY);
        tvAge1.setTextColor(Color.LTGRAY);
        tvSex.setTextColor(Color.LTGRAY);
        tvLocations.setTextColor(Color.LTGRAY);
        etMinimumAge.setEnabled(false);
        etMinimumAge.setTextColor(Color.LTGRAY);
        etMaximumAge.setEnabled(false);
        etMaximumAge.setTextColor(Color.LTGRAY);
        rbMale.setEnabled(false);
        rbMale.setTextColor(Color.LTGRAY);
        rbFemale.setEnabled(false);
        rbFemale.setTextColor(Color.LTGRAY);
        rbAllSex.setEnabled(false);
        rbAllSex.setTextColor(Color.LTGRAY);
        spinLocation.setEnabled(false);
    }

    private void visiblePayment() {
        TextView tvPayment = (TextView) findViewById(R.id.tvPayment);
        tvPayment.setVisibility(View.VISIBLE);
        rgPayment.setVisibility(View.VISIBLE);
        rbCashPayment.setEnabled(true);
        rbCardPayment.setEnabled(true);
    }

    private void invisiblePayment() {
        TextView tvPayment = (TextView) findViewById(R.id.tvPayment);
        tvPayment.setVisibility(View.GONE);
        rgPayment.setVisibility(View.GONE);
        rbCashPayment.setEnabled(false);
        rbCardPayment.setEnabled(false);
    }

    private void onClick_AddSubmit() {
        String msg = checkInputData();
        input(msg, "0");
    }

    private void onClick_AddTemp() {
        String msg = checkInputTempData();
        input(msg, "1");
    }

    private void input(String msg, String stats) {
        if (msg != null) {
            Toast.makeText(owner_Event_Form2.this, msg, Toast.LENGTH_SHORT).show();
        } else {
            String _name = etEventName.getText().toString();
            String _type = strType;
            String _stats = stats;
            String _URI = "event_img/";
            String _price = etFixedPrice.getText().toString().trim();
            String _dis_price = etDiscount.getText().toString().trim();
            String _people = etLimitPersons.getText().toString().trim();
            String _startday = etStartDateYear.getText().toString().trim() + etStartDateMonth.getText().toString().trim() + etStartDateDay.getText().toString().trim();
            String _endday = etEndDateYear.getText().toString().trim() + etEndDateMonth.getText().toString().trim() + etEndDateDay.getText().toString().trim();
            String _starttime = etStartHour.getText().toString().trim() + etStartMin.getText().toString().trim();
            String _endtime = etEndHour.getText().toString().trim() + etEndMin.getText().toString().trim();
            String _payment = strPayment;
            String _target = strConditions;
            String _minage = etMinimumAge.getText().toString().trim();
            String _maxage = etMaximumAge.getText().toString().trim();
            String _sex = strSex;
            String _area = spinLocation.getSelectedItem().toString();
            String _com_number = "11111111111";
            String _id = "1";

            InsertData inputTask = new InsertData();
            inputTask.execute(_name, _type, _stats, _URI, _price, _dis_price, _people, _startday, _endday, _starttime, _endtime, _payment, _target, _minage, _maxage, _sex, _area, _com_number, _id);
        }
    }

    private String checkInputData() {
        if (isEmptyEditText(etFixedPrice)) {
            return "정상가를 입력하세요";
        } else if (isEmptyEditText(etDiscount)) {
            return "할인가를 입력하세요";
        } else if (isEmptyEditText(etLimitPersons)) {
            return "인원제한을 입력하세요";
        } else if (isEmptyEditText(etStartDateYear) || isEmptyEditText(etStartDateMonth) || isEmptyEditText(etStartDateDay) || isEmptyEditText(etStartHour) || isEmptyEditText(etStartMin)) {
            return "이벤트 시작일시를 확인하세요";
        } else if (!checkStartDate(etStartDateYear, etStartDateMonth, etStartDateDay)) {
            return "현재 날짜보다 이전입니다";
        } else if (isEmptyEditText(etEndDateYear) || isEmptyEditText(etEndDateMonth) || isEmptyEditText(etEndDateDay) || isEmptyEditText(etEndHour) || isEmptyEditText(etEndMin)) {
            return "이벤트 종료일시를 확인하세요";
        } else if (!checkEndDate(etStartDateYear, etStartDateMonth, etStartDateDay, etEndDateYear, etEndDateMonth, etEndDateDay)) {
            return "종료 날짜가 시작 날짜보다 이전입니다";
        } else if (isEmptyEditText(etEventName)) {
            return "이벤트 명을 입력하세요";
        } else if (swConditions.isChecked()) {
            if (isEmptyEditText(etMinimumAge) && isEmptyEditText(etMaximumAge)) {
                return "참가 나이를 입력하세요";
            } else {
                int maxAge = Integer.parseInt(etMaximumAge.getText().toString().trim());
                int minAge = Integer.parseInt(etMinimumAge.getText().toString().trim());

                if (maxAge < minAge) {
                    return "참가 나이를 확인하세요";
                }
            }
        }

        return null;
    }

    private String checkInputTempData() {
        if (isEmptyEditText(etFixedPrice)) {
            return "정상가를 입력하세요";
        } else if (isEmptyEditText(etDiscount)) {
            return "할인가를 입력하세요";
        } else if (isEmptyEditText(etEventName)) {
            return "이벤트 명을 입력하세요";
        }

        return null;
    }

    private boolean isEmptyEditText(EditText et) {
        if (TextUtils.isEmpty(et.getText().toString().trim())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkStartDate(EditText etYear, EditText etMonth, EditText etDay) {
        int year = Integer.parseInt(etYear.getText().toString().trim());
        int month = Integer.parseInt(etMonth.getText().toString().trim());
        int day = Integer.parseInt(etDay.getText().toString().trim());
        int _year, _month, _day;
        Calendar cal = Calendar.getInstance();

        _year = cal.get(Calendar.YEAR);

        if (year < _year) {
            return false;
        } else {
            _month = cal.get(Calendar.MONTH);
            if (month < _month) {
                return false;
            } else {
                if (month == month) {
                    _day = cal.get(Calendar.DAY_OF_MONTH);
                    if (day < _day) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkEndDate(EditText etYear1, EditText etMonth1, EditText etDay1, EditText etYear2, EditText etMonth2, EditText etDay2) {
        int year1 = Integer.parseInt(etYear1.getText().toString().trim());
        int month1 = Integer.parseInt(etMonth1.getText().toString().trim());
        int day1 = Integer.parseInt(etDay1.getText().toString().trim());
        int year2 = Integer.parseInt(etYear2.getText().toString().trim());
        int month2 = Integer.parseInt(etMonth2.getText().toString().trim());
        int day2 = Integer.parseInt(etDay2.getText().toString().trim());

        if (year1 > year2) {
            return false;
        } else {
            if (month1 > month2) {
                return false;
            } else {
                if (month1 == month2) {
                    if (day1 > day2) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(etFixedPrice.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etDiscount.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etLimitPersons.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etStartDateYear.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etStartDateMonth.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etStartDateDay.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etStartHour.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etStartMin.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEndDateYear.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEndDateMonth.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEndDateDay.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEndHour.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEndMin.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEventName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etMinimumAge.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etMaximumAge.getWindowToken(), 0);
    }

    private static void verifyStoragePermissions(Activity activity) {
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    private static void saveBitmapToJPEG(Bitmap bitmap, String folder, String name) {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder_name = "/" + folder + "/";
        String file_name = name;
        String str_path = ex_storage + folder_name;
        uploadImgPath = str_path + file_name;

        Log.d(TAG, "saveBitmapToJPEG");

        try {
            filePath = new File(str_path);
            Log.d(TAG, "isDirectory");
            if (!filePath.isDirectory()) {
                filePath.mkdirs();
                Log.d(TAG, "mkdirs");
            }
            FileOutputStream out = new FileOutputStream(uploadImgPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("FileNotFountException", e.getMessage());
            Log.d(TAG, "FileNotFoundException");
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
            Log.d(TAG, "IOException");
        }
    }

    private String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

    private class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(owner_Event_Form2.this, "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String event_name = params[0];
            String event_type = params[1];
            String event_stats = params[2];
            String event_URI = params[3];
            String event_price = params[4];
            String event_dis_price = params[5];
            String event_people = params[6];
            String event_startday = params[7];
            String event_endday = params[8];
            String event_starttime = params[9];
            String event_endtime = params[10];
            String event_payment = params[11];
            String event_target = params[12];
            String event_minage = params[13];
            String event_maxage = params[14];
            String event_sex = params[15];
            String event_area = params[16];
            String com_number = params[17];
            String id = params[18];

            String serverURL = "http://192.168.0.7/2vent/2ventAddevent.php";

            try {

                // 데이터 구분문자
                String boundary = "^******^";

                // 데이터 경계선
                String delimiter = "\r\n--" + boundary + "\r\n";

                StringBuffer postDataBuiler = new StringBuffer();

                // 커넥션 생성 및 설정
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep_Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                // 추가하고 싶은 Key & Value 추가
                // Key & Value를 추가한 후 꼭 경계선을 삽입해줘야 데이터를 구분할 수 있다.
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_name", event_name));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_type", event_type));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_stats", event_stats));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_URI", event_URI + id + "/" + imgName));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_price", event_price));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_dis_price", event_dis_price));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_people", event_people));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_startday", event_startday));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_endday", event_endday));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_starttime", event_starttime));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_endtime", event_endtime));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_payment", event_payment));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_target", event_target));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_minage", event_minage));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_maxage", event_maxage));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_sex", event_sex));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("event_area", event_area));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("com_number", com_number));
                postDataBuiler.append(delimiter);
                postDataBuiler.append(setValue("id", id));
                postDataBuiler.append(delimiter);

                // 파일 첨부
                Log.d(TAG, "uploadedPath: " + uploadImgPath);
                postDataBuiler.append(setFile("uploaded_file", uploadImgPath));
                postDataBuiler.append("\r\n");

                Log.d(TAG, "data: " + postDataBuiler.toString());
                // 전송 작업 시작
                FileInputStream in = new FileInputStream(uploadImgPath);
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream((conn.getOutputStream())));

                out.writeUTF(postDataBuiler.toString());

                // 파일 복사 작업 시작
                int maxBufferSize = 1024;
                int bufferSize = Math.min(in.available(), maxBufferSize);
                byte[] buffer = new byte[bufferSize];

                // 버퍼 크기만큼 파일로부터 바이트 데이터를 읽는다
                int byteRead = in.read(buffer, 0, bufferSize);

                // 전송
                while (byteRead > 0) {
                    out.write(buffer);
                    bufferSize = Math.min(in.available(), maxBufferSize);
                    byteRead = in.read(buffer, 0, bufferSize);
                }

                out.writeBytes(delimiter);
                out.flush();
                out.close();
                in.close();

                int responseStatusCode = conn.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == conn.HTTP_OK) {
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString();
            } catch (NullPointerException e) {
                return new String("NullPoint: " + e.getMessage());
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error", e);

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "POST response - " + result);

            if (result.equals("SQL문 처리 성공")) {
                etFixedPrice.setText(null);
                etDiscount.setText(null);
                etLimitPersons.setText(null);
                etStartDateYear.setText(null);
                etStartDateMonth.setText(null);
                etStartDateDay.setText(null);
                etStartHour.setText(null);
                etStartMin.setText(null);
                etEndDateYear.setText(null);
                etEndDateMonth.setText(null);
                etEndDateDay.setText(null);
                etEndHour.setText(null);
                etEndMin.setText(null);
                etEventName.setText(null);
                etMinimumAge.setText(null);
                etMaximumAge.setText(null);
                imgContents.setImageResource(R.mipmap.ic_launcher);
            }
        }

        public String setValue(String key, String value) {
            return "Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n" + value;
        }

        public String setFile(String key, String fileName) {
            return "Content-Disposition: form-data; name=\"" + key + "\";filename=\"" + fileName + "\"\r\n";
        }
    }

    private class TextWatcher_MoneyToComma implements TextWatcher {
        private EditText mEditText;
        String strAmount = "";

        public TextWatcher_MoneyToComma(EditText e) {
            mEditText = e;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(strAmount)) {
                strAmount = setStrDataToComma(s.toString().replace(",", ""));
                mEditText.setText(strAmount);
                Editable e = mEditText.getText();
                Selection.setSelection(e, strAmount.length());
            }
        }

        protected String setStrDataToComma(String str) {
            if (str.length() == 0) {
                return "";
            }
            long value = Long.parseLong(str);
            DecimalFormat format = new DecimalFormat("###,###");
            return format.format(value);
        }
    }
}
