package com.engc.smartedu.ui.preference;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import com.engc.smartedu.R;

import com.engc.smartedu.ui.interfaces.AbstractAppActivity;

/**
 * User: qii
 * Date: 12-10-4
 */
@SuppressLint("NewApi")
public class AppearanceActivity extends AbstractAppActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getString(R.string.appearance));

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new AppearanceFragment())
                    .commit();
        }

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        if (key.equals(SettingActivity.THEME)) {

            Intent intent = new Intent(this, AppearanceActivity.class);
            //        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();

            overridePendingTransition(0, 0);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.stay, R.anim.alphaout);


//            finish();
//            overridePendingTransition(0, 0);
//            startActivity(new Intent(this, AppearanceActivity.class));
//            overridePendingTransition(R.anim.stay, R.anim.alphaout);
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        finish();
    }
}
