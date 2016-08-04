package com.app.customer.ejb;

import com.app.customer.dao.CustomerDao;
import com.app.customer.dao.CustomerDaoImpl;
import com.app.customer.service.ICustomer;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote
public class CustomerBean implements ICustomer,Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6849045628004238769L;
	private CustomerDao customerDao;
		
	public CustomerBean() {		
		
	}
	
	public CustomerDao getCustomerDao () {	
		return customerDao;
	}

	public String insert(Customer object) throws HexApplicationException{
	
		System.out.println("inside insert in Service ");
		customerDao = new CustomerDaoImpl();
		String result = customerDao.insert(object);
		return result;
		
	}

	public String deleteAll(List<Customer> entries) throws HexApplicationException {
	
		System.out.println("inside deleteAll in Service ");	
		customerDao = new CustomerDaoImpl();
		String result = customerDao.deleteAll(entries);
		return result;
	
	}

	public String update(Customer object) throws HexApplicationException {
	
		System.out.println("inside update in Service ");
		customerDao = new CustomerDaoImpl();
		String result = customerDao.update(object);
		return result;
	
	}

	public List getAllCustomer() throws HexApplicationException {
	
		System.out.println("inside getAllCustomer in Service ");
		customerDao = new CustomerDaoImpl();
		List<Customer> customerList = customerDao.getAllCustomer();
		return customerList;
	
	}

	public List search(String searchValue, String searchColumn) throws HexApplicationException {

		System.out.println("Entering into service implementation : " + searchValue );
		customerDao = new CustomerDaoImpl();
		List<Customer> customerList = customerDao.search(searchValue, searchColumn);
		return customerList;
	
	}	
	
}
