package com.longshihan.require;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.longshihan.require.utils.Error.LocalFileHandler;
import com.longshihan.require.utils.FileCache;

import java.util.Stack;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * @author Administrator
 * @time 2016/8/1 14:24
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class App extends Application {
    private static App _app;
    //需要共享的数据
    private int shareData;
    /*
   * 初始化TAG
   * */
    private static String TAG = App.class.getName();

    /*Activity堆*/
    private Stack<AppCompatActivity> activityStack = new Stack<AppCompatActivity>();

    public static App getInstance(){
        return _app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _app=this;
        //配置程序异常退出处理
        Thread.setDefaultUncaughtExceptionHandler(new LocalFileHandler(this));
        init();
    }

    private void init() {
        //bmob
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
        //设置appkey
        .setApplicationId("9f41f7b7dff18b6c97646d7bb4bacc73")
        //请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        //文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        //文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);
        //fresco
        Fresco.initialize(this);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //		DBManager.getDBManager(this).closeDB();
        FileCache.clear(this);
        _app = null;
    }

    //获取共享的数据
    public int getShareData() {
        return shareData;
    }

    public void setShareData(int shareData) {
        this.shareData = shareData;
    }
    /*打印出一些app的参数*/
    private void printAppParameter() {
        Log.d(TAG, "OS : " + Build.VERSION.RELEASE + " ( " + Build.VERSION.SDK_INT + " )");

    }

    public void addActivity(final AppCompatActivity curAT) {
        if (null == activityStack) {
            activityStack = new Stack<AppCompatActivity>();
        }
        activityStack.add(curAT);
    }

    public void removeActivity(final AppCompatActivity curAT) {
        if (null == activityStack) {
            activityStack = new Stack<AppCompatActivity>();
        }
        activityStack.remove(curAT);
    }

    //获取最后一个Activity
    public AppCompatActivity currentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        AppCompatActivity activity = activityStack.lastElement();
        return activity;
    }

    //返回寨内Activity的总数
    public int howManyActivities() {
        return activityStack.size();
    }

    //关闭所有Activity
    public void finishAllActivities() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        AppCompatActivity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(AppCompatActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

}
