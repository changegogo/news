package com.feicui.news.test;

import java.util.ArrayList;



public class AllData {

    private String message;
    private int status;
    private ArrayList<OneData> data;
    
	public AllData(String message, int status, ArrayList<OneData> data) {
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
	public ArrayList<OneData> getData() {
		return data;
	}
	public void setData(ArrayList<OneData> data) {
		this.data = data;
	}
   
}
