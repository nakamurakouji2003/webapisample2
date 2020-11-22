package jp.ac.hal.userauth.model;

public class UserInfo {

	long userId = 0;
	String password = null;
	String userName = null;
	String firstName = null;
	String lastName = null;
	String phoneNo = null;
	String postalCd = null;
	String pref = null;
	String address1 = null;
	String address2 = null;
	String email = null;
	String jsessionId = null;
	String useSystemId = null;
	String secret = null;
	String availabilityFlag = null;

	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getPostalCd() {
		return postalCd;
	}
	public void setPostalCd(String postalCd) {
		this.postalCd = postalCd;
	}
	public String getPref() {
		return pref;
	}
	public void setPref(String pref) {
		this.pref = pref;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJsessionId() {
		return jsessionId;
	}
	public void setJsessionId(String jsessionId) {
		this.jsessionId = jsessionId;
	}
	public String getUseSystemId() {
		return useSystemId;
	}
	public void setUseSystemId(String useSystemId) {
		this.useSystemId = useSystemId;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getAvailabilityFlag() {
		return availabilityFlag;
	}
	public void setAvailabilityFlag(String availabilityFlag) {
		this.availabilityFlag = availabilityFlag;
	}

}
