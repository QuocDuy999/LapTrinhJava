document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById("login-form");

    loginForm.addEventListener("submit", async function(event) {
        event.preventDefault();

        const formData = new FormData(loginForm);
        const response = await fetch("/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams(formData).toString()
        });

        if (response.ok || response.redirected) {
            // Generate a simple token for client-side checks (optional, can remove if not needed)
            const email = formData.get("username");
            const token = btoa(email + ":" + new Date().getTime());
            localStorage.setItem("token", token);

            alert("Đăng nhập thành công!");
            window.location.href = "/customer";
        } else {
            alert("Tên đăng nhập hoặc mật khẩu không đúng!");
        }
    });
});