package com.app.customer.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;



public class HexHelper {
	static Logger log = LogFactory.getLogger(HexHelper.class);
	public static DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	public static String regex = "[0-9]+";
	
	public static String createCustomerQuerySearchCriteria(String searchValue, String columnValue){
		String query = "select c from  Customer c where c.";
		log.debug("Inside Method createQuerySearchCriteria(query, searchValue, columnValue) .."+query+", "+searchValue+", "+columnValue);
		if(columnValue.equalsIgnoreCase("Customerid")){
			query = query + "customerid like '%"+searchValue+"%'";
		}else if(columnValue.equalsIgnoreCase("Name")){
			query = query + columnValue+" like '%"+searchValue+"%'";
		}else if(columnValue.equalsIgnoreCase("Email")){
			query = query + columnValue+" like '%"+searchValue+"%'";
		}else if(columnValue.equalsIgnoreCase("Phone")){
			query = query + columnValue+" like '%"+searchValue+"%'";
		}else if(columnValue.equalsIgnoreCase("Address")){
			query = query + columnValue+" like '%"+searchValue+"%'";
		}else if(columnValue.equalsIgnoreCase("Orders")){
			query = query + columnValue+" like '%"+searchValue+"%'";
		}else if(columnValue.equalsIgnoreCase("Action")){
			query = query + columnValue+" like '%"+searchValue+"%'";
		}else if(columnValue.equalsIgnoreCase("createdDate")){
			query = query + "createddate='"+searchValue+"'";
		}else if(columnValue.equalsIgnoreCase("createdBy")){
			query = query + "createdby like '%"+searchValue+"%'";
		}else if(columnValue.equalsIgnoreCase("modifiedDate")){
			query = query + "modifieddate='"+searchValue+"'";
		}else if(columnValue.equalsIgnoreCase("modifiedBy")){
			query = query + "modifiedby like '%"+searchValue+"%'";
		}log.debug("End of createQuerySearchCriteria(query, searchValue, columnValue) .."+query);
		return query;
	}
	
}