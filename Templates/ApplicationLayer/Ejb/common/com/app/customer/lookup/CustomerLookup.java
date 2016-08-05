package com.app.customer.lookup;


import com.app.customer.service.ICustomer;
import com.app.customer.util.JndiLookup;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import java.io.Serializable;
import java.util.List;

public class CustomerLookup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3440828310386661784L;
	private ICustomer customerService;

	public CustomerLookup(){
		System.out.println("Customer lookup Constructor call ");
		customerService = getCustomerService();
	}

	public String insert(Customer object) throws HexApplicationException {
		return customerService.insert(object);
	}

	public String deleteAll(List<Customer> customers) throws HexApplicationException {
		return customerService.deleteAll(customers);
	}

	public String update(Customer customer) throws HexApplicationException {
		return customerService.update(customer);
	}

	public List<Customer> getAllCustomer() throws HexApplicationException {
		System.out.println("the getAllCustomer ");
		return customerService.getAllCustomer();
	}

	public List<Customer> search(String searchValue, String searchColumn) throws HexApplicationException {		
		System.out.println("the do lookup search "+searchValue+ " column :"+searchColumn);
		return customerService.search(searchValue, searchColumn);
	}
	
	private ICustomer getCustomerService(){
		return (ICustomer) JndiLookup.doLookUp("Customer");
	}
}
