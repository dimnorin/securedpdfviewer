package ua.com.znannya.client.service;

/**
 *
 */
public class ComplexSearchCriteria
{
	public static final int DISSERTATIONS = 0;
	public static final int BOOKS = 1;
	public static final int PERIODICALS = 2;
	
	private int type;		// 0 - dissertation; 1 - books; 2 - periodicals
	private String name;
	private String city;
	private String description;
	private String author;
	private String gasnti;
	private String code;
	private String ugk;
	private String year;
	private String publYear;
	private String isbn;
	private String publicher;
	private String udk;
	private String bbk;
  
  	public String getName() {
  		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getAuthor()
  {
    return author;
  }

  public void setAuthor(String author)
  {
    this.author = author;
  }

  public String getGasnti()
  {
    return gasnti;
  }

  public void setGasnti(String gasnti)
  {
    this.gasnti = gasnti;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getUgk()
  {
    return ugk;
  }

  public void setUgk(String ugk)
  {
    this.ugk = ugk;
  }

  public String getYear()
  {
    return year;
  }

  public void setYear(String year)
  {
    this.year = year;
  }

public void setPublYear(String publYear) {
	this.publYear = publYear;
}

public String getPublYear() {
	return publYear;
}

public void setIsbn(String isbn) {
	this.isbn = isbn;
}

public String getIsbn() {
	return isbn;
}

public void setPublicher(String publicher) {
	this.publicher = publicher;
}

public String getPublicher() {
	return publicher;
}

public void setUdk(String udk) {
	this.udk = udk;
}

public String getUdk() {
	return udk;
}

public void setBbk(String bbk) {
	this.bbk = bbk;
}

public String getBbk() {
	return bbk;
}

public void setType(int typeOfSearch) {
	this.type = typeOfSearch;
}

public int getType() {
	return type;
}
}
