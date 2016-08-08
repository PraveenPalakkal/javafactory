<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Customer Application</title>
 <link href="/${projectName}/css/style.css" rel="stylesheet">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
</head>
<body>
	<div id="wrapper">
		<!-- Sidebar -->
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav">
				<li class="sidebar-brand"><a href="#"> <img
						src="/${projectName}/images/logo.png" />
				</a></li>
				<li><a href="/${projectName}/customerList"><img src="/${projectName}/images/product_list.png" />Customer
						List</a></li>

			</ul>
		</div>
		<!-- /#sidebar-wrapper -->

		<br />
		<br />
		<div id="page-content-wrapper">
			<div class="container-fluid">

				<div>
					<div class="breadcrumb">
						<div class="breadcrumbtext">Customer-Add</div>
						<br>
					</div>
					<form:form action="/${projectName}/auth/customerSearch" method="post">
						<input type="text" name="searchtxt" value="${r"${searchTxt}"}">
						<select name="searchField" class="dropdown" >
							<c:forEach var="searchFieldOption" items="${r"${searchFieldOptions}"}">
								<option value="${r"${searchFieldOption}"}">${r"${searchFieldOption}"}</option>
							</c:forEach>
						</select> 
						<input type="submit" class="btn btn-default" value="SEARCH"> 
						<input type="reset"
							class="btn btn-default" value="Clear" onclick="location.href='/${projectName}/auth/customerList'"><br />
						<br />
					</form:form>
					
					<form:form action="/${projectName}/auth/customerDelete" method="post" modelAttribute="customerForm">
					<table
						class="table table-striped table-condensed table-bordered table-hover">
						<tr>
							<th>CustomerId</th>
							<th>Name</th>
							<th>Email</th>
							<th>Phone</th>
							<th>Address</th>
							<th>Orders</th>
							<th>Action</th>
							<th>Delete</th>
						<tr>
						<c:forEach var="customer" items="${r"${customerForm.customers}"}" varStatus="status">
						<tr>
							<td><a href="/${projectName}/auth/editCustomer?id=${r"${customer.customerid}"}">${r"${customer.customerid}"}</a>
							<form:hidden path="customers[${r"${status.index}"}].customerid" value="${r"${customer.customerid}"}" />
							</td>
							
							<td><label name="customers[${r"${status.index}"}].name" > ${r"${customer.name} "}</label></td>
							<td><label name="customers[${r"${status.index}"}].email" > ${r"${customer.email} "}</label></td>
							<td><label name="customers[${r"${status.index}"}].phone">${r"${customer.phone}"}</label></td>
							<td><label name="customers[${r"${status.index}"}].address">${r"${customer.address}"}</label></td>
							<td><label name="customers[${r"${status.index}"}].orders" > ${r"${customer.orders}"} </label></td>
							<td><label name="customers[${r"${status.index}"}].action">${r"${customer.action}"}</label></td>
							<td><form:checkbox path="customers[${r"${status.index}"}].toBeDeleted" /></td>
							
						</tr>
						</c:forEach>
					</table>
					
					<input type="submit" class="btn btn-default" value="Delete" style="float: right"> 
					<input
						type="button" class="btn btn-default" value="Add"
						onclick="location.href='/${projectName}/auth/showAddCustomer'" style="float: right">
					</form:form>
${Email_button}
				</div>
			</div>


		</div>
	</div>
</body>
</html>