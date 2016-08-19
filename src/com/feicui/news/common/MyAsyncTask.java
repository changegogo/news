package com.feicui.news.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
/*
 * 异步任务
 * 参数1.请求数据的网络地址类型
 * 参数2.进行进度展示中传递的参数类型
 * 参数3.请求数据后返回的数据类型
 * */
public class MyAsyncTask extends AsyncTask<String, Void, Bitmap>{

	private ImageLoadListener listener;
	private String urlString;
	
	
	
	/*
	 * 异步任务开始执行之前UI线程
	 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	/*
	 * 运行在子线程进行耗时操作
	 * */
	@Override
	protected Bitmap doInBackground(String... params) {
		try {
			urlString = params[0];
			URL url = new URL(urlString);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			if(http.getResponseCode() == 200){
				InputStream isInputStream  = http.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(isInputStream);
				return bitmap;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/*
	 * 执行一次publishProgress就会调用一次onProgressUpdate
	 * */
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	/*
	 * doInBackground执行return之后，调用这个方法，UI线程
	 * */
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		listener.imageLoadOk(urlString, result);
	}
	
	public interface ImageLoadListener{
		void imageLoadOk(String url,Bitmap bitmap);
	}
	
	public void setOnImageLoadListener(ImageLoadListener listener){
		this.listener = listener;
	}

}
