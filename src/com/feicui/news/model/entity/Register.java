package com.feicui.news.model.entity;

public class Register {

	private int result; 
	private String token;  
	private String explain;
	

	public Register(int result, String token, String explain) {
		super();
		this.result = result;
		this.token = token;
		this.explain = explain;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExplain() {
		return explain;
	}

	public void setLexplain(String explain) {
		this.explain = explain;
	}
}
