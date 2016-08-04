package com.app.customer.service;

import com.app.customer.dao.CustomerDao;
import com.app.customer.vo.Customer;
import com.app.framework.QueryResultType;
import com.app.framework.exception.HexApplicationException;
import com.app.customer.util.BootStrapper;
import com.app.customer.util.GlobalConstants;
import com.app.customer.util.HexHelper;

import java.util.List;

public class CustomerImpl implements ICustomer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -834079942480093517L;
	private CustomerDao customerDao;

	public CustomerImpl() {
		this.customerDao = (CustomerDao) BootStrapper.getService().getBean("CustomerDao");
	}

	public String insert(Customer object) throws HexApplicationException {
	
		System.out.println("inside insert in Service ");	
		customerDao.createEntityManager();
		String result = customerDao.insert(object);
		customerDao.closeEntityManager();
		return result;
		
	}


	public String deleteAll(List<Customer> customers) throws HexApplicationException {
	
		System.out.println("inside deleteAll in Service ");	
		customerDao.createEntityManager();
		String result = customerDao.deleteAll(customers);
		customerDao.closeEntityManager();
		return result;
	
	}

	public String update(Customer customer) throws HexApplicationException {
	
		System.out.println("inside update in Service ");
		customerDao.createEntityManager();
		String result = customerDao.update(customer);
		customerDao.closeEntityManager();
		return result;
	
	}

	public List<Customer> getAllCustomer() throws HexApplicationException {
	
		System.out.println("inside getAllCustomer in Service ");
		customerDao.createEntityManager();
		List<Customer> result = customerDao.getAllObjects(GlobalConstants.entityName);
		customerDao.closeEntityManager();
		return result;
	
	}

	public List<Customer> search(String searchValue, String searchColumn) throws HexApplicationException {

		System.out.println("Entering into service implementation : " + searchValue + "***" +searchColumn );
		customerDao.createEntityManager();
		List<Customer> result = customerDao.executeQuery(HexHelper.createCustomerQuerySearchCriteria(
				searchValue, searchColumn), QueryResultType.LIST);;
		customerDao.closeEntityManager();		
		return result;
	
	}


}
