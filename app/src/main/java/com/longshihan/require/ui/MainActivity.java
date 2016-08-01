package com.longshihan.require.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.longshihan.require.R;
import com.longshihan.require.response.GetIpInfoResponse;
import com.longshihan.require.server.ApiService;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String ENDPOINT = "http://ip.taobao.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService=retrofit.create(ApiService.class);


        Call<GetIpInfoResponse> call=apiService.getIpInfo("63.223.108.42");
        call.enqueue(new Callback<GetIpInfoResponse>() {
            @Override
            public void onResponse(Response<GetIpInfoResponse> response, Retrofit retrofit) {
                GetIpInfoResponse getIpInfoResponse = response.body();
                Toast.makeText(MainActivity.this,getIpInfoResponse.data.country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
