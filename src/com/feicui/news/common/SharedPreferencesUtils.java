package com.feicui.news.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.feicui.news.model.entity.BaseEntity;
import com.feicui.news.model.entity.Register;
import com.feicui.news.model.entity.User;

public class SharedPreferencesUtils {
	//存储Register信息
	public static void saveRegister(Context context,BaseEntity<Register> register){
		SharedPreferences sp = context.getSharedPreferences("register", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("result", register.getData().getResult());
		editor.putString("token", register.getData().getToken());
		editor.putString("explain", register.getData().getExplain());
		editor.commit();
	}
	//获取注册信息
	public static String getResult(Context context){
		SharedPreferences sp = context.getSharedPreferences("register", Context.MODE_PRIVATE);
		return sp.getString("result", "");
	}
	public static String getToken(Context context){
		SharedPreferences sp = context.getSharedPreferences("register", Context.MODE_PRIVATE);
		return sp.getString("token", "");
	}
	public static String getExplain(Context context){
		SharedPreferences sp = context.getSharedPreferences("register", Context.MODE_PRIVATE);
		return sp.getString("explain", "");
	}
	
	
	/**
	 * 1.保存用户数据
	 * @param context
	 * @param user
	 * headImage 头像的网络地址
	 */
	public static void saveUser(Context context ,BaseEntity<User> user){
		SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		
		editor.putString("userName", user.getData().getUid());
		editor.putString("headImage", user.getData().getPortrait());
		editor.commit();
	}
	/**
	 * 2.获取用户名和用户头像地址
	 * @param context
	 * @return  String
	 */
	public static String[] getUserNameAndPhoto(Context context){
		SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		return new String []{sp.getString("userName", ""),sp.getString("headImage", "")};
	}
	/**
	 * 3.保存用户头像本地路径
	 * @param context
	 * @param path
	 */
	public static void saveUserLocalIcon(Context context ,String path){
		SharedPreferences sp = context.getSharedPreferences("user", 
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("imagePath", path);
		editor.commit();
	}
	
	/**
	 * 4.获取保存的本地头像
	 * @param context
	 * @return 
	 */
	public static String getUserLocalIcon(Context context){
		SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		return sp.getString("imagePath", null);
	}
	
	/**
	 * 5.清除用户数据
	 * @param context
	 */
	public static void clearUser(Context context){
		SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
	

}
