<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<h3>
	<title><s:text name="CUSTOMER_PAGE_TITLE"/></title>
</h3>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
	crossorigin="anonymous">
	
<script>	
		function validation()
		{
			
			 if ( document.getElementById("customerid").value.length == 0 )
			{
				alert('Please enter the value of CustomerId');
				document.getElementById("customerid").focus();
				return false;
			}
			return true; 
		}
	</script>	
</head>
<body>
<div id="wrapper">
		<!-- Sidebar -->
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav">
				<li class="sidebar-brand"><a href="#"> <img
						src="<%=request.getContextPath()%>/images/logo.png" />
				</a></li>
				<li ><a href="<s:url action="customerList"/>"><img
						src="<%=request.getContextPath()%>/images/product_list.png" />Customer List</a></li>

			</ul>
		</div>
		<!-- /#sidebar-wrapper -->







		<br />
		<br />
		<div id="page-content-wrapper">
			<div class="container-fluid">

				<div>
					<div class="breadcrumb">
						<div class="breadcrumbtext">
							<s:text name="CUSTOMER_ADD_PAGE_TITLE"/>
						</div>
					</div>
					<form id="customerForm" action="customerAdd" method="post">
						<!-- <p:panel> -->
							<table class="table table-striped table-condensed table-bordered table-hover" style="width: 90%;">
								
								<tr>
									<td>
										<s:text name="CUSTOMER_CUSTOMERID"/><label style="color: red;">*</label>
									</td>
									<td>
										<s:textfield id="customerid" name="customer.customerid" />
									</td>

								</tr>
								<tr>
									<td>
										<s:text name="CUSTOMER_NAME"/>
									</td>
									<td>
										<s:textfield name="customer.name" />
									</td>

								</tr>

								<tr>
									<td>
										<s:text name="CUSTOMER_EMAIL"/>
									</td>
									<td>
										<s:textfield name="customer.email" />
									</td>

								</tr>

								<tr>
									<td>
										<s:text name="CUSTOMER_PHONE"/>
									</td>
									<td>
										<s:textfield name="customer.phone"  />
									</td>

								</tr>
								<tr>
									<td>
										<s:text name="CUSTOMER_ADDRESS"/>
									</td>
									<td>
										<s:textfield name="customer.address" label="%{getText('CUSTOMER_ADDRESS')}" />
									</td>

								</tr>
								<tr>
									<td>
										<s:text name="CUSTOMER_ORDERS"/>
									</td>
									<td>
										<s:textfield name="customer.orders" />
									</td>

								</tr>
								<tr>
									<td>
										<s:text name="CUSTOMER_ACTION"/>
									</td>
									<td>
										<s:textfield name="customer.action" />
									</td>

								</tr>
							
							</table>
							<div style="margin-left:78%;margin-top: 3px;">	
								<s:submit value="%{getText('CUSTOMER_BUTTON_LIST')}" action="customerList"
									 	 cssClass="button" ></s:submit>
								<s:submit value="%{getText('CUSTOMER_BUTTON_ADD')}" cssClass="button"
										 	  onclick="return validation();"></s:submit>	
								
							</div>
						<!-- </p:panel> -->
					</form>
				</div>
			</div>
		</div>
	</div>				

</body>
</html>