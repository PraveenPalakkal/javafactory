<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://displaytag.sf.net" prefix="disp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<h3>
	<title><s:text name="CUSTOMER_PAGE_TITLE" /></title>
</h3>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script>

	function confirmDelete() {
		return confirm(' do you want to delete the selected row?');
	}
	function validateSearch() {
		if (document.getElementById("searchfield").value.length == 0) {
			alert('Please enter the value');
			document.forms["customerForm"].elements["customerForm:searchfield"]
					.focus();
			return false;
		}

		if (document.getElementById("select").value == '--Select the field--') {
			alert('Please choose any one option');
			document.forms["customerForm"].elements["customerForm:select"]
					.focus();
			return false;
		}
		return true;
	}
	function validateClear() {

		document.forms["customerForm"].elements["customerForm:searchfield"].value = "";
		document.forms["customerForm"].elements["customerForm:select"].value = '--Select the field--';
		return true;
	}
	
	
	function passValueToAction(val)
	{
		//if($('input:checkbox[name=toBeDeleted]').is(':checked')){
		    $.ajax({
		      type : "post",
		      url  : "customerListUpdate",
		      dataType : 'text',
		      data: { 
		          "value": val
		      },
		      success : function(){
		        //$('statesdivid').html();
		      },
		      error : ""/* alert("No values found..!!") */
		    }); 
				
		//}
	}
</script>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
	crossorigin="anonymous">

</head>
<body>
	
	<div id="wrapper">
		<!-- Sidebar -->
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav">
				<li class="sidebar-brand"><a href="#"> <img
						src="<%=request.getContextPath()%>/images/logo.png" />
				</a></li>
				<li><a href="<s:url action="customerList"/>"><img
						src="<%=request.getContextPath()%>/images/product_list.png" />Customer List</a></li>

			</ul>
		</div>
		<!-- /#sidebar-wrapper -->

		<br /> <br />
		<div id="page-content-wrapper">
			<div class="container-fluid">

				<div>
					<div class="breadcrumb">
						<div class="breadcrumbtext">
							<s:text name="CUSTOMER_PAGE_TITLE" />-List
						</div>
					</div>
				 <form id="customerForm" action="clear" method="post">
					<s:textfield id="searchfield" name="fieldValue"></s:textfield>
					<select id="select" class="dropdown" name="columnValue">
						<option value="--Select the field--">--Select the field--</option>
						<s:iterator value="columnNameList">   			    
						    <option value="<s:property value="key" />"><s:property value="value"/></option>
						</s:iterator>
					</select>
					<s:submit value="SEARCH" action="customerSearch"
									 	 cssClass="btn btn-default" onclick="validateSearch();"></s:submit> 
					<s:submit value="Clear" onclick="validateClear();"
									 	 cssClass="btn btn-default" ></s:submit> <br />
					<br />
					<disp:table id="customer" name="customerList" class="table table-striped table-condensed table-bordered table-hover"
						pagesize="10" requestURI="">
						<disp:column title="Customer Id">
							<a href="customerSelect?id=<s:property value="#attr.customer.customerid"/>">
								<s:property value="#attr.customer.customerid"/>
							</a>
						</disp:column>
						<disp:column property="name" titleKey="CUSTOMER_NAME"></disp:column>
						<disp:column property="email" titleKey="CUSTOMER_EMAIL"></disp:column>
						<disp:column property="phone" titleKey="CUSTOMER_PHONE"></disp:column>
						<disp:column property="address" titleKey="CUSTOMER_ADDRESS"></disp:column>
						<disp:column property="orders" titleKey="CUSTOMER_ORDERS"></disp:column>
						<disp:column property="action" titleKey="CUSTOMER_ACTION"></disp:column>
						<disp:column title="Delete">							
							<s:checkbox name="toBeDeleted" id="chk" fieldValue="%{#attr.customer.chk}" 
								onclick="passValueToAction('%{#attr.customer.customerid}');"/>
						</disp:column>
						<disp:setProperty name="paging.banner.placement" value="bottom" />
					</disp:table>	
					<div style="margin-left:78%;margin-top: 3px;">	
						<s:submit value="%{getText('CUSTOMER_BUTTON_ADD')}" action="navigateToAdd"
							 	 cssClass="button" onclick="return validation();"></s:submit>
						<s:submit value="%{getText('CUSTOMER_BUTTON_DELETE')}" cssClass="button"
								 	  action="customerDelete"></s:submit>	
						
					 </div>				
				 </form> 
				</div>
			</div>


		</div>
	</div>
</body>
</html>