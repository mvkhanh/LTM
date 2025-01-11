// Chuyển đổi giữa Đăng nhập và Đăng ký
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const switchToRegister = document.getElementById('switch-to-register');
const switchToLogin = document.getElementById('switch-to-login');

switchToRegister.addEventListener('click', () => {
    loginForm.classList.remove('active');
    registerForm.classList.add('active');
});

switchToLogin.addEventListener('click', () => {
    registerForm.classList.remove('active');
    loginForm.classList.add('active');
});

document
				.getElementById("register-form")
				.addEventListener(
						"submit",
						function(event) {
							const password = document
									.getElementById("reg-password").value;
							const confirmPassword = document
									.getElementById("reg-confirm-password").value;

							if (password !== confirmPassword) {
								event.preventDefault(); // Ngăn không cho form submit
								alert("Mật khẩu và nhập lại mật khẩu không khớp!");
							}
						});
