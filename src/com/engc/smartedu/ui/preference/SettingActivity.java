package com.engc.smartedu.ui.preference;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.engc.smartedu.R;

import com.engc.smartedu.ui.interfaces.AbstractAppActivity;
import com.engc.smartedu.ui.main.MainTimeLineActivity;

/**
 * User: Jiang Qi
 * Date: 12-8-6
 */
@SuppressLint("NewApi")
public class SettingActivity extends AbstractAppActivity {

    public static final String SOUND = "sound";
    public static final String AUTO_REFRESH = "auto_refresh";

    //appearance
    public static final String THEME = "theme";
    public static final String LIST_AVATAR_MODE = "list_avatar_mode";
    public static final String LIST_PIC_MODE = "list_pic_mode";
    public static final String LIST_HIGH_PIC_MODE = "list_high_pic_mode";
    public static final String LIST_FAST_SCROLL = "list_fast_scroll";
    public static final String FONT_SIZE = "font_size";
    public static final String SHOW_BIG_PIC = "show_big_pic";
    public static final String SHOW_BIG_AVATAR = "show_big_avatar";


    //notification
    public static final String FREQUENCY = "frequency";
    public static final String ENABLE_FETCH_MSG = "enable_fetch_msg";
    public static final String DISABLE_FETCH_AT_NIGHT = "disable_fetch_at_night";
    public static final String ENABLE_VIBRATE = "vibrate";
    public static final String ENABLE_LED = "led";
    public static final String ENABLE_RINGTONE = "ringtone";
    public static final String JBNOTIFICATION_STYLE = "jbnotification";
    public static final String ENABLE_MENTION_TO_ME = "mention_to_me";
    public static final String ENABLE_COMMENT_TO_ME = "comment_to_me";
    public static final String ENABLE_MENTION_COMMENT_TO_ME = "mention_comment_to_me";


    //filter
    public static final String FILTER = "filter";


    //traffic control
    public static final String UPLOAD_PIC_QUALITY = "upload_pic_quality";
    public static final String COMMENT_REPOST_AVATAR = "comment_repost_list_avatar";
    public static final String SHOW_COMMENT_REPOST_AVATAR = "show_comment_repost_list_avatar";
    public static final String DISABLE_DOWNLOAD_AVATAR_PIC = "disable_download";
    public static final String MSG_COUNT = "msg_count";

    //performance
    public static final String HARDWARE_ACCELERATED = "pref_hardware_accelerated_key";

    //about
    public static final String OFFICIAL_WEIBO = "pref_official_weibo_key";
    public static final String SUGGEST = "pref_suggest_key";
    public static final String VERSION = "pref_version_key";
    public static final String RECOMMEND = "pref_recommend_key";
    public static final String DONATE = "pref_donate_key";
    public static final String CACHE_PATH = "pref_cache_path_key";
    public static final String SAVED_PIC_PATH = "pref_saved_pic_path_key";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getString(R.string.setting));

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(this, MainTimeLineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
        }
        return false;
    }


}


