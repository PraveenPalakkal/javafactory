package com.app.framework.instrumentation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.hexaware.framework.config.ConfigHandler;
import com.jamonapi.MonitorFactory;

public class Monitor implements MethodInterceptor{
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		String fileMonitoringPath = ConfigHandler.getInstance().getSystemProperty("FileMonitoringPath");
		com.jamonapi.Monitor mon=null;

		 mon = MonitorFactory.start(methodInvocation.getMethod().getName());
		 

		try {
			// proceed to original method call
			Object result = methodInvocation.proceed();

			// same with AfterReturningAdvice
			mon.stop();
			String  monitor_Details =mon.toString();
			 File file =new File(fileMonitoringPath + "HJSF-Monitor.txt");

		    	
		    	if(!file.exists()){
		    	   file.createNewFile();
		    	}

		    	FileWriter fw = new FileWriter(file,true);
		    	BufferedWriter bw = new BufferedWriter(fw);
		    	bw.write(monitor_Details);
		    	bw.newLine();
		    	bw.close();

			return result;

		} catch (IllegalArgumentException e) {
			throw e;
		}

	}
}
