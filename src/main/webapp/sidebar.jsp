<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<%

	String pageName = (String) request.getAttribute("page");
	if (pageName == null) {
		pageName = "Home";
	}
	%>
	<div class="sidebar">
		<div class="menu-toggle" id="menu-toggle"><%=pageName %></div>
		<nav class="menu">
			<ul>
				<li><a href="${pageContext.request.contextPath}/"><i class="fas fa-home"></i> Trang
						chủ</a></li>
				<li><a href="studio"><i class="fas fa-video"></i> Studio</a></li>
			</ul>
		</nav>
	</div>
</body>
</html>