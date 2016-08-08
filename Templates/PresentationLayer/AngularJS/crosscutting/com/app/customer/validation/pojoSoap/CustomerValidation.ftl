package com.app.customer.validation;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

${Validation_import}
${importNotification}
${CacheImport}
${Authentication}
public class CustomerValidation  {

	private String serviceControllerURL;
	private String output;
	private ResourceBundle bundle;

	public CustomerValidation() {
		bundle = ResourceBundle.getBundle("SpaUrl");
	}

	public String insert(Customer customer) throws HexApplicationException {
		try {
			JSONParser parser = new JSONParser();
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(customer);
			
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "POJO");
			urlParameters.put("service",
					"${service}");
			urlParameters.put("serviceMethod", "insert");
			urlParameters.put("paramType", "com.app.customer.vo.Customer");
			urlParameters.put("param", jsonInString);
			${Validation_Call}
			output = sendPost(getServiceController(),
					urlParameters.toJSONString());
			System.out.println("inside insert in CustomerValidation ");
				
			} ${Validation_CatchEdit} 
			catch (NumberFormatException exp3) {
				return "Failure:" + exp3.getMessage();
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "Failure:" + e.getMessage();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "Failure:" + e.getMessage();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} return "Success:" + output;

	}

	public String deleteAll(List<Customer> customers)
			throws HexApplicationException {
		try{
		JSONParser parser = new JSONParser();
		JSONArray array = (JSONArray) parser.parse(customers.toString());
		
		JSONObject urlParameters = new JSONObject();
		urlParameters.put("serviceType", "POJO");
		urlParameters.put("service", "${service}");
		urlParameters.put("serviceMethod", "deleteAll");
		urlParameters.put("paramType", "java.util.List<com.app.customer.vo.Customer>");
		urlParameters.put("param", array.toJSONString());
		output = sendPost(getServiceController(), urlParameters.toJSONString());
		
		}catch(HexApplicationException Hea){
			return "failure";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return "failure";
		}
		return output;
	}

	public String update(Customer customer) throws HexApplicationException {

		System.out.println("inside update in Service ");
		try {
			JSONParser parser = new JSONParser();
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(customer);
			
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "POJO");
			urlParameters.put("service", "${service}");
			urlParameters.put("serviceMethod", "update");
			urlParameters.put("paramType", "com.app.customer.vo.Customer");
			urlParameters.put("param", jsonInString);
			${Validation_Call}
			output = sendPost(getServiceController(), urlParameters.toJSONString());
			
		} ${Validation_CatchEdit} 
		catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Failure:" + e.getMessage();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Failure:" + e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Failure:" + e.getMessage();
		}
		return "Success:" + output;

	}

	public String getAllCustomer() throws HexApplicationException {
		List customerList = null;
		${Cache_ImplStart}
			System.out.println("inside getAllCustomer in Service ");
			
			System.out.println("URL : " + serviceControllerURL);
			JSONObject urlParameters = new JSONObject();
			urlParameters.put("serviceType", "POJO");
			urlParameters.put("service",
					"${service}");
			urlParameters.put("serviceMethod", "getAllCustomer");
			urlParameters.put("paramType", "");
			urlParameters.put("param", "{}");

			System.out.println("URL Parameters : " + urlParameters);
			output = sendPost(getServiceController(),
					urlParameters.toJSONString());
			${Cache_ImplEnd}
		
		return output;

	}

	public String search(String searchValue, String searchColumn)
			throws HexApplicationException {

		System.out.println("Entering into Service implementation : "
				+ searchValue + "***" + searchColumn);
		
		JSONObject param = new JSONObject();
		param.put("searchValue", searchValue);
		param.put("searchColumn", searchColumn);
		JSONObject urlParameters = new JSONObject();
		urlParameters.put("serviceType", "POJO");
		urlParameters.put("service", "${service}");
		urlParameters.put("serviceMethod", "search");
		urlParameters.put("paramType", "String,String");
		urlParameters.put("param", param.toJSONString());
		String output = sendPost(getServiceController(), urlParameters.toJSONString());
		
		return output;

	}

	private String sendPost(String url, String urlParameters)
			throws HexApplicationException {

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Type", "application/json");
			

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			System.out.println("Parameters : " + urlParameters);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println("Business Delegate : " + response.toString());
			return response.toString();
		} catch (Exception e) {
			throw new HexApplicationException(e.getMessage());
		}

	}
	private String getServiceController(){
		String scheme = bundle.getString("scheme");
		String host = bundle.getString("host");
		String contextPath = "${projectName}";
		String serverHost = scheme + "://" + host + contextPath;
		serviceControllerURL = serverHost + "/serviceController/ws/service";
		return serviceControllerURL;
	}

	${Notification_method}

	${AuthenticationSPAMethod}
	

}
