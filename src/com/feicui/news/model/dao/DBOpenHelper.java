package com.feicui.news.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper  extends SQLiteOpenHelper{

	public DBOpenHelper(Context context) {
		super(context, "newsdb.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table types (_id integer primary key autoincrement,subid integer,subgroup text)");
		db.execSQL("create table news (_id integer primary key autoincrement,nid integer,title text,summary text,icon text,link text,type integer)");
		db.execSQL("create table lovenews(_id integer primary key autoincrement,type integer,nid integer,stamp text,icon text,title text,summary text,link text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
