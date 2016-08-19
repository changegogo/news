package com.feicui.news;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.LogUtil;
import com.feicui.news.common.SharedPreferencesUtils;
import com.feicui.news.common.SystemUtils;
import com.feicui.news.model.biz.parser.ParserComments;
import com.feicui.news.model.entity.Comment;
import com.feicui.news.model.volleyhttp.VolleyHttp;
import com.feicui.news.ui.adapter.CommentsAdapter;
import com.feicui.news.ui.base.MyBaseActivity;
import com.feicui.news.view.xlistview.XListView;
import com.feicui.news.view.xlistview.XListView.IXListViewListener;

import com.feicui.news.volley.Response.ErrorListener;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.VolleyError;

public class CommentActivity extends MyBaseActivity implements OnClickListener{
	/**新闻id*/
	private int nid;
	/**评论列表*/
	private XListView listView;
	/**评论列表适配器*/
	private CommentsAdapter adapter;
	/***/
    private  int mode;	
    /**发送评论按钮*/
    private ImageView imageView_send;
    /**返回按钮*/
    private ImageView imageView_back;
    /**评论编辑框*/
	private EditText editText_content;
	//标识是刷新还是加载
	private boolean isRefresh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//设置Activity无title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_comment);
		//webView页面跳转过来传递来的新闻nid
		nid = getIntent().getIntExtra("nid", -1);
		initView();
		initData();
	}
	private void initData() {
		adapter = new CommentsAdapter(this, listView);
		listView.setAdapter(adapter);
		//设置可以下拉刷新上拉加载
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(listViewListener);
		loadNextComment();

		imageView_back.setOnClickListener(this);
		imageView_send.setOnClickListener(this);
	}
	private void initView() {
		listView = (XListView) findViewById(R.id.listview);
		imageView_send = (ImageView) findViewById(R.id.imageview_send);
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		editText_content = (EditText) findViewById(R.id.edittext);
	}
	
	/**
	 * 请求最新的评论
	 */
	protected void loadNextComment() {
		int curId = adapter.getAdapterData().size() <= 0 ? 0 : adapter.getItem(
				0).getCid();
		LogUtil.d(LogUtil.TAG, "loadnextcomment--->currentId=" + curId);
		String url = CommonUtil.APPURL + "/cmt_list?ver=" + CommonUtil.VERSION_CODE + "&nid="
				+ nid + "&dir=" + 1 + "&cid=" + curId + "&type="
				+ 1 + "&stamp=" + "20140707";
		new VolleyHttp(this).getJSONString(url, listener, errorListener);
		
	}
	
	/**
	 * 加载下面的20条数据
	 */
	protected void loadPreComment() {
		Log.e("tag", listView.getLastVisiblePosition()+",,,,");
		Comment comment = adapter
				.getItem(listView.getLastVisiblePosition() - 2);
		if (SystemUtils.getInstance(this).isNetConn()) {
			String url = CommonUtil.APPURL + "/cmt_list?ver=" + CommonUtil.VERSION_CODE + "&nid="
					+ nid + "&dir=" + 2 + "&cid=" + comment.getCid() + "&type="
					+ 1 + "&stamp=" + "20140707";
			new VolleyHttp(this).getJSONString(url, listener, errorListener);
		}
	}
	//请求网络数据成功回调这里
	private Listener<String> listener = new Listener<String>(){

	@Override
	public void onResponse(String response) {
		ArrayList<Comment> comments = ParserComments.parserComment(response);
		if (comments == null || comments.size() < 1) {
			return;
		}
		//如果是下拉刷新，isRefresh是true，clear原来的数据
		adapter.addendData(comments, isRefresh);
		adapter.updateAdapter();
	}};
	private ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back:
			finish();
			break;
		case R.id.imageview_send:
			sendComment();
			break;
		}
	}
	//发表评论
	private void sendComment(){
		//评论的内容
		String ccontent = editText_content.getText().toString();
		if (ccontent == null || ccontent.equals("")) {
			Toast.makeText(CommentActivity.this, "要先写评论内容哦，亲！",
					Toast.LENGTH_SHORT).show();
			return;
		}
		//点击完发送立即设置发送按钮不可用，等到服务器返回数据再设置可用
		imageView_send.setEnabled(false);
		//获取设备码
		String imei = SystemUtils.getInstance(CommentActivity.this)
				.getIMEI();
		String token = SharedPreferencesUtils
				.getToken(CommentActivity.this);
		if (TextUtils.isEmpty(token)) {
			Toast.makeText(CommentActivity.this, "对不起，您还没有登录.", 0)
					.show();
			return;
		}
		//展示进度条
		showLoadingDialog(CommentActivity.this, "", true);
		//请求网络，提交评论
		String commUrl = CommonUtil.APPURL + "/cmt_commit?nid=" + nid + "&ver="
				+ CommonUtil.VERSION_CODE + "&token=" + token + "&imei=" + imei + "&ctx="
				+ ccontent;
		new VolleyHttp(this).getJSONString(commUrl, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.e("response", response);
				int status = ParserComments
						.parserSendComment(response.trim());
				if (status == 0) {
					showToast("评论成功！");
					editText_content.setText("");
					editText_content.clearFocus();
					//设为true标志将之前的数据清除
					isRefresh = true;
					loadNextComment();
				} else {
					showToast("评论失败！");
				}
				imageView_send.setEnabled(true);
				//隐藏进度条
				dialog.cancel();
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				showToast("服务器连接异常！");
				imageView_send.setEnabled(true);
				//隐藏进度条
				dialog.cancel();
			}
		});
	}
	
	private IXListViewListener listViewListener = new IXListViewListener() {
		@Override
		public void onRefresh() {
			// 加载最新数据。。。。。。。。。。。。。。。。。。。
			isRefresh = true;
			loadNextComment();
			// 加载完毕
			listView.stopLoadMore();
			listView.stopRefresh();
			listView.setRefreshTime(CommonUtil.getSystemTime("yyyy-MM-dd hh:mm:ss"));
		}

		@Override
		public void onLoadMore() {
			// 加载下面更多的数据。。。。。。。。。。。。。。。。。。。
			int count = adapter.getCount();
			if (count > 1) { // 如果当前的ListView不存在一条item是不允许用户加载更多
				loadPreComment();
			}
			listView.stopLoadMore();
			listView.stopRefresh();
		}
	};
}
