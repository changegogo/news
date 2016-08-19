package com.feicui.news;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.LogUtil;
import com.feicui.news.common.SharedPreferencesUtils;
import com.feicui.news.common.SystemUtils;
import com.feicui.news.model.biz.parser.ParserComments;
import com.feicui.news.model.entity.Comment;
import com.feicui.news.model.volleyhttp.VolleyHttp;
import com.feicui.news.ui.adapter.CommentsAdapter;
import com.feicui.news.ui.base.MyBaseActivity;
import com.feicui.news.view.xlistview.XListView;
import com.feicui.news.view.xlistview.XListView.IXListViewListener;

import com.feicui.news.volley.Response.ErrorListener;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.VolleyError;

public class CommentActivity extends MyBaseActivity implements OnClickListener{
	/**����id*/
	private int nid;
	/**�����б�*/
	private XListView listView;
	/**�����б�������*/
	private CommentsAdapter adapter;
	/***/
    private  int mode;	
    /**�������۰�ť*/
    private ImageView imageView_send;
    /**���ذ�ť*/
    private ImageView imageView_back;
    /**���۱༭��*/
	private EditText editText_content;
	//��ʶ��ˢ�»��Ǽ���
	private boolean isRefresh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//����Activity��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_comment);
		//webViewҳ����ת����������������nid
		nid = getIntent().getIntExtra("nid", -1);
		initView();
		initData();
	}
	private void initData() {
		adapter = new CommentsAdapter(this, listView);
		listView.setAdapter(adapter);
		//���ÿ�������ˢ����������
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(listViewListener);
		loadNextComment();

		imageView_back.setOnClickListener(this);
		imageView_send.setOnClickListener(this);
	}
	private void initView() {
		listView = (XListView) findViewById(R.id.listview);
		imageView_send = (ImageView) findViewById(R.id.imageview_send);
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		editText_content = (EditText) findViewById(R.id.edittext);
	}
	
	/**
	 * �������µ�����
	 */
	protected void loadNextComment() {
		int curId = adapter.getAdapterData().size() <= 0 ? 0 : adapter.getItem(
				0).getCid();
		LogUtil.d(LogUtil.TAG, "loadnextcomment--->currentId=" + curId);
		String url = CommonUtil.APPURL + "/cmt_list?ver=" + CommonUtil.VERSION_CODE + "&nid="
				+ nid + "&dir=" + 1 + "&cid=" + curId + "&type="
				+ 1 + "&stamp=" + "20140707";
		new VolleyHttp(this).getJSONString(url, listener, errorListener);
		
	}
	
	/**
	 * ���������20������
	 */
	protected void loadPreComment() {
		Log.e("tag", listView.getLastVisiblePosition()+",,,,");
		Comment comment = adapter
				.getItem(listView.getLastVisiblePosition() - 2);
		if (SystemUtils.getInstance(this).isNetConn()) {
			String url = CommonUtil.APPURL + "/cmt_list?ver=" + CommonUtil.VERSION_CODE + "&nid="
					+ nid + "&dir=" + 2 + "&cid=" + comment.getCid() + "&type="
					+ 1 + "&stamp=" + "20140707";
			new VolleyHttp(this).getJSONString(url, listener, errorListener);
		}
	}
	//�����������ݳɹ��ص�����
	private Listener<String> listener = new Listener<String>(){

	@Override
	public void onResponse(String response) {
		ArrayList<Comment> comments = ParserComments.parserComment(response);
		if (comments == null || comments.size() < 1) {
			return;
		}
		//���������ˢ�£�isRefresh��true��clearԭ��������
		adapter.addendData(comments, isRefresh);
		adapter.updateAdapter();
	}};
	private ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back:
			finish();
			break;
		case R.id.imageview_send:
			sendComment();
			break;
		}
	}
	//��������
	private void sendComment(){
		//���۵�����
		String ccontent = editText_content.getText().toString();
		if (ccontent == null || ccontent.equals("")) {
			Toast.makeText(CommentActivity.this, "Ҫ��д��������Ŷ���ף�",
					Toast.LENGTH_SHORT).show();
			return;
		}
		//����귢���������÷��Ͱ�ť�����ã��ȵ��������������������ÿ���
		imageView_send.setEnabled(false);
		//��ȡ�豸��
		String imei = SystemUtils.getInstance(CommentActivity.this)
				.getIMEI();
		String token = SharedPreferencesUtils
				.getToken(CommentActivity.this);
		if (TextUtils.isEmpty(token)) {
			Toast.makeText(CommentActivity.this, "�Բ�������û�е�¼.", 0)
					.show();
			return;
		}
		//չʾ������
		showLoadingDialog(CommentActivity.this, "", true);
		//�������磬�ύ����
		String commUrl = CommonUtil.APPURL + "/cmt_commit?nid=" + nid + "&ver="
				+ CommonUtil.VERSION_CODE + "&token=" + token + "&imei=" + imei + "&ctx="
				+ ccontent;
		new VolleyHttp(this).getJSONString(commUrl, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.e("response", response);
				int status = ParserComments
						.parserSendComment(response.trim());
				if (status == 0) {
					showToast("���۳ɹ���");
					editText_content.setText("");
					editText_content.clearFocus();
					//��Ϊtrue��־��֮ǰ���������
					isRefresh = true;
					loadNextComment();
				} else {
					showToast("����ʧ�ܣ�");
				}
				imageView_send.setEnabled(true);
				//���ؽ�����
				dialog.cancel();
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				showToast("�����������쳣��");
				imageView_send.setEnabled(true);
				//���ؽ�����
				dialog.cancel();
			}
		});
	}
	
	private IXListViewListener listViewListener = new IXListViewListener() {
		@Override
		public void onRefresh() {
			// �����������ݡ�������������������������������������
			isRefresh = true;
			loadNextComment();
			// �������
			listView.stopLoadMore();
			listView.stopRefresh();
			listView.setRefreshTime(CommonUtil.getSystemTime("yyyy-MM-dd hh:mm:ss"));
		}

		@Override
		public void onLoadMore() {
			// ���������������ݡ�������������������������������������
			int count = adapter.getCount();
			if (count > 1) { // �����ǰ��ListView������һ��item�ǲ������û����ظ���
				loadPreComment();
			}
			listView.stopLoadMore();
			listView.stopRefresh();
		}
	};
}
