package com.snail.clearvdsanalyze.core;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yongjie created on 2019-08-04.
 */
public class RetrofitFactory {


    public static Retrofit createRetrofit() {

        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://vdsblock.io/")
                .build();
        return retrofit;
    }
}
