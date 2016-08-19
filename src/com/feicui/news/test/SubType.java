package com.feicui.news.test;

public class SubType {
	private String subgroup;
    private int subid;
	public SubType(String subgroup, int subid) {
		super();
		this.subgroup = subgroup;
		this.subid = subid;
	}
	public String getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}
	public int getSubid() {
		return subid;
	}
	public void setSubid(int subid) {
		this.subid = subid;
	}
    
}
