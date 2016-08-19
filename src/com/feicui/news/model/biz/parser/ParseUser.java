package com.feicui.news.model.biz.parser;


import android.R.integer;

import com.alibaba.fastjson.JSON;
import com.feicui.news.model.entity.BaseEntity;
import com.feicui.news.model.entity.Register;
import com.feicui.news.model.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ParseUser {

	/**
	 * �����û�ע�᷵����Ϣ
	 * @param json 
	 * @return BaseEntity<Register>
	 */
	public static BaseEntity<Register> parserRegister(String json){
		
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<BaseEntity<Register>>(){}.getType());
	}
	/**
	 * �����û���������
	 * @param json
	 * @return BaseEntity<User>
	 */
	public static BaseEntity<User> parserUser(String json){
		return new Gson().fromJson(json, new TypeToken<BaseEntity<User>>(){}.getType());
	}
}
