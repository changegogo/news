package com.feicui.news.volley.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.feicui.news.volley.AuthFailureError;
import com.feicui.news.volley.NetworkResponse;
import com.feicui.news.volley.Request;
import com.feicui.news.volley.Response;
import com.feicui.news.volley.Response.ErrorListener;
import com.feicui.news.volley.Response.Listener;
import com.feicui.news.volley.VolleyLog;

public class MultiPosttRequest extends Request<String> {

	private MultipartEntity entity = new MultipartEntity();
	private final Listener<String> mListener;

	public MultiPosttRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		// TODO Auto-generated constructor stub
		mListener = listener;
	}

	/**
	 * 上传文件
	 * 
	 * @param key
	 *            :描述
	 * @param mFilePart
	 *            :文件
	 */
	public void buildMultipartEntity(String key, File mFilePart) {
		entity.addFilePart(key, mFilePart);
	}
	
	/**
	 * 上传String
	 * 
	 * @param key
	 *            :描述
	 * @param mFilePart
	 *            :文件
	 */
	public void buildMultipartEntity(String key, String value) {
		entity.addStringPart(key, value);
	}

	@Override
	public String getBodyContentType() {
		return entity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			entity.writeTo(bos);
		} catch (IOException e) {
			e.getStackTrace();
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		return Response.success("Uploaded", getCacheEntry());
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}

}
