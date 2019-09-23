package com.snail.vds.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author yongjie created on 2019-08-04.
 */
public class ClearWebEntity implements Serializable, Cloneable {

	private String webUrl;
	private String vid;
	private int subVid;
	private String message;
	private boolean isError;
	private boolean isSpecial;
	private int newHeight = -1;

	public int getNewHeight() {
		return newHeight;
	}

	public void setNewHeight(int newHeight) {
		if (this.newHeight == -1) {
			this.newHeight = newHeight;
		}
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean error) {
		isError = error;
	}

	public boolean isSpecial() {
		return isSpecial;
	}

	public void setSpecial(boolean special) {
		isSpecial = special;
	}

	public String getMessage() {
		if (TextUtils.isEmpty(message)) {
			return "";
		}
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public int getSubVid() {
		return subVid;
	}

	public void setSubVid(int subVid) {
		this.subVid = subVid;
	}

	@Override
	public String toString() {
		return "ClearWebEntity{" +
				"webUrl='" + webUrl + '\'' +
				", vid='" + vid + '\'' +
				", subVid='" + subVid + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
