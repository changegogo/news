package com.feicui.news.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 获取图片的公共类
 * @author Administrator
 *
 *  根据 图片的uri 存放两个方法
 *  1.存文件  2.存内存
 *  2.取得文件 2.1 从网络获取  2.2 从文件获取 2.3 从内存获取
 *  3.最终调用的方法 得到图片 先判断是不是内存 --文件 --网络
 *  
 */
public class LoadImage {

	/**
	 * 软引用 存内存 只要不关闭该程序 一直存放
	 */
	private static Map<String,SoftReference<Bitmap>>
	softReferences=new HashMap<String, SoftReference<Bitmap>>();
	
	/*
	 * LruCache
	 * */
	private static LruCache<String, Bitmap> lruCache;
	
	
	private Context context;
	/**
	 * 自定义回调接口 传递 图片 和图片路径
	 * @author Administrator
	 *
	 */
	public interface ImageLoadListener{
		void imageLoadOk(Bitmap bitmap,String url);
	}
	
	private ImageLoadListener listener;
	/**
	 * 构造方法赋值
	 * @param context
	 * @param listener
	 */
	public LoadImage(Context context, ImageLoadListener listener) {
		this.context = context;
		this.listener = listener;
		
		//是得到系统为app分配的最大内存
		int maxMemory=(int)Runtime.getRuntime().maxMemory();
		int mCacheSize = maxMemory / 8;
		//初始化LruCache
		//1024 * 1024 * 3
		lruCache = new LruCache<String, Bitmap>(mCacheSize){
			//得到一张图片所占的内存
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}
		};
	}

	/**
	 * 保存图片到缓存路径中
	 * @param url     图片的原路径 网络 http://aa/t.jpg
 	 * @param bitmap  来自网络得到图片
	 */
	public void saveCachFile(String url,Bitmap bitmap){
		//http://aa/t.jpg  获取文件名字
		String name=url.substring(url.lastIndexOf("/")+1);
		//返回的路径目录应用程序缓存文件
		File cacheDir=context.getCacheDir();
		if(!cacheDir.exists()){
			cacheDir.mkdir();
		}
		//建立输出流
		OutputStream outStream=null;
		try {
			outStream=new FileOutputStream(new File(cacheDir,name));
			//存图片到文件
			bitmap.compress(CompressFormat.JPEG, 100, outStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(outStream!=null){
					outStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}

	/**
	 * 保存图片到软引用缓存中
	 * @param url  图片的原路径 网络 http://aa/t.jpg
	 * @param bitmap   来自文件的图片 
	 */
	public void saveSoftReferences(String url,Bitmap bitmap){
		//存在内存中 一个图片
		SoftReference<Bitmap> softbitmap=new SoftReference<Bitmap>(bitmap);
		//存在集合中
		softReferences.put(url, softbitmap);
	}
	//保存图片内存到LruCache
	public void saveBitmapToLruCache(String url,Bitmap bitmap){
		Log.e("bitmap", "saveBitmapToLruCache");
		lruCache.put(url, bitmap);
	}

	/**
	 * 得到内存的图片 软引用
	 * @param url 图片路径
	 * @return bitmap 图片
	 */
	public Bitmap getBitmapSoftReferences(String url){
		Bitmap bitmap=null;
		//如果内存集合中根据key 得到values
		if(softReferences.containsKey(url)){
			//得到软引用中的图片
			bitmap=softReferences.get(url).get();
		}
		return bitmap;
	}
	//从lruCache中获取
	public Bitmap getBitmapFromLruCache(String url){
		Log.e("bitmap", "getBitmapFromLruCache");
		return lruCache.get(url);
	}
	/**
	 * 从缓存文件中获取图片
	 * @param url 图片路径  com.app.azy.news.cache/xxx.png
	 * @return  缓存文件中的图片
	 */
	private Bitmap getBitmapFromCache(String url){
		Log.e("bitmap", "getBitmapFromCache");
		String name=url.substring(url.lastIndexOf("/")+1);
		//获取当前包下的缓存文件路径
		File cacheDir=context.getCacheDir();
		//得到该文件夹下所有文件
		File[] files=cacheDir.listFiles();
		if(files==null){
			return null;
		}
		//图片文件
		File bitFile=null;
		//如有名字和传入的文件名一致的则找到图片
		for (File file : files) {
			if(file.getName().equals(name)){
				bitFile=file;
				break;}
		}
		//如果没有找到，返回空
		if(bitFile==null){
			return null;
		}
		/**
		 * 把找的文件 转换为bitmap
		 */
		Bitmap bitmap=BitmapFactory.decodeFile(bitFile.getAbsolutePath());
		//saveSoftReferences(url, bitmap);
		saveBitmapToLruCache(url, bitmap);
		return bitmap;
	}
	
	/**
	 * 异步加载网络图片
	 * @param url 图片在网络中的路径
	 *
	 */
	private void getBitmapAsync(String url){
		//自定义的异步加载类
		ImageAsyncTask imageAsyncTask=new ImageAsyncTask();
		//执行加载
		imageAsyncTask.execute(url);
		
		
	}
	/**
	 * 异步加载类
	 * 1.Url 处理的网络路径
	 * 2.无 Void  当加载一条时 传递的类型
	 * 3.返回的加载后的数据：Bitmap
	 */
	private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap>{

		private String url;
		
		
		//执行之前 ui线程
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		//后台运行  新线程的代码 不能修改ui
		@Override
		protected Bitmap doInBackground(String... params) {
			//单条加载更新ui
			//publishProgress(values);
			url=params[0];
			Bitmap bitmap=null;
			try {
				//建立网络连接
				URL url=new URL(params[0]);
				HttpURLConnection conn=(HttpURLConnection) url.openConnection();
				//得到输入字节流
				InputStream is=conn.getInputStream();
				//得到图片
				bitmap=BitmapFactory.decodeStream(is);
				//存入软引用中
				//saveSoftReferences(params[0], bitmap);
				saveBitmapToLruCache(params[0], bitmap);
				//存在缓存文件中
				saveCachFile(params[0], bitmap);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		//如果 新线程中执行了 publishProgress(values);
		//就会执行 此方法 实现一条一条的更新ui
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
		
		
		//doInBackground 执行return 
		//后马上执行 ui线程 并把结果传递给此方法 execute(url)
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(listener!=null){
				//线程结束后返回图片和路径
				listener.imageLoadOk(result, url);
			}
		}


	}
	
	/**
	 * 最终调用的方法:先查看内存中有没有，再看缓存文件中 有没有，最后只能向网络请求图片
	 * @param url 图片路径
	 * @return  图片
	 */
	public  Bitmap getBitmap(String url){
		Bitmap bitmap=null;
		if(url==null || url.length()<=0){
			return bitmap;
		}
		//先看内存中
		//bitmap=getBitmapSoftReferences(url);
		bitmap = getBitmapFromLruCache(url);
		if(bitmap!=null){
			return bitmap;
		}
		
		//是不是缓存文件的
		bitmap=getBitmapFromCache(url);
		if(bitmap!=null){
			return bitmap;
		}
		
		getBitmapAsync(url);
		
		return bitmap;
	}
	
}
