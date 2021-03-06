package com.app.framework.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.app.framework.exception.HexApplicationException;
import com.app.framework.util.HJSFParam;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;

@Path(value = "ws")
public class HJSFServiceController {

	Logger log = LogFactory.getLogger(HJSFServiceController.class);

	@POST
	@Path(value = "/service")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String service(@Context HttpServletRequest request, HJSFParam param) {
		log.info("Service Controller Starts...");
		log.debug("Param " + param);
		log.debug("ServiceType : " + param.getServiceType());
		String returnValue = null;
		if (param.getServiceType().equalsIgnoreCase("POJO") || param.getServiceType().equalsIgnoreCase("SOAP")) {
			log.debug("***** POJO or SOAP Service *****");
			String service = param.getService();
			String serviceMethod = param.getServiceMethod();
			String paramTypes = param.getParamType();
			String inputParam = param.getParam();

			log.debug("Input Param : " + inputParam);
			try {
				String[] paramTypeArray = paramTypes.split(",");
				JSONParser parser = new JSONParser();

				/*
				 * Creating Class[] array to call respective method using
				 * reflection.
				 */
				Class[] inputClasses = null;
				if (paramTypeArray.length > 1 && !paramTypeArray[0].equals("")) {
					inputClasses = new Class[paramTypeArray.length];
				}

				log.debug("Param Length : " + paramTypeArray.length);
				boolean customClass = false;
				Object[] methodInputValues = null;
				if (!paramTypeArray[0].equals(""))
					for (int i = 0; i < paramTypeArray.length; i++) {
						if (paramTypeArray[i].equalsIgnoreCase("String")) {
							inputClasses[i] = String.class;
						} else if (paramTypeArray[i].equalsIgnoreCase("Integer")
								|| paramTypeArray[i].equalsIgnoreCase("int")) {
							inputClasses[i] = int.class;
						} else if (paramTypeArray[i].equalsIgnoreCase("Boolean")
								|| paramTypeArray[i].equalsIgnoreCase("boolean")) {
							inputClasses[i] = boolean.class;
						} else if (paramTypeArray[i].equalsIgnoreCase("Character")
								|| paramTypeArray[i].equalsIgnoreCase("char")) {
							inputClasses[i] = char.class;
						} else if (paramTypeArray[i].equalsIgnoreCase("Byte")
								|| paramTypeArray[i].equalsIgnoreCase("byte")) {
							inputClasses[i] = byte.class;
						} else if (paramTypeArray[i].equalsIgnoreCase("Float")
								|| paramTypeArray[i].equalsIgnoreCase("float")) {
							inputClasses[i] = float.class;
						} else if (paramTypeArray[i].equalsIgnoreCase("Double")
								|| paramTypeArray[i].equalsIgnoreCase("double")) {
							inputClasses[i] = double.class;
						} else if (paramTypeArray[i].equalsIgnoreCase("Short")
								|| paramTypeArray[i].equalsIgnoreCase("short")) {
							inputClasses[i] = short.class;
						} else if (paramTypeArray[i].equalsIgnoreCase("Long")
								|| paramTypeArray[i].equalsIgnoreCase("long")) {
							inputClasses[i] = long.class;
						} else if (paramTypeArray[i].contains("<") && paramTypeArray[i].contains(">")) {
							String actualString = paramTypeArray[i];
							String collectionString = actualString.substring(0, actualString.indexOf("<"));
							String containsString = actualString.substring(actualString.indexOf("<") + 1,
									actualString.lastIndexOf(">"));

							log.debug("Collection : " + collectionString);
							log.debug("Collection Object : " + containsString);

							customClass = true;
							methodInputValues = new Object[1];
							inputClasses = new Class[1];
							JSONArray jsonInputArray = (JSONArray) parser.parse(inputParam);
							inputClasses[i] = Class.forName(collectionString);
							ObjectMapper mapper = new ObjectMapper();
							log.debug(jsonInputArray.toJSONString());
							log.debug(inputClasses[i].toString());
							Object iParam = mapper.readValue(jsonInputArray.toJSONString(), inputClasses[i]);
							methodInputValues[i] = convertJSONToCollection(collectionString, containsString,
									jsonInputArray);

						} else {
							customClass = true;
							methodInputValues = new Object[1];
							inputClasses = new Class[1];
							JSONObject jsonInputParam = (JSONObject) parser.parse(inputParam);
							inputClasses[i] = Class.forName(paramTypeArray[i]);
							ObjectMapper mapper = new ObjectMapper();
							log.debug(jsonInputParam.toJSONString());
							log.debug(inputClasses[i].toString());
							jsonInputParam.remove("$$hashKey");
							Object iParam = mapper.readValue(jsonInputParam.toJSONString(), inputClasses[i]);
							methodInputValues[i] = iParam;
						}

					}

				Class serviceClass = Class.forName(service);
				Constructor constructor = serviceClass.getConstructor();
				Object myObject = constructor.newInstance();
				Method method = serviceClass.getMethod(serviceMethod, inputClasses);

				/*
				 * Getting Methods parameter names
				 */
				Paranamer paranamer = new BytecodeReadingParanamer();
				String[] methodParameterNames = paranamer.lookupParameterNames(method, false);

				/*
				 * Creating input param values
				 */
				if (!customClass) {
					methodInputValues = new Object[methodParameterNames.length];
					for (int i = 0; i < methodParameterNames.length; i++) {
						JSONObject jsonInputParam = (JSONObject) parser.parse(inputParam);
						methodInputValues[i] = jsonInputParam.get(methodParameterNames[i]);
					}
				}
				Object methodReturnValue = method.invoke(myObject, methodInputValues);
				returnValue = methodReturnValue.toString();
				log.info("Output from Service : " + methodReturnValue);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (param.getServiceType().equalsIgnoreCase("REST")) {
			log.debug("*****Rest Service*****");
			try {
				if (param.getServiceMethod().equalsIgnoreCase("GET")) {
					log.debug("***** GET Method *****");
					String queryParam = "";
					String url = param.getService();
					JSONParser parser = new JSONParser();
					JSONObject jsonParam = (JSONObject) parser.parse(param.getParam());
					log.debug("Param : " + jsonParam);
					Iterator iterator = jsonParam.keySet().iterator();
					while(iterator.hasNext()){
						Object key = iterator.next();
						Object value = jsonParam.get(key);
						queryParam += key + "=" + value + "&";
					}
					if(url.contains("?") && !(url.lastIndexOf("?") == url.length())){
						url = url + "&";
					}
					else if(!(url.lastIndexOf("?") == url.length())){
						url = url + "?";
					}
					url = url + queryParam;
					returnValue = sendGet(request, url);
				} else if (param.getServiceMethod().equalsIgnoreCase("POST")) {
					returnValue = sendPost(request, param.getService(), param.getParam());
				}
			} catch (HexApplicationException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		log.info("Service Controller Ends...");
		return returnValue;
	}

	private Collection convertJSONToCollection(String collectionClass, String collectionObject, JSONArray jsonArray)
			throws JsonParseException, JsonMappingException, ClassNotFoundException, IOException {
		if (collectionClass.equals("java.util.List")) {
			List list = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();
			ListIterator iterator = jsonArray.listIterator();
			while (iterator.hasNext()) {
				JSONObject jsonObject = (JSONObject) iterator.next();
				jsonObject.remove("$$hashKey");
				Object iParam = mapper.readValue(jsonObject.toString(), Class.forName(collectionObject));
				list.add(iParam);
			}
			
			return list;
		} else {
			return null;
		}
	}

	// HTTP GET request
	private String sendGet(HttpServletRequest request, String url) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			con.setRequestProperty(key, request.getHeader(key));
		}

		int responseCode = con.getResponseCode();
		log.debug("\nSending 'GET' request to URL : " + url);
		log.debug("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		log.debug(response.toString());
		return response.toString();

	}

	// HTTP POST request
	private String sendPost(HttpServletRequest request, String url, String urlParameters)
			throws HexApplicationException {

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Type", "application/json");
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = headerNames.nextElement();
				con.setRequestProperty(key, request.getHeader(key));
			}

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
}
