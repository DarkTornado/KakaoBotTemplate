package com.darktornado.kakaobottemplate;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Replier {
    final private Context ctx;
    final private Notification.Action session;

    public Replier(Context ctx, Notification.Action act) {
        super();
        this.ctx = ctx;
        session = act;
    }

    public void reply(String value) {
        Intent intent = new Intent();
        Bundle msg = new Bundle();
        for (RemoteInput inputable : session.getRemoteInputs()) {
            msg.putCharSequence(inputable.getResultKey(), value);
        }
        RemoteInput.addResultsToIntent(session.getRemoteInputs(), intent, msg);

        try {
            session.actionIntent.send(ctx, 0, intent);
        } catch (PendingIntent.CanceledException e) {

        }
    }

}
