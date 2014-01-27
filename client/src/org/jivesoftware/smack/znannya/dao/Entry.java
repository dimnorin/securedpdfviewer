package org.jivesoftware.smack.znannya.dao;

public abstract class Entry {

	protected long ID;
	protected String name;
	protected String author;
	protected String city;
	protected String year;
	protected String description;
	
	public Entry(){}
	
	public Entry(long iD, String name, String author, String city, String year,
			String description) {
		super();
		ID = iD;
		this.name = name;
		this.author = author;
		this.city = city;
		this.year = year;
		this.description = description;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
