package com.engc.smartedu.ui.userinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.engc.smartedu.bean.UserListBean;
import com.engc.smartedu.dao.user.FriendListDao;
import com.engc.smartedu.support.error.WeiboException;
import com.engc.smartedu.support.utils.GlobalContext;
import com.engc.smartedu.ui.actionmenu.MyFriendSingleChoiceModeListener;
import com.engc.smartedu.ui.actionmenu.NormalFriendShipSingleChoiceModeListener;
import com.engc.smartedu.ui.basefragment.AbstractFriendsFanListFragment;

/**
 * User: Jiang Qi
 * Date: 12-8-16
 */
public class FriendsListFragment extends AbstractFriendsFanListFragment {


    public FriendsListFragment() {

    }

    public FriendsListFragment(String uid) {
        super(uid);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(new FriendListOnItemLongClickListener());

    }


    @Override
    protected UserListBean getDoInBackgroundNewData() throws WeiboException {
        FriendListDao dao = new FriendListDao(GlobalContext.getInstance().getSpecialToken(), uid);
        dao.setCursor(String.valueOf(0));
        return dao.getGSONMsgList();
    }

    @Override
    protected UserListBean getDoInBackgroundOldData() throws WeiboException {

        if (getList().getUsers().size() > 0 && Integer.valueOf(getList().getNext_cursor()) == 0) {
            return null;
        }

        FriendListDao dao = new FriendListDao(GlobalContext.getInstance().getSpecialToken(), uid);
        if (getList().getUsers().size() > 0) {
            dao.setCursor(String.valueOf(bean.getNext_cursor()));
        }
        UserListBean result = dao.getGSONMsgList();

        return result;
    }


    private class FriendListOnItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            if (position - 1 < getList().getUsers().size() && position - 1 >= 0) {
                if (mActionMode != null) {
                    mActionMode.finish();
                    mActionMode = null;
                    getListView().setItemChecked(position, true);
                    getAdapter().notifyDataSetChanged();
                    if (currentUser.getId().equals(GlobalContext.getInstance().getCurrentAccountId())) {
                        mActionMode = getActivity().startActionMode(new MyFriendSingleChoiceModeListener(getListView(), getAdapter(), FriendsListFragment.this, bean.getUsers().get(position - 1)));
                    } else {
                        mActionMode = getActivity().startActionMode(new NormalFriendShipSingleChoiceModeListener(getListView(), getAdapter(), FriendsListFragment.this, bean.getUsers().get(position - 1)));
                    }
                    return true;
                } else {
                    getListView().setItemChecked(position, true);
                    getAdapter().notifyDataSetChanged();
                    if (currentUser.getId().equals(GlobalContext.getInstance().getCurrentAccountId())) {
                        mActionMode = getActivity().startActionMode(new MyFriendSingleChoiceModeListener(getListView(), getAdapter(), FriendsListFragment.this, bean.getUsers().get(position - 1)));
                    } else {
                        mActionMode = getActivity().startActionMode(new NormalFriendShipSingleChoiceModeListener(getListView(), getAdapter(), FriendsListFragment.this, bean.getUsers().get(position - 1)));
                    }
                    return true;
                }
            }
            return false;
        }
    }
}

