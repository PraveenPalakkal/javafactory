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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;





import javax.faces.application.FacesMessage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import java.util.ArrayList;
import java.util.List;

import java.util.Date;
import java.util.Enumeration;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;


import java.lang.reflect.Field;

@ManagedBean(name = "customerBean")
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
	private boolean showEmailbutton=false;
public boolean isShowEmailbutton() {
		return showEmailbutton;
	}
	public void setShowEmailbutton(boolean showEmailbutton) {
		this.showEmailbutton = showEmailbutton;
	} 
	private String businessDelegateURL;
	private String referer = null;
	private String webServiceReferer = null;
	
	public CustomerBean() {
		log.debug("Inside CustomerBean Constructor..");
		columnNameList = new ArrayList<>();
		columnNameList.add("-- Select the Field--");
		Field[] fieldList = Customer.class.getDeclaredFields();
		for (Field field : fieldList)
			columnNameList.add(field.getName());
		customer = new Customer();
		
		//delegate = new CustomerBusinessDelegate();
		initialiseList();
		log.debug("End of CustomerBean Constructor..");
	}

	/**
	 * <b> initialiseList </b> method loads list of customers from database.
	 * 
	 * @exception Exception
	 */
	/**
	 * 
	 */
	public void initialiseList() {
		try {
			log.debug("Inside Method initialiseList..");
			HttpServletRequest request = (HttpServletRequest) FacesContext
	                .getCurrentInstance().getExternalContext().getRequest();
			
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
		            .getExternalContext().getContext();
			String contextPath = ctx.getContextPath();
			log.debug(contextPath);
			log.debug(request.getScheme());
			String scheme = request.getScheme();
			String host = request.getHeader("host");
			referer = scheme + "://" + host + request.getServletContext().getContextPath() +"/";
			webServiceReferer = scheme + "://" + host + request.getServletContext().getContextPath() +"Service/"; 
			businessDelegateURL = referer + "BusinessDelegator";
			log.debug("URL : " + businessDelegateURL);
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "REST");
			urlParameters.put("service", webServiceReferer + "rest/ws/list");
			urlParameters.put("serviceMethod", "GET");
			urlParameters.put("paramType", "");
			urlParameters.put("param", "{}");
			
			log.debug("URL Parameters : " + urlParameters.toJSONString().replace("\\", ""));


			String output = sendPost(businessDelegateURL, urlParameters.toJSONString().replace("\\", ""));
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
			
			//customerList = customers.subList(0, customers.size());
			//delegate = new CustomerBusinessDelegate();
			//customerList = delegate.getAllCustomer();
			log.debug("List Size : " + customerList.size());
			fieldValue = null;
			columnValue = "-- Select the Field--";
			log.debug("End of Method initialiseList..");
		} catch (Exception ex) {
			ex.printStackTrace();
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
	public String createNewRecord() throws IOException {
		log.debug("createNewRecord method Starts...");
FacesContext facesContext=FacesContext.getCurrentInstance();
		try {
			try {
				customer.setCreatedby(InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			customer.setCreateddate(new Date());
			customer.setModifieddate(new Date());
			JSONParser parser = new JSONParser();
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(customer);
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "REST");
			urlParameters.put("service", webServiceReferer + "rest/ws/save");
			urlParameters.put("serviceMethod", "POST");
			urlParameters.put("paramType", "");
			urlParameters.put("param", jsonInString);

			String output = sendPost(businessDelegateURL, urlParameters.toJSONString());
			log.debug("Output : " + output);
			//delegate.insert(customer);
			
			initialiseList();
			log.debug("End of Method createNewRecord..");
			FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerList.xhtml");
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

		return null;
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
	public String updateRecord() throws IOException {
		log.debug("Inside Method updateRecord..");
FacesContext facesContext=FacesContext.getCurrentInstance();
		try {
			try {
				customer.setModifiedby(InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			customer.setModifieddate(new Date());
			
			JSONParser parser = new JSONParser();
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(customer);
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "REST");
			urlParameters.put("service", webServiceReferer + "rest/ws/update");
			urlParameters.put("serviceMethod", "POST");
			urlParameters.put("paramType", "com.app.customer.vo.Customer");
			urlParameters.put("param", jsonInString);
		
			String output = sendPost(businessDelegateURL, urlParameters.toJSONString());
			log.debug("Output : " + output);

			//delegate.update(customer);
		}
 catch (Exception ex) {
			ex.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("Error",
					new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), ex.getMessage()));
			FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerEdit.xhtml");
		}
		initialiseList();
		log.debug("End of Method updateRecord..");
		FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerList.xhtml");
return null;

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
			if (deleteList.size() > 0){
				//delegate.deleteAll(deleteList);
				JSONParser parser = new JSONParser();
				JSONArray array = (JSONArray) parser.parse(deleteList.toString());
				log.debug("Total Customer to be deleted : " + array.size());
				JSONObject urlParameters = new JSONObject();
				urlParameters.put("serviceType", "REST");
				urlParameters.put("service", webServiceReferer + "rest/ws/delete");
				urlParameters.put("serviceMethod", "POST");
				urlParameters.put("paramType", "");
				urlParameters.put("param", array.toJSONString());
				String output = sendPost(businessDelegateURL, urlParameters.toJSONString());
				log.debug("Output : " + output);
			}
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
			JSONObject param = new JSONObject();
			param.put("searchValue", getFieldValue());
			param.put("searchColumn", getColumnValue());
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "REST");
			urlParameters.put("service", webServiceReferer + "rest/ws/search");
			urlParameters.put("serviceMethod", "GET");
			urlParameters.put("paramType", "");
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
			
			
			//customerList = (List<Customer>) delegate.search(getFieldValue(), getColumnValue());
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
	
	
	// HTTP POST request
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

}
