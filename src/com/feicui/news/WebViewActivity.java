package com.feicui.news;

import com.feicui.news.common.CommonUtil;
import com.feicui.news.model.biz.parser.ParserComments;
import com.feicui.news.model.dao.NewsDBManager;
import com.feicui.news.model.entity.News;
import com.feicui.news.model.volleyhttp.VolleyHttp;
import com.feicui.news.ui.base.MyBaseActivity;
import com.feicui.news.volley.Response.ErrorListener;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.VolleyError;
import com.feicui.news.volley.toolbox.Volley;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebViewActivity extends MyBaseActivity implements OnClickListener{
	//webView视图
	private WebView webView;
	//进度条
	private ProgressBar progressBar;
	//返回按钮
	private ImageView backImg;
	//收藏菜单
	private ImageView menuImg;
	//跟帖
	private TextView commentTv;
	//网址链接
	//private String htmlUrl;
	//弹出框
	private PopupWindow popupWindow;
	//本条新闻的信息,从资讯页传递过来
	private News newsitem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置Activity无title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web_view);
		
		//获取从MainActivity传递过来的Bundle
		Bundle bundle = getIntent().getExtras();
		//2.
		newsitem = (News) bundle.getSerializable("news");
		
		initView();
		initEvent();
		initData();
		initcommentCount();
		//初始化PopupWindown
		initPopupWindow();
	}
	//通过网络跟帖数量
	private void initcommentCount() {
		String countUrl = CommonUtil.APPURL+"/cmt_num?nid="+newsitem.getNid()+"&ver=1";
		new VolleyHttp(this).getJSONString(countUrl, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				int count = ParserComments.parserCommentNum(response);
				commentTv.setText(count+"跟帖");
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				showToast("获取跟帖数量失败");
			}
		});
	}

	private void initEvent() {
		backImg.setOnClickListener(this);
		menuImg.setOnClickListener(this);
		commentTv.setOnClickListener(this);
	}

	private void initData() {
		//3.
		webView.loadUrl(newsitem.getLink());
		//优先使用缓存
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		//设置支持javaScript脚本
		webView.getSettings().setJavaScriptEnabled(true);
		// 设置可以支持缩放 
		webView.getSettings().setSupportZoom(true); 
		// 设置出现缩放工具 
		webView.getSettings().setBuiltInZoomControls(true);
		//扩大比例的缩放
		webView.getSettings().setUseWideViewPort(true);
		//自适应屏幕
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		
		//在WebView中打开网页
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return true;
			}
		});
		//这里可以获取网页加载的进度
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if(progressBar != null){
					progressBar.setProgress(newProgress);
					if (newProgress >= 100) {
						progressBar.setVisibility(View.GONE);
					}
				}
			}
		});
	}

	private void initView() {
		backImg = (ImageView) findViewById(R.id.imageView_back);
		menuImg = (ImageView) findViewById(R.id.imageView_menu);
		commentTv =  (TextView) findViewById(R.id.textView2);
		
		webView = (WebView) findViewById(R.id.webView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}
	//点击返回键，回退页面
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	//初始化PopupWindow
	private void initPopupWindow() {
		View popview = getLayoutInflater().inflate(R.layout.item_pop_save, null);
		popupWindow = new PopupWindow(popview, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		
		TextView tv_savelocal = (TextView) popview.findViewById(R.id.saveLocal);
		tv_savelocal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				NewsDBManager manager = NewsDBManager.getNewsDBManager(WebViewActivity.this);
				
				if (manager.saveLoveNews(newsitem)) {
					showToast("收藏成功！\n在主界面侧滑菜单中查看");
				}
				else {
					showToast("已经收藏过这条新闻了！\n在主界面侧滑菜单中查看");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back:
			finish();
			break;
		case R.id.imageView_menu:
			//显示PopupWindow
			if (popupWindow != null && popupWindow.isShowing()) {
				popupWindow.dismiss();
			} else if (popupWindow != null) {
				popupWindow.showAsDropDown(menuImg, 0, 12);
			}
			break;
		case R.id.textView2://跟帖文本
			Bundle bundle = new Bundle();
			bundle.putInt("nid", newsitem.getNid());
			openActivity(CommentActivity.class, bundle);
			break;
		default:
			break;
		}
	}
}
