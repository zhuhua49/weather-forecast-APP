package com.example.zh49pokemon.weatherforecast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (gettool.Network(context)) {
            Toast.makeText(context, "网络已连接",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "网络已断开",Toast.LENGTH_SHORT).show();
        }
    }
}
