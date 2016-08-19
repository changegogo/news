package com.feicui.news.fragment;

import com.feicui.news.MainActivity;
import com.feicui.news.R;
import com.feicui.news.volley.Request;
import com.feicui.news.volley.Request.Method;
import com.feicui.news.volley.toolbox.Volley;

import android.R.integer;
import android.drm.DrmStore.RightsStatus;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MenuLeftFragment extends Fragment implements OnClickListener{
	
	private RelativeLayout[] rlAarray = new RelativeLayout[5];
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu_left, null);
		
		initView(view);
		initEvent();
	
		return view;
	}

	private void initView(View view) {
		RelativeLayout rlNews = (RelativeLayout) view.findViewById(R.id.rl_news);
		RelativeLayout rlReading = (RelativeLayout) view.findViewById(R.id.rl_reading);
		RelativeLayout rlLocal = (RelativeLayout) view.findViewById(R.id.rl_local);
		RelativeLayout rlCommnet = (RelativeLayout) view.findViewById(R.id.rl_commnet);
		RelativeLayout rlPhoto = (RelativeLayout) view.findViewById(R.id.rl_photo);
		rlAarray[0] = rlNews;
		rlAarray[1] = rlReading;
		rlAarray[2] = rlLocal;
		rlAarray[3] = rlCommnet;
		rlAarray[4] = rlPhoto;
	}
	
	private void initEvent() {
		for(RelativeLayout rl:rlAarray){
			rl.setOnClickListener(this);
		}
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.rl_news:
			changeAllBgColor(rlAarray[0]);
			((MainActivity)getActivity()).showNews();
			break;
		case R.id.rl_reading:
			changeAllBgColor(rlAarray[1]);
			((MainActivity)getActivity()).showFavorate();
			break;
		case R.id.rl_local:
			changeAllBgColor(rlAarray[2]);
			((MainActivity)getActivity()).showLocal();
			break;
		case R.id.rl_commnet:
			changeAllBgColor(rlAarray[3]);
			((MainActivity)getActivity()).showComment();
			break;
		case R.id.rl_photo:
			changeAllBgColor(rlAarray[4]);
			((MainActivity)getActivity()).showPhoto();
			break;

		default:
			break;
		}
	}
	
	//改变rl的颜色为透明色且设置当前点击的布局的颜色
	private void changeAllBgColor(View view){
		if(rlAarray != null){
			for(RelativeLayout rl:rlAarray){
				rl.setBackgroundColor(0);
			}
		}
		if(view != null)
			view.setBackgroundColor(0x33c85555);
	}

}
