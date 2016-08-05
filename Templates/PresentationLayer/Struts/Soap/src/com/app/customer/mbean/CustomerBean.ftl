/*
 * EmpBean.java
 *
 * Created on May 22, 2007, 3:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.app.customer.mbean;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;
${Validation_import}
${Cache_import}
${Notification_Import}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
import com.opensymphony.xwork2.ActionContext;

import java.lang.reflect.Field;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	private boolean email=true; 
	public boolean isEmail() {
		return email;
	}

	public void setEmail(boolean email) {
		this.email = email;
	}

	private String fieldValue;
	private String columnName;
	private String columnValue;
	private Map session = (Map)ActionContext.getContext().get("session");

	private int scrollerPage;
	
	private String businessDelegateURL;
	
	private String referer = null;

	public int getScrollerPage() {
		return this.scrollerPage;
	}

	public void setScrollerPage(int scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	public CustomerBean() {
		log.debug("Inside CustomerBean Constructor..");	
	${Email_button}
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
	public String initialiseList() {
		try {
			log.debug("Inside Method initialiseList..");
			populateColumnNameFields();
			HttpServletRequest request = ServletActionContext.getRequest();
			String contextPath = request.getContextPath();
			log.debug(contextPath);
			log.debug(request.getScheme());
			String scheme = request.getScheme();
			String host = request.getHeader("host");
			referer = scheme + "://" + host + request.getServletContext().getContextPath() +"/";
			businessDelegateURL = referer + "BusinessDelegator";
			log.debug("URL : " + businessDelegateURL);
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "POJO");
			urlParameters.put("service", "com.app.customer.stub.CustomerStub");
			urlParameters.put("serviceMethod", "getAllCustomer");
			urlParameters.put("paramType", "");
			urlParameters.put("param", "{}");
			
			log.debug("URL Parameters : " + urlParameters);
			${Cache_ImplStart}
			String output = sendPost(businessDelegateURL, urlParameters.toJSONString());
			log.debug("output : " + output);
			JSONParser parser = new JSONParser();
			JSONArray customers = (JSONArray) parser.parse(output);
			log.debug("Customer Array : " + customers);
			customerList = new ArrayList<>();
			for(int i =0 ; i<customers.size() ; i++){
				JSONObject customer = (JSONObject) customers.get(i);
				ObjectMapper mapper = new ObjectMapper();
				customerList.add(mapper.convertValue(customer, Customer.class));
			}
			${Cache_ImplEnd}
			//customerList = customers.subList(0, customers.size());
			//delegate = new CustomerBusinessDelegate();
			//customerList = delegate.getAllCustomer();
			log.debug("List Size : " + customerList.size());
			log.debug("End of Method initialiseList..");
		} catch (Exception ex) {
		}return "customerList";

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
	${Validation_Call}
				JSONParser parser = new JSONParser();
				ObjectMapper mapper = new ObjectMapper();
				String jsonInString = mapper.writeValueAsString(customer);
				JSONObject urlParameters = new JSONObject();
				urlParameters.put("serviceType", "POJO");
				urlParameters.put("service", "com.app.customer.stub.CustomerStub");
				urlParameters.put("serviceMethod", "insert");
				urlParameters.put("paramType", "com.app.customer.vo.Customer");
				urlParameters.put("param", jsonInString);
				String output = sendPost(businessDelegateURL, urlParameters.toJSONString());
				log.debug("Output : " + output);
			}${Validation_CatchAdd} 
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
			initialiseList();
			log.debug("getList() - resultList size..");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		log.debug("End of Method getList..");
		return "customerList";
	}
		
	public String select() throws IOException {
		log.debug("Inside Method select..");
		String id = ServletActionContext.getRequest().getParameter("id");
		log.debug("the id " + id);
		try {
			if(session.containsKey("deleteList")){
				session.remove("deleteList");
			}
			/*Customer customer = new Customer();
			customer.setCustomerid(Integer.parseInt(id));*/
			try {
				JSONObject param = new JSONObject();
				param.put("searchValue", id);
				param.put("searchColumn", "customerid");
				JSONObject urlParameters = new JSONObject();
				urlParameters.put("serviceType", "POJO");
				urlParameters.put("service", "com.app.customer.stub.CustomerStub");
				urlParameters.put("serviceMethod", "search");
				urlParameters.put("paramType", "String,String");
				urlParameters.put("param", param.toJSONString());
				String output = sendPost(businessDelegateURL, urlParameters.toJSONString());
				log.debug("Output : " + output);
				JSONParser parser = new JSONParser();
				JSONArray customers = (JSONArray) parser.parse(output);
				log.debug("Customer Array : " + customers);
				customerList = new ArrayList<>();
				for(int i =0 ; i<customers.size() ; i++){
					JSONObject customer = (JSONObject) customers.get(i);
					ObjectMapper mapper = new ObjectMapper();
					this.setCustomer(mapper.convertValue(customer, Customer.class));
				}
			} catch (HexApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("the customer name " + customer.getName());
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
${Validation_Call}
			JSONParser parser = new JSONParser();
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(customer);
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "POJO");
			urlParameters.put("service", "com.app.customer.stub.CustomerStub");
			urlParameters.put("serviceMethod", "update");
			urlParameters.put("paramType", "com.app.customer.vo.Customer");
			urlParameters.put("param", jsonInString);
			String output = sendPost(businessDelegateURL, urlParameters.toJSONString());
			//delegate.update(customer);
		} ${Validation_CatchEdit} catch (Exception ex) {
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
			//customerList = (List) delegate.getAllCustomer();
			initialiseList();
			if(session.containsKey("deleteList")){
				chkBoxList = (HashMap<String, Boolean>) session.get("deleteList");
			}
			for (String key : chkBoxList.keySet()) {
				customer = new Customer();
				customer.setCustomerid(Integer.parseInt(key));
				deleteList.add(customer);
			}	
			log.debug("delete()- deleteList size.." + deleteList.size());
			if (deleteList.size() > 0){
				JSONParser parser = new JSONParser();
				JSONArray array = (JSONArray) parser.parse(deleteList.toString());
				log.debug("Total Customer to be deleted : " + array.size());
				JSONObject urlParameters = new JSONObject();
				urlParameters.put("serviceType", "POJO");
				urlParameters.put("service", "com.app.customer.stub.CustomerStub");
				urlParameters.put("serviceMethod", "deleteAll");
				urlParameters.put("paramType", "java.util.List<com.app.customer.vo.Customer>");
				urlParameters.put("param", array.toJSONString());
				String output = sendPost(businessDelegateURL, urlParameters.toJSONString());
			}	
				//delegate.deleteAll(deleteList);
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
			JSONObject param = new JSONObject();
			param.put("searchValue", getFieldValue());
			param.put("searchColumn", getColumnValue());
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "POJO");
			urlParameters.put("service", "com.app.customer.stub.CustomerStub");
			urlParameters.put("serviceMethod", "search");
			urlParameters.put("paramType", "String,String");
			urlParameters.put("param", param.toJSONString());
			String output = sendPost(businessDelegateURL, urlParameters.toJSONString());
			log.debug("Output : " + output);
			JSONParser parser = new JSONParser();
			JSONArray customers = (JSONArray) parser.parse(output);
			log.debug("Customer Array : " + customers);
			customerList = new ArrayList<>();
			for(int i =0 ; i<customers.size() ; i++){
				JSONObject customer = (JSONObject) customers.get(i);
				ObjectMapper mapper = new ObjectMapper();
				customerList.add(mapper.convertValue(customer, Customer.class));
			}
			log.debug("search(Method)customerList size.." + customerList.size());
		} catch (Exception ex) {

			ex.printStackTrace();

		}
		log.debug("End of Method search..");
		return "customerList";
	}
	
	private String sendPost(String url, String urlParameters) throws HexApplicationException {

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("referer", referer);
			/*Enumeration<String> headerNames = request.getHeaderNames();
			while(headerNames.hasMoreElements()){
				String key = headerNames.nextElement();
				con.setRequestProperty(key, request.getHeader(key));
				log.debug(key + " : " + request.getHeader(key));
			}*/
			

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			log.debug("\nSending 'POST' request to URL : " + url);
			log.debug("Response Code : " + responseCode);
			log.debug("Parameters : " + urlParameters);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			log.debug("Business Delegate : " + response.toString());
			return response.toString();
		} catch (Exception e) {
			throw new HexApplicationException(e.getMessage());
		}

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

	${Notification_method}
	
	
}
