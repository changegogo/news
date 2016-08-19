package com.feicui.news.fragment;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.feicui.news.MainActivity;
import com.feicui.news.R;
import com.feicui.news.WebViewActivity;
import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.HttpClientUtil;
import com.feicui.news.model.biz.parser.ParseNews;
import com.feicui.news.model.dao.NewsDBManager;
import com.feicui.news.model.entity.News;
import com.feicui.news.model.entity.OnePageData;
import com.feicui.news.model.entity.SubType;
import com.feicui.news.model.volleyhttp.VolleyHttp;
import com.feicui.news.ui.adapter.NewsAdapter;
import com.feicui.news.view.xlistview.XListView;
import com.feicui.news.view.xlistview.XListView.IXListViewListener;
import com.feicui.news.volley.Response.ErrorListener;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.VolleyError;

import android.R.integer;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainFragment extends Fragment{
	private XListView listView;
	private NewsAdapter newsAdapter;
	private ArrayList<News> list;
	private ArrayList<SubType> newsTypelist;
	private LinearLayout llayout;
	private HorizontalScrollView hScrollView;
	private VolleyHttp volleyHttp;
	private boolean isRefresh;
	//数据库
	private NewsDBManager newsDBManager;
	
	//一页数据的条数20
	private int onPageCount = 20;
	//当前页码
	private int page = 0;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(isRefresh){
				listView.stopRefresh();
				listView.setRefreshTime(CommonUtil.getSystemTime("yyyy-MM-dd hh:mm:ss"));
			}else{
				listView.stopLoadMore();
			}
			
			//主线程可以进行UI更新
			if(msg.what == 101){
				OnePageData onePageData = (OnePageData)msg.obj;
				//得到新闻数据集合
				list = onePageData.getData();
				//将数据添加到原来list集合中
				newsAdapter.addendData(list, isRefresh);
				//更新ListView
				newsAdapter.updateAdapter();
			}else if(msg.what == 102){
				ArrayList<News> list = (ArrayList<News>)msg.obj;
				//将数据添加到原来list集合中
				newsAdapter.addendData(list, isRefresh);
				//更新ListView
				newsAdapter.updateAdapter();
			}else if(msg.what == 103){
				
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_newslist, null);
		
		initView(view);
		initData();
        initEvent();
        
        initInetorDbData();
        
		return view;
	}
	
	private void initInetorDbData() {
		//Integer.MAX_VALUE;
		//判断网络是否通畅
        boolean isNetworkAvailable = CommonUtil.isNetworkAvailable(getActivity());
        if(isNetworkAvailable){
        	//网络通畅，异步请求数据
        	connetInetByVolley();
        }else{
        	//网络不连通
        	
        	//获取数据库中数据条目
        	long dbCount = newsDBManager.getCount();
        	if(dbCount > 0){
        		//查询数据
        		ArrayList<News> list = newsDBManager.queryNews(onPageCount, 0);
        		//handler发送消息
            	handler.sendMessage(handler.obtainMessage(102, list));
            	//
            	Toast.makeText(getActivity(), "网络错误"+dbCount, Toast.LENGTH_LONG).show();
        	}
        }
	}

	private void initView(View view) {
		//横向滚动视图
		hScrollView = (HorizontalScrollView) view.findViewById(R.id.hScrollView);
		//隐藏横向滚动条
		hScrollView.setHorizontalScrollBarEnabled(false);
		//滚动视图中的线性布局
		llayout = (LinearLayout) view.findViewById(R.id.llayout_newslist);
		//下拉刷新上拉加载列表
		listView = (XListView) view.findViewById(R.id.listview);
	}
	
	private void initData() {
		//初始化VolleyHttp
		volleyHttp = new VolleyHttp(getActivity());
		//获取数据库管理类
    	newsDBManager = NewsDBManager.getNewsDBManager(getActivity());
    	//连接网络获取新闻类型数据
		connectInetNewsType();
		
		//创建新闻adapter对象
		newsAdapter = new NewsAdapter(getActivity());
		//为ListView设置adapter
		listView.setAdapter(newsAdapter);
		//设置可以下拉刷新上拉加载
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		
	}
	//请求网络获取新闻类型数据
	private void connectInetNewsType(){
		if(CommonUtil.isNetworkAvailable(getActivity())){
			String typeUrl = CommonUtil.APPURL+"/news_sort?ver=0000000&imei=12334";
			volleyHttp.getJSONString(typeUrl, new Listener<String>() {

				@Override
				public void onResponse(String response) {
					newsTypelist = ParseNews.parseJsonNewsType(getActivity(), response);
					addView(newsTypelist);
				
				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					
				}
			});
		}else{
			ArrayList<SubType> types = newsDBManager.queryNewsType();
			addView(types);
		}
		
	}
	private void addView(ArrayList<SubType> newsTypelist){
		if(newsTypelist != null&&newsTypelist.size()>0){
			for(int i = 0;i<newsTypelist.size();i++){
				//创建TextView
				TextView tv = new TextView(getActivity());
				//设置文本
				tv.setText(newsTypelist.get(i).getSubgroup());
				//设置尺寸
				tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				//设置内边距
				tv.setPadding(10, 20, 10, 20);
				//设置居中
				tv.setGravity(Gravity.CENTER_VERTICAL);
				//设置导航的第一个颜色为红色
				if(i == 0){
					tv.setTextColor(Color.RED);
				}
				//设置tag值
				tv.setTag(i);
				//设置点击事件
				setClickListener(tv);
				//添加到线性布局中
				llayout.addView(tv);
			}
		}
	}
	//设置导航条中TextView的点击事件
	private void setClickListener(final TextView tv){
		//设置点击事件
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//设置所有的TextView的颜色为黑色
				for(int i=0;i<llayout.getChildCount();i++){
					((TextView)llayout.getChildAt(i)).setTextColor(Color.BLACK);
				}
				tv.setTextColor(Color.RED);
				//设置滚动
				int tvWidth = tv.getWidth();
				int tag = (Integer) v.getTag();
				//滚动scrollview
				hScrollView.smoothScrollTo(tvWidth*(tag -3), 0);
				//重新连接网络获取其他类别的数据
			}
		});
	}
	
	private void initEvent() {
		//ListView的条目点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				News news = (News)parent.getAdapter().getItem(position);
				if(news != null){
					Bundle bundle = new Bundle();
					//1.
					bundle.putSerializable("news", news);
					((MainActivity)getActivity()).openActivity(WebViewActivity.class, bundle);
				}
			}
		});
		
		//长按item监听事件
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				News news = (News)parent.getAdapter().getItem(position);
				showShare(news);
				return true;
			}
		});
		//刷新加载监听
		listView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				isRefresh = true;
				initInetorDbData();
			}
			
			@Override
			public void onLoadMore() {
				isRefresh = false;
				initInetorDbData();
			}
		});
	}
	
	//一键分享
	private void showShare(News news) {
		 ShareSDK.initSDK(getActivity());
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(news.getTitle());
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl(news.getLink());
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText(news.getSummary());
		 // imagePath是图片的本地路径
		 oks.setImagePath(news.getIcon());//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl(news.getSummary());
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("好新闻");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl(news.getLink());

		// 启动分享GUI
		 oks.show(getActivity());
	}
	//手动开启子线程请求网路数据
	private void connetInetAsync() {
		new Thread(new Runnable() {	
				@Override
				public void run() {
				//子线程，禁止进行UI更新
				//进行联网请求，获取json数据
				String stamp = CommonUtil.getSystemTime("yyyyMMdd");
				String jsonString = HttpClientUtil.httpGet(CommonUtil.NETPATH+"/news_list?ver=1&subid=1&dir=1&nid=1&stamp="+stamp+"&cnt=10");
				if(jsonString != null){
					//解析json数据
					OnePageData opd = ParseNews.parseJsonByFastJson(getActivity(),jsonString);
					//发送消息给UI线程
					Message message = handler.obtainMessage(101, opd);
					handler.sendMessage(message);
				}else{
					handler.sendEmptyMessage(103);
					Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
				}
			}
		}).start();
	}
	//使用Volley请求新闻网络数据
	private void connetInetByVolley(){
		String stamp = CommonUtil.getSystemTime("yyyyMMdd");
		String url = CommonUtil.NETPATH+"/news_list?ver=1&subid=1&dir=1&nid=1&stamp="+stamp+"&cnt=10";
		
		volleyHttp.getJSONString(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				if(response != null){
					//解析json数据
					OnePageData opd = ParseNews.parseJsonByFastJson(getActivity(),response);
					if(isRefresh){
						listView.stopRefresh();
						listView.setRefreshTime(CommonUtil.getSystemTime("yyyy-MM-dd hh:mm:ss"));
					}else{
						listView.stopLoadMore();
					}
					//得到新闻数据集合
					list = opd.getData();
					//将数据添加到原来list集合中
					newsAdapter.addendData(list, isRefresh);
					//更新ListView
					newsAdapter.updateAdapter();
					
				}else{
					Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
			}
		});
	}
		
}
