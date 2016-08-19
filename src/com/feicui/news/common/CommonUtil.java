package com.feicui.news.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtil {
	//http://118.244.212.82:9092/newsClient/news_sort?ver=0000000&imei=12334
	//http://118.244.212.82:9092/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=2016722&cnt=10";
	//news_sort?ver=版本号&imei=手机标识符
	//版本号
	public static final int VERSION_CODE = 1;
	public static final String APPURL="http://118.244.212.82:9092/newsClient"; 
	/** 联网的ip */
	public static String NETIP = "118.244.212.82";
	/** 联网的路径 */
	public static String NETPATH = "http://" + NETIP + ":9092/newsClient";
	/** SharedPreferences保存用户名键 */
	public static final String SHARE_USER_NAME = "userName";
	/** SharedPreferences保存用户名密码 */
	public static final String SHARE_USER_PWD = "userPwd";
	/** SharedPreferences保存是否第一次登陆 */
	public static final String SHARE_IS_FIRST_RUN = "isFirstRun";
	/** SharedPreferences存储路径 */
	public static final String SHAREPATH = "news_share";
	
	/**
     * 检查网络是否连通
     * @return boolean
     * @since V1.0
     */
    public static boolean isNetworkAvailable(Context context) {
        // 创建并初始化连接对象
        ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(
        		Context.CONNECTIVITY_SERVICE);
        // 判断初始化是否成功并作出相应处理
        if (connMan != null) {
            // 调用getActiveNetworkInfo方法创建对象,如果不为空则表明网络连通，否则没连通
            NetworkInfo info = connMan.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }
    
    //弹出框提醒
    public static Dialog showDialog(Context ctx, int layViewID, int ThemeType) {
           Dialog res = new Dialog(ctx, ThemeType);
           res.setContentView(layViewID);
           return res;
    }
    
    //以想要的格式获取系统时间字符串
    public static String getSystemTime(String format){
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	Date date = new Date(System.currentTimeMillis());
    	return sdf.format(date);
    }
    /**
	 * 验证邮箱格式
	 * @param email  email
	 * @return 
	 */
	public static boolean verifyEmail(String email){
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)" +
						"|(([a-zA-Z0-9\\-]+\\.)+))" +
						"([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	/***
	 * 验证密码格式
	 * @param password
	 * @return
	 */
	public static boolean verifyPassword(String password){
		Pattern pattern = Pattern
				.compile("^[a-zA-Z0-9]{6,16}$");
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
	
	/**
	 * 获取当前的版本号
	 * 
	 * @param context 上下文对象
	 * @return 当前版本
	 */
	public static int getVersionCode(Context context)//获取版本号(内部识别号)
	{
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
    

}
