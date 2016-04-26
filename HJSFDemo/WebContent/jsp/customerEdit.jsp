<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
   <head>
   <title>Hello Spring MVC</title>
    <link href="/HJSFDemo/css/style.css" rel="stylesheet">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
   </head>
   <body>
 <div id="wrapper">
 <!-- Sidebar -->
        <div id="sidebar-wrapper">
            <ul class="sidebar-nav">
                <li class="sidebar-brand">
                    <a href="#">
                       <img src="/HJSFDemo/images/logo.png"/>
                    </a>
                </li>
                <li>
                    <a href="/HJSFDemo/customerList"><img src="/HJSFDemo/images/product_list.png"/>Customer List</a>
                </li>
                
            </ul>
        </div>
        <!-- /#sidebar-wrapper -->







<br/><br/>
<div id="page-content-wrapper">
<div class="container-fluid">
</div>
<div  class="rightcolumn">
	<div class="breadcrumb">
						<div class="breadcrumbtext">
							Customer-Edit
						</div>
						<br>
	</div>
	<form:form action="/HJSFDemo/customerUpdate" method="post" modelAttribute="customerBean">			
	<table class="table table-striped table-condensed table-bordered table-hover">
	
		<tr>
			<td>
				Customer Id 
			</td>
			<td>
					<form:input id="customerid" name="customerid" path="customerid" readonly="true"/><br>
			</td>
		</tr>
		<tr>		
			<td>
				Name
			</td>
			<td>
				<form:input id="name" name="name" path="name" /><br>
			</td>
		</tr>
		<tr>	
			<td>
				Email
			</td>
			<td>
				<form:input id="email" name="email" path="email" /><br>
			</td>
		</tr>
		<tr>	
			<td>
				Phone
			</td>
			<td>
				<form:input id="phone" name="phone" path="phone" /><br>
			</td>
		</tr>
		<tr>	
			<td>
				Address 
			</td>
			<td>
				<form:input id="address" name="address" path="address" /><br>
			</td>
		</tr>
		<tr>	
			<td>
				Orders
			</td>
			<td>
				<form:input id="orders" name="orders" path="orders" /><br>
			</td>
		</tr>
		<tr>	
			<td>
				Action
			</td>
			<td>
				<form:input id="action" name="action" path="action" /><br>
			</td>
		</tr>
		
		<tr>	
			<td>
				CreatedBy
			</td>
			<td>
				<form:input id="createdby" name="createdby" path="createdby" /><br>
			</td>
		</tr>
		<tr>	
			<td>
				Created Date
			</td>
			<td>
				<form:input id="createddate" name="createddate" path="createddate" /><br>
			</td>
		</tr>
		
		<tr>	
			<td>
				ModifiedBy
			</td>
			<td>
				<form:input id="modifiedby" name="modifiedby" path="modifiedby" /><br>
			</td>
		</tr>
		
		<tr>	
			<td>
				Modified Date
			</td>
			<td>
				<form:input id="modifieddate" name="modifieddate" path="modifieddate" /><br>
			</td>
		</tr>
	</table>
	
	<input type="button" class="btn btn-default" value="List" onclick="location.href='/HJSFDemo/customerList'" style="float:right" >
	<input type="submit" class="btn btn-default" value="Update" style="float:right;" >
	</form:form>
</div>

</div>
</div>
</body>
</html>