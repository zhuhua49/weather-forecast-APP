package com.example.zh49pokemon.weatherforecast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    String cityname;
    TextView showJW;
    TextView  text;
    TextView  time;
    ImageView background;

    TextView  today;
    ImageView imageView1;
    ImageView imageView2;

    TextView  tomorrow;
    ImageView imageView3;
    ImageView imageView4;

    private LocationManager locationManager;

    private double latitude = 0;
    private double longitude = 0;

    SQLiteDatabase Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetReceiver NetReceiver = new NetReceiver();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(NetReceiver, filter);

        showJW = findViewById(R.id.showJW);
        text = findViewById(R.id.text);
        time = findViewById(R.id.time);
        background = findViewById(R.id.background);

        today = findViewById(R.id.today);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);

        tomorrow = findViewById(R.id.tomorrow);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MySQLiteOpenHelper SQLiteHelper = new MySQLiteOpenHelper(this,"weatherSQLite",null,1);
        Database = SQLiteHelper.getWritableDatabase();

        new backgroundTask().execute();
        new LocationTask().execute(0);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意。
                    // 执形我们想要的操作
                }
            }
        }
    }

    public void update(View view) {
        if (gettool.Network(this)) {
            new LocationTask().execute(1);
            Toast.makeText(this, "更新成功",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "更新失败，请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }

    public void query(View view) {
        Intent intent = new Intent("com.example.QueryActivity");
        startActivityForResult(intent,0);
    }

    class LocationTask extends AsyncTask<Integer,String,Void> {
        @Override
        protected Void doInBackground(Integer... Integer) {
            @SuppressLint("MissingPermission")
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude(); // 经度
                longitude = location.getLongitude(); // 纬度

                if (Integer[0]==1){
                    List<Address> addList = null;
                    Geocoder ge = new Geocoder(getApplicationContext());
                    try {
                        addList = ge.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addList != null && addList.size() > 0) {
                        cityname = addList.get(0).getLocality();
                    }
                }
            }
            String result = latitude + ";" + longitude + ";" + "无" + ";" + "输入城市无效";

            if (Integer[0]==1){
                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm E");

                result = latitude + ";" + longitude + ";" + sdf.format(dt) + ";" + gettool.getweather(cityname.substring(0, 2));

                ContentValues Values = new ContentValues();
                Values.put("information",result);
                Database.insert("weather",null,Values);
            }

            Cursor cursor = Database.query("weather",new String[]{"information"},null,null,null,null,null);
            while (cursor.moveToNext()){
                result = cursor.getString(cursor.getColumnIndex("information"));
            }

            publishProgress(result);
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String[] arr = values[0].split(";");
            showJW.setText("经度：" + arr[0] + "\n纬度：" + arr[1]);
            time.setText("上次更新时间：\n" + arr[2]);
            String textvalues = arr[3];
            if (arr.length>5) {
                textvalues = arr[4] + "\n" + arr[8] + "\n" + arr[9] + "\n" + arr[10];

                today.setText("今天：" + arr[11] + "\n" + arr[12] + "\n" + arr[13]);
                tomorrow.setText("明天：" + arr[16] + "\n" + arr[17] + "\n" + arr[18]);

                imageView1.setImageResource(gettool.picture(arr[14]));
                imageView2.setImageResource(gettool.picture(arr[15]));

                imageView3.setImageResource(gettool.picture(arr[19]));
                imageView4.setImageResource(gettool.picture(arr[20]));
            }else {
                imageView1.setImageResource(R.drawable.a_nothing);
                imageView2.setImageResource(R.drawable.a_nothing);
                imageView3.setImageResource(R.drawable.a_nothing);
                imageView4.setImageResource(R.drawable.a_nothing);
            }
            text.setText(textvalues);
        }
    }

    class backgroundTask extends AsyncTask<Void,Bitmap,Void> {
        @Override
        protected Void doInBackground(Void... Void) {
            try {
                Bitmap bitmap = gettool.readBitmap(MainActivity.this);
                if (bitmap != null){
                    publishProgress(bitmap);
                }else {
                    bitmap = gettool.getBitmap();
                    gettool.saveBitmap(MainActivity.this,bitmap);
                    publishProgress(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Bitmap... values) {
            super.onProgressUpdate(values);
            background.setImageBitmap(values[0]);
        }
    }

}
