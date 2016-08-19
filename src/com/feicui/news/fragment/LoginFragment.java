package com.feicui.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.feicui.news.MainActivity;
import com.feicui.news.R;
import com.feicui.news.UserActivity;
import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.LogUtil;
import com.feicui.news.common.SharedPreferencesUtils;
import com.feicui.news.common.SystemUtils;
import com.feicui.news.model.biz.parser.ParseUser;
import com.feicui.news.model.entity.BaseEntity;
/*import com.feicui.news.model.biz.UserManager;
import com.feicui.news.model.biz.parser.ParserUser;*/
import com.feicui.news.model.entity.Register;
import com.feicui.news.model.volleyhttp.VolleyHttp;
import com.feicui.news.volley.Response.ErrorListener;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.VolleyError;

/** 登陆界面 **/
public class LoginFragment extends Fragment {
	private View view;
	private EditText editTextNickname, editTextPwd;
	private Button but_register, btn_login, btn_forgetPass;
	//private UserManager userManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_login, container, false);
		editTextNickname = (EditText) view.findViewById(R.id.editText_nickname);
		editTextPwd = (EditText) view.findViewById(R.id.editText_pwd);
		but_register = (Button) view.findViewById(R.id.button_register);
		btn_forgetPass = (Button) view.findViewById(R.id.button_forgetPass);
		btn_login = (Button) view.findViewById(R.id.button_login);

		but_register.setOnClickListener(clickListener);
		btn_forgetPass.setOnClickListener(clickListener);
		btn_login.setOnClickListener(clickListener);
		return view;
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_login://点击的是登录按钮
				//获取文本框中的文本
				//用户昵称
				String name = editTextNickname.getText().toString().trim();
				//用户密码
				String pwd = editTextPwd.getText().toString().trim();
				//非空判断
				if(TextUtils.isEmpty(name)){
					Toast.makeText(getActivity(), "请输入用户名", 0).show();
					return;
				}
				if(TextUtils.isEmpty(pwd)){
					Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				if(pwd.length() < 6 || pwd.length()  > 16 ){
					Toast.makeText(getActivity(), "密码长度错误", Toast.LENGTH_SHORT).show();
					return ;
				}
				//请求网络获取返回数据
				// args 包含参数如下： ver : 版本 uid : 用户昵称    pwd : 密码    imei: 手机IMEI号   device :登录设备 0为移动端  ，1 为pc端 
				//http://118.244.212.82:9092/newsClient/user_login?uid=name&pwd=pwd&imei=abc&ver=1&device=1
				String longinUrl = CommonUtil.APPURL
						+ "/user_login?ver=" + CommonUtil.VERSION_CODE + "&uid=" + name + "&pwd="
						+ pwd+"&imei="+SystemUtils.getIMEI(getActivity()) + "&device=" + 0;
				
				new VolleyHttp(getActivity()).getJSONString(longinUrl, listener, errorListener);
				
				break;
			case R.id.button_register://点击的是注册按钮
				((MainActivity) getActivity()).showFragmentRegister();
				break;
			case R.id.button_forgetPass://点击的是忘记密码按钮
				((MainActivity) getActivity()).showFragmentForgetPass();
				break;
			}

		}
	};

	public Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			LogUtil.d(LogUtil.TAG, "执行登陆操作，返回信息：" + response);
			//解析注册回调信息，去做相应的处理
			BaseEntity<Register> registerBaseEntity = ParseUser.parserRegister(response);
			int status = registerBaseEntity.getStatus();
			String result = "";
			if (status == 0) {
				result = "登陆成功";
				//将登录信息保存
				SharedPreferencesUtils.saveRegister(getActivity(), registerBaseEntity);
				// TODO 跳转到用户界面
				startActivity(new Intent(getActivity(), UserActivity.class));
				// 增加动画=======
				getActivity().overridePendingTransition(
						R.anim.anim_activity_right_in,
						R.anim.anim_activity_bottom_out);
			} else if (status == -1) {
				result = "用户名或密码错误";
			} else {
				result = "登陆失败";
			}
			Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
		}
	};

	ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Toast.makeText(getActivity(), "登陆异常", Toast.LENGTH_SHORT).show();
		}
	};
}
