package com.app.customer.ejb;

import com.app.customer.dao.CustomerDao;
import com.app.customer.dao.CustomerDaoImpl;
import com.app.customer.service.ICustomer;
import com.app.customer.util.GlobalConstants;
import com.app.customer.util.HexHelper;
import com.app.customer.vo.Customer;
import com.app.framework.QueryResultType;
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
		closeEntityManager();
		return result;
		
	}

	public String deleteAll(List<Customer> entries) throws HexApplicationException {
	
		System.out.println("inside deleteAll in Service ");	
		customerDao = new CustomerDaoImpl();
		String result = customerDao.deleteAll(entries);
		closeEntityManager();
		return result;
	
	}

	public String update(Customer object) throws HexApplicationException {
	
		System.out.println("inside update in Service ");
		customerDao = new CustomerDaoImpl();
		String result = customerDao.update(object);
		closeEntityManager();
		return result;
	
	}

	public List getAllCustomer() throws HexApplicationException {
	
		System.out.println("inside getAllCustomer in Service ");
		customerDao = new CustomerDaoImpl();
		List<Customer> customerList = customerDao.getAllObjects(GlobalConstants.entityName);
		closeEntityManager();		
		return customerList;
	
	}


	public List search(String fieldValue, String columnName) throws HexApplicationException {

		System.out.println("Entering into service implementation : " + fieldValue );
		customerDao = new CustomerDaoImpl();
		List<Customer> customerList = customerDao.executeQuery(HexHelper.createCustomerQuerySearchCriteria(
						fieldValue, columnName), QueryResultType.LIST);
		closeEntityManager();
		return customerList;
	
	}
	
	public void closeEntityManager(){
		customerDao.closeEntityManager();
	}

		
}
