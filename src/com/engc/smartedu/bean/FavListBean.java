package com.engc.smartedu.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qii
 * Date: 12-8-18
 */
public class FavListBean extends ListBean<MessageBean, FavListBean> {
    private List<FavBean> favorites = new ArrayList<FavBean>();
    private List<MessageBean> actualStore = null;

    public List<FavBean> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<FavBean> favorites) {
        this.favorites = favorites;
    }

    @Override
    public int getSize() {
        return favorites.size();
    }


    @Override
    public MessageBean getItem(int position) {
        return favorites.get(position).getStatus();
    }

    @Override
    public List<MessageBean> getItemList() {
        if (actualStore == null) {
            actualStore = new ArrayList<MessageBean>();
            for (FavBean b : favorites) {
                actualStore.add(b.getStatus());
            }
        }
        return actualStore;
    }

    @Override
    public void addNewData(FavListBean newValue) {
        if (newValue != null && newValue.getSize() > 0) {

            this.getItemList().clear();
            this.getItemList().addAll(newValue.getItemList());
            this.setTotal_number(newValue.getTotal_number());

            this.favorites.clear();
            this.favorites.addAll(newValue.getFavorites());
        }
    }

    @Override
    public void addOldData(FavListBean oldValue) {
        if (oldValue != null && oldValue.getSize() > 0) {
            getItemList().addAll(oldValue.getItemList());
            setTotal_number(oldValue.getTotal_number());
            this.favorites.addAll(oldValue.getFavorites());
        }
    }
}
