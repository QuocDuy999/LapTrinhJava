document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("role-form");
  const message = document.getElementById("message");
  const userList = document.getElementById("user-list");
  const token = localStorage.getItem("token");

  // Nếu chưa đăng nhập thì chuyển về trang đăng nhập
  if (!token) {
    alert("Bạn cần đăng nhập để truy cập trang này!");
    window.location.href = "/login";
    return;
  }

  // Gọi luôn khi có token
  loadUsers();

  function loadUsers() {
    fetch("/api/admin/users", {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      }
    })
      .then(res => {
        if (!res.ok) throw new Error("Không thể tải danh sách người dùng.");
        return res.json();
      })
      .then(users => {
        userList.innerHTML = "";
        if (users.length === 0) {
          userList.innerHTML = "<tr><td colspan='2'>Không có người dùng nào.</td></tr>";
          return;
        }
        users.forEach(user => {
          const row = document.createElement("tr");
          row.innerHTML = `
            <td>${user.email}</td>
            <td>${user.role}</td>
          `;
          userList.appendChild(row);
        });
      })
      .catch(err => {
        userList.innerHTML = `<tr><td colspan="2" style="color:red;">${err.message}</td></tr>`;
      });
  }

  form.addEventListener("submit", function (e) {
    e.preventDefault();
    const email = document.getElementById("email").value.trim();
    const role = document.getElementById("role").value;

    if (!email || !role) {
      message.textContent = "Vui lòng nhập đầy đủ email và chọn vai trò!";
      message.style.color = "red";
      return;
    }

    fetch(`/api/admin/set-role?email=${encodeURIComponent(email)}&role=${role}`, {
      method: "PUT",
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      }
    })
      .then(res => {
        if (!res.ok) throw new Error("Gán quyền thất bại.");
        return res.text();
      })
      .then(text => {
        message.textContent = text;
        message.style.color = "green";
        form.reset();
        loadUsers(); // cập nhật lại danh sách
      })
      .catch(err => {
        message.textContent = err.message;
        message.style.color = "red";
      });
  });
});
