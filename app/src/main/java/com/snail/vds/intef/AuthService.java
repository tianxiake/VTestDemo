package com.snail.vds.intef;

import com.snail.vds.entity.AuthInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author yongjie created on 2019-08-26.
 */
public interface AuthService {

    @GET("/auth.auth")
    Observable<AuthInfo> getAuthInfo();
}
