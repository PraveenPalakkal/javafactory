<!DOCTYPE html>
<html lang="en">


<head>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>

<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
<script src="js/ui-bootstrap-tpls-1.2.4.min.js"></script>


<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
	crossorigin="anonymous">
<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
	integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r"
	crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
	integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
	crossorigin="anonymous"></script>
<script src="js/script.js"></script>
<link rel="stylesheet" href="css/style.css" />
</head>

<body ng-app="customerApp">
	<div id="wrapper" ng-controller="globalController">
		<!-- Sidebar -->
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav">
				<li class="sidebar-brand"><a href="#"> <img
						src="images/logo.png" />
				</a></li>
				<li><a href="#" ng-click="showListPage();"><img
						src="images/product_list.png" />Customer List</a></li>

			</ul>
		</div>
		<!-- /#sidebar-wrapper -->







		<br />
		<br />
		<div id="page-content-wrapper">
			<div class="container-fluid">

				<div ng-controller="AddListController" ng-show="listPage">
					<div class="breadcrumb">
						<div class="breadcrumbtext">Customer-Add</div>
						<br>
					</div>

					<input type="text" ng-model="searchtxt" /> <select
						ng-model="searchField" class="dropdown">
						<option ng-repeat="option in availableOptions" value="{{option}}">{{option}}</option>
					</select> <input type="button" class="btn btn-default" value="SEARCH"
						ng-click="search();"> <input type="button"
						class="btn btn-default" value="Clear" ng-click="clear();"><br />
					<br />

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
						<tr ng-repeat="customer in filteredCustomers">
							<td><a href="" ng-click="edit(customer);">{{customer.customerid}}</a>
							</td>
							<td>{{customer.name}}</td>
							<td>{{customer.email}}</td>
							<td>{{customer.phone}}</td>
							<td>{{customer.address}}</td>
							<td>{{customer.orders}}</td>
							<td>{{customer.action}}</td>
							<td><input type="checkbox" ng-model="customer.delete" /></td>

						</tr>
					</table>
					<uib-pagination items-per-page="itemsPerPage"
						total-items="customers.length" ng-model="currentPage"
						ng-change="pageChanged();"></uib-pagination>
					<input type="button" class="btn btn-default" value="Email"
						ng-click="showEmailPage();" style="float: right"
						ng-show="showListEmailButton"> <input type="button"
						class="btn btn-default" value="Delete"
						ng-click="deleteCustomers();" style="float: right"> <input
						type="button" class="btn btn-default" value="Add"
						ng-click="showAddPage();" style="float: right">

				</div>
			</div>
			<div ng-controller="addController" class="rightcolumn"
				ng-show="addPage">
				<div class="breadcrumb">
					<div class="breadcrumbtext">Customer-Add</div>
					<br>
				</div>
				<table
					class="table table-striped table-condensed table-bordered table-hover">

					<tr>
						<td>Customer Id</td>
						<td><input type="text" ng-model="customer.customerid"
							ng-readonly="!showAddButton" /></td>
					</tr>
					<tr>
						<td>Name</td>
						<td><input type="text" ng-model="customer.name" /></td>
					</tr>
					<tr>
						<td>Email</td>
						<td><input type="text" ng-model="customer.email" /></td>
					</tr>
					<tr>
						<td>Phone</td>
						<td><input type="text" ng-model="customer.phone" /></td>
					</tr>
					<tr>
						<td>Address</td>
						<td><input type="text" ng-model="customer.address" /></td>
					</tr>
					<tr>
						<td>Orders</td>
						<td><input type="text" ng-model="customer.orders" /></td>
					</tr>
					<tr>
						<td>Action</td>
						<td><input type="text" ng-model="customer.action" /></td>
					</tr>

				</table>

				<input type="button" class="btn btn-default" value="List"
					ng-click="list();" style="float: right"> <input
					type="button" class="btn btn-default" value="Add" ng-click="add();"
					style="float: right" ng-show="showAddButton"> <input
					type="button" class="btn btn-default" value="Update"
					ng-click="update();" style="float: right" ng-hide="showAddButton">

			</div>
			<!-- Email div -->
			${EmailHtml}

			${LoginHtml}
		</div>
	</div>




</body>

</html>