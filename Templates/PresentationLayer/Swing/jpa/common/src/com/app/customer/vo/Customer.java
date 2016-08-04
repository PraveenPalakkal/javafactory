package com.app.customer.vo;

import java.util.Date;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name="customer")
public class Customer implements Serializable {

	/**
	 * 
	 */
	@Id
	@Column(name="CustomerId",insertable=true,updatable=true,length=11)
	private Integer customerid;
	@Column(name="Name",insertable=true,updatable=true,length=45)
	private String name;
	@Column(name="Email",insertable=true,updatable=true,length=45)
	private String email;
	@Column(name="Phone",insertable=true,updatable=true,length=45)
	private String phone;
	@Column(name="Address",insertable=true,updatable=true,length=45)
	private String address;
	@Column(name="Orders",insertable=true,updatable=true,length=45)
	private String orders;
	@Column(name="Action",insertable=true,updatable=true,length=45)
	private String action;
	@Temporal(TemporalType.TIMESTAMP)
	
	@Column(name="createdDate",insertable=true,updatable=true,length=10)	
	private Date createddate = null;
	@Column(name="createdBy",insertable=true,updatable=true,length=45)
	private String createdby = "";
	@Temporal(TemporalType.TIMESTAMP)
	
	@Column(name="modifiedDate",insertable=true,updatable=true,length=10)
	private Date modifieddate = null;
	@Column(name="modifiedBy",insertable=true,updatable=true,length=45)
	private String modifiedby = "";
	
	@Transient
	private int hashCode = -1;

	public Customer() {
		super();
		createddate = new Date();
		modifieddate = new Date();
	}

	public Customer(Integer customerid, String name, String email,
			String phone, String address, String orders, String action,
			Date createddate, String createdby, Date modifieddate,
			String modifiedby, int hashCode, boolean toBeDeleted) {
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
		this.hashCode = hashCode;
		this.toBeDeleted = toBeDeleted;
	}

	public void setCustomerid(Integer newValue) {
		customerid = newValue;
	}

	public Integer getCustomerid() {
		return customerid;
	}

	public void setName(String newValue) {
		name = newValue;
	}

	public String getName() {
		return name;
	}

	public void setEmail(String newValue) {
		email = newValue;
	}

	public String getEmail() {
		return email;
	}

	public void setPhone(String newValue) {
		phone = newValue;
	}

	public String getPhone() {
		return phone;
	}

	public void setAddress(String newValue) {
		address = newValue;
	}

	public String getAddress() {
		return address;
	}

	public void setOrders(String newValue) {
		orders = newValue;
	}

	public String getOrders() {
		return orders;
	}

	public void setAction(String newValue) {
		action = newValue;
	}

	public String getAction() {
		return action;
	}

	public void setCreateddate(Date newValue) {
		createddate = newValue;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreatedby(String newValue) {
		createdby = newValue;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setModifieddate(Date newValue) {
		modifieddate = newValue;
	}

	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifiedby(String newValue) {
		modifiedby = newValue;
	}

	public String getModifiedby() {
		return modifiedby;
	}
	
	@Transient
	private boolean toBeDeleted;

	public boolean equals(Object aObj) {

		return true;

	}

	public int hashCode() {

		return 1;

	}

	public void setToBeDeleted(boolean toBeDeleted) {
		this.toBeDeleted = toBeDeleted;
	}
	
	public boolean isToBeDeleted() {
		return toBeDeleted;
	}

	@Override
	public String toString() {
		return "{\"customerid\":\"" + customerid + "\", \"name\":\"" + name + "\", \"email\":\"" + email
				+ "\", \"phone\":\"" + phone + "\", \"address\":\"" + address + "\", \"orders\":\"" + orders
				+ "\", \"action\":\"" + action + "\", \"createddate\":" + createddate.getTime() + ", \"createdby\":\""
				+ createdby + "\", \"modifieddate\":" + modifieddate.getTime() + ", \"modifiedby\":\"" + modifiedby
				+ "\", \"toBeDeleted\":\"" + toBeDeleted + "\"}";
	}

}
