package com.engc.smartedu.dao.maintimeline;

import com.engc.smartedu.bean.MessageBean;
import com.engc.smartedu.bean.MessageListBean;
import com.engc.smartedu.dao.URLHelper;
import com.engc.smartedu.support.database.DatabaseManager;
import com.engc.smartedu.support.error.WeiboException;
import com.engc.smartedu.support.http.HttpMethod;
import com.engc.smartedu.support.http.HttpUtility;
import com.engc.smartedu.support.settinghelper.SettingUtility;
import com.engc.smartedu.support.utils.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: qii
 * Date: 12-10-17
 */
public class FriendGroupTimeLineDao extends MainFriendsTimeLineDao {

    protected String getUrl() {
        return URLHelper.FRIENDSGROUP_TIMELINE;
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
        map.put("list_id", list_id);

        String jsonData = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, map);

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
                } else if (SettingUtility.isEnableFilter() && ListViewTool.haveFilterWord(msg, filterWordList)) {
                    iterator.remove();
                } else {
                    msg.getListViewSpannableString();
                    TimeTool.dealMills(msg);
                }
            }

        }


        return value;
    }


    public FriendGroupTimeLineDao(String access_token, String list_id) {

        super(access_token);
        this.list_id = list_id;
    }

    private String list_id;
}
