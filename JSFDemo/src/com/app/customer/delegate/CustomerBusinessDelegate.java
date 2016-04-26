package com.app.customer.delegate;

import com.app.customer.service.ICustomer;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import java.io.Serializable;
import java.util.List;

public class CustomerBusinessDelegate implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3440828310386661784L;
	private ICustomer customer;

	

	
	public ICustomer getCustomer() {
		return customer;
	}


	public void setCustomer(ICustomer customer) {
		this.customer = customer;
	}


	public void insert(Customer object) throws HexApplicationException {
		customer.insert(object);	
	}

	public void delete(Customer object) throws HexApplicationException {
		customer.delete(object);
	}

	public void deleteAll(java.util.Collection entries) throws HexApplicationException {
		customer.deleteAll(entries);
	}

	public void update(Customer object) throws HexApplicationException {
		customer.update(object);	
	}

	public Object select(Customer object) throws HexApplicationException {
		return customer.select(object);	
	}

	public List getAllCustomer() throws HexApplicationException {
		return customer.getAllCustomer();	
	}

	public Object getAllCustomer(int startRecord, int endRecord) throws HexApplicationException {
		return customer.getAllCustomer(startRecord, endRecord);	
	}

	public int getCustomerCount() throws HexApplicationException {
		return customer.getCustomerCount();	
	}
	
	public List search(String fieldValue, String columnName) throws HexApplicationException {
		return customer.search(fieldValue, columnName);	
	}



}
