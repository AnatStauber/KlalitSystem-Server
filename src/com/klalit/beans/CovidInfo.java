package com.klalit.beans;

import java.sql.Date;
import java.util.List;

public class CovidInfo {

	private long customer_id;
	private Date covid_start;
	private Date covid_end;
	private List<Vaccine> vaccine_info;

	public CovidInfo() {
		super();
	}

	public CovidInfo(long customer_id, Date covid_start, Date covid_end, List<Vaccine> vacc_info) {
		super();
		this.customer_id = customer_id;
		this.covid_start = covid_start;
		this.covid_end = covid_end;
		this.vaccine_info = vacc_info;
	}

	public long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}

	public Date getCovid_start() {
		return covid_start;
	}

	public void setCovid_start(Date covid_start) {
		this.covid_start = covid_start;
	}

	public Date getCovid_end() {
		return covid_end;
	}

	public void setCovid_end(Date covid_end) {
		this.covid_end = covid_end;
	}

	public List<Vaccine> getVaccine_info() {
		return vaccine_info;
	}

	public void setVaccine_info(List<Vaccine> vaccine_info) {
		this.vaccine_info = vaccine_info;
	}

	@Override
	public String toString() {
		return "CovidInfo [customer_id=" + customer_id + ", covid_start=" + covid_start + ", covid_end=" + covid_end
				+ ", vaccine_info=" + vaccine_info + "]";
	}

}
