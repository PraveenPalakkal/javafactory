/*
 * EmpBean.java
 *
 * Created on May 22, 2007, 3:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.app.customer.mbean;

import java.io.IOException;
import java.io.Serializable;

import com.app.customer.delegate.CustomerBusinessDelegate;
import com.app.customer.util.BootStrapper;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
import com.opensymphony.xwork2.ActionContext;

import java.lang.reflect.Field;

import org.apache.struts2.ServletActionContext;

public class CustomerBean implements Serializable {
	/**
	 * 
	 */
	static Logger log = LogFactory.getLogger(CustomerBean.class);
	private static final long serialVersionUID = 1L;
	private List customerList;
	private boolean deleteAll;
	private boolean hasContents;
	private Customer customer;
	private int pageSize;
	private String currentPage;
	private String id;
	private HashMap<String,Boolean> chkBoxList;

	private HashMap columnNameList;

	private String fieldValue;
	private String columnName;
	private String columnValue;
	private Map session = (Map)ActionContext.getContext().get("session");
	CustomerBusinessDelegate delegate = null;

	private int scrollerPage;

	public int getScrollerPage() {
		return this.scrollerPage;
	}

	public void setScrollerPage(int scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	public CustomerBean() {
		log.debug("Inside CustomerBean Constructor..");	
		delegate = (CustomerBusinessDelegate) BootStrapper.getService().getBean("CustomerDelegate");
		customer = new Customer();
		pageSize = 100;
		initialiseList();
		log.debug("End of CustomerBean Constructor..");
	}

	/**
	 * <b> initialiseList </b> method loads list of customers from database.
	 * 
	 * @exception Exception
	 */
	private void initialiseList() {
		try {
			log.debug("Inside Method initialiseList..");
			customerList = getAllContents();
			log.debug("End of Method initialiseList..");
		} catch (Exception ex) {
		}

	}

	public String getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setHasContents(boolean hasContents) {
		this.hasContents = hasContents;
	}

	public boolean isHasContents() {
		return hasContents;
	}

	public void setDeleteAll(boolean deleteAll) {
		this.deleteAll = deleteAll;
	}

	public boolean isDeleteAll() {
		return deleteAll;
	}

	public List getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List customerList) {
		this.customerList = customerList;
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
	public String createNewRecord() throws IOException {

		log.debug("Inside createNewRecord...");
			try {
				customer.setCreatedby(InetAddress.getLocalHost().getHostName());	
				customer.setCreateddate(new Date());
				delegate.insert(customer);
			}
		catch (NumberFormatException exp3) {
			return "customerAdd";
		} catch (Exception e) {
			return "customerAdd";
		} catch (Error e) {
			return "customerAdd";
		}
		/* End: Bug# 11 dated :25 Mar Updated by bhushan */
		initialiseList();
		log.debug("End of Method createNewRecord..");
		return "customerList";
	}

	public String clear() throws IOException {
		if(session.containsKey("deleteList")){
			session.remove("deleteList");
		}
		setFieldValue("");
		setColumnValue("");
		return "customerList";
	}

	/**
	 * <b> getList </b> method get list of Customer info depends upon the
	 * current page , start and end.
	 * 
	 * @throws IOException
	 * 
	 */
	public String getList() throws IOException {
		log.debug("Inside Method getList..");
		try {
			System.out.println("Inside Method getList..");
			if(session.containsKey("deleteList")){
				session.remove("deleteList");
			}
			List resultList = getAllContents();
			log.debug("getList() - resultList size.." + resultList.size());
			this.setCustomerList(resultList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		log.debug("End of Method getList..");
		return "customerList";
	}

	/**
	 * <b> getContents </b> method get list of Customer info with the help of
	 * startRecord and pagesize.
	 * 
	 * @return Object - List of Customers
	 * 
	 * @exception Exception
	 */
	private List getContents(int startRecord, int pageSize) {
		log.debug("Inside Method getContents(int startRecord, int pageSize).."
				+ startRecord + " , " + pageSize);
		try {
			log.debug("End of Method getContents(int startRecord, int pageSize)..");
			return (List) delegate.getAllCustomer(startRecord, pageSize);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	/**
	 * <b> getAllContents </b> method get All Customers info.
	 * 
	 * @return Object - List of Customers
	 * 
	 * @exception Exception
	 * 
	 */
	private List getAllContents() {
		log.debug("Inside Method getAllContents..");
		populateColumnNameFields(); // Calling local method to populate fields
									// in the drop-down.
		try {
			log.debug("End of Method getAllContents..");
			return (List) delegate.getAllCustomer();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public String select() throws IOException {
		log.debug("Inside Method select..");
		String id = ServletActionContext.getRequest().getParameter("id");
		log.debug("the id " + id);
		try {
			if(session.containsKey("deleteList")){
				session.remove("deleteList");
			}
			Customer customer = new Customer();
			customer.setCustomerid(Integer.parseInt(id));
			try {
				this.setCustomer((Customer) delegate.select(customer));
			} catch (HexApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("End of  Method select..");
		return "customerEdit";
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
	public String updateRecord() throws IOException {
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
			return "customerEdit";
		}
		initialiseList();
		log.debug("End of Method updateRecord..");
		return "customerList";
	}

	public void updateCustomerList() throws IOException {
		String value = ServletActionContext.getRequest().getParameter("value");
		try {
			chkBoxList = new HashMap<>();
			if(session.containsKey("deleteList")){
				chkBoxList = (HashMap<String, Boolean>) session.get("deleteList");
				session.remove("deleteList");
			}
			if(chkBoxList.containsKey(value)){
				if(chkBoxList.get(value)){
					//chkBoxList.put(value, false);
					chkBoxList.remove(value);
				}else if(!chkBoxList.get(value)){
					chkBoxList.put(value, true);
				}
			}else{
				chkBoxList.put(value, true);				
			}
			if(chkBoxList.size()>0){
				session.put("deleteList", chkBoxList);
			}else{
				session.remove("deleteList");
			}		
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * <b> delete </b> method delete existing Customer Information.
	 * 
	 * @throws IOException
	 * 
	 * @exception Exception
	 */
	public String delete() throws IOException {
		log.debug("Inside Method delete..");
		List deleteList = new ArrayList();
		try {
			customerList = (List) delegate.getAllCustomer();
			if(session.containsKey("deleteList")){
				chkBoxList = (HashMap<String, Boolean>) session.get("deleteList");
			}
			for (String key : chkBoxList.keySet()) {
				customer = new Customer();
				customer.setCustomerid(Integer.parseInt(key));
				deleteList.add(customer);
			}	
			log.debug("delete()- deleteList size.." + deleteList.size());
			if (deleteList.size() > 0)
				delegate.deleteAll(deleteList);
			if(session.containsKey("deleteList")){
				session.remove("deleteList");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		getList();
		log.debug("End of Method delete..");
		return "customerList";
	}

	public String add() throws IOException {
		if(session.containsKey("deleteList")){
			session.remove("deleteList");
		}
		System.out.println("getting into add method");
		customer = new Customer();
		return "customerAdd";
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

	private Field[] getFields(Object source) {
		return source.getClass().getDeclaredFields();
	}

	/**
	 * <b> populateColumnNameFields </b> method populates search column names.
	 * 
	 */
	private void populateColumnNameFields() {
		log.debug("Inside Method populateColumnNameFields..");
		columnNameList = new HashMap<>();
		if (columnNameList == null || columnNameList.size() == 0) {
			Field fields[] = getFields(customer);
			for (int index = 0; index < fields.length - 2; index++) {
				Field field = fields[index];
				if (field.getName() != null && field.getName().length() != 0
						&& field.getName() != "hashCode"
						&& field.getName() != "toBeDeleted") {
					String tempColumnValue = field.getName();
					tempColumnValue = tempColumnValue.substring(0, 1)
							.toUpperCase()
							+ tempColumnValue.substring(1,
									tempColumnValue.length()).toLowerCase();
					columnValue = field.getName();
					columnNameList.put(columnValue, tempColumnValue);
				}
			}
		}
		log.debug("End of Method populateColumnNameFields..");

	}

	/**
	 * <b> search </b> method search existing Customers Info.
	 * 
	 * @throws IOException
	 * @exception Exception
	 */
	public String search() throws IOException {

		log.debug("Inside Method search..");
		if(session.containsKey("deleteList")){
			session.remove("deleteList");
		}
		try {
			this.setCustomerList((List) delegate.search(getFieldValue(),
					getColumnValue()));
			log.debug("search(Method)customerList size.." + customerList.size());
		} catch (Exception ex) {

			ex.printStackTrace();

		}
		log.debug("End of Method search..");
		return "customerList";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HashMap getColumnNameList() {
		return columnNameList;
	}

	public void setColumnNameList(HashMap columnNameList) {
		this.columnNameList = columnNameList;
	}

	public HashMap<String, Boolean> getChkBoxList() {
		return chkBoxList;
	}

	public void setChkBoxList(HashMap<String, Boolean> chkBoxList) {
		this.chkBoxList = chkBoxList;
	}

	
	
	
}
