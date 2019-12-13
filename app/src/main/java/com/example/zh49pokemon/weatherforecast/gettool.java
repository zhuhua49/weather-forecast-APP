package com.example.zh49pokemon.weatherforecast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class gettool {
    public static String getweather(String city) {
        URL url = null;
        try {
            url = new URL("http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather?theCityCode=" + city + "&theUserID=");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = "输入城市无效";
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(inputStream,"UTF-8");
            int eventType = parser.getEventType();
            while (eventType != parser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("string")) {
                            result = result + ";" + parser.nextText();
                        }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int picture(String name) {
        int result = R.drawable.a_nothing;
        if (name.equals("0.gif")) {
            result = R.drawable.a_0;
        }else if (name.equals("1.gif")){
            result = R.drawable.a_1;
        }else if (name.equals("2.gif")){
            result = R.drawable.a_2;
        }else if (name.equals("3.gif")){
            result = R.drawable.a_3;
        }else if (name.equals("4.gif")){
            result = R.drawable.a_4;
        }else if (name.equals("5.gif")){
            result = R.drawable.a_5;
        }else if (name.equals("6.gif")){
            result = R.drawable.a_6;
        }else if (name.equals("7.gif")){
            result = R.drawable.a_7;
        }else if (name.equals("8.gif")){
            result = R.drawable.a_8;
        }else if (name.equals("9.gif")){
            result = R.drawable.a_9;
        }else if (name.equals("10.gif")){
            result = R.drawable.a_10;
        }else if (name.equals("11.gif")){
            result = R.drawable.a_11;
        }else if (name.equals("12.gif")){
            result = R.drawable.a_12;
        }else if (name.equals("13.gif")){
            result = R.drawable.a_13;
        }else if (name.equals("14.gif")){
            result = R.drawable.a_14;
        }else if (name.equals("15.gif")){
            result = R.drawable.a_15;
        }else if (name.equals("16.gif")){
            result = R.drawable.a_16;
        }else if (name.equals("17.gif")){
            result = R.drawable.a_17;
        }else if (name.equals("18.gif")){
            result = R.drawable.a_18;
        }else if (name.equals("19.gif")){
            result = R.drawable.a_19;
        }else if (name.equals("20.gif")){
            result = R.drawable.a_20;
        }else if (name.equals("21.gif")){
            result = R.drawable.a_21;
        }else if (name.equals("22.gif")){
            result = R.drawable.a_22;
        }else if (name.equals("23.gif")){
            result = R.drawable.a_23;
        }else if (name.equals("24.gif")){
            result = R.drawable.a_24;
        }else if (name.equals("25.gif")){
            result = R.drawable.a_25;
        }else if (name.equals("26.gif")){
            result = R.drawable.a_26;
        }else if (name.equals("27.gif")){
            result = R.drawable.a_27;
        }else if (name.equals("28.gif")){
            result = R.drawable.a_28;
        }else if (name.equals("29.gif")){
            result = R.drawable.a_29;
        }else if (name.equals("30.gif")){
            result = R.drawable.a_30;
        }else if (name.equals("31.gif")){
            result = R.drawable.a_31;
        }

        return result;
    }

    public static boolean Network(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return true;
            }
        }
        return false;
    }

    public static Bitmap getBitmap() throws IOException {

        URL url = new URL("http://guolin.tech/api/bing_pic");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream inputStream = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);

        String tempResult;
        StringBuilder result = new StringBuilder();
        while ((tempResult = br.readLine()) != null) {
            result.append(tempResult);
        }

        url = new URL(result.toString());
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        inputStream = conn.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        return bitmap;

    }

    public static void saveBitmap(Context context,Bitmap bitmap) throws IOException{
        File externalFilesDir = context.getExternalFilesDir("background");
        if (!externalFilesDir.exists()) {
            externalFilesDir.mkdir();
        }

        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
        String fileName = sdf.format(dt) + ".jpg";

        File file = new File(externalFilesDir, fileName);

        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    }

    public static Bitmap readBitmap(Context context) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
        String fileName = sdf.format(dt) + ".jpg";

        File externalFilesDir = context.getExternalFilesDir("background");
        File file = new File(externalFilesDir, fileName);

        try
        {
            FileInputStream fileInputStream=new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            return bitmap;
        }
        catch (FileNotFoundException e)
        {
            return null;
        }
    }
}


