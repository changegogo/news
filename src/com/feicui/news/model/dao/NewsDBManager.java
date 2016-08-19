package com.feicui.news.model.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.feicui.news.model.entity.News;
import com.feicui.news.model.entity.SubType;

/**
 * ���ݿ������
 * @author Administrator
 *
 */
public class NewsDBManager {

	//����ģʽ
	private static NewsDBManager dbManager;
	private DBOpenHelper helper;
	
	private NewsDBManager(Context context){
		helper=new DBOpenHelper(context);
	}
	//ͬ����
	public static NewsDBManager getNewsDBManager(Context context){
		if(dbManager==null){
			synchronized (NewsDBManager.class) {
				if(dbManager==null){
					dbManager=new NewsDBManager(context);
				}
			}
		}
		return dbManager;
	}
	
	/**
	 * ���
	 */
	public void insertNews(News news){
		SQLiteDatabase db=helper.getWritableDatabase();
		ContentValues values=new ContentValues();
		
		values.put("nid", news.getNid());
		values.put("title", news.getTitle());
		values.put("summary", news.getSummary());
		values.put("icon", news.getIcon());
		values.put("link", news.getLink());
		values.put("type", news.getType());
		db.insert("news", null, values);
		db.close();
	}
	
	/** �������� */
	public long getCount() {
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select count(*) from news",null);
		long len = 0;
		if (cursor.moveToFirst()) {
			len = cursor.getLong(0);
		}
		cursor.close();
		db.close();
		return len;
	}

	
	/** ��ѯ���� */
	public ArrayList<News> queryNews(int count, int offset) {
		ArrayList<News> newsList=new ArrayList<News>();
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="select * from news order by _id desc limit "+count+" offset "+offset;
		Cursor cursor=db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				int nid = cursor.getInt(cursor.getColumnIndex("nid"));
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String summary = cursor.getString(cursor.getColumnIndex("summary"));
				String icon = cursor.getString(cursor.getColumnIndex("icon"));
				String link = cursor.getString(cursor.getColumnIndex("link"));
				int type = cursor.getInt(cursor.getColumnIndex("type"));
				
				
				News news = new News(summary, icon, null, title,nid, link,type);
				
				newsList.add(news);
			} while (cursor.moveToNext());
			cursor.close();
			db.close();
		}
		return newsList;
	}
	
	/**
	 * �������ŷ���
	 * @param news
	 */
	public boolean saveNewsType(List<SubType> types){
		for(SubType type:types) {
			try {
				SQLiteDatabase db=helper.getWritableDatabase();
				Cursor cursor=db.rawQuery("select * from types where subid="+type.getSubid(),null);
				if(cursor.moveToFirst()){
					cursor.close();
					return false;
				}
				cursor.close();
				ContentValues values=new ContentValues();
				values.put("subid", type.getSubid());
				values.put("subgroup", type.getSubgroup());
				db.insert("types", null, values);
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * ��ȡ���ŷ���
	 * @return ���ŵ��б�
	 */
	public ArrayList<SubType> queryNewsType(){
		ArrayList<SubType> newsList=new ArrayList<SubType>();
		SQLiteDatabase db=helper.getReadableDatabase();
		String sql="select * from types order by _id desc";
		Cursor cursor=db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				int subId = cursor.getInt(cursor.getColumnIndex("subid"));
				String subGroup = cursor.getString(cursor.getColumnIndex("subgroup"));
				SubType subType = new SubType(subId, subGroup);
				newsList.add(subType);
			} while (cursor.moveToNext());
			cursor.close();
			db.close();
		}
		return newsList;
	}
	
	/**
	 * �ղ�����
	 * @param news
	 */
	public boolean saveLoveNews(News news){
		if(news == null)
			return false;
		try {
			SQLiteDatabase db=helper.getWritableDatabase();
			Cursor cursor=db.rawQuery("select * from lovenews where nid="+news.getNid(),null);
			if(cursor.moveToFirst()){
				cursor.close();
				return false;
			}
			cursor.close();
			ContentValues values=new ContentValues();
			values.put("type", news.getType());
			values.put("nid", news.getNid());
			values.put("stamp", news.getStamp());
			values.put("icon", news.getIcon());
			values.put("title", news.getTitle());
			values.put("summary", news.getSummary());
			values.put("link", news.getLink());
			db.insert("lovenews", null, values);
			db.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ��ȡ�ղ����ŵ��б�
	 * @return ���ŵ��б�
	 */
	public ArrayList<News> queryLoveNews(){
		ArrayList<News> newsList=new ArrayList<News>();
		SQLiteDatabase db=helper.getReadableDatabase();
		String sql="select * from lovenews order by _id desc";
		Cursor cursor=db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				int type = cursor.getInt(cursor.getColumnIndex("type"));
				int nid = cursor.getInt(cursor.getColumnIndex("nid"));
				String stamp = cursor.getString(cursor.getColumnIndex("stamp"));
				String icon = cursor.getString(cursor.getColumnIndex("icon"));
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String summary = cursor.getString(cursor.getColumnIndex("summary"));
				String link = cursor.getString(cursor.getColumnIndex("link"));
				News news = new News(summary, icon, stamp, title, nid, link, type);
				newsList.add(news);
			} while (cursor.moveToNext());
			cursor.close();
			db.close();
		}
		return newsList;
	}
	
}
