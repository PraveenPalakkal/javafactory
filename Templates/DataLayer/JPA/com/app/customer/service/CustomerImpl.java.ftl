package com.app.customer.service;

import com.app.customer.dao.CustomerDao;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;
import com.app.customer.util.BootStrapper;
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

	public String insert(Customer object) throws HexApplicationException{
	
		System.out.println("inside insert in Service ");
		String result = customerDao.insert(object);
		closeEntityManager();
		return result;
		
	}

	public String deleteAll(List<Customer> entries) throws HexApplicationException {
	
		System.out.println("inside deleteAll in Service ");	
		String result = customerDao.deleteAll(entries);
		closeEntityManager();
		return result;
	
	}

	public String update(Customer object) throws HexApplicationException {
	
		System.out.println("inside update in Service ");
		String result = customerDao.update(object);
		closeEntityManager();
		return result;
	
	}

	public List getAllCustomer() throws HexApplicationException {
	
		System.out.println("inside getAllCustomer in Service ");
		List<Customer> customerList = customerDao.getAllObjects(GlobalConstants.entityName);
		closeEntityManager();		
		return customerList;
	
	}


	public List search(String fieldValue, String columnName) throws HexApplicationException {

		System.out.println("Entering into service implementation : " + fieldValue );
		List<Customer> customerList = customerDao.executeQuery(HexHelper.createCustomerQuerySearchCriteria(
						fieldValue, columnName), QueryResultType.LIST);
		closeEntityManager();
		return customerList;
	
	}
	
	public void closeEntityManager(){
		customerDao.closeEntityManager();
	}


}
