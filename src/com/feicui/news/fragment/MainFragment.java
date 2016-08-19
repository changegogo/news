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
	//���ݿ�
	private NewsDBManager newsDBManager;
	
	//һҳ���ݵ�����20
	private int onPageCount = 20;
	//��ǰҳ��
	private int page = 0;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(isRefresh){
				listView.stopRefresh();
				listView.setRefreshTime(CommonUtil.getSystemTime("yyyy-MM-dd hh:mm:ss"));
			}else{
				listView.stopLoadMore();
			}
			
			//���߳̿��Խ���UI����
			if(msg.what == 101){
				OnePageData onePageData = (OnePageData)msg.obj;
				//�õ��������ݼ���
				list = onePageData.getData();
				//��������ӵ�ԭ��list������
				newsAdapter.addendData(list, isRefresh);
				//����ListView
				newsAdapter.updateAdapter();
			}else if(msg.what == 102){
				ArrayList<News> list = (ArrayList<News>)msg.obj;
				//��������ӵ�ԭ��list������
				newsAdapter.addendData(list, isRefresh);
				//����ListView
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
		//�ж������Ƿ�ͨ��
        boolean isNetworkAvailable = CommonUtil.isNetworkAvailable(getActivity());
        if(isNetworkAvailable){
        	//����ͨ�����첽��������
        	connetInetByVolley();
        }else{
        	//���粻��ͨ
        	
        	//��ȡ���ݿ���������Ŀ
        	long dbCount = newsDBManager.getCount();
        	if(dbCount > 0){
        		//��ѯ����
        		ArrayList<News> list = newsDBManager.queryNews(onPageCount, 0);
        		//handler������Ϣ
            	handler.sendMessage(handler.obtainMessage(102, list));
            	//
            	Toast.makeText(getActivity(), "�������"+dbCount, Toast.LENGTH_LONG).show();
        	}
        }
	}

	private void initView(View view) {
		//���������ͼ
		hScrollView = (HorizontalScrollView) view.findViewById(R.id.hScrollView);
		//���غ��������
		hScrollView.setHorizontalScrollBarEnabled(false);
		//������ͼ�е����Բ���
		llayout = (LinearLayout) view.findViewById(R.id.llayout_newslist);
		//����ˢ�����������б�
		listView = (XListView) view.findViewById(R.id.listview);
	}
	
	private void initData() {
		//��ʼ��VolleyHttp
		volleyHttp = new VolleyHttp(getActivity());
		//��ȡ���ݿ������
    	newsDBManager = NewsDBManager.getNewsDBManager(getActivity());
    	//���������ȡ������������
		connectInetNewsType();
		
		//��������adapter����
		newsAdapter = new NewsAdapter(getActivity());
		//ΪListView����adapter
		listView.setAdapter(newsAdapter);
		//���ÿ�������ˢ����������
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		
	}
	//���������ȡ������������
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
				//����TextView
				TextView tv = new TextView(getActivity());
				//�����ı�
				tv.setText(newsTypelist.get(i).getSubgroup());
				//���óߴ�
				tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				//�����ڱ߾�
				tv.setPadding(10, 20, 10, 20);
				//���þ���
				tv.setGravity(Gravity.CENTER_VERTICAL);
				//���õ����ĵ�һ����ɫΪ��ɫ
				if(i == 0){
					tv.setTextColor(Color.RED);
				}
				//����tagֵ
				tv.setTag(i);
				//���õ���¼�
				setClickListener(tv);
				//��ӵ����Բ�����
				llayout.addView(tv);
			}
		}
	}
	//���õ�������TextView�ĵ���¼�
	private void setClickListener(final TextView tv){
		//���õ���¼�
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//�������е�TextView����ɫΪ��ɫ
				for(int i=0;i<llayout.getChildCount();i++){
					((TextView)llayout.getChildAt(i)).setTextColor(Color.BLACK);
				}
				tv.setTextColor(Color.RED);
				//���ù���
				int tvWidth = tv.getWidth();
				int tag = (Integer) v.getTag();
				//����scrollview
				hScrollView.smoothScrollTo(tvWidth*(tag -3), 0);
				//�������������ȡ������������
			}
		});
	}
	
	private void initEvent() {
		//ListView����Ŀ����¼�
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
		
		//����item�����¼�
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				News news = (News)parent.getAdapter().getItem(position);
				showShare(news);
				return true;
			}
		});
		//ˢ�¼��ؼ���
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
	
	//һ������
	private void showShare(News news) {
		 ShareSDK.initSDK(getActivity());
		 OnekeyShare oks = new OnekeyShare();
		 //�ر�sso��Ȩ
		 oks.disableSSOWhenAuthorize(); 

		// ����ʱNotification��ͼ�������  2.5.9�Ժ�İ汾�����ô˷���
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		 oks.setTitle(news.getTitle());
		 // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		 oks.setTitleUrl(news.getLink());
		 // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		 oks.setText(news.getSummary());
		 // imagePath��ͼƬ�ı���·��
		 oks.setImagePath(news.getIcon());//ȷ��SDcard������ڴ���ͼƬ
		 // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		 oks.setUrl(news.getSummary());
		 // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		 oks.setComment("������");
		 // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		 oks.setSiteUrl(news.getLink());

		// ��������GUI
		 oks.show(getActivity());
	}
	//�ֶ��������߳�������·����
	private void connetInetAsync() {
		new Thread(new Runnable() {	
				@Override
				public void run() {
				//���̣߳���ֹ����UI����
				//�����������󣬻�ȡjson����
				String stamp = CommonUtil.getSystemTime("yyyyMMdd");
				String jsonString = HttpClientUtil.httpGet(CommonUtil.NETPATH+"/news_list?ver=1&subid=1&dir=1&nid=1&stamp="+stamp+"&cnt=10");
				if(jsonString != null){
					//����json����
					OnePageData opd = ParseNews.parseJsonByFastJson(getActivity(),jsonString);
					//������Ϣ��UI�߳�
					Message message = handler.obtainMessage(101, opd);
					handler.sendMessage(message);
				}else{
					handler.sendEmptyMessage(103);
					Toast.makeText(getActivity(), "�������", Toast.LENGTH_SHORT).show();
				}
			}
		}).start();
	}
	//ʹ��Volley����������������
	private void connetInetByVolley(){
		String stamp = CommonUtil.getSystemTime("yyyyMMdd");
		String url = CommonUtil.NETPATH+"/news_list?ver=1&subid=1&dir=1&nid=1&stamp="+stamp+"&cnt=10";
		
		volleyHttp.getJSONString(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				if(response != null){
					//����json����
					OnePageData opd = ParseNews.parseJsonByFastJson(getActivity(),response);
					if(isRefresh){
						listView.stopRefresh();
						listView.setRefreshTime(CommonUtil.getSystemTime("yyyy-MM-dd hh:mm:ss"));
					}else{
						listView.stopLoadMore();
					}
					//�õ��������ݼ���
					list = opd.getData();
					//��������ӵ�ԭ��list������
					newsAdapter.addendData(list, isRefresh);
					//����ListView
					newsAdapter.updateAdapter();
					
				}else{
					Toast.makeText(getActivity(), "�������", Toast.LENGTH_SHORT).show();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity(), "�������", Toast.LENGTH_SHORT).show();
			}
		});
	}
		
}
