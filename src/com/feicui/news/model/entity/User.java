package com.feicui.news.model.entity;

import java.util.ArrayList;
import java.util.List;

public class User {

	public User(String uid, String email, int integration, 
			int comnum, String portrait, ArrayList<LoginLog> loginlog) {
		this.uid = uid;
		this.integration = integration;

		this.comnum = comnum;
		this.portrait = portrait;
		this.loginlog = loginlog;
	}

	/** 用户id */
	private String uid;
	/** 用户邮箱 */
	private String email;
	/** 用户积分 */
	private int integration;
	/** 评论数量 */
	private int comnum;
	/** 头像 */
	private String portrait;
	/** 登陆日志 */
	private ArrayList<LoginLog> loginlog;

	public String getUid() {
		return uid;
	}

	public int getIntegration() {
		return integration;
	}



	public int getComnum() {
		return comnum;
	}

	public String getPortrait() {
		return portrait;
	}

	public ArrayList<LoginLog> getLoginlog() {
		return loginlog;
	}

}
