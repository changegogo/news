package com.feicui.news.model.biz.parser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.feicui.news.common.LogUtil;
import com.feicui.news.model.entity.BaseEntity;
import com.feicui.news.model.entity.Comment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ParserComments extends TestCase{
	/**
	 * ���������б�
	 * @param json
	 * @return
	 */
	public static ArrayList<Comment> parserComment(String json){
		Type type = new TypeToken<BaseEntity<ArrayList<Comment>>>(){}.getType();
		BaseEntity<ArrayList<Comment>> entity = new Gson().fromJson(json,type);
		return entity.getData();
	}
	/**
	 * ������������
	 * @param json
	 * @return
	 */
	public static int parserCommentNum(String json){
		Type type = new TypeToken<BaseEntity<Integer>>(){}.getType();
		BaseEntity<Integer> entity = new Gson().fromJson(json,type);
		return entity.getData().intValue();
	}
	/**
	 * �������ۺ����
	 * @param json
	 * @return
	 */
	public static int parserSendComment(String json){
		LogUtil.d(LogUtil.TAG, "�����������ۺ󷵻�json==="+json);
		Type type = new TypeToken<BaseEntity>(){}.getType();
		BaseEntity entity = new Gson().fromJson(json,type);
		return entity.getStatus();
	}
}
