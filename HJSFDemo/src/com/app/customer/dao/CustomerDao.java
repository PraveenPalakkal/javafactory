package com.app.customer.dao;

import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import java.io.Serializable;
import java.util.List;

public interface CustomerDao extends Serializable {

	public Customer select(Customer object) throws HexApplicationException;

	public void insert(Customer object) throws HexApplicationException;

	public void update(Customer object) throws HexApplicationException;

	public void delete(Customer object) throws HexApplicationException;

	public void deleteAll(java.util.Collection entries) throws HexApplicationException;

	public java.util.List getAllCustomer() throws HexApplicationException;

	public java.util.List getAllCustomer( int startRecord, int endRecord ) throws HexApplicationException;

	public int getCustomerCount() throws HexApplicationException;

	public List search(String fieldValue, String columnName)throws HexApplicationException;

	
}
