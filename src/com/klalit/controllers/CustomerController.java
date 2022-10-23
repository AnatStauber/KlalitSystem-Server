package com.klalit.controllers;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.klalit.utils.DateUtils;
import com.klalit.beans.CovidInfo;
import com.klalit.beans.Customer;
import com.klalit.beans.Vaccine;
import com.klalit.dao.CustomerDao;
import com.klalit.enums.ErrorType;
import com.klalit.enums.VaccineManufacturer;
import com.klalit.utils.ApplicationException;

@Controller
public class CustomerController {

	@Autowired
	private CustomerDao customerDao;
	
	public CustomerController() {
		this.customerDao = new CustomerDao();
	}

	// 1. calling the create customer + validation
	public void createCustomer(Customer customer) throws ApplicationException {

		long id = customer.getId();
		// checking that no other customer exists with the same ID
		if (this.customerDao.doesCustomerExistById(id)) {
			throw new ApplicationException(ErrorType.INVALID_PARAMETER, "customerId already exist.");
		}
		
		//checking date of birth 
		validateDateOfBirth(customer.getDateOfBirth());
		
		//checking phone numbers
		validatePhoneNumber(customer.getPhone());
		validateMobilePhoneNumber(customer.getMobilePhone());
		
		//if all are ok:
		this.customerDao.createCustomer(customer);
	}

	// 2. calling the get customer +validating
	public Customer getCustomer(long customerId) throws ApplicationException {

		validateCustomerId(customerId);
		return this.customerDao.getCustomerByCustomerId(customerId);
	}

	// 3. calling the update customer + validation
	public void updateCustomer(Customer customer) throws ApplicationException {

		validateCustomerId(customer.getId());
		validateDateOfBirth(customer.getDateOfBirth());
		validatePhoneNumber(customer.getPhone());
		validateMobilePhoneNumber(customer.getMobilePhone());
		this.customerDao.updateCustomer(customer);
	}

	// 4.calling delete customer+validating
	public void deleteCustomer(long customerId) throws ApplicationException {

		validateCustomerId(customerId);

		// if valid - (no exceptions are thrown) - activate the delete method
		this.customerDao.deleteCustomer(customerId);
	}

	// 5. calling the get covid info + validationg
	public CovidInfo getCovidInfoById(long customerId) throws ApplicationException {

		validateCustomerId(customerId);

		return this.customerDao.getCovidInfoById(customerId);

	}

	// 6. calling the vaccinate function + validating
	public void vaccinate(long customerId, VaccineManufacturer vacc, Date date) throws ApplicationException {

		// checking if id exists
		validateCustomerId(customerId);

		// checking if date is later than first vaccination - 01/12/2020
		Date vacc_start = Date.valueOf("2020-12-01");
		if (!DateUtils.isDate1AfterDate2(date, vacc_start)) {
			throw new ApplicationException(ErrorType.INVALID_PARAMETER,
					"vaccine date should be after 2020-12-01" + DateUtils.getCurrentDateAndTime());
		}

		// checking if date is later then today
		if (DateUtils.isDateAfterCurrent(date)) {
			throw new ApplicationException(ErrorType.INVALID_PARAMETER,
					"vaccine date cn't be later than today" + DateUtils.getCurrentDateAndTime());
		}

		// checking the number of vaccines the customer received
		int vacc_id = 0;
		CovidInfo info = getCovidInfoById(customerId);
		List<Vaccine> vaccines = info.getVaccine_info();
		if (vaccines.isEmpty()) {
			vacc_id = 1;
		} else if (vaccines.size()==4) {
			throw new ApplicationException(ErrorType.ACTION_CANNOT_BE_COMPLETED,
					"customer already received the max vaccines allowed." + DateUtils.getCurrentDateAndTime());
		} else {
			// creating vacc_id
			vacc_id = vaccines.size() + 1;
		}

//		// checking if the date entered is earlier than last vacc
//		if (vacc_id > 1) {
//			Date last_vacc = vaccines.get(vacc_id - 1).getVaccine_date();
//			if (DateUtils.isDate1AfterDate2(last_vacc, date)) {
//				throw new ApplicationException(ErrorType.ACTION_CANNOT_BE_COMPLETED,
//						"vaccination date must be later than the last vaccination."
//								+ DateUtils.getCurrentDateAndTime());
//			}
		

		this.customerDao.vaccinate(customerId, vacc_id, vacc, date);
	}

	// 7. calling the get all customers
	public List<Customer> getAllCustomers() throws ApplicationException {

		// checking if the list is empty
		if (this.customerDao.getAllCustomers() == null) {
			throw new ApplicationException(ErrorType.EMPTY_LIST,
					"getAllCustomers failed: The list of customers is empty " + DateUtils.getCurrentDateAndTime());
		}
		return this.customerDao.getAllCustomers();
	}

	// 8. calling the count of vaccinated
	public int countVaccinated() throws ApplicationException {
		return this.customerDao.countVaccinated();
	}

	// 9.calling the update covid info
	public void updateCovidInfo(long customerId, Date start, Date end) throws ApplicationException {

		validateCustomerId(customerId);
		validateSicknessDates(start, end);

		this.customerDao.updateCovidInfo(customerId, start, end);

	}

//////////////////////////////////////// Utility functions ////////////////////////////////////////

	// 1. checking that ID exists in DB
	private void validateCustomerId(long customerId) throws ApplicationException {

		if (!this.customerDao.doesCustomerExistById(customerId)) {
			throw new ApplicationException(ErrorType.INVALID_PARAMETER, "customerId doesnt exist.");
		}

	}

	// 2. parse string dates into sql format
//	private Date validateDatesFormat (String date) throws ApplicationException{
//		// validating that the input dates fits the format "yyyy-mm-dd"
//		//while parsing the date into an sql date type
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//	        java.util.Date parsed ;
//	        try {
//	        	parsed= format.parse(date);
//	        }catch (Exception e) {
//				throw new ApplicationException(ErrorType.INVALID_PARAMETER,
//						"validating the date failed. the given Date/s is not in the right format: 'yyyy-mm-dd'"
//								+ DateUtils.getCurrentDateAndTime());
//			}
//	        java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
//	        return sqlDate;
//		}

	// 3. validate date of birth
	private void validateDateOfBirth(Date dateOfBirth) throws ApplicationException {
		String baseDate = "1900-01-01";
		Date sqlBaseDate = Date.valueOf(baseDate);
		if (!dateOfBirth.after(sqlBaseDate)) {
			throw new ApplicationException(ErrorType.INVALID_PARAMETER,
					"birth date should be after 1900-01-01" + DateUtils.getCurrentDateAndTime());
		} else if (DateUtils.isDateAfterCurrent(dateOfBirth)) {
			throw new ApplicationException(ErrorType.INVALID_PARAMETER,
					"birth date can't be after currend date" + DateUtils.getCurrentDateAndTime());
		}
	}

	// 4. validate sickness dates
	private void validateSicknessDates(Date start, Date end) throws ApplicationException {
		String baseDate = "2020-01-01";
		Date sqlBaseDate = Date.valueOf(baseDate);
		if (sqlBaseDate.after(start)) {
			throw new ApplicationException(ErrorType.INVALID_PARAMETER,
					"start date can't be prior to 2020-01-01 " + DateUtils.getCurrentDateAndTime());
		}
		if (!end.after(start)) {
			throw new ApplicationException(ErrorType.INVALID_PARAMETER,
					"start date is later than end date." + DateUtils.getCurrentDateAndTime());
		}
	}
	
	//5. validate phone number
	private void validatePhoneNumber(String phone) throws ApplicationException {
		if (phone!=null && phone.length()!=9) {
			throw new ApplicationException(ErrorType.INVALID_PARAMETER,
					"phone number doesnt match the standart : 03-0000000 " + DateUtils.getCurrentDateAndTime());
		}
		}
	
	//6. validate mobile phone number
	private void validateMobilePhoneNumber(String mobilePhone) throws ApplicationException {
		if (mobilePhone.length()!=10) {
			throw new ApplicationException(ErrorType.INVALID_PARAMETER,
					"phone number doesnt match the standart : 050-0000000 " + DateUtils.getCurrentDateAndTime());
		}
		}
	

}