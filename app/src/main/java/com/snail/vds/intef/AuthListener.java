package com.snail.vds.intef;

/**
 * @author yongjie created on 2019-09-22.
 */
public interface AuthListener {

	void authSuccess();

	void authFailure(String message, Exception exception);
}
