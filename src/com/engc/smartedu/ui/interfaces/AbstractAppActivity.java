package com.engc.smartedu.ui.interfaces;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.engc.smartedu.support.asyncdrawable.TimeLineBitmapDownloader;
import com.engc.smartedu.support.error.WeiboException;
import com.engc.smartedu.support.settinghelper.SettingUtility;
import com.engc.smartedu.support.utils.GlobalContext;

/**
 * User: Jiang Qi Date: 12-7-31
 */
@SuppressLint("NewApi")
public class AbstractAppActivity extends Activity {
	public static ArrayList<BackPressHandler> mListeners = new ArrayList<BackPressHandler>();

	private int theme = 0;

	protected TimeLineBitmapDownloader commander = null;

	@Override
	protected void onResume() {
		super.onResume();
		GlobalContext.getInstance().setActivity(this);

		if (theme == SettingUtility.getAppTheme()) {

		} else {
			reload();
		}

	}
	
	


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("theme", theme);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			theme = SettingUtility.getAppTheme();
		} else {
			theme = savedInstanceState.getInt("theme");
		}
		setTheme(theme);
		super.onCreate(savedInstanceState);
		forceShowActionBarOverflowMenu();
		// initNFC();
		commander = new TimeLineBitmapDownloader();
	}

	

	private void forceShowActionBarOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ignored) {

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		commander.totalStopLoadPicture();
		commander = null;
	}

	private void initNFC() {
		NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			return;
		}

		mNfcAdapter.setNdefPushMessageCallback(
				new NfcAdapter.CreateNdefMessageCallback() {
					@Override
					public NdefMessage createNdefMessage(NfcEvent event) {
						String text = (GlobalContext.getInstance()
								.getCurrentAccountName());

						NdefMessage msg = new NdefMessage(
								new NdefRecord[] {
										createMimeRecord(
												"application/com.engc.smartedu.beam",
												text.getBytes()),
										NdefRecord
												.createApplicationRecord(getPackageName()) });
						return msg;
					}
				}, this);

	}

	private NdefRecord createMimeRecord(String mimeType, byte[] payload) {
		byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
		NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				mimeBytes, new byte[0], payload);
		return mimeRecord;
	}

	private void reload() {

		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();

		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	public TimeLineBitmapDownloader getBitmapDownloader() {
		return commander;
	}

	protected void dealWithException(WeiboException e) {
		Toast.makeText(this, e.getError(), Toast.LENGTH_SHORT).show();
	}
	
	 public static abstract interface BackPressHandler {

			public abstract void activityOnResume();

			public abstract void activityOnPause();

		}
}
