package com.engc.smartedu.ui.preference;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.engc.smartedu.R;

import com.engc.smartedu.ui.interfaces.AbstractAppActivity;

/**
 * User: qii
 * Date: 12-10-24
 */
@SuppressLint("NewApi")
public class NotificationActivity extends AbstractAppActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getString(R.string.notification));

        if (savedInstanceState == null) {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new NotificationFragment())
                .commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(this, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
        }
        return false;
    }

}
