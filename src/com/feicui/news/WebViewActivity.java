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
	//webView��ͼ
	private WebView webView;
	//������
	private ProgressBar progressBar;
	//���ذ�ť
	private ImageView backImg;
	//�ղز˵�
	private ImageView menuImg;
	//����
	private TextView commentTv;
	//��ַ����
	//private String htmlUrl;
	//������
	private PopupWindow popupWindow;
	//�������ŵ���Ϣ,����Ѷҳ���ݹ���
	private News newsitem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//����Activity��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web_view);
		
		//��ȡ��MainActivity���ݹ�����Bundle
		Bundle bundle = getIntent().getExtras();
		//2.
		newsitem = (News) bundle.getSerializable("news");
		
		initView();
		initEvent();
		initData();
		initcommentCount();
		//��ʼ��PopupWindown
		initPopupWindow();
	}
	//ͨ�������������
	private void initcommentCount() {
		String countUrl = CommonUtil.APPURL+"/cmt_num?nid="+newsitem.getNid()+"&ver=1";
		new VolleyHttp(this).getJSONString(countUrl, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				int count = ParserComments.parserCommentNum(response);
				commentTv.setText(count+"����");
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				showToast("��ȡ��������ʧ��");
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
		//����ʹ�û���
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		//����֧��javaScript�ű�
		webView.getSettings().setJavaScriptEnabled(true);
		// ���ÿ���֧������ 
		webView.getSettings().setSupportZoom(true); 
		// ���ó������Ź��� 
		webView.getSettings().setBuiltInZoomControls(true);
		//�������������
		webView.getSettings().setUseWideViewPort(true);
		//����Ӧ��Ļ
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		
		//��WebView�д���ҳ
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return true;
			}
		});
		//������Ի�ȡ��ҳ���صĽ���
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
	//������ؼ�������ҳ��
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	//��ʼ��PopupWindow
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
					showToast("�ղسɹ���\n��������໬�˵��в鿴");
				}
				else {
					showToast("�Ѿ��ղع����������ˣ�\n��������໬�˵��в鿴");
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
			//��ʾPopupWindow
			if (popupWindow != null && popupWindow.isShowing()) {
				popupWindow.dismiss();
			} else if (popupWindow != null) {
				popupWindow.showAsDropDown(menuImg, 0, 12);
			}
			break;
		case R.id.textView2://�����ı�
			Bundle bundle = new Bundle();
			bundle.putInt("nid", newsitem.getNid());
			openActivity(CommentActivity.class, bundle);
			break;
		default:
			break;
		}
	}
}
