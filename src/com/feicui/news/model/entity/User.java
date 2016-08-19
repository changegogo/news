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

	/** �û�id */
	private String uid;
	/** �û����� */
	private String email;
	/** �û����� */
	private int integration;
	/** �������� */
	private int comnum;
	/** ͷ�� */
	private String portrait;
	/** ��½��־ */
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
