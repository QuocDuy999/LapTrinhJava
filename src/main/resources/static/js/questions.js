document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("token");
  if (!token) {
    alert("Bạn cần đăng nhập!");
    window.location.href = "/login";
    return;
  }

  const questionInput = document.getElementById("questionInput");
  const submitBtn = document.getElementById("askBtn");
  const historyContainer = document.getElementById("questionHistory");
  const patientName = document.getElementById("user-name-display");
  const filterSelect = document.getElementById("filterStatus");

  // 📌 Lấy tên người dùng
  async function loadPatientName() {
    try {
      const res = await fetch("/api/customer", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (res.ok) {
        const data = await res.json();
        const fullName = data.name || data.username;
        if (patientName) patientName.textContent = fullName;
      } else {
        patientName.textContent = "Không xác định được bệnh nhân";
      }
    } catch (error) {
      console.error("Lỗi khi lấy thông tin người dùng:", error);
      patientName.textContent = "Lỗi khi lấy tên bệnh nhân";
    }
  }

  // 📌 Tải câu hỏi theo bộ lọc
  function loadQuestions(filter = "all") {
    fetch("/api/questions/my-questions", {
      method: "GET",
      headers: {
        Authorization: "Bearer " + token,
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Không thể tải câu hỏi.");
        return res.json();
      })
      .then((data) => {
        historyContainer.innerHTML = "";
        if (data.length === 0) {
          historyContainer.innerHTML = "<p>Bạn chưa gửi câu hỏi nào.</p>";
          return;
        }

        data.reverse()
          .filter((item) => {
            if (filter === "answered") return item.answer;
            if (filter === "unanswered") return !item.answer;
            return true;
          })
          .forEach((item) => {
            const div = document.createElement("div");
            div.classList.add("question-item");
            div.innerHTML = `
              <p><strong>📝Người gửi:</strong> ${item.username}</p>
              <p><strong>Hỏi:</strong> ${item.question}</p>
              <p><em>Thời gian gửi: ${new Date(item.askedAt).toLocaleString()}</em></p>
              ${
                item.answer
                  ? `
                    <p><strong>💬Người trả lời:</strong> ${item.answeredBy || "Tư Vấn Viên"}</p>
                    <p><strong>Đáp:</strong> ${item.answer}</p>
                    <p><em>Thời gian trả lời: ${new Date(item.answeredAt).toLocaleString()}</em></p>
                  `
                  : `
                    <p><em>⏳Đang chờ tư vấn viên trả lời...</em></p>
                    <button class="delete-btn" data-id="${item.id}">Thu hồi câu hỏi</button>
                  `
              }
            `;
            historyContainer.appendChild(div);
          });

        attachDeleteListeners();
      })
      .catch((err) => {
        console.error(err);
        historyContainer.innerHTML = "<p>Lỗi khi tải dữ liệu câu hỏi.</p>";
      });
  }

  // 📌 Gửi câu hỏi mới
  submitBtn.addEventListener("click", () => {
    const content = questionInput.value.trim();
    if (content.length < 5) {
      alert("Vui lòng nhập câu hỏi rõ ràng (ít nhất 5 ký tự).");
      return;
    }

    fetch("/api/questions/ask", {
      method: "POST",
      headers: {
        Authorization: "Bearer " + token,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ question: content }),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Gửi câu hỏi thất bại.");
        return res.json();
      })
      .then(() => {
        questionInput.value = "";
        loadQuestions(filterSelect.value);
      })
      .catch((err) => {
        console.error(err);
        alert("Gửi câu hỏi thất bại. Vui lòng thử lại.");
      });
  });

  // 📌 Gán sự kiện xóa cho các nút "Thu hồi"
  function attachDeleteListeners() {
    const deleteButtons = document.querySelectorAll(".delete-btn");
    deleteButtons.forEach((btn) => {
      btn.addEventListener("click", () => {
        const id = btn.getAttribute("data-id");
        if (confirm("Bạn có chắc chắn muốn thu hồi câu hỏi này?")) {
          fetch(`/api/questions/delete/${id}`, {
            method: "DELETE",
            headers: {
              Authorization: "Bearer " + token,
            },
          })
            .then((res) => {
              if (!res.ok) throw new Error("Thu hồi thất bại.");
              loadQuestions(filterSelect.value);
            })
            .catch((err) => {
              console.error(err);
              alert("Lỗi khi thu hồi câu hỏi.");
            });
        }
      });
    });
  }

  // 📌 Bộ lọc
  filterSelect.addEventListener("change", (e) => {
    loadQuestions(e.target.value);
  });

  // 📌 Khởi động
  loadPatientName();
  loadQuestions();
});
document.addEventListener("DOMContentLoaded", () => {
    const path = window.location.pathname;
    document.querySelectorAll(".nav-link").forEach(link => {
      if (link.getAttribute("href") === path) {
        link.classList.add("active");
      }
    });
  });