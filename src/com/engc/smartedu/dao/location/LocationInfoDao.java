package com.engc.smartedu.dao.location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.engc.smartedu.bean.GeoBean;
import com.engc.smartedu.dao.URLHelper;
import com.engc.smartedu.support.error.WeiboException;
import com.engc.smartedu.support.http.HttpMethod;
import com.engc.smartedu.support.http.HttpUtility;
import com.engc.smartedu.support.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * User: qii
 * Date: 12-8-29
 */
public class LocationInfoDao {

    private double[] latlng = {0.0, 0.0};

    public LocationInfoDao(GeoBean bean) {
        this.latlng[0] = bean.getLat();
        this.latlng[1] = bean.getLon();
    }

    private String getLatlng() {
        return String.valueOf(latlng[0]) + "," + String.valueOf(latlng[1]);
    }

    public String getInfo() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("language", "zh-CN");
        map.put("sensor", "false");
        map.put("latlng", getLatlng());

        String url = URLHelper.GOOGLELOCATION;

        String jsonData = null;
        try {
            jsonData = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, map);
        } catch (WeiboException e) {
            AppLogger.e(e.getMessage());
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray results = jsonObject.optJSONArray("results");
            JSONObject jsonObject1 = results.getJSONObject(0);
            String formatAddress = jsonObject1.optString("formatted_address");
            int index = formatAddress.indexOf(" ");
            if (index > 0) {
                String location = formatAddress.substring(0, index);
                return location;
            }else {
                return formatAddress;
            }
        } catch (JSONException e) {
            AppLogger.e(e.getMessage());
        }

        return "";
    }
}
