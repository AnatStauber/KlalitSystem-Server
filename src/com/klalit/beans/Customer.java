package com.klalit.beans;

import java.sql.Date;

public class Customer {

	private long id;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String phone;
	private String mobilePhone;
	private String street;
	private int houseNum;
	private String city;

	public Customer() {
		super();
	}

	public Customer(long id, String firstName, String lastName, Date dateOfBirth, String phone, String mobilePhone,
			String street, int houseNum, String city) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.phone = phone;
		this.mobilePhone = mobilePhone;
		this.street = street;
		this.houseNum = houseNum;
		this.city = city;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(int houseNum) {
		this.houseNum = houseNum;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth="
				+ dateOfBirth + ", phone=" + phone + ", mobilePhone=" + mobilePhone + ", street=" + street
				+ ", houseNum=" + houseNum + ", city=" + city + "]";
	}

}
