package com.engc.smartedu.dao.login;

import com.engc.smartedu.bean.UserBean;
import com.engc.smartedu.dao.URLHelper;
import com.engc.smartedu.support.error.WeiboException;
import com.engc.smartedu.support.http.HttpMethod;
import com.engc.smartedu.support.http.HttpUtility;
import com.engc.smartedu.support.utils.AppLogger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Jiang Qi
 * Date: 12-7-30
 */
public class OAuthDao {

    private String access_token;

    public OAuthDao(String access_token) {

        this.access_token = access_token;
    }

    public UserBean getOAuthUserInfo() throws WeiboException {

        String uidJson = getOAuthUserUIDJsonData();
        String uid = "";

        try {
            JSONObject jsonObject = new JSONObject(uidJson);
            uid = jsonObject.optString("uid");
        } catch (JSONException e) {
            AppLogger.e(e.getMessage());
        }


        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", uid);
        map.put("access_token", access_token);

        String url = URLHelper.USER_SHOW;

        String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, map);


        Gson gson = new Gson();
        UserBean user = new UserBean();
        try {
            user = gson.fromJson(result, UserBean.class);
        } catch (JsonSyntaxException e) {
            AppLogger.e(result);
        }

        return user;
    }

    private String getOAuthUserUIDJsonData() throws WeiboException {

        String url = URLHelper.UID;
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);

        return HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, map);

    }

}
