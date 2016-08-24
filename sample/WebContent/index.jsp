<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/Login_style.css" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<body >

<div class="login-page">
  <div class="form">
    <form action="login" method="post">
      <input type="text" placeholder="username" name="username"/>
      <input type="password" placeholder="password" name="password"/>
      <input type="submit" name="submit" value="Login" align="middle" style="background: #1ba8b3; color: #FFFFFF;">
    </form>
  </div>
</div>



</body>
</html>
