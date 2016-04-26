/*
 * EmpBean.java
 *
 * Created on May 22, 2007, 3:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.app.customer.mbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

import java.io.IOException;
import java.io.Serializable;

import com.app.customer.delegate.CustomerBusinessDelegate;
import com.app.customer.util.BootStrapper;
import com.app.customer.vo.Customer;

import java.util.ArrayList;
import java.util.List;

import java.util.Date;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

import java.lang.reflect.Field;

@ManagedBean(name = "CustomerBean")
@SessionScoped
public class CustomerBean implements Serializable {
	/**
	 * 
	 */
	static Logger log = LogFactory.getLogger(CustomerBean.class);
	private static final long serialVersionUID = 1L;
	private List<Customer> customerList;
	private Customer customer;

	private List<String> columnNameList;

	private String fieldValue;
	private String columnValue;

	private CustomerBusinessDelegate delegate;

	public CustomerBean() {
		log.debug("Inside CustomerBean Constructor..");
		columnNameList = new ArrayList<>();
		columnNameList.add("-- Select the Field--");
		Field[] fieldList = Customer.class.getDeclaredFields();
		for (Field field : fieldList)
			columnNameList.add(field.getName());
		customer = new Customer();
		delegate = (CustomerBusinessDelegate) BootStrapper.getService().getBean("CustomerDelegate");
		initialiseList();
		log.debug("End of CustomerBean Constructor..");
	}

	/**
	 * <b> initialiseList </b> method loads list of customers from database.
	 * 
	 * @exception Exception
	 */
	public void initialiseList() {
		try {
			log.debug("Inside Method initialiseList..");
			customerList = (List<Customer>) delegate.getAllCustomer();
			fieldValue = null;
			columnValue = "-- Select the Field--";
			log.debug("End of Method initialiseList..");
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage("Error",
					new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), ex.getMessage()));
		}

	}

	/**
	 * <b> createNewRecord </b> method create New Customer Information.
	 * 
	 * @throws IOException
	 * 
	 * @error Error
	 * @exception UnknownHostException
	 * @exception HexApplicationException
	 * @exception Exception
	 */
	public void createNewRecord() throws IOException {

		try {
			try {
				customer.setCreatedby(InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			customer.setCreateddate(new Date());
			delegate.insert(customer);
		}

		catch (NumberFormatException exp3) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"primary key violation Duplicate entry", "Duplicate entry"));
			FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerAdd.xhtml");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"primary key violation Duplicate entry", "Duplicate entry"));
			FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerAdd.xhtml");
		} catch (Error e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"primary key violation Duplicate entry", "Duplicate entry"));
			FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerAdd.xhtml");
		}

		initialiseList();
		log.debug("End of Method createNewRecord..");
		FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerList.xhtml");
	}

	public void clear() {
		fieldValue = "";
		setColumnValue("");
		initialiseList();
	}

	public void select(Customer customer) throws IOException {
		log.debug("Inside Method select..");
		log.debug("Customer Id : " + customer.getCustomerid());
		this.setCustomer(customer);

		FacesContext.getCurrentInstance().renderResponse();
		log.debug("End of  Method select..");
		FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerEdit.xhtml");
	}

	/**
	 * <b> updateRecord </b> method update existing Customer Information.
	 * 
	 * @throws IOException
	 * 
	 * @exception UpdateException
	 * @exception ValidationException
	 * @exception SystemException
	 */
	public void updateRecord() throws IOException {
		log.debug("Inside Method updateRecord..");
		try {
			try {
				customer.setModifiedby(InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			customer.setModifieddate(new Date());

			delegate.update(customer);
		} catch (Exception ex) {
			ex.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("Error",
					new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), ex.getMessage()));
			FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerEdit.xhtml");
		}
		initialiseList();
		log.debug("End of Method updateRecord..");
		FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerList.xhtml");
	}

	/**
	 * <b> delete </b> method delete existing Customer Information.
	 * 
	 * @throws IOException
	 * 
	 * @exception Exception
	 */
	public void delete() throws IOException {
		log.debug("Inside Method delete..");
		List<Customer> deleteList = new ArrayList<>();
		for (int index = 0; index < customerList.size(); index++) {
			Customer customer = (Customer) customerList.get(index);
			if (customer.isToBeDeleted()) {
				deleteList.add(customer);
			}
		}
		log.debug("delete()- deleteList size.." + deleteList.size());

		try {
			if (deleteList.size() > 0)
				delegate.deleteAll(deleteList);
		} catch (Exception ex) {
			ex.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("Error",
					new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), ex.getMessage()));
		}
		initialiseList();
		log.debug("End of Method delete..");
		FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerList.xhtml");

	}

	public void add() throws IOException {

		customer = new Customer();
		FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerAdd.xhtml");
	}

	/**
	 * <b> search </b> method search existing Customers Info.
	 * 
	 * @throws IOException
	 * @exception Exception
	 */
	public void search() throws IOException {

		log.debug("Inside Method search..");

		try {
			customerList = (List<Customer>) delegate.search(getFieldValue(), getColumnValue());
			log.debug("search(Method)customerList size.." + customerList.size());
			if (customerList.size() == 0) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"The value you have entered is not available in the Database...", "");
				facesContext.addMessage("customerForm", facesMessage);
				log.debug("End of Method search..");

				return;
			}
		} catch (Exception ex) {

			ex.printStackTrace();

		}
		log.debug("End of Method search..");
		FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerList.xhtml");
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

	public List<String> getColumnNameList() {
		return columnNameList;
	}

	public void setColumnNameList(List<String> columnNameList) {
		this.columnNameList = columnNameList;
	}

	
	public CustomerBusinessDelegate getDelegate() {
		return delegate;
	}

}
