package com.feicui.news;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

import com.feicui.news.common.LogUtil;


import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;
/**
 * 
 * @author Administrator
 *  全局存储容器
 *  整个应用程序唯一实例
 * 描述： 当android程序启动时系统会创建一个 application对象，用来存储系统的一些信息。
 * android系统会为每个程序运行时创建一个Application类的对象且仅创建一个(单例)。
 * 且application对象的生命周期是整个程序中最长的，它的生命周期就等于这个程序的生命周期。
 * 因为它是全局的单例的，所以在不同的Activity,Service中获得的对象都是同一个对象。
 * 所以通过Application来进行一些，数据传递，数据共享,数据缓存等操作。
 *  
 */
public class MyApplication extends Application {

	/**用来保存整个应用程序的数据*/
	private HashMap<String, Object> allData=new HashMap<String, Object>();
	/**存数据 */
	public  void addAllData(String key,Object value){
		
		allData.put(key, value);	
	}
	/**取数据*/
	public Object getAllData(String key){
		if(allData.containsKey(key)){
			return allData.get(key);
		}
		return null;
	}
	/**删除一条数据*/
	public void delAllDataBykey(String key){
		if(allData.containsKey(key)){
			allData.remove(key);
		}
	}
	
	/**删除所有数据*/
	public void delAllData(){
		allData.clear();
	}
	
	/**单例模式*/
	private static MyApplication application;
	
	
	public static MyApplication getInstance(){
		LogUtil.d(LogUtil.TAG, "MyApplication onCreate");
		return application;
		
		
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
	
		application=this;
		Log.e("asdf", "dasfsafdsafas------");
		
		LogUtil.d(LogUtil.TAG, "application oncreate");
		
	}
	
	
	/**内存不足的时候*/
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		LogUtil.d(LogUtil.TAG, "MyApplication onLowMemory");
	}

	/**
	 * 结束的时候
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		LogUtil.d(LogUtil.TAG, "MyApplication onTerminate");
	}

	/**配置改变的时候*/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		LogUtil.d(LogUtil.TAG, "MyApplication onConfigurationChanged");
	}
	
	
	
	
}
