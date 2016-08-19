package com.feicui.news.ui.base;

import com.feicui.news.MyApplication;
import com.feicui.news.R;
import com.feicui.news.common.LogUtil;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



/**
 * ����activity�������Դ�ӡactivity�������ںͽ�Ŀ�Ľ�����˳�����
 * 
 * @author Administrator
 * 
 */
public class MyBaseActivity extends FragmentActivity {

	// ����
	protected MyApplication app;// ȫ�ִ洢����
	protected Dialog dialog;// �Ի���
	protected FrameLayout layout_screenoff;// �ر�ʱ�Ķ���
	protected int screen_w,screen_h;

	/******************************** ��Activity LifeCycle For Debug Start�� ***************************************/
	/**
	 * ��ǰActivity����ʱ������(��һ������,��Activity�����ٺ��ٴ�����,δ��android:
	 * configChanges�������������÷����ı�ʱ)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onCreate");
		app = (MyApplication) getApplication();
		screen_w=getWindowManager().getDefaultDisplay().getWidth();
		screen_h=getWindowManager().getDefaultDisplay().getHeight();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onDestroy");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onPause");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onResume");

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onStart");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onStop");
	}

	/*********************** ��װ�˵����ķ��� **********************************/
	// ��ͨ��ת
	public void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	// ��ת��������
	public void openActivity(Class<?> pClass, Bundle pbBundle) {
		openActivity(pClass, pbBundle, null);
	}

	// ��ת������
	public void openActivity(Class<?> pClass, Bundle pbBundle, Uri uri) {
		Intent intent = new Intent(this, pClass);
		if (pbBundle != null) {
			intent.putExtras(pbBundle);
		}
		if (uri != null) {
			intent.setData(uri);
		}
		startActivity(intent);
		// ��ת��ִ�еĶ��� ��һ�������ǽ�����activity �ڶ�������ʧ��activity
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);

	}

	// ����action
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	protected void openActivity(String pAction, Bundle pBundle) {
		openActivity(pAction, pBundle, null);
	}

	protected void openActivity(String pAction, Bundle pBundle, Uri uri) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		if (uri != null) {
			intent.setData(uri);
		}
		startActivity(intent);
		// ��ת��ִ�еĶ��� ��һ�������ǽ�����activity �ڶ�������ʧ��activity
		/*overridePendingTransition(R.anim.screen_right_in,
				R.anim.screen_down_out);*/

	}
	/**
	 * @Title: showLoadingDialog
	 * @Description: ��ʾһ���ȴ��Ի���
	 * @param mContext
	 *            �����Ļ���
	 * @param msg
	 *            ��Ϣ
	 * @param cancelable
	 *            �Ƿ��ȡ��
	 * @return ����Dialog�������
	 * 
	 */
	public void showLoadingDialog(Context context, String msg,
			boolean cancelable) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loading, null);// �õ�����view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// ���ز���
		//�Զ���ͼƬ
		ImageView iv_img = (ImageView) v.findViewById(R.id.iv_dialogloading_img);
		// ��ʾ����
		TextView tv_msg = (TextView) v.findViewById(R.id.tv_dialogloading_msg);
		// ���ض���
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
		// ʹ��ImageView��ʾ����
		iv_img.startAnimation(animation);
		if(null != msg) {
			// ���ü�����Ϣ	
			tv_msg.setText(msg);		
		}
		// �����Զ�����ʽdialog
		dialog = new Dialog(context, R.style.loading_dialog);
		// �������á����ؼ���ȡ��
		dialog.setCancelable(cancelable);
		// ���ò���
		dialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		//��ʾdialog
		dialog.show();
	}

	/**
	 * @Title: cancelDialog
	 * @Description: ȡ��dialog��ʾ
	 * @author hj
	 */
	public void cancelDialog() {
		if (null != dialog) {
			dialog.dismiss();
		}
	}
	
	
	/**************************�������ܷ�װ****************************************/
	private Toast toast;
	public void showToast(int resId){
		showToast(getString(resId));
	}
	
	public void showToast(String msg){
		if(toast==null)
			toast=Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setText(msg);
		toast.show();
	}

}
