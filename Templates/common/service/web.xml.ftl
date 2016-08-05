<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" id="WebApp_ID" version="3.0">
	<display-name>Customer Application</display-name>
	<welcome-file-list>
		<welcome-file>${welcomeFile}</welcome-file>
	</welcome-file-list>

	${restServiceConfiguration}
	

	${soapServiceConfiguration}

	
	<session-config>
		<session-timeout>120</session-timeout>
	 </session-config>
	
</web-app>
	
