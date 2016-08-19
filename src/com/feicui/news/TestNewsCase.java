package com.feicui.news;

import java.util.List;

import com.feicui.news.common.HttpClientUtil;
import com.feicui.news.model.biz.parser.ParseNews;
import com.feicui.news.model.entity.SubType;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestNewsCase extends AndroidTestCase{
	//≤‚ ‘Õ¯¬Á£¨«Î«Û ˝æ›
	public void testInternet(){
		String jsonString = HttpClientUtil.httpGet("http://118.244.212.82:9092/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=20140321&cnt=20");
		Log.e("TestNewsCase", jsonString);
	}
	public void testType(){
		String jsonString = HttpClientUtil.httpGet("http://118.244.212.82:9092/newsClient/news_sort?ver=0000000&imei=12334");
		Log.e("TestNewsCase", jsonString);
		List<SubType> list = ParseNews.parseJsonNewsType(null, jsonString);
		Log.e("ewrew", list.toString());
	}

}
