package com.snail.clearvdsanalyze.core;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * @author yongjie created on 2019-08-10.
 */
public interface GetWebService {


    @GET
    Call<ResponseBody> getWeb(@Url String url);
}
