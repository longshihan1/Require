package com.longshihan.require.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

public class BaseFragment extends FragmentActivity {
	private final static int ACTIVITY_LOGIN_REQ = 10000;
	public final static int LOGIN_RESULT = 10002;
	public final static String LOGIN_RESULT_CODE = "login_result";
	private InputMethodManager imm;
	private Intent mIntent;
	private int req;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		int reqCode = -1;
		if (mIntent != null) {
			reqCode = mIntent.getIntExtra("req", -1);
			switch (requestCode) {
			case ACTIVITY_LOGIN_REQ:
				if (resultCode == LOGIN_RESULT) {
					boolean isLogin = data.getBooleanExtra(LOGIN_RESULT_CODE,
							false);
					if (isLogin) {
						if (reqCode == -1) {
							startActivity(mIntent);
						} else {
							startActivityForResult(mIntent, reqCode);
						}
					}
				}
				break;
			}
		}
		FragmentManager manager = getSupportFragmentManager();
		List<Fragment> list = manager.getFragments();
		if (list != null && !list.isEmpty()) {
			final int count = list.size();
			for (int i = 0; i < count; i++) {
				Fragment fragment = list.get(i);
				if (fragment != null && fragment.isVisible()) {
					fragment.onActivityResult(requestCode, resultCode, data);
				}
			}
		}
	}

	public void startActivity(Intent intent, boolean checkLogin) {
		/*mIntent = intent;
		if (mIntent != null) {
			if (checkLogin) {
				if (!LoginUtil.isLogin()) {
					Intent newIntent = new Intent(this, Register1Activity.class);
					startActivityForResult(newIntent, ACTIVITY_LOGIN_REQ);
				} else {
					startActivity(intent);
				}
			} else {
				startActivity(intent);
			}
		}*/
	}

	public void startActivityForResult(Intent intent, int reqCode,
			boolean checkLogin) {
		mIntent = intent;
		/*if (mIntent != null) {
			mIntent.putExtra("REQ", reqCode);
			if (checkLogin) {
				if (!LoginUtil.isLogin()) {
					Intent newIntent = new Intent(this, Register1Activity.class);
					startActivityForResult(newIntent, ACTIVITY_LOGIN_REQ);
				} else {
					startActivityForResult(intent, req);
				}
			} else {
				startActivityForResult(intent, req);
			}
		}*/
	}

	public void hideInputFromwindow() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && getCurrentFocus() != null) {
			if (getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	public void toPage(Class<?> c) {
		hideInputFromwindow();
		Intent intent = new Intent(this, c);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void toPage(Class<?> c, Bundle bundle) {
		hideInputFromwindow();
		Intent intent = new Intent(this, c);
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
