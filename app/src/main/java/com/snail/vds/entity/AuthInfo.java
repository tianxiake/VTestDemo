package com.snail.vds.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yongjie created on 2019-08-26.
 */
public class AuthInfo implements Serializable {

    private List<String> auth = new ArrayList<>();

    public List<String> getAuth() {
        return auth;
    }

    public void setAuth(List<String> auth) {
        this.auth = auth;
    }
}
