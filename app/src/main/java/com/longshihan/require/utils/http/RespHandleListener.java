package com.longshihan.require.utils.http;

/**
 * Created by Administrator on 2016/4/11.
 * 项目名称：AsFrame
 * 类描述：网络判断的工具接口
 * 创建人：longshihan
 * 创建时间：2016/4/11 14:21
 * 修改人：Administrator
 * 修改时间：2016/4/11 14:21
 * 修改备注：
 * 邮箱： longshihan@163.com
 */
public interface RespHandleListener {
    class ErrCode {

        public static final int ERR_SUCCEED = 0;
        public static final int ERR_NETWORK_NOT_AVAILABLE = -1;
        public static final int ERR_TIME_OUT = -2;
        public static final int ERR_SERVER_ERROR = -3;
        public static final int ERR_CLIENT_ERROR = -4;
        public static final int ERR_UNKNOWN_ERROR = -5;

    }
    void onError(int code);
    void onReqBegin();
    void onReqEnd(String jsonResp);
}
