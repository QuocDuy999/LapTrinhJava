document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("token");
  if (!token) {
    alert("Bạn cần đăng nhập!");
    window.location.href = "/login";
    return;
  }

  // ❌ Đã bỏ kiểm tra quyền ROLE_ADMIN

  const questionList = document.getElementById("admin-question-list");

  function loadQuestions() {
    fetch("/api/questions/all", {
      method: "GET",
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Không được phép truy cập.");
        return res.json();
      })
      .then((data) => {
        questionList.innerHTML = "";

        if (data.length === 0) {
          questionList.innerHTML = "<p>Không có câu hỏi nào.</p>";
          return;
        }

        data.reverse().forEach((item) => {
          const div = document.createElement("div");
          div.classList.add("question-item");
          div.innerHTML = `
            <p><strong>Người hỏi:</strong> ${item.username}</p>
            <p><strong>Câu hỏi:</strong> ${item.question}</p>
            <p><em>Gửi lúc: ${new Date(item.askedAt).toLocaleString()}</em></p>
            ${
              item.answer
                ? `<p><strong>Đã trả lời:</strong> ${item.answer}</p>
                   <button class="btn btn-delete" data-id="${item.id}">🗑 Xoá câu hỏi</button>`
                : `<textarea class="admin-answer" rows="3" placeholder="Nhập câu trả lời..."></textarea>
                   <div class="btn-group">
                     <button class="btn btn-save" data-id="${item.id}">✅ Gửi trả lời</button>
                     <button class="btn btn-delete" data-id="${item.id}">🗑 Xoá câu hỏi</button>
                   </div>`
            }
          `;
          questionList.appendChild(div);
        });

        attachAnswerListeners();
        attachDeleteListeners();
      })
      .catch((err) => {
        questionList.innerHTML = `<p style="color:red;">Lỗi tải danh sách: ${err.message}</p>`;
      });
  }

  function attachAnswerListeners() {
    const buttons = document.querySelectorAll(".btn-save");
    buttons.forEach((btn) => {
      btn.addEventListener("click", () => {
        const id = btn.getAttribute("data-id");
        const textarea = btn.closest(".question-item").querySelector(".admin-answer");
        const answer = textarea.value.trim();

        if (answer.length < 5) {
          alert("Câu trả lời quá ngắn.");
          return;
        }

        fetch(`/api/questions/answer/${id}`, {
          method: "POST",
          headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json",
          },
          body: JSON.stringify(answer),
        })
          .then((res) => {
            if (res.ok) {
              alert("✅ Đã gửi câu trả lời.");
              loadQuestions();
            } else {
              alert("❌ Gửi thất bại.");
            }
          });
      });
    });
  }

  function attachDeleteListeners() {
    const buttons = document.querySelectorAll(".btn-delete");
    buttons.forEach((btn) => {
      btn.addEventListener("click", () => {
        const id = btn.getAttribute("data-id");
        if (confirm("Bạn có chắc chắn muốn xoá câu hỏi này không?")) {
          fetch(`/api/questions/admin/delete/${id}`, {
            method: "DELETE",
            headers: {
              "Authorization": "Bearer " + token,
            },
          })
            .then((res) => {
              if (!res.ok) throw new Error("Xoá thất bại");
              loadQuestions();
            })
            .catch((err) => {
              console.error(err);
              alert("Lỗi khi xoá câu hỏi.");
            });
        }
      });
    });
  }

  loadQuestions();
});
document.addEventListener("DOMContentLoaded", () => {
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      localStorage.removeItem("token"); // Xóa JWT
      window.location.href = "/login"; // Chuyển hướng về trang đăng nhập
    });
  }
});
