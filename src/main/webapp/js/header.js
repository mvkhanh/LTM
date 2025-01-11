function toggleMenu() {
	const menu = document.getElementById('dropdown-menu');
	menu.style.display = menu.style.display === 'block' ? 'none'
		: 'block';
}

window.addEventListener('click', function(e) {
	const menu = document.getElementById('dropdown-menu');
	const avatar = document.querySelector('.avatar');
	if (menu != null && !menu.contains(e.target) && !avatar.contains(e.target)) {
		menu.style.display = 'none';
	}
});

document.addEventListener('DOMContentLoaded', function() {
    // Lấy tất cả các phần tử có class 'undone'
    const undoneButtons = document.querySelectorAll('.undone');

    // Duyệt qua tất cả các phần tử và thêm sự kiện click cho mỗi cái
    undoneButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            e.preventDefault();  // Ngừng hành động mặc định (tránh việc chuyển trang)
            Swal.fire({
                title: 'Thông báo',
                text: 'Chức năng này đang phát triển!',
                icon: 'info',
                confirmButtonText: 'Đóng'
            });
        });
    });
});