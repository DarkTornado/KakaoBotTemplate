package com.darktornado.jsapi;

import android.content.Context;
import android.widget.Toast;

import com.darktornado.kakaobottemplate.KakaotalkListener;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

public class Api extends ScriptableObject {

    @Override
    public String getClassName() {
        return "Api";
    }

    @JSStaticFunction
    public static Context getContext() {
        return KakaotalkListener.ctx;
    }

    @JSStaticFunction
    public static void showToast(final String msg, final int leng) {
        KakaotalkListener.handler.post(() -> Toast.makeText(KakaotalkListener.ctx, msg, leng).show()); //일반 쓰레드 안에서 호출하는거 대응
    }

}
