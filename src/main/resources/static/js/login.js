document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.querySelector("form");

    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        const response = await fetch("/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const data = await response.json();
            const token = data.token;
            localStorage.setItem("token", token);

            // Gọi API lấy thông tin user (bao gồm roles)
            const userRes = await fetch("/api/customer", {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token,
                    "Content-Type": "application/json"
                }
            });

            if (userRes.ok) {
                const user = await userRes.json();
                const roles = user.roles || [];

                if (roles.includes("ROLE_ADMIN")) {
                    window.location.href = "/admin-consultation";
                } else if (roles.includes("ROLE_CONSULTANT")) {
                    window.location.href = "/admin-consultation";
                } else {
                    window.location.href = "/customer";
                }
            } else {
                alert("Lỗi xác thực người dùng!");
            }
        } else {
            alert("Tên đăng nhập hoặc mật khẩu không đúng!");
        }
    });
});
