package com.feicui.news.model.entity;

public class BaseEntity<T> {
	private String message;
	private int status;
	private T data;
	public BaseEntity(String message, int status, T data) {
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
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "BaseEntity [message=" + message + ", status=" + status
				+ ", data=" + data + "]";
	}
	

}
