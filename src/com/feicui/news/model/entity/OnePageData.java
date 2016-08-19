package com.feicui.news.model.entity;

import java.util.ArrayList;

public class OnePageData {
	private String message;
    private int status;
    private ArrayList<News> data;
    
	public OnePageData() {
		super();
	}

	public OnePageData(String message, int status, ArrayList<News> data) {
		super();
		this.message = message;
		this.status = status;
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ArrayList<News> getData() {
		return data;
	}

	public void setData(ArrayList<News> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "OnePageData [message=" + message + ", status=" + status
				+ ", data=" + data + "]";
	}
	
    
}
