package com.engc.smartedu.ui.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import com.engc.smartedu.R;

import com.engc.smartedu.bean.AtUserBean;
import com.engc.smartedu.dao.search.AtUserDao;
import com.engc.smartedu.support.error.WeiboException;
import com.engc.smartedu.support.lib.MyAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qii
 * Date: 12-10-8
 */
@SuppressLint({ "NewApi", "ValidFragment" })
public class AtUserFragment extends ListFragment {

    private ArrayAdapter<String> adapter;

    private List<String> result = new ArrayList<String>();
    private List<AtUserBean> atList = new ArrayList<AtUserBean>();

    private String token;

    private AtUserTask task;


    public AtUserFragment() {

    }

    public AtUserFragment(String token) {
        this.token = token;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (task != null)
            task.cancel(true);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("token", token);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            token = savedInstanceState.getString("token");
        }
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, result);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("name", "@" + atList.get(position).getNickname() + " ");
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu_atuserfragment, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.at_other));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    if (task != null) {
                        task.cancel(true);
                    }
                    task = new AtUserTask(newText);
                    task.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    result.clear();
                    atList.clear();
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }


    class AtUserTask extends MyAsyncTask<Void, List<AtUserBean>, List<AtUserBean>> {
        WeiboException e;
        String q;

        public AtUserTask(String q) {
            this.q = q;
        }

        @Override
        protected List<AtUserBean> doInBackground(Void... params) {
            AtUserDao dao = new AtUserDao(token, q);
            try {
                return dao.getUserInfo();
            } catch (WeiboException e) {
                this.e = e;
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<AtUserBean> atUserBeans) {
            super.onPostExecute(atUserBeans);
            if (isCancelled())
                return;
            if (atUserBeans == null||atUserBeans.size() == 0) {
                result.clear();
                atList.clear();
                adapter.notifyDataSetChanged();
                return;
            }

            result.clear();
            for (AtUserBean b : atUserBeans) {
                if (b.getRemark().contains(q)) {
                    result.add(b.getNickname() + "(" + b.getRemark() + ")");
                } else {
                    result.add(b.getNickname());
                }
            }
            atList = atUserBeans;
            adapter.notifyDataSetChanged();
        }
    }
}
