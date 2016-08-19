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
 * 父类activity用来调试打印activity生命周期和节目的进入和退出动画
 * 
 * @author Administrator
 * 
 */
public class MyBaseActivity extends FragmentActivity {

	// 属性
	protected MyApplication app;// 全局存储容器
	protected Dialog dialog;// 对话框
	protected FrameLayout layout_screenoff;// 关闭时的动画
	protected int screen_w,screen_h;

	/******************************** 【Activity LifeCycle For Debug Start】 ***************************************/
	/**
	 * 当前Activity创建时来调用(第一次启动,本Activity被销毁后再次启动,未对android:
	 * configChanges进行设置且配置发生改变时)
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

	/*********************** 封装了调整的方法 **********************************/
	// 普通跳转
	public void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	// 跳转传递数据
	public void openActivity(Class<?> pClass, Bundle pbBundle) {
		openActivity(pClass, pbBundle, null);
	}

	// 跳转带动画
	public void openActivity(Class<?> pClass, Bundle pbBundle, Uri uri) {
		Intent intent = new Intent(this, pClass);
		if (pbBundle != null) {
			intent.putExtras(pbBundle);
		}
		if (uri != null) {
			intent.setData(uri);
		}
		startActivity(intent);
		// 跳转后执行的动画 第一个参数是进来的activity 第二个是消失的activity
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);

	}

	// 传递action
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
		// 跳转后执行的动画 第一个参数是进来的activity 第二个是消失的activity
		/*overridePendingTransition(R.anim.screen_right_in,
				R.anim.screen_down_out);*/

	}
	/**
	 * @Title: showLoadingDialog
	 * @Description: 显示一个等待对话框
	 * @param mContext
	 *            上下文环境
	 * @param msg
	 *            消息
	 * @param cancelable
	 *            是否可取消
	 * @return 返回Dialog这个对象
	 * 
	 */
	public void showLoadingDialog(Context context, String msg,
			boolean cancelable) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		//自定义图片
		ImageView iv_img = (ImageView) v.findViewById(R.id.iv_dialogloading_img);
		// 提示文字
		TextView tv_msg = (TextView) v.findViewById(R.id.tv_dialogloading_msg);
		// 加载动画
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
		// 使用ImageView显示动画
		iv_img.startAnimation(animation);
		if(null != msg) {
			// 设置加载信息	
			tv_msg.setText(msg);		
		}
		// 创建自定义样式dialog
		dialog = new Dialog(context, R.style.loading_dialog);
		// 不可以用“返回键”取消
		dialog.setCancelable(cancelable);
		// 设置布局
		dialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		//显示dialog
		dialog.show();
	}

	/**
	 * @Title: cancelDialog
	 * @Description: 取消dialog显示
	 * @author hj
	 */
	public void cancelDialog() {
		if (null != dialog) {
			dialog.dismiss();
		}
	}
	
	
	/**************************公共功能封装****************************************/
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
