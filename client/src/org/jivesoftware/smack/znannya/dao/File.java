package org.jivesoftware.smack.znannya.dao;

public class File {
	private long fileID;
	private long dissID;
	private String name;
	private String language;
	private int pages;
	private String author;
	private float viewCost;
	private float downloadCost;
	private long size;
	
	public File(){}
	
	public File(long fileID, long dissID, String name, String language, int pages, String author) {
		super();
		this.fileID = fileID;
		this.dissID = dissID;
		this.name = name;
		this.language = language;
		this.pages = pages;
		this.author = author;
	}

	public long getFileID() {
		return fileID;
	}

	public long getDissID() {
		return dissID;
	}

	public String getName() {
		return name;
	}

	public String getLanguage() {
		return language;
	}

	public int getPages() {
		return pages;
	}

	public String getAuthor() {
		return author;
	}

	public void setFileID(long fileID) {
		this.fileID = fileID;
	}

	public void setDissID(long dissID) {
		this.dissID = dissID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public float getViewCost() {
		return viewCost;
	}

	public void setViewCost(float viewCost) {
		this.viewCost = viewCost;
	}

	public float getDownloadCost() {
		return downloadCost;
	}

	public void setDownloadCost(float downloadCost) {
		this.downloadCost = downloadCost;
	}

	@Override
	public String toString() {
		return "File [author=" + author + ", dissID=" + dissID
				+ ", downloadCost=" + downloadCost + ", fileID=" + fileID
				+ ", language=" + language + ", name=" + name + ", pages="
				+ pages + ", size=" + size + ", viewCost=" + viewCost + "]";
	}
}
