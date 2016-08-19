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
	//标题
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
    //更改标题
    private void changeTitle(String title){
    	tvTitle.setText(title);
    }
    /*
     * 初始化SlidingMenu
     * */
	private void initSlidingMenu() {
		slidingMenu = new SlidingMenu(this);
		//左边右边都有
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		//设置触摸类型
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//设置打开之后的尺寸
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		//设置this为主布局
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		//设置左边布局容器
		slidingMenu.setMenu(R.layout.layout_menu);
		//设置右边布局容器
		slidingMenu.setSecondaryMenu(R.layout.layout_menu_right);
		//首先显示主布局
		slidingMenu.showContent();
		
		showLeftRightMenu();
	}
	
	//展示左右的菜单页面
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
	
	//展示新闻页面
	public void showNews(){
		changeTitle("資訊");
		//显示主页面
		slidingMenu.showContent();
		if(mainFragment == null){
			mainFragment = new MainFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, mainFragment)
			.commit();
	}
	//展示收藏页面
	public void showFavorate(){
		changeTitle("收藏新闻");
		//显示主页面
		slidingMenu.showContent();
		if(favorateFragment == null){
			favorateFragment = new FavorateFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, favorateFragment)
			.commit();
	}
	//展示本地页面
	public void showLocal(){
		changeTitle("本地");
		//显示主页面
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
		changeTitle("跟帖");
		//显示跟帖
		slidingMenu.showContent();
		if(commentFragment == null){
			commentFragment = new CommentFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, commentFragment)
			.commit();
	}
	//展示
	public void showPhoto(){
		changeTitle("图片");
		//显示图片页
		slidingMenu.showContent();
		if(photoFragment == null){
			photoFragment = new PhotoFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, photoFragment)
			.commit();
	}
	//展示登录界面
	public void showFragmentLogin() {
		changeTitle("登录");
		slidingMenu.showContent();
		if(loginFragment == null){
			loginFragment = new LoginFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, loginFragment)
			.commit();
	}
	//展示注册界面
	public void showFragmentRegister() {
		changeTitle("注册");
		slidingMenu.showContent();
		if(registerFragment == null){
			registerFragment = new RegisterFragment();
		}
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.framelayout_main, registerFragment)
			.commit();
	}
	//展示忘记密码界面
	public void showFragmentForgetPass() {
		changeTitle("忘记密码");
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
	 * 右侧是否登录的切换
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
	
	//两次退出
	private boolean isFirstExit=true;
	private void exitTwice(){
		if(isFirstExit){
			Toast.makeText(this, "再按一次退出！", Toast.LENGTH_SHORT).show();
			isFirstExit=false;
			//开启一个线程去设置isFirstExit
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
