/* filepath: d:\Project\gender-healthcare-system\src\main\resources\templates\register\register.js */
document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('form');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirm-password');

    form.addEventListener('submit', (event) => {
        if (password.value !== confirmPassword.value) {
            event.preventDefault();
            alert('Mật khẩu và xác nhận mật khẩu không khớp!');
        }
    });
});