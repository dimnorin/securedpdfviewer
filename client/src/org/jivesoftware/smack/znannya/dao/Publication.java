package org.jivesoftware.smack.znannya.dao;

public class Publication extends Entry{
	
	public static final String TYPE_BOOK			= "Book";
	public static final String TYPE_MAGAZINE		= "Magazine";

	protected String isbn;
	protected String publisherName;
	protected String UDK_idx;
	protected String BBK_idx;
	
	public Publication(){}
	
	public Publication(long ID, String name, String author, String isbn, String UDK_idx, String BBK_idx,
			String city, String year, String description,
			String publisherName) {
		super(ID, publisherName, author, city, year, description);
		this.isbn = isbn;
		this.UDK_idx = UDK_idx;
		this.BBK_idx = BBK_idx;
		this.publisherName = publisherName;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getUDK_idx() {
		return UDK_idx;
	}

	public void setUDK_idx(String uDKIdx) {
		UDK_idx = uDKIdx;
	}

	public String getBBK_idx() {
		return BBK_idx;
	}

	public void setBBK_idx(String bBKIdx) {
		BBK_idx = bBKIdx;
	}

	@Override
	public String toString() {
		return "Publication [BBK_idx=" + BBK_idx + ", UDK_idx=" + UDK_idx
				+ ", isbn=" + isbn + ", publisherName=" + publisherName
				+ ", ID=" + ID + ", author=" + author + ", city=" + city
				+ ", description=" + description + ", name=" + name + ", year="
				+ year + "]";
	}
		
}
