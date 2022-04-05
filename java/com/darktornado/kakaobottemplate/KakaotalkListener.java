package com.darktornado.kakaobottemplate;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.HashMap;

public class KakaotalkListener extends NotificationListenerService {

    /* 임시 전원 스위치 */
    public static boolean botEnabled = false;

    /* JS 및 관련 API 구현용 */
    public static RhinoAdapter js;
    public static Context ctx;
    public static Handler handler;

    /* Api.replyRoom(room, msg); 구현용 */
    public static HashMap<String, Replier> sessions = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        handler = new Handler(); //서비스에는 runOnUiThread가 없어요
        js = new RhinoAdapter(this);
    }

    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if(!botEnabled) return;
        if (!sbn.getPackageName().equals("com.kakao.talk")) return; //블록 너무 깊어서 반대로 적음

        Notification.Action[] actions = getActions(sbn.getNotification());
        for (Notification.Action act : actions) {
            if (act.getRemoteInputs() != null && act.getRemoteInputs().length > 0) {
                if (act.title.toString().toLowerCase().contains("reply") ||
                        act.title.toString().toLowerCase().contains("답장")) {
                    Bundle bundle = sbn.getNotification().extras;

                    /* 채팅 보낸사람 */
                    String sender = bundle.getString("android.title");

                    /* 채팅 내용
                     * 멘션이 포함되 경우는 String이 아님 */
                    String msg = bundle.get("android.text").toString();

                    /* 채팅이 수신된 방
                     * 안드로이드 버전 및 카카오톡 버전에 따라 room이 담긴 곳이 다름
                     * 그냥 이렇게 하면 알아서 잘 처리됨 */
                    String room = bundle.getString("android.subText");
                    if (room == null) room = bundle.getString("android.summaryText");

                    /* 1:1채팅방/단체채팅방 구분
                     * 안드로이드 버전 및 카카오톡 버전에 따라 알림에 정보가 담겨있기도 함 */
                    boolean isGroupChat = room != null;


                    /* 1:1채팅방 방 이름 처리 */
                    if (room == null) room = sender;

                    /* 세션 캐싱 객체 */
                    Replier replier = new Replier(this, act);

                    /* Api.replyRoom(room, msg); 같은거 구현용 */
                    sessions.put(room, replier);


                    /* 자바 및 코틀린. 아무튼 앱 내부 */
                    response(room, msg, sender, isGroupChat, replier);

                    /* 자바스크립트 */
                    js.callEventListener("response", new Object[]{room, msg, sender, isGroupChat, replier});
                }
            }
        }
    }

    private void response(String room, String msg, String sender, boolean isGroupChat, Replier replier) {
        //안에 알아서 구현
        toast("room: " + room + "\nmsg: " + msg + "\nsender: " + sender + "\nisGroupChat: " + isGroupChat);
//        if (msg.equals("/test")) {
//            replier.reply("test!!");
//        }
    }

    private Notification.Action[] getActions(Notification noti) {
        Notification.Action[] acts = noti.actions;
        if (acts.length > 0) return acts;
        
        /* 카카오톡 9.7.5부터는 아래 방식으로는 Action이 나오지 않음 */
        Notification.WearableExtender wExt = new Notification.WearableExtender(noti);
        return (Notification.Action[]) wExt.getActions().toArray(new Notification.Action[0]);
    }

    private void toast(String msg) {
        Intent intent = new Intent(this, MainService.class);
        intent.putExtra("msg", msg);
        startService(intent);
    }

}
