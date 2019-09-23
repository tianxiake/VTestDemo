package com.snail.clearvdsanalyze.core;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author yongjie created on 2019-08-04.
 */
public interface ClearVDSWebSevice {


    @GET("index.php/api/tx-address-list/{vid}")
    Call<ResponseBody> getContentByVID(@Path("vid") String vid, @Query("page") int pageIndex);

    @GET("index.php/api/market")
    Call<ResponseBody> getCurrentBlockMarket();

}
