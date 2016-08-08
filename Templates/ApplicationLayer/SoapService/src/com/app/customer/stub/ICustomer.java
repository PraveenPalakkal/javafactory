package com.app.customer.stub;

import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import java.io.Serializable;

public interface ICustomer extends Serializable {
	
	public String insert(Customer customer) throws HexApplicationException;

	public String deleteAll(List<Customer> customers) throws HexApplicationException;

	public String update(Customer customer) throws HexApplicationException;

	public List<Customer> getAllCustomer() throws HexApplicationException;

	public List<Customer> search(String fieldValue, String columnName) throws HexApplicationException;

	
}