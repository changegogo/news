package com.feicui.news.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.feicui.news.MainActivity;
import com.feicui.news.R;
import com.feicui.news.UserActivity;
import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.SharedPreferencesUtils;
import com.feicui.news.model.biz.parser.ParseUser;
import com.feicui.news.model.entity.BaseEntity;
/*import com.feicui.news.model.biz.UserManager;
import com.feicui.news.model.biz.parser.ParserUser;
import com.feicui.news.model.entity.BaseEntity;*/
import com.feicui.news.model.entity.Register;
import com.feicui.news.model.volleyhttp.VolleyHttp;
import com.feicui.news.volley.Response.ErrorListener;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.VolleyError;

/**注册界面**/
public class RegisterFragment extends Fragment {
	private View view;
	private EditText editTextEmail, editTextName, editTextPwd;
	private Button but_register;
	private CheckBox checkBox;
	private String email, name, pwd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_register, container, false);
		editTextEmail = (EditText) view.findViewById(R.id.editText_email);
		editTextName = (EditText) view.findViewById(R.id.editText_name);
		editTextPwd = (EditText) view.findViewById(R.id.editText_pwd);
		but_register = (Button) view.findViewById(R.id.button_register);
		checkBox = (CheckBox) view.findViewById(R.id.checkBox1);

		but_register.setOnClickListener(clickListener);

		return view;
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//判断是否同意条款
			if(!checkBox.isChecked()){
				Toast.makeText(getActivity(), "没有同意协议条款！", Toast.LENGTH_SHORT).show();
				return;
			}
			//去除3个文本框中的文本
			String email=editTextEmail.getText().toString().trim();
			String name=editTextName.getText().toString().trim();
			String pwd=editTextPwd.getText().toString().trim();
			if(!CommonUtil.verifyEmail(email)){
				Toast.makeText(getActivity(), "请求输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
				return;
			}
			//对用户名进行非空判断
			if(TextUtils.isEmpty(name) ){
				Toast.makeText(getActivity(), "请输入用户昵称", Toast.LENGTH_SHORT).show();
				return ;
			}
			//密码长度判断
			if(pwd.length() < 6 || pwd.length()  > 16 ){
				Toast.makeText(getActivity(), "密码长度错误", Toast.LENGTH_SHORT).show();
				return ;
			}
			//对密码格式进行判断
			if(!CommonUtil.verifyPassword(pwd)){
				Toast.makeText(getActivity(), "请输入6-16位数字和字母组合的密码", Toast.LENGTH_SHORT).show();
				return;
			}
			//注册逻辑
			String registerUrl = CommonUtil.APPURL + "/user_register?ver=" + CommonUtil.VERSION_CODE
					+ "&uid=" + name + "&pwd=" + pwd + "&email=" + email;
			/*
			// ver : 版本 uid : 用户昵称 pwd : 密码 email : 邮箱 device : 手机IMEI号
			userManager.register(getActivity(), listener, errorListener,
					CommonUtil.VERSION_CODE + "", name, pwd, email);*/
			new VolleyHttp(getActivity()).getJSONString(registerUrl, listener, errorListener);
		}
	};
	ErrorListener errorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
		}
	};
	Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			//javaBean对象通过json数据得到的
			BaseEntity<Register> register = ParseUser
					.parserRegister(response);
			//data
			Register data = register.getData();
			//状态码
			int status = register.getStatus();
			if (status == 0) {
				int result = data.getResult();
				String explain = data.getExplain();
				//result ="注册成功";
				if(result==0){
					//保存用户信息
					SharedPreferencesUtils.saveRegister(getActivity(), register);
					//注册成功跳转到用户信息界面
					startActivity(new Intent(getActivity(),UserActivity.class));
					//增加动画
					getActivity().overridePendingTransition(R.anim.anim_activity_right_in
							, R.anim.anim_activity_bottom_out);
					//更新界面
					/**
					 * 右侧是否登录的切换
					 */
					((MainActivity) getActivity()).changeUserFragment();
				}
				 ((MainActivity)getActivity()).showNews(); 
				Toast.makeText(getActivity(), explain, Toast.LENGTH_SHORT)
						.show();
			}

		}
	};
}
