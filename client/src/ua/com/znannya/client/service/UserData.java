package ua.com.znannya.client.service;

/**
 * User's registration data.
 */
public class UserData
{
  private String login;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private String organization;
  private String jobTitle;
  private String phone;
  private String extraInfo;

  	@Override
	public String toString() {
		return "UserData [email=" + email + ", extraInfo=" + extraInfo
				+ ", firstName=" + firstName + ", jobTitle=" + jobTitle
				+ ", lastName=" + lastName + ", login=" + login + ", organization="
				+ organization + ", password=" + password + ", phone=" + phone
				+ "]";
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
}
