<servlet>
  <servlet-name>${projectName}</servlet-name>
  <servlet-class>
     org.springframework.web.servlet.DispatcherServlet
  </servlet-class>
  <init-param>
     <param-name>contextConfigLocation</param-name> 
	 <param-value>/WEB-INF/${projectName}-servlet.xml</param-value>
  </init-param>
  <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
  <servlet-name>${projectName}</servlet-name>
  <url-pattern>/auth/*</url-pattern>
</servlet-mapping>