<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Customer Application</title>
 <link href="/HJSFDemo/css/style.css" rel="stylesheet">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
</head>
<body>
	<div id="wrapper">
		<!-- Sidebar -->
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav">
				<li class="sidebar-brand"><a href="#"> <img
						src="/HJSFDemo/images/logo.png" />
				</a></li>
				<li><a href="/HJSFDemo/customerList"><img src="/HJSFDemo/images/product_list.png" />Customer
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
					<form:form action="/HJSFDemo/customerSearch" method="post">
						<input type="text" name="searchtxt" value="${searchTxt}">
						<select name="searchField" class="dropdown" >
							<c:forEach var="searchFieldOption" items="${searchFieldOptions}">
								<option value="${searchFieldOption}">${searchFieldOption}</option>
							</c:forEach>
						</select> 
						<input type="submit" class="btn btn-default" value="SEARCH"
							onclick="search();"> 
						<input type="reset"
							class="btn btn-default" value="Clear" onclick="clear();"><br />
						<br />
					</form:form>
					
					<form:form action="/HJSFDemo/customerDelete" method="post" modelAttribute="customerForm">
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
						<c:forEach var="customer" items="${customerForm.customers}" varStatus="status">
						<tr>
							<td><a href="/HJSFDemo/editCustomer?id=${customer.customerid}">${customer.customerid}</a>
							<form:hidden path="customers[${status.index}].customerid" value="${customer.customerid}" />
							</td>
							
							<td><label name="customers[${status.index}].name" > ${customer.name} </label></td>
							<td><label name="customers[${status.index}].email" > ${customer.email} </label></td>
							<td><label name="customers[${status.index}].phone">${customer.phone}</label></td>
							<td><label name="customers[${status.index}].address">${customer.address}</label></td>
							<td><label name="customers[${status.index}].orders" > ${customer.orders} </label></td>
							<td><label name="customers[${status.index}].action">${customer.action}</label></td>
							<td><form:checkbox path="customers[${status.index}].toBeDeleted" /></td>
							
						</tr>
						</c:forEach>
					</table>
					
					<input type="submit" class="btn btn-default" value="Delete" style="float: right"> 
					<input
						type="button" class="btn btn-default" value="Add"
						onclick="location.href='/HJSFDemo/showAddCustomer'" style="float: right">
					</form:form>
				</div>
			</div>


		</div>
	</div>
</body>
</html>