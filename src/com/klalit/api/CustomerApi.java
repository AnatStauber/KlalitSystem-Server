package com.klalit.api;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klalit.beans.CovidInfo;
import com.klalit.beans.Customer;
import com.klalit.beans.Vaccine;
import com.klalit.controllers.CustomerController;
import com.klalit.enums.VaccineManufacturer;
import com.klalit.utils.ApplicationException;


@RestController
//@CrossOrigin
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(exposedHeaders="internalMessage,errorMessage", origins="*", allowedHeaders = "*" )
@RequestMapping(value = "/customers")
public class CustomerApi {

	@Autowired
	CustomerController customerController ;
	
	@PutMapping ("/createNewCustomer")
	 public void createCustomer (@RequestBody Customer customer) throws ApplicationException {
		this.customerController.createCustomer(customer);	
		long id = customer.getId();
		System.out.println("customer #"+ id+ " created");
		//http://localhost:8080/KlalitSystem/customers/createNewCustomer
	 }
	
	@GetMapping ("/{customerId}")
	 public Customer getCustomer(@PathVariable("customerId") long customerId) throws ApplicationException, ClassNotFoundException{
		 return (customerController.getCustomer(customerId));
		 
	 }
	
	@PutMapping ("/update")
	 public void updateCustomer (@RequestBody Customer customer)  throws ApplicationException {
		 this.customerController.updateCustomer(customer);
		 long id = customer.getId();
		 System.out.println("the customer with id : " + id + " has updated");
		 //http://localhost:8080/
	 } 
	
	@DeleteMapping ("/delete/{customerId}")
	 public void deleteCustomer (@PathVariable("customerId") long customerId) throws ApplicationException {
		 this.customerController.deleteCustomer(customerId);		 
		 //http://localhost:8080/
	 }
	
	@GetMapping ("/getAll")
	 public List<Customer> getAllCustomers () throws ApplicationException {
		 return this.customerController.getAllCustomers();
		 //http://localhost:8080/
	 }
	 
	@GetMapping ("/getCovidInfo/{customerId}")
	public CovidInfo getCovidInfoById(@PathVariable("customerId") long customerId) throws ApplicationException {
	 return this.customerController.getCovidInfoById(customerId);
	}
	
	@GetMapping ("/vaccinate/{customerId}/{vaccine_date}/{vacc_manu}")
	public void vaccinate (@PathVariable("customerId") long customerId,@PathVariable("vaccine_date") Date date, @PathVariable("vacc_manu") VaccineManufacturer vacc_manu ) throws ApplicationException {
		this.customerController.vaccinate(customerId, vacc_manu, date);
//		this.customerController.vaccinate(vaccine.getCustomerId(), vaccine.getVacc_manu(), vaccine.getVaccine_date());
	}
	
	@GetMapping ("/countVaccinated")
	public int countVaccinated() throws ApplicationException {
		return this.customerController.countVaccinated();
	}
	
	
	@GetMapping ("updateCovidInfo")
	public void updateCovidInfo(@PathVariable("customerId") long customerId, @PathVariable("startDate") Date start, @PathVariable("endDate") Date end) throws ApplicationException {
//		Date startDate = Date.valueOf(start);
//		Date endDate = Date.valueOf(end);
		this.customerController.updateCovidInfo(customerId, start, end);;
	}
}