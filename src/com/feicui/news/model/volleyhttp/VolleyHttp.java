package com.feicui.news.model.volleyhttp;

import java.io.File;

import android.content.Context;
import android.widget.ImageView;

import com.feicui.news.R;
import com.feicui.news.volley.RequestQueue;
import com.feicui.news.volley.Response.ErrorListener;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.toolbox.ImageLoader;
import com.feicui.news.volley.toolbox.ImageLoader.ImageCache;
import com.feicui.news.volley.toolbox.ImageLoader.ImageListener;
import com.feicui.news.volley.toolbox.MultiPosttRequest;
import com.feicui.news.volley.toolbox.StringRequest;
import com.feicui.news.volley.toolbox.Volley;

public class VolleyHttp {

	public static RequestQueue mQueue;
	private Context context;

	public VolleyHttp(Context context) {
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(context);
		}
		this.context = context;
	}
	//获取json字符串
	public void getJSONString(String url, Listener<String> listener,
			ErrorListener errorListener) {
		StringRequest request = new StringRequest(url, listener, errorListener);
		mQueue.add(request);
	}
	//显示图片
	public void addImage(String url, ImageCache imageCache, ImageView iv) {
		ImageLoader mImageLoader = new ImageLoader(mQueue, imageCache);
		ImageListener listener = ImageLoader.getImageListener(iv,
				R.drawable.ic_launcher, android.R.drawable.ic_delete);
		mImageLoader.get(url, listener);
	}

	//上传图片
	public void upLoadImage(String url, File file, 
			Listener<String> listener,
			ErrorListener errorListener) {
		MultiPosttRequest request = new MultiPosttRequest(url, listener,
				errorListener);
		request.buildMultipartEntity("portrait", file);
		mQueue.add(request);
	}
	//提交用户信息
	public void addUserString(String url, String token, String imei,
			Listener<String> listener, ErrorListener errorListener) {
		MultiPosttRequest request = new MultiPosttRequest(url, listener,
				errorListener);
		request.buildMultipartEntity("token", token);
		request.buildMultipartEntity("imei", imei);
		request.buildMultipartEntity("ver", 1 + "");
		mQueue.add(request);
	}

}
