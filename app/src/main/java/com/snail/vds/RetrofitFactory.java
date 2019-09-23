package com.snail.vds;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yongjie created on 2019-08-04.
 */
public class RetrofitFactory {
    private volatile static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            synchronized (RetrofitFactory.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(buildOkHttpClient())
                            .baseUrl("https://vdsblock.io/")
                            .build();
                }
            }
        }

        return retrofit;
    }

    public static OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS).build();
    }


    private RetrofitFactory() {
    }

}
