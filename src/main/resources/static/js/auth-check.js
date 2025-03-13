document.addEventListener('DOMContentLoaded', function() {

    const accessToken = localStorage.getItem('accessToken');

    if (!window.location.pathname.includes('/login') && !accessToken) {
        window.location.href = '/login';
    }

    if (window.location.pathname.includes('/login') && accessToken) {
        window.location.href = '/home';
    }
});