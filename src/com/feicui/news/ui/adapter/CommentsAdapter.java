package com.feicui.news.ui.adapter;

import com.feicui.news.R;
import com.feicui.news.common.LoadImage;
import com.feicui.news.common.LogUtil;
import com.feicui.news.model.entity.Comment;
import com.feicui.news.ui.base.MyBaseAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CommentsAdapter extends MyBaseAdapter<Comment> {
	private ListView listView;

	public CommentsAdapter(Context context,ListView listView) {
		super(context);
		this.listView=listView;
	}

	@Override
	public View getMyView(int position, View convertView, ViewGroup parent) {
		HoldView holdView=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.item_list_comment, null);
			holdView=new HoldView(convertView);
			convertView.setTag(holdView);
		}else{
			holdView=(HoldView) convertView.getTag();
		}
		Comment comment=list.get(position);

		LogUtil.d(LogUtil.TAG, "position--->"+position +"--- cid="+comment.getCid());
//		  entityÐÞ¸Ä£¬ ÏÈ×¢ÊÍ¡£
		holdView.tv_comment.setText(comment.getContent());
		holdView.tv_time.setText(comment.getStamp());
		holdView.tv_user.setText(comment.getUid());
		
	
		return convertView;
	}
	
	public class HoldView {
		public ImageView iv_list_image;
		public TextView tv_user;
		public TextView tv_time;
		public TextView tv_comment;

		public HoldView(View view) {
			iv_list_image = (ImageView) view.findViewById(R.id.imageView1);
			tv_user = (TextView) view.findViewById(R.id.textView2);
			tv_time = (TextView) view.findViewById(R.id.textView3);
			tv_comment = (TextView) view.findViewById(R.id.textView1);
		}
	}
	
	private LoadImage.ImageLoadListener listener=new LoadImage.ImageLoadListener(){

		@Override
		public void imageLoadOk(Bitmap bitmap, String url) {
			ImageView iv=(ImageView) listView.findViewWithTag(url);
			if(iv!=null)
			   iv.setImageBitmap(bitmap);
		}		
	};	
}
