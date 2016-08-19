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
 * �첽����
 * ����1.�������ݵ������ַ����
 * ����2.���н���չʾ�д��ݵĲ�������
 * ����3.�������ݺ󷵻ص���������
 * */
public class MyAsyncTask extends AsyncTask<String, Void, Bitmap>{

	private ImageLoadListener listener;
	private String urlString;
	
	
	
	/*
	 * �첽����ʼִ��֮ǰUI�߳�
	 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	/*
	 * ���������߳̽��к�ʱ����
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
	 * ִ��һ��publishProgress�ͻ����һ��onProgressUpdate
	 * */
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	/*
	 * doInBackgroundִ��return֮�󣬵������������UI�߳�
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
