<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<link type="text/css" rel="stylesheet" href="css/style.css">
<style>
body {
  margin: 0;
  font-family: Arial, Helvetica, sans-serif;
}

.topnav {
  overflow: hidden;
}

.topnav a {
  float: left;
  color: #00000;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 17px;
}

.topnav a:hover {
  background-color: #ddd;
  color: black;
}

.topnav a.active {
  background-color: #4CAF50;
  color: white;
}

</style>

	<%
		// This scriplet function will validate the user logged in or not
		String userId = null;
	    String user_type = null;
	    String username = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user_id"))
					userId = cookie.getValue();
				if(cookie.getName().equals("user_type"))
					user_type = cookie.getValue();
				if(cookie.getName().equals("username"))
					username = cookie.getValue();
			}
		}
		if (userId == null)
			response.sendRedirect("userAccountControllerServlet");
	%>

	<div class="topnav">
		<div id="header">
			<h2 style="text-align:center; background-color: #333;" >XYZ University</h2>
		</div>
	</div>

	<div>
		<div class="topnav">
			<a href="studentControllerServlet?command=PROFILE&student_id=<%=userId%>">My Profile</a> 
			<a href="propertyController">Properties </a> 
			<a href="viewRequestController?command=MYLIST&student_id=<%=userId%>">My View Requests </a> 
			<a href="userAccountControllerServlet?command=CHANGE">Change Password</a>
			<a href="userAccountControllerServlet">Logout</a>
		</div>
	</div>
	
	