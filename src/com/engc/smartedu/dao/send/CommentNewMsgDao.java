package com.engc.smartedu.dao.send;

import com.engc.smartedu.bean.CommentBean;
import com.engc.smartedu.dao.URLHelper;
import com.engc.smartedu.support.error.WeiboException;
import com.engc.smartedu.support.http.HttpMethod;
import com.engc.smartedu.support.http.HttpUtility;
import com.engc.smartedu.support.utils.AppLogger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;

/**
 * User: qii
 * Date: 12-8-13
 */
public class CommentNewMsgDao {
    public CommentBean sendNewMsg() throws WeiboException {
        String url = URLHelper.COMMENT_CREATE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("id", id);
        map.put("comment", comment);
        map.put("comment_ori", comment_ori);


        String jsonData = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post, url, map);

        Gson gson = new Gson();

        CommentBean value = null;
        try {
            value = gson.fromJson(jsonData, CommentBean.class);
        } catch (JsonSyntaxException e) {

            AppLogger.e(e.getMessage());
        }


        return value;

    }

    public CommentNewMsgDao(String token, String id, String comment) {

        this.access_token = token;
        this.id = id;
        this.comment = comment;
    }


    public void enableComment_ori(boolean enable) {
        if (enable)
            this.comment_ori = "1";
        else
            this.comment_ori = "0";
    }

    private String access_token;
    private String id;
    private String comment;
    private String comment_ori;
}
