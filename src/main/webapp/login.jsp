<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="model.dto.UserDTO"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Đăng nhập</title>
<link rel="icon" type="image/x-icon"
	href="<%=request.getContextPath()%>/favicon.ico">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
	<%
String username = request.getParameter("username");
String password = request.getParameter("password");
String reg_username = request.getParameter("reg-username");
String reg_name = request.getParameter("reg-name");
String reg_password = request.getParameter("reg-password");
String reg_confirm_password = request.getParameter("reg-confirm-password");
%>

	<header>
		<h1
			style="font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif; text-align: center;">MVK</h1>
	</header>

	<div class="form-container">
		<!-- Form Đăng nhập -->
		<form id="login-form"
			class="auth-form <%=reg_username == null ? "active" : ""%>"
			action="login" method="post">
			<h2>Đăng nhập</h2>
			<div class="form-group">
				<label for="username">Tài khoản</label> <input type="text"
					name="username" id="username" placeholder="Nhập tài khoản" required
					value=<%=username != null ? username : ""%>>
			</div>
			<div class="form-group">
				<label for="password">Mật khẩu</label> <input type="password"
					name="password" id="password" placeholder="Nhập mật khẩu" required
					value=<%=password != null ? password : ""%>>
			</div>
			<button type="submit" class="form-btn">Đăng nhập</button>
			<%
			if (username != null && !username.isBlank()) {
			%>
			<p style="color: red; font-weight: bold; text-align: center;">Sai
				thông tin đăng nhập</p>
			<%
			}
			%>
			<p class="toggle-form">
				Chưa có tài khoản? <span id="switch-to-register">Đăng ký</span>
			</p>
		</form>

		<!-- Form Đăng ký -->
		<form id="register-form"
			class="auth-form <%=reg_username != null ? "active" : ""%>"
			action="register" method="post">
			<h2>Đăng ký</h2>
			<div class="form-group">
				<label for="reg-name">Tên</label> <input type="text" id="reg-name"
					name="reg-name" placeholder="Nhập tên của bạn" required
					value=<%=reg_name != null ? reg_name : ""%>>
			</div>
			<div class="form-group">
				<label for="reg-username">Tài khoản</label> <input type="text"
					name="reg-username" id="reg-username" placeholder="Nhập tài khoản"
					required value=<%=reg_username != null ? reg_username : ""%>>
			</div>
			<div class="form-group">
				<label for="reg-password">Mật khẩu</label> <input type="password"
					name="reg-password" id="reg-password" placeholder="Nhập mật khẩu"
					required value=<%=reg_password != null ? reg_password : ""%>>
			</div>
			<div class="form-group">
				<label for="reg-confirm-password">Nhập lại mật khẩu</label> <input
					name="reg-confirm-password" type="password"
					id="reg-confirm-password" placeholder="Nhập lại mật khẩu" required
					value=<%=reg_confirm_password != null ? reg_confirm_password : ""%>>
			</div>
			<button type="submit" class="form-btn">Đăng ký</button>
			<%
			if (reg_username != null && !reg_username.isBlank()) {
			%>
			<p style="color: red; font-weight: bold; text-align: center;">Tài
				khoản đã tồn tại</p>
			<%
			}
			%>
			<p class="toggle-form">
				Đã có tài khoản? <span id="switch-to-login">Đăng nhập</span>
			</p>
		</form>
	</div>

	<script src="${pageContext.request.contextPath}/js/login.js" defer></script>
</body>
</html>
