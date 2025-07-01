const form = document.getElementById("appointment-form");
const department = document.getElementById("department");
const doctorSelect = document.getElementById("doctorName");
const historyList = document.getElementById("history");
const patientName = document.getElementById("patient-name");

const token = localStorage.getItem("token");

const doctorsByDept = {
  "Nội tổng quát": ["Bs. Hoàng", "Bs. Minh", "Bs. Lan", "Bs. An", "Bs. Tuyết"],
  "Ngoại chấn thương": ["Bs. Tùng", "Bs. Dũng", "Bs. Vinh", "Bs. Hải", "Bs. Quang"],
  "Nhi": ["Bs. Nga", "Bs. Hòa", "Bs. Thủy", "Bs. Bích", "Bs. Sơn"],
  "Sản phụ khoa": ["Bs. Hạnh", "Bs. Yến", "Bs. Phương", "Bs. Mai", "Bs. Thanh"],
  "Tai mũi họng": ["Bs. Thành", "Bs. Trang", "Bs. Long", "Bs. Chi", "Bs. Quỳnh"]
};

// Cập nhật danh sách bác sĩ theo khoa
department.addEventListener("change", () => {
  doctorSelect.innerHTML = '<option value="">Chọn bác sĩ</option>';
  const selected = department.value;
  if (doctorsByDept[selected]) {
    doctorsByDept[selected].forEach(doctor => {
      const opt = document.createElement("option");
      opt.value = doctor;
      opt.textContent = doctor;
      doctorSelect.appendChild(opt);
    });
  }
});

// Lấy tên bệnh nhân từ API /api/customer
async function loadPatientName() {
  try {
    const res = await fetch("/api/customer", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (res.ok) {
      const data = await res.json();
      patientName.textContent = `Tên bệnh nhân: ${data.name}`;
    } else {
      patientName.textContent = "Không xác định được bệnh nhân";
    }
  } catch (error) {
    patientName.textContent = "Lỗi khi lấy tên bệnh nhân";
  }
}

// Gửi yêu cầu đặt lịch khám
form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const data = {
    department: department.value,
    doctorName: doctorSelect.value,
    illness: document.getElementById("illness").value,
    date: document.getElementById("date").value,
    time: document.getElementById("time").value
  };

  const res = await fetch("/api/appointment", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(data)
  });

  if (res.ok) {
    alert("Đặt lịch thành công");
    loadHistory();
  } else {
    alert("Lỗi khi đặt lịch");
  }
});

// Hiển thị lịch sử khám
async function loadHistory() {
  try {
    const res = await fetch("/api/appointment/history", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (!res.ok) {
      historyList.innerHTML = "<tr><td colspan='5'>Lỗi tải lịch sử</td></tr>";
      return;
    }

    const list = await res.json();
    historyList.innerHTML = "";

    if (list.length === 0) {
      historyList.innerHTML = "<tr><td colspan='5'>Chưa có lịch sử khám</td></tr>";
      return;
    }

    list.forEach(app => {
      const datetime = new Date(app.appointmentDate);
      const formattedDateTime = datetime.toLocaleString("vi-VN", {
        hour: "2-digit", minute: "2-digit", second: "2-digit",
        day: "2-digit", month: "2-digit", year: "numeric"
      });

      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${formattedDateTime}</td>
        <td>${app.department}</td>
        <td>${app.doctorName}</td>
        <td>${app.illness}</td>
        <td>${app.status}</td>
      `;
      historyList.appendChild(row);
    });

  } catch (error) {
    historyList.innerHTML = "<tr><td colspan='5'>Lỗi kết nối máy chủ</td></tr>";
  }
}

// Tải dữ liệu khi trang được load
document.addEventListener("DOMContentLoaded", () => {
  loadPatientName();
  loadHistory();
});
