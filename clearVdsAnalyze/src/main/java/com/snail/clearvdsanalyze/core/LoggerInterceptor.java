package com.snail.clearvdsanalyze.core;


import com.snail.clearvdsanalyze.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author yongjie created on 2019-08-04.
 */
public class LoggerInterceptor implements Interceptor {
    private static final String TAG = "LoggerInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response proceed = chain.proceed(request);
        Log.log(TAG, "url:" + request.url() + ",isSuccess:" + proceed.isSuccessful() + ",code:" + proceed.code());
        return proceed;
    }
}
