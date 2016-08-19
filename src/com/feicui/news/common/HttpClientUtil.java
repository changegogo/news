package com.feicui.news.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.Environment;
import android.util.Log;

/**
 * 网络下载公共类
 * @author Administrator
 *
 */
public class HttpClientUtil {

	/**
	 * java 的url网络下载
	 * @param url 网络路径
	 * @return  得到的网络数据 字符串
	 * @throws Exception 
	 */
	public static String UrlNet(String path) throws Exception{
		
		StringBuffer sb=new StringBuffer();
		URL url;
		//建立连接
		url = new URL(path);
		//得到输入流  普通字节流
		InputStream in=url.openStream();
		//in.read();
		byte[] ch=new byte[1024];
		int count=0;
		
		//保存到文件中
		//获取当前sd路径
		File f=new File(Environment.getExternalStorageDirectory(),"ab");
		f.mkdir();
		File testNet=new File(f,"test.txt");
		FileOutputStream out=new FileOutputStream(testNet);
		
		while((count=in.read(ch))!=-1){
			
			out.write(ch, 0, count);
			out.flush();
			sb.append(new String(ch,0,count));
			//System.out.println(new String(ch,0,count));
			
		}
		out.close();
		in.close();
		//System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static String HttpURLConnectionNet(String path) throws Exception{
		
		String str="";
		URL url;
		//建立连接
		url = new URL(path);
		
		HttpURLConnection  httpConnection=(HttpURLConnection) url.openConnection();
		//得到输入流
		InputStream in=httpConnection.getInputStream();
		
		//in.read();
		byte[] ch=new byte[1024];
		int count=0;
		while((count=in.read(ch))!=-1){
			
			str+=new String(ch,0,count);
			//cou+=new String(ch,0,count).length();
			System.out.println(str);
		}
		in.close();
		//System.out.println("数量"+cou);
		return str;
	}

	//安卓提供的网络下载 HttpClient
	//建立对象
	private static HttpClient mHttpClient;
	private static int Timeout=5000;//超时时间
	private static int MaxTotalConnections=8;//最大连接数量
	
	/**设置HTTPclient对象：的超时时间，最大连接数量*/
	public static synchronized HttpClient getHttpClient(){
		if(mHttpClient==null){
			//建立参数对象
			HttpParams params=new BasicHttpParams();
			//设置连接池中超时时间
			ConnManagerParams.setTimeout(params, Timeout);
			ConnManagerParams.setMaxTotalConnections(params, MaxTotalConnections);
			//连接超时，这定义了通过网络与服务器建立连接的超时时间
			HttpConnectionParams.setConnectionTimeout(params, Timeout);
		
			mHttpClient=new DefaultHttpClient(params);
		}
		return mHttpClient;
	}
	/**连接网络，解析返回的数据*/
	public static String httpGet(String url){
		
		try {
			HttpClient httpClient=getHttpClient();
			HttpGet httpGet=new HttpGet(url);
			HttpResponse response=httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String resultStr = EntityUtils.toString(entity, "GBK");
			return resultStr;
		} catch (Exception e) {
			return null;
		}
	}
	
}
