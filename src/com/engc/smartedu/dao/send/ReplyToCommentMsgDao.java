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
 * Date: 12-8-28
 */
public class ReplyToCommentMsgDao {

    public CommentBean reply() throws WeiboException {
        String url = URLHelper.COMMENT_REPLY;
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("id", id);
        map.put("cid", cid);
        map.put("comment", comment);
        map.put("comment_ori", comment_ori);
        map.put("without_mention", without_mention);


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

    public ReplyToCommentMsgDao(String token, CommentBean bean, String replyContent) {
        this.access_token = token;
        this.cid = bean.getId();
        this.id = bean.getStatus().getId();
        this.comment = replyContent;
    }

    private String access_token;
    private String cid;
    private String id;
    private String comment;
    private String without_mention;
    private String comment_ori;
}
