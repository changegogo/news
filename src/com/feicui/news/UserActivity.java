package com.feicui.news;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.feicui.news.R;
import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.LoadImage;
import com.feicui.news.common.LoadImage.ImageLoadListener;
import com.feicui.news.common.LogUtil;
import com.feicui.news.common.SharedPreferencesUtils;
import com.feicui.news.common.SystemUtils;
import com.feicui.news.model.biz.parser.ParseUser;
//import com.feicui.news.model.biz.UserManager;
//import com.feicui.news.model.biz.parser.ParserUser;
import com.feicui.news.model.entity.BaseEntity;
import com.feicui.news.model.entity.LoginLog;
import com.feicui.news.model.entity.Register;
import com.feicui.news.model.entity.User;
import com.feicui.news.model.httpclient.ResponseHandlerInterface;
import com.feicui.news.model.httpclient.TextHttpResponseHandler;
import com.feicui.news.model.volleyhttp.VolleyHttp;
import com.feicui.news.ui.adapter.LoginLogAdapter;
//import com.feicui.news.ui.adapter.LoginLogAdapter;
import com.feicui.news.ui.base.MyBaseActivity;
import com.feicui.news.volley.Response.ErrorListener;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.VolleyError;
/**用户中心**/
public class UserActivity extends MyBaseActivity implements OnClickListener{
	private LinearLayout layout;
	private ImageView imageView, imageView_back;//头像和返回按钮
	private TextView textView ,integralTextView , commentTextView ;
	private ListView logListview;
	private SharedPreferences sharedPreferences;
	private PopupWindow popupWindow;
	private Bitmap bitmap, alterBitmap;
	private File file;
	private LoadImage loadImage;
	private LoginLogAdapter adapter ;
	private Button btn_exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		initView();
		initViewData();
		
		//第一个参数：上下文，第二个参数：ImageLoadListener的接口实现对象
		//创建图片请求LoadImage对象
		loadImage = new LoadImage(UserActivity.this, new ImageLoadListener(){
			//此方法是用于网络请求后回调返回bitmap对象
			@Override
			public void imageLoadOk(Bitmap bitmap, String url) {
				if(bitmap !=null){
					imageView.setImageBitmap(bitmap);
				}
			}});
		
		initData();
		
		//获取本地存储的用户名和图片路径进行设置
		sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
		//用户名
		textView.setText(sharedPreferences.getString("userName", "无名氏"));
		//本地头像路径
		String localpath = sharedPreferences.getString("imagePath",null);
		if (localpath != null) {
			bitmap = BitmapFactory.decodeFile(localpath);
			imageView.setImageBitmap(bitmap);
		}
		imageView.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		imageView_back.setOnClickListener(this);
		initpopupwindow();//初始化点击头像的PopupWindow
	}

	

	private void initView() {
		layout = (LinearLayout) findViewById(R.id.layout);
		imageView = (ImageView) findViewById(R.id.icon);
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		textView = (TextView) findViewById(R.id.name);
		integralTextView = (TextView) findViewById(R.id.integral);
		commentTextView = (TextView) findViewById(R.id.comment_count);
		logListview = (ListView) findViewById(R.id.list);
		btn_exit = (Button) findViewById(R.id.btn_exit);
	}
	
	private void initViewData() {
		adapter = new LoginLogAdapter(this , new ArrayList<LoginLog>());
		logListview.setAdapter(adapter);
	}
	//初始化点击头像的PopupWindow
	private void initpopupwindow() {
		View contentView = getLayoutInflater().inflate(
				R.layout.item_pop_selectpic, null);
		//设置popupwindow视图
		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);  //
		LinearLayout photo_take = (LinearLayout) contentView
				.findViewById(R.id.photo_take);
		LinearLayout photo_sel = (LinearLayout) contentView
				.findViewById(R.id.photo_sel);
		//添加调用相机和从图片库中选择的监听
		photo_take.setOnClickListener(this);
		photo_sel.setOnClickListener(this);
	}

	/**
	 * 请求用户中心数据
	 */
	private void initData(){
		//获取本地存储的token值，这是在注册或者登录成功时获取
		String token = SharedPreferencesUtils.getToken(this);
		//http://118.244.212.82:9092/newsClient/user_home?ver=1&token=59ffa21224f7232324cf5f0c7c0726e8&imei=abc
		String centerUrl = CommonUtil.APPURL
				+ "/user_home?ver=" + CommonUtil.VERSION_CODE + "&token=" + token + "&imei="
				+ SystemUtils.getIMEI(this);
		
		new VolleyHttp(this).getJSONString(centerUrl, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				LogUtil.d("请求用户中心返回字符串", response);
				BaseEntity<User> user = ParseUser.parserUser(response);
				
				if(user.getStatus()!= 0 ){
					Toast.makeText(UserActivity.this, "请求用户中心失败", 0).show();
					return ;
				}
				//保存用户数据到本地 ： 用户昵称  、 用户头像地址
				SharedPreferencesUtils.saveUser(UserActivity.this, user);
				//显示数据更新UI
				User userCore = user.getData();
				//设置用户名显示文本
				textView.setText(userCore.getUid());
				//更新积分、发帖数
				integralTextView.setText("积分:"+userCore.getIntegration());
				commentTextView.setText(userCore.getComnum()+"");
				//更新登录记录数据，下方ListView
				adapter.addendData(userCore.getLoginlog(), true);
				adapter.updateAdapter();
				//获取用户头像地址ַ
				String portrait = userCore.getPortrait();
				if(!TextUtils.isEmpty(portrait)){
					//此方法内部优先判断缓存中是否有图片，若有则返回。
					//否则判断本地文件是否有图片存在，若有则返回，
					//反之，则请求网络数据，最后会回调下面的imageLoadOk()
					Bitmap bitmap = loadImage.getBitmap(portrait);
					if(bitmap != null){
						imageView.setImageBitmap(bitmap);
					}
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(UserActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
			}
		});
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon: //点击用户头像，底部弹起popupwindow
			popupWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.imageView_back:  
			startActivity(new Intent(UserActivity.this ,MainActivity.class));
			finish();
			break;
		case R.id.photo_take: //拍照
			popupWindow.dismiss();
			takePhoto();
			break;
		case R.id.photo_sel: //从相册选择
			popupWindow.dismiss();
			selectPhoto();
			break;
		case R.id.btn_exit://退出登录
			SharedPreferencesUtils.clearUser(UserActivity.this);
			startActivity(new Intent(UserActivity.this,MainActivity.class));
			finish();
			break;
		}
	}

	/* 跳转到系统的拍照功能 */
	protected void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, 100);
	}
	//跳转到系统相片，选择照片
	protected void selectPhoto() {
		final Intent intent = getPhotoPickIntent();
		startActivityForResult(intent, 200);
	}
	
	//封装请求Gallery的intent
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		//设置类型为图片
		intent.setType("image/*");
		intent.putExtra("crop", "true");//设置裁剪功能
		intent.putExtra("aspectX", 1); 	//宽高比例
		intent.putExtra("aspectY", 1);
		
		intent.putExtra("outputX", 80); //宽高值
		intent.putExtra("outputY", 80);
		
		intent.putExtra("return-data", true); //返回裁剪结果
		return intent;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("user", "1");
		if (requestCode == 100) {
			if (resultCode == Activity.RESULT_OK) {
				Log.e("user", "2");
				Bundle bundle = data.getExtras();
				bitmap = (Bitmap) bundle.get("data");
				save(bitmap); // 缓存用户选择的图片
			}
		} else if (requestCode == 200) {
			Log.e("user", "3");
			if (resultCode == Activity.RESULT_OK) {
				bitmap = data.getParcelableExtra("data");
				save(bitmap); // 缓存用户选择的图片
			}
		}
	}
	
	/**缓存用户上传的图片**/
	private void save(Bitmap bitmap) {
		if (bitmap == null)
			return;
		// if(Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED){
		//裁剪图片
		roundPic();
		
		
		File dir = new File(Environment.getExternalStorageDirectory(),
				"azynews");
		dir.mkdirs();
		file = new File(dir, "userpic.jpg");
		try {
			OutputStream stream = new FileOutputStream(file);
			if (alterBitmap.compress(CompressFormat.PNG, 100, stream)) {
				//上传图片
				/*UserManager.getInstance(this).changePhoto(this,SharedPreferencesUtils.getToken(this), file,
						listener,errorListener);*/
				//http://118.244.212.82:9092/newsClient/user_image?token=
				String uploadUrlString = CommonUtil.APPURL+"/user_image?" +
						"token=" + SharedPreferencesUtils.getToken(this);
				
				Log.e("uploadUrlString", uploadUrlString);
				Log.e("uploadUrlString", file.getAbsolutePath());
				new VolleyHttp(UserActivity.this).upLoadImage(uploadUrlString
						, file, listener, errorListener);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**裁剪图片*/
	private void roundPic() {
		//使用背景图生成的Bitmap
		Bitmap backBp = BitmapFactory.decodeResource(getResources(),
				R.drawable.userbg);
		//根据背景图Bitmap生成一个新的Bitmap
		alterBitmap = Bitmap.createBitmap(backBp.getWidth(),
				backBp.getHeight(), backBp.getConfig());
		//使用alterBitmap画布
		Canvas canvas = new Canvas(alterBitmap);
		//画笔
		Paint paint = new Paint();
		//设置抗锯齿
		paint.setAntiAlias(true);
		
		canvas.drawBitmap(backBp, new Matrix(), paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		//获取的图片Bitmap
		bitmap = Bitmap.createScaledBitmap(bitmap, backBp.getWidth(),
				backBp.getHeight(), true);
		canvas.drawBitmap(bitmap, new Matrix(), paint);
		//使用获取的图片设置头像
		imageView.setImageBitmap(alterBitmap);
	}
	
	private Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			BaseEntity<Register> entity  =  ParseUser.parserRegister(response);
			if(entity.getData().getResult()==0){
				//保存用户头像本地的路径
				SharedPreferencesUtils.saveUserLocalIcon(UserActivity.this
						, file.getAbsolutePath());
				imageView.setImageBitmap(alterBitmap);
			}
		}
	};

	private ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			SharedPreferencesUtils.saveUserLocalIcon(UserActivity.this
					, file.getAbsolutePath());
			
			System.out.println( "上传头像返回信息--->"+error.getMessage());
		}
	};

	/*

	

	
	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK == event.getKeyCode()){
			startActivity(new Intent(this ,ActivityMain.class));
			finish();
			return  true;
		}
		return super.onKeyDown(keyCode, event);
	}

	

	private ResponseHandlerInterface picResponseHandler = new TextHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				String responseString) {

			LogUtil.d(LogUtil.TAG, "上传头像返回信息--->"+responseString);
			BaseEntity<Register> entity  =  ParserUser.parserUploadImage(responseString);
			if(entity.getData().getResult().equals("0")){
				//保存用户头像的路径
				SharedPreferencesUtils.saveUserLocalIcon(ActivityUser.this, file.getAbsolutePath());
				imageView.setImageBitmap(alterBitmap);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			LogUtil.d(LogUtil.TAG, "上传用户头像失败---"+responseString);
		}
	};*/
}
