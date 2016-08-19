package com.feicui.news.ui.adapter;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.feicui.news.R;
import com.feicui.news.common.LoadImage;
import com.feicui.news.common.MyAsyncTask;
import com.feicui.news.common.MyAsyncTask.ImageLoadListener;
import com.feicui.news.model.entity.News;
import com.feicui.news.ui.base.MyBaseAdapter;

import com.feicui.news.volley.RequestQueue;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.VolleyError;
import com.feicui.news.volley.toolbox.ImageLoader;
import com.feicui.news.volley.toolbox.ImageLoader.ImageCache;
import com.feicui.news.volley.toolbox.NetworkImageView;
import com.feicui.news.volley.toolbox.Volley;
import com.feicui.news.volley.toolbox.ImageRequest;

import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.Response.ErrorListener;


public class NewsAdapter extends MyBaseAdapter<News>{
	private RequestQueue rQueue;
	private ImageLoader imageLoader;
	public NewsAdapter(Context c) {
		super(c);
		rQueue = Volley.newRequestQueue(context);
		//ImageLoader
		imageLoader = new ImageLoader(rQueue, new BitmapCache());
	}

	@Override
	public View getMyView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_list_news, null);
			holder = new ViewHolder();
			holder.imageView = (NetworkImageView) convertView.findViewById(R.id.imageView1);
			holder.tv1 = (TextView) convertView.findViewById(R.id.textView1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.textView2);
			convertView.setTag(holder);
		}
		holder = (ViewHolder)convertView.getTag();
		
		
		String imgUrl = list.get(position).getIcon();
		
		if (!TextUtils.isEmpty(imgUrl)) {
			holder.imageView.setImageUrl(imgUrl, imageLoader);
		}
		
		
		holder.tv1.setText(list.get(position).getTitle());
		holder.tv2.setText(list.get(position).getSummary());
		return convertView;
	}
	
	class ViewHolder{
		NetworkImageView imageView;
		TextView tv1;
		TextView tv2;
	}
	
	//ImageCache
	public static class BitmapCache implements ImageCache {

		private static final String TAG = "BitmapCache";
		private LruCache<String, Bitmap> mCache;
		public BitmapCache() {

			int maxSize = (int)Runtime.getRuntime().maxMemory();
			maxSize /= 8;

			mCache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {

					return bitmap.getRowBytes() * bitmap.getHeight();
				}
			};

		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			if (bitmap != null) {
				mCache.put(url, bitmap);
			}
		}

	}
	

}