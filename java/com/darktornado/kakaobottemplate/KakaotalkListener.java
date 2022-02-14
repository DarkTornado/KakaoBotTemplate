package com.darktornado.kakaobottemplate;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class KakaotalkListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        //알아서 봇 on/off 구분 넣으셈
        if (!sbn.getPackageName().equals("com.kakao.talk")) return; //블록 너무 깊어서 반대로 적음
        Notification.WearableExtender wExt = new Notification.WearableExtender(sbn.getNotification());
        for (Notification.Action act : wExt.getActions()) {
            if (act.getRemoteInputs() != null && act.getRemoteInputs().length > 0) {
                if (act.title.toString().toLowerCase().contains("reply") ||
                        act.title.toString().toLowerCase().contains("답장")) {
                    Bundle bundle = sbn.getNotification().extras;
                    String sender = bundle.getString("android.title");
                    String msg = bundle.get("android.text").toString(); //멘션이 포함도니 경우는 String이 아니라서 Object로 가지고오고 .toString(); 사용
                    String room = bundle.getString(Build.VERSION.SDK_INT > 23 ? "android.summaryText" : "android.subText");
                    boolean isGroupChat = room != null;
                    if (room == null) room = sender;
                    response(room, msg, sender, isGroupChat, new Replier(this, act));
                }
            }
        }
    }

    private void response(String room, String msg, String sender, boolean isGroupChat, Replier replier) {
        //안에 알아서 구현
        toast("room: " + room + "\nmsg: " + msg + "\nsender: " + sender + "\nisGroupChat: " + isGroupChat);
        if (msg.equals("/test")) {
            replier.reply("test!!");
        }
    }

    private void toast(String msg) {
        Intent intent = new Intent(this, MainService.class);
        intent.putExtra("msg", msg);
        startService(intent);
    }

}
