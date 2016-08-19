package com.feicui.news.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.feicui.news.MainActivity;
import com.feicui.news.R;
import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.LogUtil;

import com.feicui.news.model.biz.parser.ParseUser;
import com.feicui.news.model.entity.BaseEntity;
import com.feicui.news.model.entity.Register;
import com.feicui.news.model.volleyhttp.VolleyHttp;
import com.feicui.news.volley.Response.ErrorListener;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.VolleyError;
import com.feicui.news.volley.VolleyError;
import com.feicui.news.volley.Response.ErrorListener;

public class ForgetPassFragment extends Fragment {
	/** 邮箱编辑框 */
	private EditText editEmail;
	/** 确认按钮 */
	private Button btnCommit;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_forgetpass, container,
				false);
		editEmail = (EditText) view.findViewById(R.id.edit_email);
		btnCommit = (Button) view.findViewById(R.id.btn_commit);
		btnCommit.setOnClickListener(clickListener);
		return view;
	}

	private View.OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// 点击确认按钮，向服务器发送忘记密码请求
			if (arg0.getId() == R.id.btn_commit) {
				String email = editEmail.getText().toString().trim();
				if (!CommonUtil.verifyEmail(email)) {
					Toast.makeText(getActivity(), "请求输入正确的邮箱格式ʽ",
							Toast.LENGTH_SHORT).show();
					return;
				}
				//请求忘记密码接口
				String forgetPassUrl = CommonUtil.APPURL
						+ "/user_forgetpass?ver=" 
						+ CommonUtil.VERSION_CODE 
						+ "&email=" + email;
				new VolleyHttp(getActivity()).getJSONString(forgetPassUrl, listener, errorListener);

			}
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
			LogUtil.d(LogUtil.TAG, "执行忘记密码操作，返回信息：" + response);
			BaseEntity<Register> register = ParseUser.parserRegister(response);
			int status = register.getStatus();
			String explain = "";
			if (status == 0) {
				Register entity = register.getData();
				explain = entity.getExplain();
				if (entity.getResult()==0) {
					//忘记密码请求成功展示登录界面
					((MainActivity) getActivity()).showFragmentLogin();
				} else if (entity.getResult()==-2) {
					editEmail.requestFocus();
				}
				Toast.makeText(getActivity(), explain, Toast.LENGTH_SHORT)
						.show();
			}

		}
	};
}
