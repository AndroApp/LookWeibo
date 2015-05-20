package com.shine.look.weibo.ui.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shine.look.weibo.R;

/**
 * User:Shine
 * Date:2015-05-19
 * Description:
 */
public class SendMsgBroadcastReceiver extends BroadcastReceiver {

    public static final String BROADCAST_SEND_MSG = "com.shine.look.weibo.sendmsg";
    public static final String ARG_SEND_STATUS = "com.shine.look.weibo.sendStatu";
    public static final int SEND_STATUS_REQUEST = 1;
    public static final int SEND_STATUS_SUCCESS = 2;
    public static final int SEND_STATUS_FAILURE = 3;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            int status = intent.getIntExtra(ARG_SEND_STATUS, 0);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification.Builder builder = new Notification.Builder(context);
            switch (status) {
                case SEND_STATUS_REQUEST:
                    builder.setSmallIcon(R.drawable.ic_action_send_msg);
                    builder.setTicker(context.getString(R.string.sending));
                    break;
                case SEND_STATUS_SUCCESS:
                    builder.setSmallIcon(R.drawable.ic_action_done);
                    builder.setTicker(context.getString(R.string.send_success));
                    break;
                case SEND_STATUS_FAILURE:
                    builder.setSmallIcon(R.drawable.ic_action_error);
                    builder.setTicker(context.getString(R.string.send_failure));
                    break;
            }
            Notification notification = builder.getNotification();
            manager.notify(status, notification);
            manager.cancel(status);
        }
    }
}
