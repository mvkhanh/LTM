<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="model.dto.UserDTO"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
	<%
	UserDTO usr = (UserDTO) request.getSession().getAttribute("account");

	String pageName = (String) request.getAttribute("page");
	if (pageName == null) {
		pageName = "Home"; // Giá trị mặc định
	}
	%>

	<header class="header">
		<div class="search-container">
			<div class="search-box">
				<input type="text"
					placeholder="<%=pageName.equals("Home") ? "Tìm kiếm" : "Tìm kiếm video của bạn"%>"
					class="search-input">
				<button class="search-btn" title="Search">🔍</button>
			</div>
		</div>

		<%
		if (usr == null) {
		%>
		<button class="login-btn" onclick="window.location.href='login'">Đăng
			nhập</button>
		<%
		} else {
		%>
		<div class="user-actions">
			<button class="upload-btn" id="openModalBtn">Upload Video</button>
			<div class="avatar-container">
				<img
					src="https://cdn2.vectorstock.com/i/1000x1000/57/91/profile-avatar-icon-design-template-vector-28515791.jpg"
					alt="Avatar" class="avatar" onclick="toggleMenu()">
				<div class="dropdown-menu" id="dropdown-menu">
					<a href="#" class="undone">Chỉnh sửa thông tin</a> <a href="#"
						class="undone">Cài đặt</a> <a href="logout">Đăng xuất</a>
				</div>
			</div>
		</div>
		<%
		}
		%>
	</header>

	<!-- Modal -->
	<div class="modal" id="uploadModal">
		<div class="modal-content">
			<span class="close-btn" id="closeModalBtn">&times;</span>
			<h2 style="text-align: center;">Tải lên Video</h2>

			<!-- Form Upload Video -->
			<form id="uploadForm" method="post" action="upload"
				enctype="multipart/form-data">
				<div class="form-group">
					<label for="title">Tiêu đề:</label> <input type="text" id="title"
						name="title" required>
				</div>

				<div class="form-group">
					<label for="description">Mô tả (optional):</label>
					<textarea id="description" name="description"></textarea>
				</div>

				<div>
					<label for="thumbnail">Thumbnail (optional):</label> <input
						type="file" id="thumbnail" name="thumbnail" accept="image/*">
				</div>
				<br> <br>
				<!-- Kéo thả video hoặc chọn file -->
				<div>
					<label for="dragDropArea">Video:</label>
					<div class="drag-drop-area" id="dragDropArea">
						<p style="text-align: center;">
							Kéo thả video hoặc <span class="browse-btn" id="browseBtn">chọn
								file</span>
						</p>
						<input type="file" id="fileInput" name="videoFile"
							accept="video/*" hidden required>
						<!-- Hiển thị tên file video đã chọn -->
						<p id="fileName" style="text-align: center; display: none;">
							<span id="videoFileName"></span>
						</p>
					</div>
				</div>

				<div class="upload-warning">
					<p>Vui lòng không rời khỏi trang hiện tại trong quá trình
						upload.</p>
				</div>
				<button type="button" id="submitBtn" class="form-btn"
					style="width: 30%">OK</button>
			</form>
		</div>
	</div>

	<script defer src="${pageContext.request.contextPath}/js/upload.js"></script>
	<script defer src="${pageContext.request.contextPath}/js/header.js"></script>

</body>
</html>