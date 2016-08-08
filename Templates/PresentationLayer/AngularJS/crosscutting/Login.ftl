<div ng-controller="loginController" class="rightcolumn"
				ng-show="loginPage">
				<div class="breadcrumb">
					<div class="breadcrumbtext">Customer-Login</div>
					<br>
				</div>
				<table
					class="table table-striped table-condensed table-bordered table-hover">

					<tr>
						<td>Username</td>
						<td><input type="text" ng-model="login.name" /></td>
					</tr>
					<tr>
						<td>Password</td>
						<td><input type="password" ng-model="login.password" /></td>
					</tr>

				</table>

				<input type="button" class="btn btn-default" value="Login"
					ng-click="checkLogin();" style="float: right">

			</div>