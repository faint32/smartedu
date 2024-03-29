package com.engc.smartedu.othercomponent.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.engc.smartedu.R;

import com.engc.smartedu.bean.AccountBean;
import com.engc.smartedu.bean.CommentListBean;
import com.engc.smartedu.bean.MessageListBean;
import com.engc.smartedu.bean.UnreadBean;
import com.engc.smartedu.support.settinghelper.SettingUtility;
import com.engc.smartedu.support.utils.Utility;
import com.engc.smartedu.ui.main.MainTimeLineActivity;

/**
 * User: qii
 * Date: 12-12-5
 */
public class ICSNotification {

    private Context context;

    private AccountBean accountBean;

    private CommentListBean comment;
    private MessageListBean repost;
    private CommentListBean mentionCommentsResult;

    private UnreadBean unreadBean;

    public ICSNotification(Context context,
                           AccountBean accountBean,
                           CommentListBean comment,
                           MessageListBean repost,
                           CommentListBean mentionCommentsResult, UnreadBean unreadBean) {
        this.context = context;
        this.accountBean = accountBean;
        this.comment = comment;
        this.repost = repost;
        this.mentionCommentsResult = mentionCommentsResult;
        this.unreadBean = unreadBean;
    }

    private PendingIntent getPendingIntent() {
        Intent i = new Intent(context, MainTimeLineActivity.class);
        i.putExtra("account", accountBean);
        i.putExtra("comment", comment);
        i.putExtra("repost", repost);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, Long.valueOf(accountBean.getUid()).intValue(), i, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private String getTicker() {
        int mentionCmt = unreadBean.getMention_cmt();
        int mentionStatus = unreadBean.getMention_status();
        int mention = mentionStatus + mentionCmt;
        int cmt = unreadBean.getCmt();

        StringBuilder stringBuilder = new StringBuilder();
        if (mention > 0) {
            String txt = String.format(context.getString(R.string.new_mentions), String.valueOf(mention));
            stringBuilder.append(txt);
        }

        if (cmt > 0) {
            if (mention > 0)
                stringBuilder.append("、");
            String txt = String.format(context.getString(R.string.new_comments), String.valueOf(cmt));
            stringBuilder.append(txt);
        }
        return stringBuilder.toString();
    }

    private int getCount() {
        int count = 0;

        if (SettingUtility.allowMentionToMe()) {
            count += unreadBean.getMention_status();
        }

        if (SettingUtility.allowMentionToMe()) {
            count += unreadBean.getCmt();
        }

        if (SettingUtility.allowMentionCommentToMe()) {
            count += unreadBean.getMention_cmt();
        }

        return count;

    }


    @SuppressLint("NewApi")
	public Notification get() {

        Notification.Builder builder = new Notification.Builder(context)
                .setTicker(getTicker())
                .setContentTitle(getTicker())
                .setContentText(accountBean.getUsernick())
                .setSmallIcon(R.drawable.notification)
                .setAutoCancel(true)
                .setContentIntent(getPendingIntent())
                .setOnlyAlertOnce(true);


        if (getCount() > 1) {
            builder.setNumber(getCount());
        }

        Utility.configVibrateLedRingTone(builder);

        return builder.getNotification();
    }


}
