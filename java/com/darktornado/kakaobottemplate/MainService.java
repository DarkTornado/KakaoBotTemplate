package com.darktornado.kakaobottemplate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MainService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /* onNotificationPosted 안에서 토스트 띄우면 안떠서 굳이 분리 */
        String msg = intent.getStringExtra("msg");
        if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
