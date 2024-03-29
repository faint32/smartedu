package com.engc.smartedu.ui.task;

import android.widget.Toast;
import com.engc.smartedu.R;

import com.engc.smartedu.bean.FavBean;
import com.engc.smartedu.dao.fav.FavDao;
import com.engc.smartedu.support.error.WeiboException;
import com.engc.smartedu.support.lib.MyAsyncTask;
import com.engc.smartedu.support.utils.GlobalContext;

/**
 * User: qii
 * Date: 12-9-12
 */
public class FavAsyncTask extends MyAsyncTask<Void, FavBean, FavBean> {

    private String token;
    private String id;
    private WeiboException e;

    public FavAsyncTask(String token, String id) {
        this.token = token;
        this.id = id;
    }

    @Override
    protected FavBean doInBackground(Void... params) {
        FavDao dao = new FavDao(token, id);
        try {
            return dao.favIt();
        } catch (WeiboException e) {
            this.e = e;
            cancel(true);
            return null;
        }
    }

    @Override
    protected void onCancelled(FavBean favBean) {
        super.onCancelled(favBean);
        if (favBean == null && this.e != null)
            Toast.makeText(GlobalContext.getInstance(), e.getError(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(FavBean favBean) {
        super.onPostExecute(favBean);
        if (favBean != null)
            Toast.makeText(GlobalContext.getInstance(), GlobalContext.getInstance().getString(R.string.fav_successfully), Toast.LENGTH_SHORT).show();
    }
}
