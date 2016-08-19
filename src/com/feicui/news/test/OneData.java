package com.feicui.news.test;

import java.util.ArrayList;

public class OneData {
	private ArrayList<SubType> subgrp;
	private int gid;
	private String group;
	public OneData(ArrayList<SubType> subgrp, int gid, String group) {
		super();
		this.subgrp = subgrp;
		this.gid = gid;
		this.group = group;
	}
	public ArrayList<SubType> getSubgrp() {
		return subgrp;
	}
	public void setSubgrp(ArrayList<SubType> subgrp) {
		this.subgrp = subgrp;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
}
