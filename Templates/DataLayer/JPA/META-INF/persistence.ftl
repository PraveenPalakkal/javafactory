<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
         version="2.0">
	<persistence-unit name="MyDb" transaction-type="RESOURCE_LOCAL">
		<provider>org.hibernate.ejb.HibernatePersistence</provider>
		${JTA_Datasource}
		<class>com.app.customer.vo.Customer</class>
		<properties>
		<property name="hibernate.connection.driver_class" value="${Driver}"/>
		 
		<property name="hibernate.connection.url" value="${url}"/>
		 
		<property name="hibernate.cache.provider_class" value="org.hibernate.cache.NoCacheProvider"/>
		 
		<property name="hibernate.hbm2ddl.auto" value="validate"/>
		 
		<property name="hibernate.connection.username" value="${userName}"/>
		 
		<property name="hibernate.connection.password" value="${password}"/>
		
		<property name="hibernate.dialect" value="${Dialect}" />
		 
		</properties>
	</persistence-unit>
</persistence> 
