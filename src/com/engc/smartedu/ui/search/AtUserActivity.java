package com.engc.smartedu.ui.search;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import com.engc.smartedu.R;

import com.engc.smartedu.ui.interfaces.AbstractAppActivity;

/**
 * User: qii
 * Date: 12-10-8
 */
@SuppressLint("NewApi")
public class AtUserActivity extends AbstractAppActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.at_other);

        String token = getIntent().getStringExtra("token");

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new AtUserFragment(token))
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
