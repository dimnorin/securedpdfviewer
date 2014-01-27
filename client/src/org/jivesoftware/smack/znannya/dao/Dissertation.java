package org.jivesoftware.smack.znannya.dao;

public class Dissertation extends Entry{
	protected String kind;
	protected String code;
	protected String organization;
	protected int pages;
	protected String DACNTI_code;
	protected String UDK_idx;
	
	public Dissertation(){}
	
	public Dissertation(long dissID, String name, String author, String kind,
			String code, String organization, String city, String year, int pages,
			String dACNTICode, String uDKIdx, String description) {
		super(dissID, name, author, city, year, description);
		this.kind = kind;
		this.code = code;
		this.organization = organization;
		this.pages = pages;
		DACNTI_code = dACNTICode;
		UDK_idx = uDKIdx;
	}

    public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getDACNTI_code() {
		return DACNTI_code;
	}

	public void setDACNTI_code(String dACNTICode) {
		DACNTI_code = dACNTICode;
	}

	public String getUDK_idx() {
		return UDK_idx;
	}

	public void setUDK_idx(String uDKIdx) {
		UDK_idx = uDKIdx;
	}

	@Override
    public String toString()
    {
      return ID + ", " + name + ", " + author + ", " + kind + ", " + code + ", " + organization + ", " +
             city + ", " + year + ", " + pages + ", " + DACNTI_code + ", " + UDK_idx + ", " + description;
    }
}
