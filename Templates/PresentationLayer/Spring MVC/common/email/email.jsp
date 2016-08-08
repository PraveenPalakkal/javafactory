<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
  	<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/Login_style.css" />
	<div class="Email-page">
		<div class="Email">
			<form id="Emailform" method="POST" action="sendEmail"
				accept-charset="UTF-8">
				<table>
					<tr>
						<td><label>From<strong>*:</strong></label></td>
						<td><input type="email" autofocus required
							title="Please enter a valid email address" size="48" name="name"
							id="fromId" value="" placeholder="Email@hexaware.com"></td>
					</tr>
					<tr>
						<td><label>To<strong>*:</strong></label></td>
						<td><input type="text" size="48" required name="toemail"
							id="toId" value="" placeholder="Email@hexaware.com"></td>
					</tr>
					<tr>
						<td><label>Cc..<strong>:</strong></label></td>
						<td><input type="text" size="48" name="ccemail" value=""
							id="ccemail" placeholder="Email@hexaware.com"></td>
					</tr>
					<tr>
						<td><label>Bc<strong>:</strong></label></td>
						<td><input type="text" size="48" name="bcemail" value=""
							id="bcemail" placeholder="Email@hexaware.com"></td>
					</tr>
					<tr>
						<td><label>Subject :</label></td>
						<td><input type="text" size="48" name="subject" value=""
							id="subId" placeholder="Subject"></td>
					</tr>
					<tr>
						<td><label>Body<strong>*</strong></label></td>
						<td><textarea required name="message" cols="50" rows="5"
								id="mesId" placeholder="Message"></textarea></td>
					</tr>
					<tr>
						<td></td>
						<td>
							<button type="submit">Send Mail</button>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
  </head>
</html>