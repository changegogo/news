package com.feicui.news.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.feicui.news.MainActivity;
import com.feicui.news.R;
import com.feicui.news.UserActivity;
import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.LoadImage;
import com.feicui.news.common.LoadImage.ImageLoadListener;
import com.feicui.news.common.LogUtil;
import com.feicui.news.common.SharedPreferencesUtils;
import com.feicui.news.common.SystemUtils;
import com.feicui.news.model.biz.parser.ParseVersion;
import com.feicui.news.model.entity.Ver;
import com.feicui.news.model.volleyhttp.VolleyHttp;
import com.feicui.news.receiver.DownloadCompleteReceiver;
import com.feicui.news.volley.Response.ErrorListener;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.VolleyError;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuRightFragment extends Fragment 
implements ImageLoadListener,OnClickListener{
	private String[] userInfo;
	
	private View view;
	private RelativeLayout relativelayout_unlogin;
	private RelativeLayout relativeLayout_logined;
	private boolean islogin;
	private SharedPreferences sharedPreferences;
	private ImageView imageView1, iv_pic;
	private TextView textView1, updateTv;
	//广播接收者
	private DownloadCompleteReceiver receiver;
	/**
	 * 分享到微信
	 */
	private ImageView iv_friend;
	/**
	 * 分享到QQ
	 */
	private ImageView iv_qq;
	/**
	 * 分享到朋友圈
	 */
	private ImageView iv_friends;
	/**
	 * 分享到微博
	 */
	private ImageView iv_weibo;
	/**
	 * 分享位置规定
	 */
	public static final int WEBCHAT = 1, QQ = 2, WEBCHATMOMENTS = 3, SINA = 4;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.fragment_menu_right, null);
			initView();
			initData();
			initEvent();
			
			return view;
	}
	
	
	private void initView() {
		//拿到用户信息文件
		sharedPreferences = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		//获取是否登录的信息
		islogin = sharedPreferences.getBoolean("islogin", false);
		
		//未登录视图控件
		relativelayout_unlogin = (RelativeLayout) view
				.findViewById(R.id.relativelayout_unlogin);
		//登录视图控件
		relativeLayout_logined = (RelativeLayout) view
				.findViewById(R.id.relativelayout_logined);
		//未登录时头像处显示的图片
		imageView1 = (ImageView) view.findViewById(R.id.imageView1);
		//立刻登录文本
		textView1 = (TextView) view.findViewById(R.id.textView1);
		//更新版本文本
		updateTv = (TextView) view.findViewById(R.id.update_version);
		// 初始化分享功能控件
		iv_friend = (ImageView) view.findViewById(R.id.fun_friend);
		iv_qq = (ImageView) view.findViewById(R.id.fun_qq);
		iv_friends = (ImageView) view.findViewById(R.id.fun_friends);
		iv_weibo = (ImageView) view.findViewById(R.id.fun_weibo);
		//初始化广播接收者
		receiver = new DownloadCompleteReceiver();
	}


	private void initData() {
		// TODO Auto-generated method stub
		
	}


	private void initEvent() {
		updateTv.setOnClickListener(this);
		imageView1.setOnClickListener(this);
		textView1.setOnClickListener(this);
		//登录展示的布局点击监听
		relativeLayout_logined.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), UserActivity.class);
				startActivity(intent);
			}
		});
		//分享设置监听
		iv_friend.setOnClickListener(this);
		iv_qq.setOnClickListener(this);
		iv_friends.setOnClickListener(this);
		iv_weibo.setOnClickListener(this);
	}


	@Override
	public void onResume() {
		super.onResume();
		
		userInfo = SharedPreferencesUtils.getUserNameAndPhoto(getActivity());
		if (!TextUtils.isEmpty(userInfo[0])) {
			relativeLayout_logined.setVisibility(View.VISIBLE);
			relativelayout_unlogin.setVisibility(View.GONE);
			initUserInfo();
		} else {
			relativelayout_unlogin.setVisibility(View.VISIBLE);
			relativeLayout_logined.setVisibility(View.GONE);
		}
		
		getActivity().registerReceiver(receiver,
				new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//取消广播的注册
		getActivity().unregisterReceiver(receiver);
	}
	
	/**根据用户信息是否存在本地来设置当前视图**/
	public void changeView() {
		islogin = sharedPreferences.getBoolean("islogin", false);
		if (islogin) {
			relativeLayout_logined.setVisibility(View.VISIBLE);
			relativelayout_unlogin.setVisibility(View.GONE);
			initUserInfo();
		} else {
			relativelayout_unlogin.setVisibility(View.VISIBLE);
			relativeLayout_logined.setVisibility(View.GONE);
		}
	}
	
	/** 初始化用户信息**/
	private void initUserInfo() {
		TextView tv_name = (TextView) view.findViewById(R.id.textView_name);
		iv_pic = (ImageView) view.findViewById(R.id.imageView_photo);
		tv_name.setText(userInfo[0]);
		String iconPath = SharedPreferencesUtils
				.getUserLocalIcon(getActivity());
		//本地存储的头像
		if (!TextUtils.isEmpty(iconPath)) {
			LogUtil.d(LogUtil.TAG, "menu right 本地存在用户主动上传的头像");
			Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
			iv_pic.setImageBitmap(bitmap);
			return;
		}
		//网络上的头像
		if (!TextUtils.isEmpty(userInfo[1])) {
			LogUtil.d(LogUtil.TAG, "menu right 本地存在用户主动上传的头像");
			LoadImage loadImage = new LoadImage(getActivity(), this);
			Bitmap bitmap = loadImage.getBitmap(userInfo[1]);
			if(bitmap != null){
				iv_pic.setImageBitmap(bitmap);
			}
		}
	}


	@Override
	public void imageLoadOk(Bitmap bitmap, String url) {
		if (bitmap != null) { 
			iv_pic.setImageBitmap(bitmap);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		// 判断登陆
		if (v.getId() == R.id.imageView1 || v.getId() == R.id.textView1) {
			((MainActivity) getActivity()).showFragmentLogin();
		}
		if(v.getId() == R.id.update_version){
			//请求网络，判断是否需要更新
			//http://118.244.212.82:9092/newsClient/update?imei=0&pkg=package-name&ver=1
			String versionUrl = CommonUtil.APPURL+"/update?imei="+SystemUtils.getIMEI(getActivity())
					+"&pkg="+"package-name"+"&ver="+CommonUtil.VERSION_CODE;
			judgeUpdate(versionUrl);
		}

		// 判断分享
		switch (v.getId()) {
		case R.id.fun_friend:// 分享到微信
			showShare(WEBCHAT);
			break;
		case R.id.fun_qq:
			showShare(QQ);
			break;
		case R.id.fun_friends:
			showShare(WEBCHATMOMENTS);
			break;
		case R.id.fun_weibo:
			showShare(SINA);
			break;
		}
	}
	
	private void judgeUpdate(String versionUrl) {
		new VolleyHttp(getActivity()).getJSONString(versionUrl, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.e("version", response);
				Ver versionInfo = ParseVersion.parse(response);
				String verCode = versionInfo.getVersion();
				//目前运行软件的版本号
				int curVer = CommonUtil.getVersionCode(getActivity());
				if(curVer < Integer.parseInt(verCode)){
					//执行下载请求
					Toast.makeText(getActivity(), "正在下载最新版本",
							0).show();
					downLoad(versionInfo.getLink());
				} else {
					Toast.makeText(getActivity(), "当前已是最新版本", 0)
							.show();
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
			}
		});
	}


	
	@SuppressLint("NewApi") protected void downLoad(String url) {
		// 初始化下载管理器
		DownloadManager manager = (DownloadManager) getActivity()
				.getSystemService(Context.DOWNLOAD_SERVICE); 
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(url));// 创建请求
		// 设置允许使用的网络类型，wifi
		request.setAllowedNetworkTypes(
				DownloadManager.Request.NETWORK_WIFI);
		// 在通知栏显示下载详情  在API 11中被setNotificationVisibility()取代
		request.setNotificationVisibility(
		DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		
		//表示允许MediaScanner扫描到这个文件，默认不允许。
		request.allowScanningByMediaScanner();
		//设置下载中通知栏提示的标题
		request.setTitle("每日新闻");
		//设置下载中通知栏提示的介绍
		request.setDescription("最新版");
		// 显示下载界面 
		request.setVisibleInDownloadsUi(true);
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd hh-mm-ss");
		String date = dateformat.format(new Date());
		//设置下载后文件存放的位置--如果目标位置已经存在这个文件名，则不执行下载，所以用date类型随机取名。
		request.setDestinationInExternalFilesDir(getActivity(), 
				null, date + ".apk");
		manager.enqueue(request);// 将下载请求放入队列
	}


	/**
	 * 全部分享界面显示
	 * 
	 * @param 分享的位置
	 */
	private void showShare(int platforms) {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("Tower新闻客户端");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        oks.setSite("每日新闻");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("Tower新闻客户端是一款好的新闻软件");
        
        switch (platforms) {
		case WEBCHAT:
			oks.setPlatform(Wechat.NAME);
			break;
		case WEBCHATMOMENTS:
			oks.setPlatform(WechatMoments.NAME);
			break;
		case QQ:
			oks.setPlatform(cn.sharesdk.tencent.qq.QQ.NAME);
			break;
		case SINA:
			oks.setPlatform(SinaWeibo.NAME);
			break;
		}
        // 启动分享GUI
        oks.show(getActivity());
   }
}
