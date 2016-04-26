package com.app.customer.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.customer.util.CustomerForm;
import com.app.customer.delegate.CustomerBusinessDelegate;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class CustomerController {

	@Autowired
	private CustomerBusinessDelegate customerBusinessDelegate;

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

		List<Customer> customers;
		try {
			customers = customerBusinessDelegate.search(fieldValue, columnName);
			List<String> searchFiledOptions = getSearchFieldOptions();

			CustomerForm customerForm = new CustomerForm();
			customerForm.setCustomers(customers);

			model.getModelMap().addAttribute("customerForm", customerForm);
			model.getModelMap().addAttribute("searchFieldOptions", searchFiledOptions);
			model.getModelMap().addAttribute("searchTxt", fieldValue);

		} catch (HexApplicationException e) {
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
	public String customerList(ModelMap model) {
		try {
			List<String> searchFiledOptions = getSearchFieldOptions();

			List<Customer> customers = customerBusinessDelegate.getAllCustomer();

			CustomerForm customerForm = new CustomerForm();
			customerForm.setCustomers(customers);

			model.addAttribute("searchFieldOptions", searchFiledOptions);
			model.addAttribute("searchTxt", "");
			model.addAttribute("customerForm", customerForm);

		} catch (HexApplicationException e) {
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
			customerBusinessDelegate.insert(customer);
			/*
			 * Resetting the values
			 */
			customer = new Customer();
			model.getModelMap().addAttribute("customerBean", customer);
		} catch (HexApplicationException e) {
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
			Customer customer = (Customer) customerBusinessDelegate.search(customerId, "customerid").get(0);
			model.getModelMap().addAttribute("customerBean", customer);
		} catch (HexApplicationException e) {
			// TODO Auto-generated catch block
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
			model = new ModelAndView("customerList");
			/*
			 * Updating the customer
			 */
			customerBusinessDelegate.update(customer);

			/*
			 * Retrieving values for Customer List
			 */
			List<Customer> customers = customerBusinessDelegate.getAllCustomer();
			List<String> searchFiledOptions = getSearchFieldOptions();

			CustomerForm customerForm = new CustomerForm();
			customerForm.setCustomers(customers);

			model.getModelMap().addAttribute("customerForm", customerForm);
			model.getModelMap().addAttribute("searchFieldOptions", searchFiledOptions);
		} catch (HexApplicationException e) {
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
				customerBusinessDelegate.deleteAll(customersToBeDeleted);
			}

			/*
			 * Retrieving values for Customer List
			 */
			List<Customer> customers = customerBusinessDelegate.getAllCustomer();
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
	 * @return List<String>.
	 * 		contains list of search field options.
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
}
