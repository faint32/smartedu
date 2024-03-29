package com.engc.smartedu.dao.maintimeline;

import com.engc.smartedu.bean.MessageBean;
import com.engc.smartedu.bean.MessageListBean;
import com.engc.smartedu.dao.URLHelper;
import com.engc.smartedu.dao.unread.ClearUnreadDao;
import com.engc.smartedu.support.database.DatabaseManager;
import com.engc.smartedu.support.error.WeiboException;
import com.engc.smartedu.support.http.HttpMethod;
import com.engc.smartedu.support.http.HttpUtility;
import com.engc.smartedu.support.settinghelper.SettingUtility;
import com.engc.smartedu.support.utils.AppLogger;
import com.engc.smartedu.support.utils.ListViewTool;
import com.engc.smartedu.support.utils.TimeTool;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: qii
 * Date: 12-7-28
 */
public class MainFriendsTimeLineDao {

    protected String getUrl() {
        return URLHelper.FRIENDS_TIMELINE;
    }

    private String getMsgListJson() throws WeiboException {
        String url = getUrl();

        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("since_id", since_id);
        map.put("max_id", max_id);
        map.put("count", count);
        map.put("page", page);
        map.put("base_app", base_app);
        map.put("feature", feature);
        map.put("trim_user", trim_user);

        String jsonData = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, map);
        try {
            new ClearUnreadDao(access_token, ClearUnreadDao.STATUS).clearUnread();
        } catch (WeiboException ignored) {

        }
        return jsonData;
    }

    public MessageListBean getGSONMsgList() throws WeiboException {

        String json = getMsgListJson();
        Gson gson = new Gson();

        MessageListBean value = null;
        try {
            value = gson.fromJson(json, MessageListBean.class);
        } catch (JsonSyntaxException e) {

            AppLogger.e(e.getMessage());
            return null;
        }
        if (value != null && value.getItemList().size() > 0) {
            List<MessageBean> msgList = value.getItemList();
            Iterator<MessageBean> iterator = msgList.iterator();

            List<String> filterWordList = DatabaseManager.getInstance().getFilterList();

            while (iterator.hasNext()) {
                MessageBean msg = iterator.next();
                if (msg.getUser() == null) {
                    iterator.remove();
                    value.removedCountPlus();
                } else if (SettingUtility.isEnableFilter() && ListViewTool.haveFilterWord(msg, filterWordList)) {
                    iterator.remove();
                    value.removedCountPlus();
                } else {
                    msg.getListViewSpannableString();
                    TimeTool.dealMills(msg);
                }
            }

        }


        return value;
    }


    protected String access_token;
    protected String since_id;
    protected String max_id;
    protected String count;
    protected String page;
    protected String base_app;
    protected String feature;
    protected String trim_user;

    public MainFriendsTimeLineDao(String access_token) {

        this.access_token = access_token;
        this.count = SettingUtility.getMsgCount();
    }

    public MainFriendsTimeLineDao setSince_id(String since_id) {
        this.since_id = since_id;
        return this;
    }

    public MainFriendsTimeLineDao setMax_id(String max_id) {
        this.max_id = max_id;
        return this;
    }

    public MainFriendsTimeLineDao setCount(String count) {
        this.count = count;
        return this;
    }

    public MainFriendsTimeLineDao setPage(String page) {
        this.page = page;
        return this;
    }

    public MainFriendsTimeLineDao setBase_app(String base_app) {
        this.base_app = base_app;
        return this;
    }

    public MainFriendsTimeLineDao setFeature(String feature) {
        this.feature = feature;
        return this;
    }

    public MainFriendsTimeLineDao setTrim_user(String trim_user) {
        this.trim_user = trim_user;
        return this;
    }


}
