package com.feicui.news.model.biz.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;

import com.feicui.news.model.dao.NewsDBManager;
import com.feicui.news.model.entity.News;
import com.feicui.news.model.entity.OnePageData;
import com.feicui.news.model.entity.SubType;

public class ParseNews {
	
	public static OnePageData parseJson(Context context,String jsonString){
		try {
			//�õ���ҳ���ݵ�JSONObject����
			JSONObject jsonObject = new JSONObject(jsonString);
			String msg = jsonObject.getString("message");
			int status = jsonObject.getInt("status");
			JSONArray array = jsonObject.getJSONArray("data");
			//���鳤��
			int length = array.length();
			ArrayList<News> list = new ArrayList<News>();
			//��ʼ�����ݿ������
			NewsDBManager dbManager=NewsDBManager.getNewsDBManager(context);
			
			for(int i = 0;i<length;i++){
				JSONObject obj = array.getJSONObject(i);
				News news = new News(obj.getString("summary"),
						obj.getString("icon"), 
						obj.getString("stamp"),
						obj.getString("title"), 
						obj.getInt("nid"), 
						obj.getString("link"),
						obj.getInt("type"));
				list.add(news);
				//�������ݿ���
				dbManager.insertNews(news);
			}
			OnePageData onePageData = new OnePageData(msg, status, list);
			
			//ע�ⷵ�ض���
			return onePageData;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//ʹ��fastJson�����������ݽ���
	public static OnePageData parseJsonByFastJson(Context context,String json){
		OnePageData onePageData = com.alibaba.fastjson.JSONObject.parseObject(json, 
				OnePageData.class);
		if(onePageData != null){
			//��ʼ�����ݿ������
			NewsDBManager dbManager=NewsDBManager.getNewsDBManager(context);
			ArrayList<News> list = onePageData.getData();
			//������
			Iterator<News> iter = list.iterator();
			while(iter.hasNext()){
				News news = iter.next();
				dbManager.insertNews(news);
			}
		}
		
		return onePageData;
	}
	//���������������ݽ���
	public static ArrayList<SubType> parseJsonNewsType(Context context,String json ){
		ArrayList<SubType> list= new ArrayList<SubType>();
		
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray array = obj.getJSONArray("data");
			for(int i=0;i<array.length();i++){
				JSONObject obj2 = array.getJSONObject(i);
				JSONArray array2 = obj2.getJSONArray("subgrp");
				for(int j = 0;j<array2.length();j++){
					JSONObject obj3 = array2.getJSONObject(j);
					
					String subgroup = obj3.getString("subgroup");
					int subid = obj3.getInt("subid");
					//����SubType����
					SubType subType = new SubType(subid, subgroup);
					list.add(subType);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		//���������ͱ��浽���ݿ�
		NewsDBManager dbManager=NewsDBManager.getNewsDBManager(context);
		dbManager.saveNewsType(list);
		return list;
	}
	
	public static ArrayList<SubType> parseType(String json){
		ArrayList<SubType> list = new ArrayList<SubType>();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray array = obj.getJSONArray("data");
			for(int i=0;i<array.length();i++){
				JSONObject obj2 = array.getJSONObject(i);
				JSONArray array2 = obj2.getJSONArray("subgrp");
				for(int j=0;j<array2.length();j++){
					JSONObject obj3 = array2.getJSONObject(j);
					String subgroup =  obj3.getString("subgroup");
					int subid =  obj3.getInt("subid");
					SubType subType = new SubType(subid, subgroup);
					list.add(subType);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	

}
