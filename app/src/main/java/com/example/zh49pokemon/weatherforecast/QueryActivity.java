package com.example.zh49pokemon.weatherforecast;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class QueryActivity extends AppCompatActivity {

    TextView text;
    ImageView background;

    TextView  today;
    ImageView imageView1;
    ImageView imageView2;

    TextView  tomorrow;
    ImageView imageView3;
    ImageView imageView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        text = findViewById(R.id.text);

        today = findViewById(R.id.today);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);

        tomorrow = findViewById(R.id.tomorrow);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);

        background = findViewById(R.id.background);
        new backgroundTask().execute();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText city = findViewById(R.id.editText);
                String cityname = city.getText().toString();

                if (gettool.Network(QueryActivity.this)) {
                    new MyTask().execute(cityname);
                    Toast.makeText(QueryActivity.this, "查询成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(QueryActivity.this, "查询失败，请检查网络设置",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class MyTask extends AsyncTask<String,String,Void> {
        @Override
        protected Void doInBackground(String... String) {
            String result = gettool.getweather(String[0]);
            publishProgress(result);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String[] arr = values[0].split(";");
            String textvalues = arr[0];
            if (arr.length>2) {
                textvalues = arr[1] + "\n" + arr[5] + "\n" + arr[6] + "\n" + arr[7];

                today.setText("今天：" + arr[8] + "\n" + arr[9] + "\n" + arr[10]);
                tomorrow.setText("明天：" + arr[13] + "\n" + arr[14] + "\n" + arr[15]);

                imageView1.setImageResource(gettool.picture(arr[11]));
                imageView2.setImageResource(gettool.picture(arr[12]));

                imageView3.setImageResource(gettool.picture(arr[16]));
                imageView4.setImageResource(gettool.picture(arr[17]));
            }else {
                today.setText("");
                tomorrow.setText("");

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
                Bitmap bitmap = gettool.readBitmap(QueryActivity.this);
                if (bitmap != null){
                    publishProgress(bitmap);
                }else {
                    bitmap = gettool.getBitmap();
                    gettool.saveBitmap(QueryActivity.this,bitmap);
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
