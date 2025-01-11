<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="model.dto.VideoDTO"%>
<%@page import="model.bo.StudioBO"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Trang chủ</title>
<link rel="icon" type="image/x-icon"
	href="<%=request.getContextPath()%>/favicon.ico">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">
<link href="https://vjs.zencdn.net/8.0.4/video-js.css" rel="stylesheet">
<script src="https://vjs.zencdn.net/8.0.4/video.min.js"></script>
</head>
<body>
	<%
	request.setAttribute("page", "Home");

	ArrayList<VideoDTO> videos = StudioBO.getVideos(null);
	%>
	<jsp:include page="header.jsp" />
	<jsp:include page="sidebar.jsp" />

	<main id="main-panel" class="main-panel active">
		<div style="width: 100%; display: none" id="video-container">
			<div>
				<span id="closeVideoBtn" style="font-size: 30px; cursor: pointer"
					onclick="closeVideo()">&times;</span>
			</div>
			<div style="display: flex; gap: 60px">
				<video id="video-player" class="video-js" controls preload="auto"
					style="width: 70%; height: auto;">
				</video>
				<div class="video-card">
					<img alt="Thumbnail" class="video-thumbnail" id="play-thumbnail">
					<div class="video-info">
						<h3 class="video-title" id="play-title"></h3>
						<div class="video-meta">
							<p class="video-channel" id="play-name"></p>
							<p class="video-date" id="play-date"></p>
						</div>
						<p class="video-channel" id="play-time"></p>
						<p class="video-description" id="play-description"></p>
					</div>
				</div>
			</div>
		</div>
		<%
		if (videos != null && videos.size() > 0) {
			for (VideoDTO video : videos)
				if (video.isPublic()) {
		%>
		<div class="video-card" onclick="playVideo('<%=video.getId()%>')">
			<img
				src="data/thumbnail/<%=video.getId()%>.<%=video.getImgExtension()%>"
				alt="Thumbnail" class="video-thumbnail">
			<div class="video-info">
				<h3 class="video-title"><%=video.getTitle()%></h3>
				<div class="video-meta">
					<p class="video-channel"><%=video.getAuthorName()%></p>
					<p class="video-date"><%=video.getUploadDate()%></p>
				</div>
				<span class="video-time"><%=video.getDuration()%></span>
			</div>
		</div>
		<%
		}
		} else {
		%>
		<h3>Chưa có video nào!</h3>
		<%
		}
		%>

	</main>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/hls.js/0.5.14/hls.js"></script>
	<script>
		var hls;
		function playVideo(videoId) {
			const url = "stream?id=" + videoId;
			fetch(url)
			  .then(response => {
			    if (!response.ok) {
			      throw new Error('Network response was not ok');
			    }
			    return response.json();
			  })
			  .then(data => updateVideoInfo(data))
			  .catch(error => {
			    console.error('Có lỗi xảy ra:', error);
			    return;
			  });
			var video = document.getElementById('video-player');
			var container = document.getElementById('video-container');
			container.style.display = 'block';
			const videoSrc = "data/video/" + videoId + "/master_playlist.m3u8";
			if (Hls.isSupported()) {
				hls = new Hls();
				hls.loadSource(videoSrc); // Tải nguồn HLS
				hls.attachMedia(video); // Kết nối với thẻ video
				hls.on(Hls.Events.MANIFEST_PARSED, function() {
					video.play(); // Bắt đầu phát video khi manifest được tải
				});
			} else if (video.canPlayType('application/vnd.apple.mpegurl')) {
				// Hỗ trợ HLS trên Safari (hoặc các trình duyệt hỗ trợ HLS natively)
				video.src = videoSrc;
				video.addEventListener('loadedmetadata', function() {
					video.play();
				});
			}
		}
		function closeVideo() {
			var video = document.getElementById('video-player');
			var container = document.getElementById('video-container');
			if (Hls.isSupported()) {
				hls.destroy();
			} else if (video.canPlayType('application/vnd.apple.mpegurl')) {
				video.src = null;
			}
			container.style.display = 'none';
		}
		function updateVideoInfo(data) {
		    const videoId = data.id;
		    const title = data.title;
		    const description = data.description;
		    const author = data.author;
		    const duration = data.duration;
		    const uploadDate = data.uploadDate;
		    const img_extension = data.img_extension;

		    // Cập nhật các thông tin trong video-card
		    document.getElementById('play-thumbnail').src = "data/thumbnail/" + videoId + "." + img_extension; // Giả sử bạn có ảnh thumbnail
		    document.getElementById('play-title').textContent = title;
		    document.getElementById('play-name').textContent = author;
		    document.getElementById('play-date').textContent = uploadDate;
		    document.getElementById('play-time').textContent = "Thời lượng: " + duration;
		    document.getElementById('play-description').textContent = description;
		}
	</script>
</body>
</html>
