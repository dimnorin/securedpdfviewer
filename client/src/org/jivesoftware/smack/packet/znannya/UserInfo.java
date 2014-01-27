package org.jivesoftware.smack.packet.znannya;

import org.jivesoftware.smack.packet.IQ;

public class UserInfo extends IQ{
	public static final String ELEMENT_NAME = "query";
	public static final String NAMESPACE = "urn:xmpp:userinfo";
	
	private String password;
	private String newPassword;
	private String name;
	private String organization;
	private String position;
	private String phone;
	private String email;
	private String extraInfo;
	
	public UserInfo() {}
	
	public UserInfo(String password, String newPassword, String name,
			String organization, String position, String phone, String email,
			String extraInfo) {
		super();
		this.password = password;
		this.newPassword = newPassword;
		this.name = name;
		this.organization = organization;
		this.position = position;
		this.phone = phone;
		this.email = email;
		this.extraInfo = extraInfo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\""+NAMESPACE+"\">");
        buf.append("<").append(ELEMENT_NAME).append(">");
        if(password != null)
        	buf.append("<password>").append(password).append("</password>");
        if(newPassword != null)
        	buf.append("<newPassword>").append(newPassword).append("</newPassword>");
        buf.append("<name>").append(name).append("</name>");
        buf.append("<organization>").append(organization).append("</organization>");
        buf.append("<position>").append(position).append("</position>");
        buf.append("<phone>").append(phone).append("</phone>");
        buf.append("<email>").append(email).append("</email>");
        buf.append("<extraInfo>").append(extraInfo).append("</extraInfo>");
        buf.append("</").append(ELEMENT_NAME).append(">");
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
	}

	@Override
	public String toString() {
		return "UserInfo [email=" + email + ", extraInfo=" + extraInfo
				+ ", name=" + name + ", newPassword=" + newPassword
				+ ", organization=" + organization + ", password=" + password
				+ ", phone=" + phone + ", position=" + position + "]";
	}
}
