package com.feicui.news.fragment;

import java.util.ArrayList;

import com.feicui.news.MainActivity;
import com.feicui.news.R;
import com.feicui.news.WebViewActivity;
import com.feicui.news.model.dao.NewsDBManager;
import com.feicui.news.model.entity.News;
import com.feicui.news.ui.adapter.NewsAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FavorateFragment extends Fragment{
	private View view;
	private ListView listView;
	private NewsAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_favorite,container,false);
		listView=(ListView) view.findViewById(R.id.listview);
		adapter=new NewsAdapter(getActivity());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(itemListener);
		//加载数据库
		loadLoveNews();
		return view;
	}
	/**从数据库中加载保存的新闻*/
	private void loadLoveNews() {
		ArrayList<News> data=NewsDBManager.getNewsDBManager(getActivity()).queryLoveNews();
		adapter.addendData(data, true);
	}

	private OnItemClickListener itemListener=new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			// 打开显示当前选中的新闻
			News news = (News) parent.getItemAtPosition(position);
			if(news != null){
				Bundle bundle = new Bundle();
				bundle.putSerializable("news", news);
				((MainActivity)getActivity()).openActivity(WebViewActivity.class, bundle);
			}
		}
	};
}
