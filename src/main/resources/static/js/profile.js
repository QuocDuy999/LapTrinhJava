document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("token");
  const profileView = document.getElementById("profile-view");
  const profileFormContainer = document.getElementById("profile-form-container");
  const profileForm = document.getElementById("profile-form");
  const showFormBtn = document.getElementById("show-form-btn");

  if (!token) {
    alert("Vui lòng đăng nhập trước!");
    window.location.href = "/login";
    return;
  }

  // Hiển thị loading
  const loading = document.createElement("div");
  loading.textContent = "Đang tải hồ sơ...";
  loading.style.textAlign = "center";
  profileView.appendChild(loading);

  // Load hồ sơ từ server
  fetch("/api/profile/my", {
    headers: {
      "Authorization": "Bearer " + token,
    },
  })
    .then(res => {
      if (res.status === 401) {
        alert("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");
        window.location.href = "/login";
        return;
      }
      if (res.status === 404) {
        showFormBtn.style.display = "inline-block";
        return null;
      }
      return res.json();
    })
    .then(data => {
      profileView.removeChild(loading);
      if (data) {
        renderProfile(data);
      }
    })
    .catch(err => {
      console.error("Lỗi khi tải hồ sơ:", err);
    });

  // Render hồ sơ
  function renderProfile(data) {
    profileView.innerHTML = `
      <div class="profile-section">
        <h2>Thông tin cá nhân</h2>
        <div class="profile-info">
          <label>Họ tên:</label><span>${data.name}</span>
          <label>Ngày sinh:</label><span>${data.dob}</span>
          <label>Giới tính:</label><span>${data.gender}</span>
          <label>Số điện thoại:</label><span>${data.phone}</span>
          <label>Địa chỉ:</label><span>${data.address}</span>
          <label>Chiều cao:</label><span>${data.height || "Chưa cập nhật"} cm</span>
          <label>Cân nặng:</label><span>${data.weight || "Chưa cập nhật"} kg</span>
          <label>Tiểu sử bệnh:</label><span>${data.medicalHistory || "Chưa có"}</span>
        </div>
        <button class="btn-edit-profile" id="edit-profile-btn">Cập nhật hồ sơ</button>
      </div>
    `;
    profileView.style.display = "block";

    document.getElementById("edit-profile-btn").addEventListener("click", () => {
      showFormBtn.style.display = "none";
      profileView.style.display = "none";
      profileFormContainer.style.display = "block";

      profileForm.name.value = data.name;
      profileForm.dob.value = data.dob;
      profileForm.gender.value = data.gender;
      profileForm.phone.value = data.phone;
      profileForm.address.value = data.address;
      profileForm.height.value = data.height || "";
      profileForm.weight.value = data.weight || "";
      profileForm.medicalHistory.value = data.medicalHistory || "";
    });
  }

  // Hiện form khi ấn nút "Thêm hồ sơ mới"
  showFormBtn.addEventListener("click", () => {
    profileForm.reset();
    profileFormContainer.style.display = "block";
    showFormBtn.style.display = "none";
  });

  // Gửi hồ sơ lên server
  profileForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const formData = {
      name: profileForm.name.value,
      dob: profileForm.dob.value,
      gender: profileForm.gender.value,
      phone: profileForm.phone.value,
      address: profileForm.address.value,
      height: profileForm.height.value,
      weight: profileForm.weight.value,
      medicalHistory: profileForm.medicalHistory.value,
    };

    fetch("/api/profile", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token,
      },
      body: JSON.stringify(formData),
    })
      .then(res => {
        if (!res.ok) throw new Error("Lỗi khi lưu hồ sơ");
        return res.json();
      })
      .then(data => {
        alert("Lưu hồ sơ thành công!");
        location.reload();
      })
      .catch(err => {
        console.error(err);
        alert("Đã có lỗi xảy ra khi lưu hồ sơ.");
      });
  });
});
