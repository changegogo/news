package com.feicui.news;

import com.feicui.news.fragment.CommentFragment;
import com.feicui.news.fragment.FavorateFragment;
import com.feicui.news.fragment.ForgetPassFragment;
import com.feicui.news.fragment.LocalFragment;
import com.feicui.news.fragment.LoginFragment;
import com.feicui.news.fragment.MainFragment;
import com.feicui.news.fragment.MenuLeftFragment;
import com.feicui.news.fragment.MenuRightFragment;
import com.feicui.news.fragment.PhotoFragment;
import com.feicui.news.fragment.RegisterFragment;
import com.feicui.news.ui.base.MyBaseActivity;
import com.feicui.news.view.slidingmenu.SlidingMenu;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends MyBaseActivity {
	private SlidingMenu slidingMenu;
	private MainFragment mainFragment;
	private FavorateFragment favorateFragment;
	private LocalFragment localFragment;
	private CommentFragment commentFragment;
	private PhotoFragment photoFragment;
	private LoginFragment loginFragment;
	private RegisterFragment registerFragment;
	private ForgetPassFragment forgetPassFragment;
	private MenuRightFragment menuRightFragment;
	//����
	private TextView tvTitle;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    
        initView();
        initSlidingMenu();
        showNews();
        
       
    }
    
    private void initView() {
		setContentView(R.layout.activity_main);
		tvTitle = (TextView) findViewById(R.id.title);
    }
    //���ı���
    private void changeTitle(String title){
    	tvTitle.setText(title);
    }
    /*
     * ��ʼ��SlidingMenu
     * */
	private void initSlidingMenu() {
		slidingMenu = new SlidingMenu(this);
		//����ұ߶���
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		//���ô�������
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//���ô�֮��ĳߴ�
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		//����thisΪ������
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		//������߲�������
		slidingMenu.setMenu(R.layout.layout_menu);
		//�����ұ߲�������
		slidingMenu.setSecondaryMenu(R.layout.layout_menu_right);
		//������ʾ������
		slidingMenu.showContent();
		
		showLeftRightMenu();
	}
	
	//չʾ���ҵĲ˵�ҳ��
	private void showLeftRightMenu(){
		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.frame_left, new MenuLeftFragment())
		.commit();
		menuRightFragment = new MenuRightFragment();
		getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.frame_right, menuRightFragment)
			.commit();
	}
	
	//չʾ����ҳ��
	public void showNews(){
		changeTitle("�YӍ");
		//��ʾ��ҳ��
		slidingMenu.showContent();
		if(mainFragment == null){
			mainFragment = new MainFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, mainFragment)
			.commit();
	}
	//չʾ�ղ�ҳ��
	public void showFavorate(){
		changeTitle("�ղ�����");
		//��ʾ��ҳ��
		slidingMenu.showContent();
		if(favorateFragment == null){
			favorateFragment = new FavorateFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, favorateFragment)
			.commit();
	}
	//չʾ����ҳ��
	public void showLocal(){
		changeTitle("����");
		//��ʾ��ҳ��
		slidingMenu.showContent();
		if(localFragment == null){
			localFragment = new LocalFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, localFragment)
			.commit();
	}
	
	public void showComment(){
		changeTitle("����");
		//��ʾ����
		slidingMenu.showContent();
		if(commentFragment == null){
			commentFragment = new CommentFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, commentFragment)
			.commit();
	}
	//չʾ
	public void showPhoto(){
		changeTitle("ͼƬ");
		//��ʾͼƬҳ
		slidingMenu.showContent();
		if(photoFragment == null){
			photoFragment = new PhotoFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, photoFragment)
			.commit();
	}
	//չʾ��¼����
	public void showFragmentLogin() {
		changeTitle("��¼");
		slidingMenu.showContent();
		if(loginFragment == null){
			loginFragment = new LoginFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, loginFragment)
			.commit();
	}
	//չʾע�����
	public void showFragmentRegister() {
		changeTitle("ע��");
		slidingMenu.showContent();
		if(registerFragment == null){
			registerFragment = new RegisterFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, registerFragment)
			.commit();
	}
	//չʾ�����������
	public void showFragmentForgetPass() {
		changeTitle("��������");
		slidingMenu.showContent();
		if(forgetPassFragment == null){
			forgetPassFragment = new ForgetPassFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, forgetPassFragment)
			.commit();
	}
	/**
	 * �Ҳ��Ƿ��¼���л�
	 */
	public void changeUserFragment() {
		menuRightFragment.changeView();
	}
	
	@Override
	public void onBackPressed() {
		if (slidingMenu.isMenuShowing()) {
			slidingMenu.showContent();
		} else {
			exitTwice();
		}
	}
	
	//�����˳�
	private boolean isFirstExit=true;
	private void exitTwice(){
		if(isFirstExit){
			Toast.makeText(this, "�ٰ�һ���˳���", Toast.LENGTH_SHORT).show();
			isFirstExit=false;
			//����һ���߳�ȥ����isFirstExit
			new Thread(){
				public void run() {
					try {
						Thread.sleep(2000);
						isFirstExit=true;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				};
			}.start(); 
		}else{
			finish();
		}
	}
	
}
