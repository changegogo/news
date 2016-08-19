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
	//news_sort?ver=�汾��&imei=�ֻ���ʶ��
	//�汾��
	public static final int VERSION_CODE = 1;
	public static final String APPURL="http://118.244.212.82:9092/newsClient"; 
	/** ������ip */
	public static String NETIP = "118.244.212.82";
	/** ������·�� */
	public static String NETPATH = "http://" + NETIP + ":9092/newsClient";
	/** SharedPreferences�����û����� */
	public static final String SHARE_USER_NAME = "userName";
	/** SharedPreferences�����û������� */
	public static final String SHARE_USER_PWD = "userPwd";
	/** SharedPreferences�����Ƿ��һ�ε�½ */
	public static final String SHARE_IS_FIRST_RUN = "isFirstRun";
	/** SharedPreferences�洢·�� */
	public static final String SHAREPATH = "news_share";
	
	/**
     * ��������Ƿ���ͨ
     * @return boolean
     * @since V1.0
     */
    public static boolean isNetworkAvailable(Context context) {
        // ��������ʼ�����Ӷ���
        ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(
        		Context.CONNECTIVITY_SERVICE);
        // �жϳ�ʼ���Ƿ�ɹ���������Ӧ����
        if (connMan != null) {
            // ����getActiveNetworkInfo������������,�����Ϊ�������������ͨ������û��ͨ
            NetworkInfo info = connMan.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }
    
    //����������
    public static Dialog showDialog(Context ctx, int layViewID, int ThemeType) {
           Dialog res = new Dialog(ctx, ThemeType);
           res.setContentView(layViewID);
           return res;
    }
    
    //����Ҫ�ĸ�ʽ��ȡϵͳʱ���ַ���
    public static String getSystemTime(String format){
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	Date date = new Date(System.currentTimeMillis());
    	return sdf.format(date);
    }
    /**
	 * ��֤�����ʽ
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
	 * ��֤�����ʽ
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
	 * ��ȡ��ǰ�İ汾��
	 * 
	 * @param context �����Ķ���
	 * @return ��ǰ�汾
	 */
	public static int getVersionCode(Context context)//��ȡ�汾��(�ڲ�ʶ���)
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
