<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="model.dto.VideoDTO"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Studio</title>
<link rel="icon" type="image/x-icon"
	href="<%=request.getContextPath()%>/favicon.ico">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>
	<%
	request.setAttribute("page", "Studio");
	ArrayList<VideoDTO> videos = (ArrayList<VideoDTO>) request.getAttribute("videos");
	%>
	<jsp:include page="header.jsp" />

	<jsp:include page="sidebar.jsp" />

	<main id="studio-main" class="studio-main">
		<%
		if (videos != null && videos.size() > 0) {
		%>
		<h2 class="main-title">Video đã đăng</h2>
		<table class="video-table">
			<thead>
				<tr>
					<th>Video</th>
					<th>Trạng thái</th>
					<th>Tiêu đề</th>
					<th>Ngày đăng</th>
					<th>Hành động</th>
				</tr>
			</thead>
			<tbody>
				<%
				for (VideoDTO video : videos) {
				%>
				<tr data-id="<%=video.getId()%>">
					<td>
						<div class="video-studio-card">
							<img
								src="data/thumbnail/<%=video.getId()%>.<%=video.getImgExtension()%>"
								alt="Thumbnail" class="video-thumbnail-studio"> <span
								class="video-time"><%=video.getDuration()%></span>
						</div>
					</td>
					<td><%=video.isPublic() ? "Public" : "Đang chờ xử lý"%></td>
					<td><%=video.getTitle()%></td>
					<td><%=video.getUploadDate()%></td>
					<td>
						<button class="undone">
							<i class="fas fa-edit"></i>
						</button>
						<button class="delete-btn"
							onclick="deleteVideo(<%=video.getId()%>)">
							<i class="fas fa-trash-alt"></i>
						</button>
					</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>
		<div class="pagination-container">
			<div class="records-per-page">
				Hiển thị: <select id="recordsPerPage">
					<option value="5">5</option>
					<option value="10" selected>10</option>
					<option value="20">20</option>
				</select> bản ghi/trang
			</div>
			<div class="pagination">
				<button id="prevPage" class="pagination-btn" disabled>&laquo;
					Lùi</button>
				<span id="currentPage">1</span> / <span id="totalPages">5</span>
				<button id="nextPage" class="pagination-btn">&raquo; Tới</button>
			</div>
		</div>
		<%
		} else {
		%>
		<p>Bạn chưa đăng video nào!</p>
		<%
		}
		%>
	</main>
	<script type="text/javascript">
		const eventSource = new EventSource("/LTM-Yt/progress");
		eventSource.onmessage = function(event) {
			const data = event.data.trim().split(" ");
			if (data.length === 2) {
				markDone(data[0]);
			} else if (data.length === 3) {
				const videoId = data[0];
				const progress = data[1];
				const remainingTime = data[2];
				if (!isNaN(progress)) {
					updateRowById(videoId, progress, remainingTime);
				}
			}
		};
		function updateRowById(id, newValue, remainingTime) {
			const row = document.querySelector("tr[data-id='" + id + "']");
			if (row) {
				row.cells[1].textContent = "Đang xử lý: " + newValue + "%\n"
						+ formatTime(remainingTime);
			} else {
				console.log(`Row with ID ${id} not found`);
			}
		}
		function markDone(id) {
			const row = document.querySelector("tr[data-id='" + id + "']");
			if (row) {
				row.cells[1].textContent = "Public";
			} else {
				console.log(`Row with ID ${id} not found`);
			}
		}
		function formatTime(seconds) {
			const hours = Math.floor(seconds / 3600);
			const minutes = Math.floor((seconds % 3600) / 60);
			const remainingSeconds = seconds % 60;

			let result = "Thời gian ước tính còn lại: ";

			if (hours > 0) {
				result += hours + " giờ ";
			}
			if (minutes > 0) {
				result += minutes + " phút ";
			}
			if (remainingSeconds > 0) {
				result += remainingSeconds + " giây";
			}

			return "(" + result.trim() + ")";
		}
		function deleteVideo(videoId) {
		    Swal.fire({
		        title: 'Bạn có chắc chắn muốn xóa video này?',
		        icon: 'warning',
		        showCancelButton: true,
		        confirmButtonText: 'Xóa',
		        cancelButtonText: 'Hủy',
		        reverseButtons: true
		    }).then((result) => {
		        if (result.isConfirmed) {
		            fetch("deleteVideo?id=" + videoId, {
		                method: "DELETE",
		            })
		            .then(response => {
		                if (!response.ok) {
		                    throw new Error('Không thể xóa video');
		                }
		                // Tìm và xóa dòng chứa videoId trong bảng
		                const row = document.querySelector("tr[data-id='" + videoId + "']");
		                row.remove();
		                Swal.fire('Đã xóa!', 'Video đã được xóa thành công.', 'success');
		            })
		            .catch(error => {
		                console.error('Có lỗi xảy ra:', error);
		                Swal.fire('Lỗi!', 'Không thể xóa video. Vui lòng thử lại sau.', 'error');
		            });
		        }
		    });
		}
	</script>
</body>

</html>