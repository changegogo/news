package com.feicui.news.model.entity;

public class Ver {
/*
 * "pkgName": "com.ys.android",
    "link": "http://118.244.212.82:9092/Images/test.apk",
    "md5": "9770ad0a9dc59fc51ec2db3079697536",
    "version": "1"
 * */
	private String pkgName;
	private String link;
	private String md5;
	private String version;
	
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Ver(String pkgName, String link, String md5, String version) {
		super();
		this.pkgName = pkgName;
		this.link = link;
		this.md5 = md5;
		this.version = version;
	}
	@Override
	public String toString() {
		return "Ver [pkgName=" + pkgName + ", link=" + link + ", md5=" + md5
				+ ", version=" + version + "]";
	}
	
	
}
