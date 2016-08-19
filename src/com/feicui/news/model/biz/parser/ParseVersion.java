package com.feicui.news.model.biz.parser;

import com.feicui.news.model.entity.Ver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ParseVersion {
	public static Ver parse(String json){
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<Ver>(){}.getType());
	}
}
