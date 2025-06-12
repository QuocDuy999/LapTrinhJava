document.addEventListener("DOMContentLoaded", function() {
    const registerForm = document.querySelector("form");

    registerForm.addEventListener("submit", async function(event) {
        event.preventDefault();

        const name = document.getElementById("name").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value.trim();
        const confirmPassword = document.getElementById("confirm-password").value.trim();

        if (!name || !email || !password || !confirmPassword) {
            alert("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (password !== confirmPassword) {
            alert("Mật khẩu xác nhận không khớp!");
            return;
        }

        try {
            const response = await fetch("/api/user/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name, email, password })
            });

            if (response.ok) {
                alert("Đăng ký thành công! Vui lòng đăng nhập.");
                window.location.href = "/login.html";
            } else {
                const errorText = await response.text();
                alert("Đăng ký thất bại: " + errorText);
            }
        } catch (error) {
            alert("Lỗi kết nối đến server!");
        }
    });
});
