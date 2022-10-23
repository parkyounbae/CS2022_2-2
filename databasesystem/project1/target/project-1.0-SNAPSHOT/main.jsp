<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.PrintWriter" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8">
    <title>메인페이지</title>
</head>
<body>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null)
		{
			userID = (String) session.getAttribute("userID");
		}
	%>
	<h3 style="test-align: center;">메인페이지</h3>
	<%
		if(userID == null)
		{
	%>
		<button type="button" class="navyBtn" onClick="location.href='login.jsp'">로그인</button>
		<button type="button" class="navyBtn" onClick="location.href='join.jsp'">회원가입</button>
	
	<%		
		}
		else
		{
	%>
		<button type="button" class="navyBtn" onClick="location.href='logoutAction.jsp'">로그아웃</button>
		<button type="button" class="navyBtn" onClick="location.href='showAll.jsp'">모든 사용자 정보</button>
	
	<%		
		}
	%>
</body>
</html>