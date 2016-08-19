package com.feicui.news.model.entity;

public class Comment {
	/*
	“cid”:评论编号,
	“uid”:评论者名字,
	“portrait”:用户头像链接,
	“stamp”:评论时间,
	“content":评论内容
	 */
	private int cid ;
	private String uid;
	private String portrait;
	private String stamp;
	private String content;
	
	
	public Comment() {
		
	}
	
	public Comment(int cid, String uid, String portrait, String stamp,
			String content) {
		this.cid = cid;
		this.uid = uid;
		this.portrait = portrait;
		this.stamp = stamp;
		this.content = content;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	public String getStamp() {
		return stamp;
	}
	public void setStamp(String stamp) {
		this.stamp = stamp;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
	
	
}
