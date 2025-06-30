document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.querySelector("form");

    loginForm.addEventListener("submit", async function(event) {
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
            localStorage.setItem("token", data.token);
            // alert("Đăng nhập thành công!");
            window.location.href = "/customer";
        } else {
            alert("Tên đăng nhập hoặc mật khẩu không đúng!");
        }
    });
});
