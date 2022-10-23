package com.klalit.beans;

import java.sql.Date;

import com.klalit.enums.VaccineManufacturer;

public class Vaccine {

	private long customerId;
	private int vaccine_id;
	private Date vaccine_date;
	private VaccineManufacturer vacc_manu;

	public Vaccine() {
		super();
	}

	public Vaccine(long customerId, int vaccine_id, Date vaccine_date, VaccineManufacturer vacc_manu) {
		super();
		this.customerId = customerId;
		this.vaccine_id = vaccine_id;
		this.vaccine_date = vaccine_date;
		this.vacc_manu = vacc_manu;
	}

	public Vaccine(long customerId, Date vaccine_date, VaccineManufacturer vacc_manu) {
		super();
		this.customerId = customerId;
		this.vaccine_date = vaccine_date;
		this.vacc_manu = vacc_manu;
	} 
	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public int getVaccine_id() {
		return vaccine_id;
	}

	public void setVaccine_id(int vaccine_id) {
		this.vaccine_id = vaccine_id;
	}

	public Date getVaccine_date() {
		return vaccine_date;
	}

	public void setVaccine_date(Date vaccine_date) {
		this.vaccine_date = vaccine_date;
	}

	public VaccineManufacturer getVacc_manu() {
		return vacc_manu;
	}

	public void setVacc_manu(VaccineManufacturer vacc_manu) {
		this.vacc_manu = vacc_manu;
	}

	@Override
	public String toString() {
		return "Vaccine [customerId=" + customerId + ", vaccine_id=" + vaccine_id + ", vaccine_date=" + vaccine_date
				+ ", vacc_manu=" + vacc_manu + "]";
	}

}
