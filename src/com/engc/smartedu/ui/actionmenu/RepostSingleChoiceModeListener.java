package com.engc.smartedu.ui.actionmenu;

import android.app.Fragment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.engc.smartedu.bean.MessageBean;
import com.engc.smartedu.ui.adapter.StatusListAdapter;

/**
 * User: qii
 * Date: 12-9-9
 */
public class RepostSingleChoiceModeListener extends StatusSingleChoiceModeListener {
    LinearLayout quick_repost;
    int initState;

    public RepostSingleChoiceModeListener(ListView listView, StatusListAdapter adapter, Fragment activity, LinearLayout quick_repost, MessageBean bean) {
        super(listView, adapter, activity, bean);
        this.quick_repost = quick_repost;
        initState = this.quick_repost.getVisibility();

    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        if (initState == View.VISIBLE)
            quick_repost.setVisibility(View.GONE);
        return super.onCreateActionMode(mode, menu);

    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if (initState == View.VISIBLE)
            quick_repost.setVisibility(View.VISIBLE);
        super.onDestroyActionMode(mode);
    }
}