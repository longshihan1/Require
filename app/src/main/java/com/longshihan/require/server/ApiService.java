package com.longshihan.require.server;

import com.longshihan.require.response.GetIpInfoResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * 定义接口
 * @author Administrator
 * @time 2016/7/29 17:36
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public interface ApiService {
    @GET("service/getIpInfo.php")
    Call<GetIpInfoResponse> getIpInfo(@Query("ip") String ip);
}
