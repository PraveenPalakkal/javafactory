package com.app.customer.util;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;





import org.apache.log4j.Logger;

import com.app.customer.util.GlobalConstants;
/*import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
*/


public class JndiLookup {
	//static Logger log = LogFactory.getLogger(JndiLookup.class);
	static Logger log = Logger.getLogger(JndiLookup.class);
	
	public static Object doLookUp(String beanName) {	
		log.debug("Inside Method doLookUp(beanName).."+beanName);
		Context context = null;
		Object bean = null;
		try {
			Properties properties = new Properties();
			properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			context = new InitialContext(properties);
			String LookUpName = getLookUpName(beanName);
			bean = context.lookup(LookUpName);
			context.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}log.debug("End of Method doLookUp(beanName).."+beanName);
		return bean;
	}

	private static String getLookUpName(String bean) {
		log.debug("Inside Method getLookUpName(bean).."+bean);
		String appName = GlobalConstants.APPNAME;
		String moduleName = GlobalConstants.MODULE;
		String distinctName = "";
		String beanName = GlobalConstants.EJBBEAN;
		final String interfaceName = GlobalConstants.INTERFACE+bean;
		String name = "ejb:" + appName + "/" + moduleName + "/" + distinctName
				+ "/" + beanName + "!" + interfaceName;
		log.debug("End of Method getLookUpName(bean).."+name);
		return name;
	}
	
}
