package com.klalit.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.klalit.beans.CovidInfo;
import com.klalit.beans.Customer;
import com.klalit.beans.Vaccine;
import com.klalit.enums.ErrorType;
import com.klalit.enums.VaccineManufacturer;
import com.klalit.utils.ApplicationException;
import com.klalit.utils.DateUtils;
import com.klalit.utils.JdbcUtils;

@Repository
public class CustomerDao {

	// 1. creating a new customer in the customers table
	public void createCustomer(Customer customer) throws ApplicationException {

		java.sql.PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			connection = JdbcUtils.getConnection();
			String sql = "insert into customers (id, first_name,last_name,date_of_birth,phone,mobile_phone,street,house_num,city) values (?,?,?,?,?,?,?,?,?)";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setLong(1, customer.getId());
			preparedStatement.setString(2, customer.getFirstName());
			preparedStatement.setString(3, customer.getLastName());
			preparedStatement.setDate(4, customer.getDateOfBirth());
			preparedStatement.setString(5, customer.getPhone());
			preparedStatement.setString(6, customer.getMobilePhone());
			preparedStatement.setString(7, customer.getStreet());
			preparedStatement.setInt(8, customer.getHouseNum());
			preparedStatement.setString(9, customer.getCity());
			preparedStatement.executeUpdate();

			System.out.println("customer created Successfully");
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"error in CustomerDao: failed to create a customer" + DateUtils.getCurrentDateAndTime());
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	// 2. getting a full customer object by it's id
	public Customer getCustomerByCustomerId(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Customer customer = null;

		try {
			connection = JdbcUtils.getConnection();
			String sql = "SELECT * FROM Customers WHERE Id=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerId);
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return null;
			}
			customer = extractCustomerFromResultSet(resultSet);

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"error in CustomerDao: failed to get a customer by ID " + DateUtils.getCurrentDateAndTime());
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

		return customer;
	}

	// 3. updating a customer
	public void updateCustomer(Customer customer) throws ApplicationException {

		java.sql.PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			connection = JdbcUtils.getConnection();
			String sql = "update customers set first_name = ?,last_name=?,date_of_birth=?,phone=?,mobile_phone=?,street=?,house_num=?,city=? where Id=?";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, customer.getFirstName());
			preparedStatement.setString(2, customer.getLastName());
			preparedStatement.setDate(3, customer.getDateOfBirth());
			preparedStatement.setString(4, customer.getPhone());
			preparedStatement.setString(5, customer.getMobilePhone());
			preparedStatement.setString(6, customer.getStreet());
			preparedStatement.setInt(7, customer.getHouseNum());
			preparedStatement.setString(8, customer.getCity());

			preparedStatement.setLong(9, customer.getId());

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"error in CustomerDao: failed to update customers information "
							+ DateUtils.getCurrentDateAndTime());
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	// 4. Deleting a customer
	@Transactional
	public void deleteCustomer(long customerId) throws ApplicationException {

		java.sql.PreparedStatement preparedStatementDelete1 = null;
		java.sql.PreparedStatement preparedStatementDelete2 = null;
		java.sql.PreparedStatement preparedStatementDelete3 = null;

		Connection connection = null;

		try {
			connection = JdbcUtils.getConnection();

			// applying JDBC transaction make sure all of the statements are executed
			// successfully,
			// if either one of the statements within the block fails, abort and rollback
			// everything in the transaction block.

			// deleting all of the vaccines information for the deleted customer
			// (since the customer is a FK in the vaccines table.)
			String sql = "delete from vaccines where customer_id=?";
			preparedStatementDelete1 = connection.prepareStatement(sql);
			preparedStatementDelete1.setLong(1, customerId);
			preparedStatementDelete1.executeUpdate();

			// deleting all of the covid information for the deleted customer
			sql = "delete from covid where customer_id=?";
			preparedStatementDelete2 = connection.prepareStatement(sql);
			preparedStatementDelete2.setLong(1, customerId);
			preparedStatementDelete2.executeUpdate();

			// deleting the customer from the customers table
			sql = "delete from customers where Id=?";
			preparedStatementDelete3 = connection.prepareStatement(sql);
			preparedStatementDelete3.setLong(1, customerId);
			preparedStatementDelete3.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"error in CustomerDao: failed to delete a customer " + DateUtils.getCurrentDateAndTime());
		} finally {
			JdbcUtils.closeResources(null, preparedStatementDelete1);
			JdbcUtils.closeResources(null, preparedStatementDelete2);
			JdbcUtils.closeResources(null, preparedStatementDelete3);
		}
	}

	// 5. vaccinating a customer
	public void vaccinate(long customerId, int vacc_id, VaccineManufacturer vacc, Date date)
			throws ApplicationException {

		java.sql.PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			connection = JdbcUtils.getConnection();
			String sql = "insert into vaccines (customer_id,vacc_id,date,vacc_manufacturer) values (?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerId);
			preparedStatement.setInt(2, vacc_id);
			preparedStatement.setDate(3, date);
			preparedStatement.setString(4, vacc.name());

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"error in CustomerDao: failed to add customer's vaccine information "
							+ DateUtils.getCurrentDateAndTime());
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	// 6. getting a list of all customers
	public List<Customer> getAllCustomers() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Customer customer = null;
		List<Customer> list = new ArrayList<Customer>();

		try {
			connection = JdbcUtils.getConnection();

			String sql = "SELECT * FROM customers ";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				customer = extractCustomerFromResultSet(resultSet);
				list.add(customer);
			}
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"error in CustomerDao: failed to get a list of all customers " + DateUtils.getCurrentDateAndTime());
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

		return list;
	}

	// 7. checking if a customer exist
	public boolean doesCustomerExistById(long customerId) throws ApplicationException {
		java.sql.PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			connection = JdbcUtils.getConnection();

			String sql = "select * from customers where Id=? ";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerId);
			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"error in CustomerDao:doesCustomerExistById Failed. " + DateUtils.getCurrentDateAndTime());
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	// 8. get num of vaccinated customer
	public int countVaccinated() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int num_vaccinated = 0;

		try {
			connection = JdbcUtils.getConnection();

			String sql = "select COUNT(DISTINCT customer_id) from vaccines;";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				return num_vaccinated;
			} else {
				num_vaccinated = resultSet.getInt(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"error in CustomerDao: failed to count vaccinated customers " + DateUtils.getCurrentDateAndTime());
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

		return num_vaccinated;
	}

	// 9.get covid info by customer id
	public CovidInfo getCovidInfoById(long customerId) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
//			Customer customer = null;
		CovidInfo covidInfo = new CovidInfo();
		List<Vaccine> vaccList = new ArrayList<Vaccine>();
		Vaccine vaccine = new Vaccine();
		

		try {
			connection = JdbcUtils.getConnection();

			String sql = "SELECT * from covid where customer_id=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerId);
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				covidInfo.setCustomer_id(customerId);
				covidInfo.setCovid_start(null);
				covidInfo.setCovid_end(null);
			}else {
			covidInfo.setCustomer_id(customerId);
			covidInfo.setCovid_start(resultSet.getDate("start_date"));
			covidInfo.setCovid_end(resultSet.getDate("end_date"));
			}
			sql = "select * from vaccines where customer_id=?";
			preparedStatement2 = connection.prepareStatement(sql);
			preparedStatement2.setLong(1, customerId);
			resultSet2 = preparedStatement2.executeQuery();
			while (resultSet2.next()) {
				vaccine.setCustomerId(customerId);
				vaccine.setVaccine_id(resultSet2.getInt("vacc_id"));
				vaccine.setVaccine_date(resultSet2.getDate("date"));
				vaccine.setVacc_manu(VaccineManufacturer.valueOf(resultSet2.getString("vacc_manufacturer")));
				vaccList.add(vaccine);
				vaccine = new Vaccine();
			}
			covidInfo.setVaccine_info(vaccList);

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"error in CustomerDao: failed to get covid information " + DateUtils.getCurrentDateAndTime());
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
			JdbcUtils.closeResources(connection, preparedStatement2);
		}
		return covidInfo;
	}

	// 10. update about customer's sickness
	public void updateCovidInfo(long customerId, Date start, Date end) throws ApplicationException {
		java.sql.PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			connection = JdbcUtils.getConnection();
			String sql = "insert into covid (customer_id, start_date,end_date) values (?,?,?)";
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setLong(1, customerId);
			preparedStatement.setDate(2, start);
			preparedStatement.setDate(3, end);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR,
					"error in CustomerDao: failed to update covid info for a customer"
							+ DateUtils.getCurrentDateAndTime());
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	//// extracting an object from the resultSet
	private Customer extractCustomerFromResultSet(ResultSet resultSet) throws ApplicationException {
		Customer customer = new Customer();
		try {
			customer.setId(resultSet.getLong("id"));
			customer.setFirstName(resultSet.getString("first_name"));
			customer.setLastName(resultSet.getString("last_name"));
			customer.setDateOfBirth(resultSet.getDate("date_of_birth"));
			customer.setPhone(resultSet.getString("phone"));
			customer.setMobilePhone(resultSet.getString("mobile_phone"));
			customer.setStreet(resultSet.getString("street"));
			customer.setHouseNum(resultSet.getInt("house_num"));
			customer.setCity(resultSet.getString("city"));
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.DATABASE_ERROR,
					"error in CustomerDao: failed to extract an instance from the resultSet");
		}
		return customer;
	}

}
