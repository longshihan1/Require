package com.longshihan.require.base;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.longshihan.require.App;
import com.longshihan.require.R;
import com.longshihan.require.utils.http.RespHandleListener;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/22.
 * 类描述：Activity的baseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final int ACTIVITY_RESUME = 0;
    private static final int ACTIVITY_PAUSE = 2;

    InputMethodManager _inputMethodManager;
    protected Resources res;
    protected App baseApp;
    protected static final String TAG = BaseFragment.class.getName();
    protected Toast mToast;

    public int activityState;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_CONTACTS
            , Manifest.permission.GET_ACCOUNTS
            , Manifest.permission.READ_CONTACTS
            , Manifest.permission.READ_CALL_LOG
            , Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.CALL_PHONE
            , Manifest.permission.WRITE_CALL_LOG
            , Manifest.permission.USE_SIP
            , Manifest.permission.PROCESS_OUTGOING_CALLS
            , Manifest.permission.ADD_VOICEMAIL
            , Manifest.permission.READ_CALENDAR
            , Manifest.permission.WRITE_CALENDAR
            , Manifest.permission.CAMERA
            , Manifest.permission.CAMERA
            , Manifest.permission.BODY_SENSORS
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.READ_SMS
            , Manifest.permission.RECEIVE_WAP_PUSH
            , Manifest.permission.RECEIVE_MMS
            , Manifest.permission.RECEIVE_SMS
            , Manifest.permission.SEND_SMS
    };

    // 是否允许全屏
    private boolean mAllowFullScreen = true;

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();

    protected abstract int getLayout();


    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout());
        res = this.getApplicationContext().getResources();
        ButterKnife.bind(this);
        baseApp = (App) this.getApplication();
        getSupportActionBar().hide();

        _inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏锁定
    /*    if (mAllowFullScreen) {
            getSupportActionBar().hide(); // 取消标题
        }*/
        verifyStoragePermissions(this);

        initView();
        initData();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = ACTIVITY_RESUME;
    }


    @Override
    protected void onPause() {
        super.onPause();
        activityState = ACTIVITY_PAUSE;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseApp.removeActivity(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null
                    && getCurrentFocus().getWindowToken() != null) {
                _inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    //网络错误种类
    protected int getNetworkErrorTip(int code) {

        Log.d(TAG, "getNetworkErrorTip - code = \"" + code + "\"");

        int textResId = R.string.error_network_time_out;
        switch (code) {

            case RespHandleListener.ErrCode.ERR_NETWORK_NOT_AVAILABLE:
                textResId = R.string.error_network_not_available;
                break;

            case RespHandleListener.ErrCode.ERR_SERVER_ERROR:
                textResId = R.string.error_network_server_busy;
                break;

            case RespHandleListener.ErrCode.ERR_TIME_OUT:
            case RespHandleListener.ErrCode.ERR_CLIENT_ERROR:
            case RespHandleListener.ErrCode.ERR_UNKNOWN_ERROR:
                break;

            default:
                break;

        }
        Log.d(TAG, "getNetworkErrorTip - textResId = \"" + textResId + "\"");
        return textResId;
    }

    public static void verifyStoragePermissions(AppCompatActivity activity) {
        // Check if we have write permission
        //访问媒体文件的权限
        int WRITE_EXTERNAL_STORAGE = ActivityCompat.checkSelfPermission(activity, Manifest
                .permission.WRITE_EXTERNAL_STORAGE);
        if (WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            // 我们没有权限，以提示用户
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    public void showToast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), resId,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    //隐藏软键盘
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager
                            .HIDE_NOT_ALWAYS);
        }
    }
}
