package com.app.customer.vo;

import java.io.Serializable;
import java.util.Date;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SimpleIntegerProperty customerid = new SimpleIntegerProperty();
	private SimpleStringProperty name = new SimpleStringProperty("");
	private SimpleStringProperty email = new SimpleStringProperty("");
	private SimpleStringProperty phone = new SimpleStringProperty();
	private SimpleStringProperty address = new SimpleStringProperty("");
	private SimpleStringProperty orders = new SimpleStringProperty();
	private SimpleStringProperty action = new SimpleStringProperty("");
	private Date createddate = new Date();
	private SimpleStringProperty createdby = new SimpleStringProperty("");
	private Date modifieddate = new Date();
	private SimpleStringProperty modifiedby = new SimpleStringProperty("");

	public Customer() {
		super();
	}

	public Customer(SimpleIntegerProperty customerid,
			SimpleStringProperty name, SimpleStringProperty email,
			SimpleStringProperty phone, SimpleStringProperty address,
			SimpleStringProperty orders, SimpleStringProperty action,
			Date createddate, SimpleStringProperty createdby,
			Date modifieddate, SimpleStringProperty modifiedby) {
		super();
		this.customerid = customerid;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.orders = orders;
		this.action = action;
		this.createddate = createddate;
		this.createdby = createdby;
		this.modifieddate = modifieddate;
		this.modifiedby = modifiedby;
	}

	public int getCustomerid() {
		return customerid.get();
	}

	public void setCustomerid(int customerid) {
		this.customerid.set(customerid);
	}

	public String getCreatedby() {
		return createdby.get();
	}

	public void setCreatedby(String createdby) {
		this.createdby.set(createdby);
	}

	public String getModifiedby() {
		return modifiedby.get();
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby.set(modifiedby);
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getEmail() {
		return email.get();
	}

	public void setEmail(String email) {
		this.email.set(email);
	}

	public String getPhone() {
		return phone.get();
	}

	public void setPhone(String phone) {
		this.phone.set(phone);
	}

	public String getAddress() {
		return address.get();
	}

	public void setAddress(String address) {
		this.address.set(address);
	}

	public String getOrders() {
		return orders.get();
	}

	public void setOrders(String orders) {
		this.orders.set(orders);
	}

	public String getAction() {
		return action.get();
	}

	public void setAction(String action) {
		this.action.set(action);
	}

	private boolean toBeDeleted;

	public boolean isToBeDeleted() {
		return toBeDeleted;
	}

	public void setToBeDeleted(boolean toBeDeleted) {
		this.toBeDeleted = toBeDeleted;
	}
	@Override
	public String toString() {
		return "{\"customerid\":\"" + customerid.get() + "\", \"name\":\"" + name.get()+ "\", \"email\":\"" + email.get()
				+ "\", \"phone\":\"" + phone.get() + "\", \"address\":\"" + address.get() + "\", \"orders\":\"" + orders.get()
				+ "\", \"action\":\"" + action.get() + "\", \"createddate\":" + createddate.getTime() + ", \"createdby\":\""
				+ createdby.get() + "\", \"modifieddate\":" + modifieddate.getTime() + ", \"modifiedby\":\"" + modifiedby.get()
				+ "\", \"toBeDeleted\":\"" + toBeDeleted + "\"}";
	}
}
