<div ng-controller="emailController" class="rightcolumn"
				ng-show="emailPage">
				<div class="breadcrumb">
					<div class="breadcrumbtext">Customer-email</div>
					<br>
				</div>
				<table
					class="table table-striped table-condensed table-bordered table-hover">

					<tr>
						<td>From</td>
						<td><input type="text" ng-model="email.from" /></td>
					</tr>
					<tr>
						<td>To</td>
						<td><input type="text" ng-model="email.to" /></td>
					</tr>
					<tr>
						<td>Cc</td>
						<td><input type="text" ng-model="email.cc" /></td>
					</tr>
					<tr>
						<td>Bcc</td>
						<td><input type="text" ng-model="email.bc" /></td>
					</tr>
					<tr>
						<td>Subject</td>
						<td><input type="text" ng-model="email.sub" /></td>
					</tr>
					<tr>
						<td>Body</td>
						<td><TEXTAREA NAME="comments" COLS=30 ROWS=4
								ng-model="email.meg"></TEXTAREA> <!-- <input type="textarea" ng-model="email.meg" rows="5" cols="10"/> -->
						</td>
					</tr>

				</table>

				<input type="button" class="btn btn-default" value="List"
					ng-click="list();" style="float: right"> <input
					type="button" class="btn btn-default" value="sendEmail"
					ng-click="sendEmail();" style="float: right"
					ng-show="showEmailButton">

			</div>