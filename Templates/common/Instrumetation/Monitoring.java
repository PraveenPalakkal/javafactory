package com.jsf.instrumentaion;



import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.jsf.instrumentation.Instrumentation;

@Aspect
public class Monitoring {
	
    
	@Around("execution(* com.app.*.*.*.*())")
	public Object employeeAroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
		Object value=null;
		try {
			String filePath="D://Monitor.txt";
		    	new Instrumentation().monitor(proceedingJoinPoint, filePath);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return value;
	}
}
