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
	//�㲥������
	private DownloadCompleteReceiver receiver;
	/**
	 * ����΢��
	 */
	private ImageView iv_friend;
	/**
	 * ����QQ
	 */
	private ImageView iv_qq;
	/**
	 * ��������Ȧ
	 */
	private ImageView iv_friends;
	/**
	 * ����΢��
	 */
	private ImageView iv_weibo;
	/**
	 * ����λ�ù涨
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
		//�õ��û���Ϣ�ļ�
		sharedPreferences = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		//��ȡ�Ƿ��¼����Ϣ
		islogin = sharedPreferences.getBoolean("islogin", false);
		
		//δ��¼��ͼ�ؼ�
		relativelayout_unlogin = (RelativeLayout) view
				.findViewById(R.id.relativelayout_unlogin);
		//��¼��ͼ�ؼ�
		relativeLayout_logined = (RelativeLayout) view
				.findViewById(R.id.relativelayout_logined);
		//δ��¼ʱͷ����ʾ��ͼƬ
		imageView1 = (ImageView) view.findViewById(R.id.imageView1);
		//���̵�¼�ı�
		textView1 = (TextView) view.findViewById(R.id.textView1);
		//���°汾�ı�
		updateTv = (TextView) view.findViewById(R.id.update_version);
		// ��ʼ�������ܿؼ�
		iv_friend = (ImageView) view.findViewById(R.id.fun_friend);
		iv_qq = (ImageView) view.findViewById(R.id.fun_qq);
		iv_friends = (ImageView) view.findViewById(R.id.fun_friends);
		iv_weibo = (ImageView) view.findViewById(R.id.fun_weibo);
		//��ʼ���㲥������
		receiver = new DownloadCompleteReceiver();
	}


	private void initData() {
		// TODO Auto-generated method stub
		
	}


	private void initEvent() {
		updateTv.setOnClickListener(this);
		imageView1.setOnClickListener(this);
		textView1.setOnClickListener(this);
		//��¼չʾ�Ĳ��ֵ������
		relativeLayout_logined.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), UserActivity.class);
				startActivity(intent);
			}
		});
		//�������ü���
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
		//ȡ���㲥��ע��
		getActivity().unregisterReceiver(receiver);
	}
	
	/**�����û���Ϣ�Ƿ���ڱ��������õ�ǰ��ͼ**/
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
	
	/** ��ʼ���û���Ϣ**/
	private void initUserInfo() {
		TextView tv_name = (TextView) view.findViewById(R.id.textView_name);
		iv_pic = (ImageView) view.findViewById(R.id.imageView_photo);
		tv_name.setText(userInfo[0]);
		String iconPath = SharedPreferencesUtils
				.getUserLocalIcon(getActivity());
		//���ش洢��ͷ��
		if (!TextUtils.isEmpty(iconPath)) {
			LogUtil.d(LogUtil.TAG, "menu right ���ش����û������ϴ���ͷ��");
			Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
			iv_pic.setImageBitmap(bitmap);
			return;
		}
		//�����ϵ�ͷ��
		if (!TextUtils.isEmpty(userInfo[1])) {
			LogUtil.d(LogUtil.TAG, "menu right ���ش����û������ϴ���ͷ��");
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
		// �жϵ�½
		if (v.getId() == R.id.imageView1 || v.getId() == R.id.textView1) {
			((MainActivity) getActivity()).showFragmentLogin();
		}
		if(v.getId() == R.id.update_version){
			//�������磬�ж��Ƿ���Ҫ����
			//http://118.244.212.82:9092/newsClient/update?imei=0&pkg=package-name&ver=1
			String versionUrl = CommonUtil.APPURL+"/update?imei="+SystemUtils.getIMEI(getActivity())
					+"&pkg="+"package-name"+"&ver="+CommonUtil.VERSION_CODE;
			judgeUpdate(versionUrl);
		}

		// �жϷ���
		switch (v.getId()) {
		case R.id.fun_friend:// ����΢��
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
				//Ŀǰ��������İ汾��
				int curVer = CommonUtil.getVersionCode(getActivity());
				if(curVer < Integer.parseInt(verCode)){
					//ִ����������
					Toast.makeText(getActivity(), "�����������°汾",
							0).show();
					downLoad(versionInfo.getLink());
				} else {
					Toast.makeText(getActivity(), "��ǰ�������°汾", 0)
							.show();
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity(), "�������", Toast.LENGTH_SHORT).show();
			}
		});
	}


	
	@SuppressLint("NewApi") protected void downLoad(String url) {
		// ��ʼ�����ع�����
		DownloadManager manager = (DownloadManager) getActivity()
				.getSystemService(Context.DOWNLOAD_SERVICE); 
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(url));// ��������
		// ��������ʹ�õ��������ͣ�wifi
		request.setAllowedNetworkTypes(
				DownloadManager.Request.NETWORK_WIFI);
		// ��֪ͨ����ʾ��������  ��API 11�б�setNotificationVisibility()ȡ��
		request.setNotificationVisibility(
		DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		
		//��ʾ����MediaScannerɨ�赽����ļ���Ĭ�ϲ�����
		request.allowScanningByMediaScanner();
		//����������֪ͨ����ʾ�ı���
		request.setTitle("ÿ������");
		//����������֪ͨ����ʾ�Ľ���
		request.setDescription("���°�");
		// ��ʾ���ؽ��� 
		request.setVisibleInDownloadsUi(true);
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd hh-mm-ss");
		String date = dateformat.format(new Date());
		//�������غ��ļ���ŵ�λ��--���Ŀ��λ���Ѿ���������ļ�������ִ�����أ�������date�������ȡ����
		request.setDestinationInExternalFilesDir(getActivity(), 
				null, date + ".apk");
		manager.enqueue(request);// ����������������
	}


	/**
	 * ȫ�����������ʾ
	 * 
	 * @param �����λ��
	 */
	private void showShare(int platforms) {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        //�ر�sso��Ȩ
        oks.disableSSOWhenAuthorize();
        
        // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
        oks.setTitle("����");
        // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
        oks.setTitleUrl("http://sharesdk.cn");
        // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
        oks.setText("Tower���ſͻ���");
        // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
        oks.setImagePath("/sdcard/test.jpg");
        // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
        oks.setUrl("http://sharesdk.cn");
        oks.setSite("ÿ������");
        // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
        oks.setComment("Tower���ſͻ�����һ��õ��������");
        
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
        // ��������GUI
        oks.show(getActivity());
   }
}
