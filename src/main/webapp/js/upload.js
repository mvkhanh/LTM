document.addEventListener("DOMContentLoaded", function() {
	const openModalBtn = document.getElementById("openModalBtn");
	const closeModalBtn = document.getElementById("closeModalBtn");
	const uploadModal = document.getElementById("uploadModal");
	const dragDropArea = document.getElementById("dragDropArea");
	const fileInput = document.getElementById("fileInput");
	const browseBtn = document.getElementById("browseBtn");

	// Mở modal khi nhấn nút Upload
	if (openModalBtn != null) {
		openModalBtn.addEventListener("click", () => {
			uploadModal.style.display = "block";
		});

		// Đóng modal khi nhấn nút đóng hoặc bên ngoài modal
		closeModalBtn.addEventListener("click", () => {
			uploadModal.style.display = "none";
		});

		window.addEventListener("click", (e) => {
			if (e.target === uploadModal) {
				uploadModal.style.display = "none";
			}
		});

		// Xử lý khi nhấn vào "chọn videoFile"
		browseBtn.addEventListener("click", () => {
			fileInput.click();
		});

		// Xử lý khi videoFile được chọn qua input
		fileInput.addEventListener("change", (e) => {
			handleFiles(e.target.files);
		});

		// Xử lý kéo và thả videoFile
		dragDropArea.addEventListener("dragover", (e) => {
			e.preventDefault();
			dragDropArea.style.backgroundColor = "#e8e8e8";
		});

		dragDropArea.addEventListener("dragleave", () => {
			dragDropArea.style.backgroundColor = "#f9f9f9";
		});

		dragDropArea.addEventListener("drop", (e) => {
			e.preventDefault();
			dragDropArea.style.backgroundColor = "#f9f9f9";
			handleFiles(e.dataTransfer.files);
		});

		function handleFiles(files) {
			var fileName = files[0].name; // Lấy tên videoFile video
			var fileNameDisplay = document.getElementById("fileName"); // Phần tử hiển thị tên videoFile
			var videoFileNameSpan = document.getElementById("videoFileName"); // Phần tử span để hiển thị tên videoFile

			// Hiển thị tên video đã chọn
			videoFileNameSpan.textContent = fileName;
			fileNameDisplay.style.display = "block"; // Hiển thị phần tên videoFile
		}

		const CHUNK_SIZE = 5 * 1024 * 1024; // 5MB mỗi chunk
		const videoInput = document.getElementById("fileInput");
		const uploadBtn = document.getElementById("submitBtn");
		const uploadBtn2 = document.getElementById("openModalBtn");

		uploadBtn.addEventListener("click", () => {
			const videoFile = videoInput.files[0];
			const title = document.getElementById("title").value;
			if (title.trim() === "") {
				Swal.fire({
					title: 'Chú ý!',
					text: 'Vui lòng nhập tiêu đề cho video trước khi upload!',
					icon: 'warning',
					showConfirmButton: true // Không hiển thị nút xác nhận
				});
				return;
			}
			if (!videoFile) {
				Swal.fire({
					title: 'Chú ý!',
					text: 'Vui lòng chọn file video trước khi upload!',
					icon: 'warning',
					showConfirmButton: true // Không hiển thị nút xác nhận
				});
				return;
			}
			const videoName = videoFile.name;
			const videoExtension = videoName.substring(videoName.lastIndexOf('.') + 1);
			const totalChunks = Math.ceil(videoFile.size / CHUNK_SIZE);
			let start = 0;
			let chunkIndex = 0;
			const thumbnailInput = document.getElementById("thumbnail");
			let thumbnailExtension;
			let thumbnailFile;
			if (thumbnailInput.files.length > 0) {
				thumbnailFile = thumbnailInput.files[0];
				const thumbnailName = thumbnailFile.name;
				thumbnailExtension = thumbnailName.substring(thumbnailName.lastIndexOf('.') + 1);
			}
			else thumbnailExtension = "png";
			uploadBtn2.disabled = true;
			closeModalBtn.click();
			Swal.fire({
				title: 'Chú ý!',
				text: 'Vui lòng không rời khỏi trang trong khi video đang được tải lên. Cảm ơn bạn!',
				icon: 'warning',
				showConfirmButton: true // Không hiển thị nút xác nhận
			});
			function uploadInfor() {
				const formData = new FormData();
				formData.append("title", title);
				formData.append("description", document.getElementById("description").value);
				if (thumbnailInput.files.length > 0) {
					formData.append("thumbnail", thumbnailFile);
					formData.append("thumbnailExtension", thumbnailExtension);
				}
				formData.append("videoExtension", videoExtension);
				fetch("upload", {
					method: "POST",
					body: formData
				})
					.then(response => response.text())
					.then(id => {
						console.log(id);
						if (id == -1) {
							alert("Lỗi không thể upload video");
							uploadBtn.disabled = false;
							uploadBtn2.disabled = false;
						}
						else uploadChunk(id, false);
					})
			}

			function uploadChunk(id, isLast) {
				const end = Math.min(start + CHUNK_SIZE, videoFile.size);
				const chunk = videoFile.slice(start, end);

				const formData = new FormData();
				formData.append("videoChunk", chunk);
				formData.append("chunkIndex", chunkIndex);
				formData.append("totalChunks", totalChunks);
				formData.append("id", id);
				formData.append("videoExtension", videoExtension);
				if (isLast) {
					formData.append("thumbnailExtension", thumbnailExtension);
				}
				fetch("upload", {
					method: "POST",
					body: formData,
				})
					.then(response => response.text())
					.then(result => {
						uploadBtn2.innerText = `\tĐã upload: ${Math.floor(Math.min(end, videoFile.size) / videoFile.size * 100)}%`;
						if (end < videoFile.size) {
							start = end;
							chunkIndex++;
							if (chunkIndex == totalChunks - 1)
								uploadChunk(id, true);
							else uploadChunk(id, false);
						} else {
							Swal.fire({
								title: "Upload hoàn tất!",
								text: "Video của bạn đã được tải lên thành công.",
								icon: "success",
								timer: 2000, // 2 giây
								showConfirmButton: true, // Có nút OK
								confirmButtonText: "OK",
								allowOutsideClick: false, // Không cho click ra ngoài để tắt
							}).then(() => {
								// Chuyển hướng sau khi SweetAlert đóng
								window.location.href = "studio";
							});
						}
					})
					.catch(error => {
						uploadBtn2.innerText = "Upload Video";
						uploadBtn2.disabled = false;
						console.error("Upload error:", error);
						Swal.fire({
							title: 'Lỗi',
							text: 'Đã xảy ra lỗi khi tải video lên. Vui lòng thử lại.',
							icon: 'error',
							confirmButtonText: 'OK'
						});
					});
			}

			uploadInfor();
		});
	};
});

