// src/main/resources/static/js/auth-check.js
document.addEventListener('DOMContentLoaded', function() {
    // Kiểm tra xem người dùng đã xác thực chưa
    const accessToken = localStorage.getItem('accessToken');

    // Nếu không ở trang đăng nhập và không có token, chuyển hướng đến trang đăng nhập
    if (!window.location.pathname.includes('/login') && !accessToken) {
        window.location.href = '/login';
    }

    // Nếu đang ở trang đăng nhập và có token, chuyển hướng đến trang chủ
    if (window.location.pathname.includes('/login') && accessToken) {
        window.location.href = '/home';
    }
});