package com.app.customer.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
${Validation_import}
${Cache_import}
${Notification_Import}
import com.app.customer.util.CustomerForm;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class CustomerController {

	static Logger log = LogFactory.getLogger(CustomerController.class);
	private String serviceControllerURL = null;
	private String referer = null;
	private String webServiceReferer = null;

	/**
	 * This method is used to search the customer with given searchText and
	 * searchField.
	 * 
	 * @param request
	 *            HttpServletRequest object.
	 * @param response
	 *            HttpServletResponse object.
	 * @return ModelAndView object.
	 * @see HttpServletRequest, HttpServletResponse, ModelAndView
	 */
	@RequestMapping(value = "customerSearch", method = RequestMethod.POST)
	public ModelAndView customerSearch(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("customerList");
		String fieldValue = request.getParameter("searchtxt");
		String columnName = request.getParameter("searchField");

		List<Customer> customerList;
		try {
			JSONObject param = new JSONObject();
			param.put("searchValue", fieldValue);
			param.put("searchColumn", columnName);
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "REST");
			urlParameters.put("service", webServiceReferer + "${Rest_URL}/ws/search");
			urlParameters.put("serviceMethod", "GET");
			urlParameters.put("paramType", "");
			urlParameters.put("param", param.toJSONString());
			String output = sendPost(serviceControllerURL, urlParameters.toJSONString());
			log.debug("Output : " + output);
			JSONParser parser = new JSONParser();
			JSONArray customers = (JSONArray) parser.parse(output);
			log.debug("Customer Array : " + customers);
			customerList = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				JSONObject customer = (JSONObject) customers.get(i);
				ObjectMapper mapper = new ObjectMapper();
				customerList.add(mapper.convertValue(customer, Customer.class));
			}

			List<String> searchFiledOptions = getSearchFieldOptions();

			CustomerForm customerForm = new CustomerForm();
			customerForm.setCustomers(customerList);

			model.getModelMap().addAttribute("customerForm", customerForm);
			model.getModelMap().addAttribute("searchFieldOptions", searchFiledOptions);
			model.getModelMap().addAttribute("searchTxt", fieldValue);

		} catch (HexApplicationException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return model;
	}

	/**
	 * This method is used to List the customer.
	 * 
	 * @param model
	 *            ModelMap object.
	 * @return String of "customerList", so that customerList.jsp page will be
	 *         redirected.
	 */
	@RequestMapping(value = "customerList", method = RequestMethod.GET)
	public String customerList(HttpServletRequest request, ModelMap model) {
		try {
			List<String> searchFiledOptions = getSearchFieldOptions();
		List<Customer> customerList=null;
			String scheme = request.getScheme();
			String host = request.getHeader("host");
			referer = scheme + "://" + host + request.getServletContext().getContextPath() +"/";
			webServiceReferer = scheme + "://" + host + request.getServletContext().getContextPath() +"${webServiceReferer}";
			serviceControllerURL = referer + "BusinessDelegator";
			log.debug("");
			${Cache_ImplStart}

			customerList = getCustomerList();
			${Cache_ImplEnd}

			CustomerForm customerForm = new CustomerForm();
			customerForm.setCustomers(customerList);

			model.addAttribute("searchFieldOptions", searchFiledOptions);
			model.addAttribute("searchTxt", "");
			model.addAttribute("customerForm", customerForm);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HexApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "customerList";
	}

	/**
	 * This method is used to add a new customer.
	 * 
	 * @param request
	 *            HttpServletRequest object.
	 * @param response
	 *            HttpServletResponse object.
	 * @param customer
	 *            Customer object which is to be added.
	 * @return ModelAndView.
	 * 
	 * @see HttpServletRequest, HttpServletResponse, ModelAndView.
	 */
	@RequestMapping(value = "customerAdd", method = RequestMethod.POST)
	public ModelAndView customerAdd(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("customerBean") Customer customer) {
		ModelAndView model = null;
		try {
			model = new ModelAndView("customerAdd");
			/*
			 * Saving the customer
			 */
			customer.setCreatedby(customer.getName());
			customer.setCreateddate(new Date());
			customer.setModifiedby(customer.getName());
			customer.setModifieddate(new Date());
			${Validation_Call}

			JSONParser parser = new JSONParser();
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(customer);
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "REST");
			urlParameters.put("service", webServiceReferer + "${Rest_URL}/ws/save");
			urlParameters.put("serviceMethod", "POST");
			urlParameters.put("paramType", "");
			urlParameters.put("param", jsonInString);
			String output = sendPost(serviceControllerURL, urlParameters.toJSONString());
			log.debug("Output : " + output);

			/*
			 * Resetting the values
			 */
			customer = new Customer();
			model.getModelMap().addAttribute("customerBean", customer);
		}  ${Validation_CatchAdd}
		catch (HexApplicationException e) {
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * This method is used to navigate to the customerAdd.jsp page from
	 * customerList.jsp page.
	 * 
	 * @return ModelAndView.
	 * 
	 * @see ModelAndView.
	 */
	@RequestMapping(value = "showAddCustomer", method = RequestMethod.GET)
	public ModelAndView showAddCustomer() {
		ModelAndView model = new ModelAndView("customerAdd");
		Customer customer = new Customer();
		model.getModelMap().addAttribute("customerBean", customer);
		return model;
	}

	/**
	 * This method is used to populate the details in the customerEdit.jsp page,
	 * with the customer details to be edited.
	 * 
	 * @param request
	 *            HttpServletRequest object.
	 * @param response
	 *            HttpServletResponse object.
	 * @return ModelAndView.
	 * 
	 * @see HttpServletRequest, HttpServletResponse, ModelAndView.
	 */
	@RequestMapping(value = "editCustomer", method = RequestMethod.GET)
	public ModelAndView editCustomer(HttpServletRequest request, HttpServletResponse response) {
		String customerId = request.getParameter("id");
		ModelAndView model = new ModelAndView("customerEdit");
		try {
			List<Customer> customerList = null;
			JSONObject param = new JSONObject();
			param.put("searchValue", customerId);
			param.put("searchColumn", "customerid");
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "REST");
			urlParameters.put("service", webServiceReferer + "${Rest_URL}/ws/search");
			urlParameters.put("serviceMethod", "GET");
			urlParameters.put("paramType", "");
			urlParameters.put("param", param.toJSONString());
			String output = sendPost(serviceControllerURL, urlParameters.toJSONString());
			log.debug("Output : " + output);
			JSONParser parser = new JSONParser();
			JSONArray customers = (JSONArray) parser.parse(output);
			log.debug("Customer Array : " + customers);
			customerList = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				JSONObject customer = (JSONObject) customers.get(i);
				ObjectMapper mapper = new ObjectMapper();
				customerList.add(mapper.convertValue(customer, Customer.class));
			}

			Customer customer = customerList.get(0);
			model.getModelMap().addAttribute("customerBean", customer);
		} catch (HexApplicationException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * This method is used to update the customer details.
	 * 
	 * @param request
	 *            HttpServletRequest object.
	 * 
	 * @param response
	 *            HttpServletResponse object.
	 * 
	 * @param customer
	 *            Customer object with the updated property values.
	 * @return ModelAndView.
	 * 
	 * @see HttpServletRequest, HttpServletResponse, ModelAndView.
	 */
	@RequestMapping(value = "customerUpdate", method = RequestMethod.POST)
	public ModelAndView customerUpdate(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("customerBean") Customer customer) {
		ModelAndView model = null;
		System.out.println("---->>>>" + customer.getCustomerid());
		try {
		${Validation_Call}

			model = new ModelAndView("customerList");
			/*
			 * Updating the customer
			 */
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(customer);
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "REST");
			urlParameters.put("service", webServiceReferer + "${Rest_URL}/ws/update");
			urlParameters.put("serviceMethod", "POST");
			urlParameters.put("paramType", "");
			urlParameters.put("param", jsonInString);
			String output = sendPost(serviceControllerURL, urlParameters.toJSONString());
			log.debug("Output : " + output);

			/*
			 * Retrieving values for Customer List
			 */
			List<Customer> customerList = getCustomerList();

			List<String> searchFiledOptions = getSearchFieldOptions();

			CustomerForm customerForm = new CustomerForm();
			customerForm.setCustomers(customerList);

			model.getModelMap().addAttribute("customerForm", customerForm);
			model.getModelMap().addAttribute("searchFieldOptions", searchFiledOptions);
		}  ${Validation_CatchEdit}
catch (HexApplicationException e) {
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * This method is used to delete the selected customer details.
	 * 
	 * @param customerForm
	 *            CustomerForm object contains the list of customers(toBeDeleted
	 *            and notToBeDeleted customers).
	 * @return ModelAndView.
	 * 
	 * @see ModelAndView.
	 */
	@RequestMapping(value = "/customerDelete", method = RequestMethod.POST)
	public ModelAndView customerDelete(@ModelAttribute("customerForm") CustomerForm customerForm) {
		ModelAndView model = new ModelAndView("customerList");
		try {
			/*
			 * Deleting the selected Records
			 */
			if (customerForm != null) {
				List<Customer> customersToBeDeleted = new ArrayList<>();
				for (Customer customer : customerForm.getCustomers()) {
					System.out.println("Name : " + customer.getCustomerid() + " --> " + customer.isToBeDeleted());
					if (customer.isToBeDeleted())
						customersToBeDeleted.add(customer);
				}

				JSONParser parser = new JSONParser();
				JSONArray array = (JSONArray) parser.parse(customersToBeDeleted.toString());
				log.debug("Total Customer to be deleted : " + array.size());
				JSONObject urlParameters = new JSONObject();
				urlParameters.put("serviceType", "REST");
				urlParameters.put("service", webServiceReferer + "${Rest_URL}/ws/delete");
				urlParameters.put("serviceMethod", "POST");
				urlParameters.put("paramType", "");
				urlParameters.put("param", array.toJSONString());
				String output = sendPost(serviceControllerURL, urlParameters.toJSONString());
				log.debug("Output : " + output);

			}

			/*
			 * Retrieving values for Customer List
			 */
			List<Customer> customers = getCustomerList();
			List<String> searchFiledOptions = getSearchFieldOptions();

			CustomerForm custForm = new CustomerForm();
			custForm.setCustomers(customers);

			model.getModelMap().addAttribute("customerForm", custForm);
			model.getModelMap().addAttribute("searchFieldOptions", searchFiledOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * This is a helper method which is used to get the List of search Field
	 * options.
	 * 
	 * @return List<String>. contains list of search field options.
	 */
	private List<String> getSearchFieldOptions() {
		List<String> searchFieldOptions = new ArrayList<>();
		searchFieldOptions.add("--Select the Field--");
		searchFieldOptions.add("customerid");
		searchFieldOptions.add("name");
		searchFieldOptions.add("email");
		searchFieldOptions.add("phone");
		searchFieldOptions.add("address");
		searchFieldOptions.add("orders");
		searchFieldOptions.add("action");

		return searchFieldOptions;
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
			/*
			 * Enumeration<String> headerNames = request.getHeaderNames();
			 * while(headerNames.hasMoreElements()){ String key =
			 * headerNames.nextElement(); con.setRequestProperty(key,
			 * request.getHeader(key)); log.debug(key + " : " +
			 * request.getHeader(key)); }
			 */

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

	private List<Customer> getCustomerList() throws ParseException, HexApplicationException {
		JSONObject urlParameters = new JSONObject();
		urlParameters.put("serviceType", "REST");
		urlParameters.put("service", webServiceReferer + "${Rest_URL}/ws/list");
		urlParameters.put("serviceMethod", "GET");
		urlParameters.put("paramType", "");
		urlParameters.put("param", "{}");

		ObjectMapper mapper = new ObjectMapper();
		log.debug("URL Parameters : " + urlParameters);
		String output = sendPost(serviceControllerURL, urlParameters.toJSONString());
		log.debug("output : " + output);
		JSONParser parser = new JSONParser();
		JSONArray customers = (JSONArray) parser.parse(output);
		log.debug("Customer Array : " + customers);
		List<Customer> customerList = new ArrayList<>();
		for (int i = 0; i < customers.size(); i++) {
			JSONObject customerObj = (JSONObject) customers.get(i);
			customerList.add(mapper.convertValue(customerObj, Customer.class));
		}
		return customerList;
	}
@RequestMapping(value = "/customerListLogin", method = RequestMethod.POST)	public String customerListLogin(HttpServletRequest request, ModelMap model) {
customerList(request, model);
return "customerList";
}

${Notification_method}
}
