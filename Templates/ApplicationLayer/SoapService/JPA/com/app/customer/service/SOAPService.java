package com.app.customer.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import com.app.customer.dao.CustomerDao;
import com.app.customer.util.BootStrapper;
import com.app.customer.util.GlobalConstants;
import com.app.customer.util.HexHelper;
import com.app.customer.vo.Customer;
import com.app.framework.QueryResultType;
import com.app.framework.exception.HexApplicationException;

@WebService(endpointInterface = "com.app.customer.service.ICustomer")
public class SOAPService implements ICustomer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6115098335114488177L;
	CustomerDao customerDao;
	public SOAPService(){
		customerDao = (CustomerDao) BootStrapper.getService().getBean("CustomerDao");
	}
	
	@Override
	public ArrayList<Customer> getAllCustomer() throws HexApplicationException {
		customerDao.createEntityManager();
		List<Customer> result = customerDao.getAllObjects(GlobalConstants.entityName);
		customerDao.closeEntityManager();
		return (ArrayList<Customer>) result;
	}

	@Override
	public String insert(Customer customer) throws HexApplicationException {
		customerDao.createEntityManager();
		String result = customerDao.insert(customer);
		customerDao.closeEntityManager();
		return result;
	}

	@Override
	public String deleteAll(List<Customer> customers) throws HexApplicationException {
		customerDao.createEntityManager();
		String result = customerDao.deleteAll(customers);
		customerDao.closeEntityManager();
		return result;
	}

	@Override
	public String update(Customer customer) throws HexApplicationException {
		customerDao.createEntityManager();
		String result = customerDao.update(customer);
		customerDao.closeEntityManager();
		return result;
	}

	@Override
	public ArrayList<Customer> search(String fieldValue, String columnName) throws HexApplicationException {
		System.out.println("Entering into service implementation : " + fieldValue + "***" +columnName );
		customerDao.createEntityManager();
		List<Customer> result = customerDao.executeQuery(HexHelper.createCustomerQuerySearchCriteria(
				fieldValue, columnName), QueryResultType.LIST);;
		customerDao.closeEntityManager();		
		return (ArrayList<Customer>) result;
	}

}
