package com.app.customer.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import com.app.customer.util.JndiLookup;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

@WebService(endpointInterface = "com.app.customer.service.ICustomer")
public class SOAPService implements ICustomer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6115098335114488177L;
	ICustomer customerService;
	public SOAPService(){
		customerService = getCustomerService();
	}
	
	@Override
	public ArrayList<Customer> getAllCustomer() throws HexApplicationException {
		System.out.println("Reached getAllCustomer...");
		return (ArrayList<Customer>) customerService.getAllCustomer();
	}

	@Override
	public String insert(Customer customer) throws HexApplicationException {
		customerService.insert(customer);
		return "Customer Added Successfully";
	}

	@Override
	public String deleteAll(List<Customer> customers) throws HexApplicationException {
		customerService.deleteAll(customers);
		return "Customers deleted Successfully";
	}

	@Override
	public String update(Customer customer) throws HexApplicationException {
		customerService.update(customer);
		return "Customer Updated Successfully";
	}

	@Override
	public ArrayList<Customer> search(String fieldValue, String columnName) throws HexApplicationException {
		
		return (ArrayList<Customer>) customerService.search(fieldValue, columnName);
	}
	
	private ICustomer getCustomerService(){
		return (ICustomer) JndiLookup.doLookUp("Customer");
	}

}
