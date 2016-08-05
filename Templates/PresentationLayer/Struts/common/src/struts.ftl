<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts PUBLIC
   "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN"
   "http://struts.apache.org/dtds/struts-2.0.dtd">
<struts>
<constant name="struts.devMode" value="false" />
<constant name="struts.custom.i18n.resources" value="ApplicationResources" />
<constant name="struts.enable.DynamicMethodInvocation" value="true" />
${Constant_Rest}
<constant name="struts.ui.theme" value="simple" />
   <package name="Customer" namespace="/" extends="struts-default">
      <action name="customerAdd" 
            class="com.app.customer.mbean.CustomerBean" 
            method="createNewRecord">
            <result name="customerList">/pages/CustomerList.jsp</result>
            <result name="customerAdd">/pages/CustomerAdd.jsp</result>
      </action>
      <action name="customerList" 
            class="com.app.customer.mbean.CustomerBean" 
            method="getList">
            <result name="customerList">/pages/CustomerList.jsp</result>
      </action>
      <action name="customerEditList" 
            class="com.app.customer.mbean.CustomerBean" 
            method="getList">
            <result name="customerList">/pages/CustomerList.jsp</result>
      </action>
      <action name="customerEdit" 
            class="com.app.customer.mbean.CustomerBean" 
            method="updateRecord">
            <result name="customerList">/pages/CustomerList.jsp</result>
            <result name="customerEdit">/pages/CustomerEdit.jsp</result>
      </action> 
      <action name="customerSelect" 
            class="com.app.customer.mbean.CustomerBean" 
            method="select">
            <result name="customerEdit">/pages/CustomerEdit.jsp</result>
      </action>
      <action name="customerSearch" 
            class="com.app.customer.mbean.CustomerBean" 
            method="search">
            <result name="customerList">/pages/CustomerList.jsp</result>
      </action>
      <action name="clear" 
            class="com.app.customer.mbean.CustomerBean" 
            method="clear">
            <result name="customerList">/pages/CustomerList.jsp</result>
      </action>
      <action name="customerDelete" 
            class="com.app.customer.mbean.CustomerBean" 
            method="delete">
            <result name="customerList">/pages/CustomerList.jsp</result>
      </action>
      <action name="customerListUpdate" 
            class="com.app.customer.mbean.CustomerBean" 
            method="updateCustomerList">          
      </action>
      <action name="navigateToAdd" 
            class="com.app.customer.mbean.CustomerBean" 
            method="add"> 
            <result name="customerAdd">/pages/CustomerAdd.jsp</result>
 </action> 
${Email_Action}          
     
   </package>
</struts>