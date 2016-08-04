package com.app.customer.util;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;


public class GlobalConstants {
	static Logger log = LogFactory.getLogger(GlobalConstants.class);
	public static final String APPNAME = "${APP_NAME}";
	public static final String MODULE = APPNAME+"EJB";
	public static final String EJBBEAN = "CustomerBean";
	public static final String INTERFACE = "com.app.customer.service.I";
	public static final String entityName = "Customer";
	public static final String PERSISTENCE_UNIT = "MyDb";
}
