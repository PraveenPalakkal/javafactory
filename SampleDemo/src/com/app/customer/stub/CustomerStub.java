package com.app.customer.stub;

import com.app.customer.service.ICustomer;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class CustomerStub implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3440828310386661784L;
	private ICustomer customerService;

	public CustomerStub() {

		String serverHost = "http://localhost:8080/";
		customerService = getSoapCustomerService(serverHost);

	}

	public String insert(Customer object) throws HexApplicationException {
		
		return customerService.insert(object);
	}

	public String deleteAll(List<Customer> customers) throws HexApplicationException {
		return customerService.deleteAll(customers);
	}

	public String update(Customer customer) throws HexApplicationException {
	
		return customerService.update(customer);
	}

	public List<Customer> getAllCustomer() throws HexApplicationException {
		return customerService.getAllCustomer();
	}

	public List<Customer> search(String searchValue, String searchColumn) throws HexApplicationException {
		return customerService.search(searchValue, searchColumn);
	}

	private ICustomer getSoapCustomerService(String serverHost) {
		URL url;
		ICustomer customerService = null;
		try {
			url = new URL(serverHost + "SampleDemoService/customerService?wsdl");

			QName qname = new QName("http://service.customer.app.com/", "SOAPServiceService");

			Service service = Service.create(url, qname);

			customerService = service.getPort(ICustomer.class);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return customerService;
	}

}
