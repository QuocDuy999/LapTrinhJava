document.addEventListener('DOMContentLoaded', () => {
    const cycleForm = document.getElementById("cycle-form");
    const calendarContainer = document.querySelector(".calendar");
    const currentMonthDisplay = document.querySelector(".current-month");
    const prevMonthButton = document.querySelector(".month-nav.prev");
    const nextMonthButton = document.querySelector(".month-nav.next");
    const appointmentForm = document.getElementById("appointment-form");
    const appointmentFormContainer = document.getElementById("appointment-form-container");
    const historySection = document.getElementById("history-section");

    let currentDate = new Date();
    let currentMonth = currentDate.getMonth();
    let currentYear = currentDate.getFullYear();

    // Load danh sách tư vấn viên
    async function loadConsultants() {
        try {
            console.log("Loading consultants...");
            const response = await fetch("/api/consultant/list", { credentials: 'include' });
            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
            const consultants = await response.json();
            console.log("Danh sách tư vấn viên từ API:", consultants);
            const consultantSelect = document.getElementById("consultant");
            if (!consultantSelect) {
                console.error("Element #consultant not found");
                return;
            }
            consultantSelect.innerHTML = '<option value="">Chọn tư vấn viên</option>';
            consultants.forEach(consultant => {
                const option = document.createElement("option");
                option.value = consultant.id;
                option.textContent = `${consultant.name} (${consultant.role})`;
                consultantSelect.appendChild(option);
            });
        } catch (error) {
            console.error("Lỗi khi tải danh sách tư vấn viên:", error);
        }
    }

    // Load lịch sử tư vấn
    async function loadAppointments() {
        try {
            const response = await fetch("/api/appointment/list", { credentials: 'include' });
            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
            const appointments = await response.json();
            const appointmentList = document.getElementById("appointment-list");
            appointmentList.innerHTML = "";
            if (appointments.length === 0) {
                appointmentList.innerHTML = "<p>Chưa có lịch hẹn nào.</p>";
                return;
            }
            appointments.forEach(appointment => {
                const div = document.createElement("div");
                div.className = "appointment-item";
                div.innerHTML = `
                    <p>${appointment.consultant.name} (${appointment.consultant.role}) - ${new Date(appointment.appointmentDate).toLocaleString('vi-VN')}</p>
                    <button onclick="deleteAppointment(${appointment.id})">Xóa</button>
                `;
                appointmentList.appendChild(div);
            });
        } catch (error) {
            console.error("Lỗi khi tải lịch sử tư vấn:", error);
        }
    }

    // Xóa lịch hẹn
    window.deleteAppointment = async function(id) {
        if (confirm("Bạn có chắc chắn muốn xóa lịch hẹn này?")) {
            try {
                const response = await fetch(`/api/appointment/delete/${id}`, {
                    method: "DELETE",
                    credentials: 'include'
                });
                if (response.ok) {
                    alert("Lịch hẹn đã được xóa!");
                    loadAppointments();
                } else {
                    alert("Xóa lịch hẹn thất bại!");
                }
            } catch (error) {
                console.error("Lỗi khi xóa lịch hẹn:", error);
                alert("Lỗi khi xóa lịch hẹn. Vui lòng thử lại sau.");
            }
        }
    };

    // Xử lý form đặt lịch
    appointmentForm.addEventListener("submit", async function(event) {
        event.preventDefault();
        const appointmentDate = document.getElementById("appointmentDate").value;
        const consultantId = document.getElementById("consultant").value;

        if (!appointmentDate || !consultantId) {
            alert("Vui lòng chọn ngày giờ và tư vấn viên!");
            return;
        }

        try {
            const response = await fetch("/api/appointment/book", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: `appointmentDate=${encodeURIComponent(appointmentDate)}&consultantId=${consultantId}`,
                credentials: 'include'
            });
            if (response.ok) {
                alert("Đặt lịch thành công!");
                appointmentFormContainer.style.display = "none";
                appointmentForm.reset();
                if (historySection.style.display === "block") {
                    loadAppointments();
                }
            } else {
                alert("Đặt lịch thất bại!");
            }
        } catch (error) {
            console.error("Lỗi khi đặt lịch:", error);
            alert("Lỗi khi đặt lịch. Vui lòng thử lại sau.");
        }
    });

    // Hiển thị/Ẩn form đặt lịch
    window.toggleAppointmentForm = function() {
        console.log("Toggle form called");
        appointmentFormContainer.style.display = appointmentFormContainer.style.display === "none" ? "block" : "none";
        if (appointmentFormContainer.style.display === "block") {
            loadConsultants();
            console.log("loadConsultants called");
        }
    };

    // Hiển thị lịch sử tư vấn
    window.showHistory = function() {
        historySection.style.display = historySection.style.display === "none" ? "block" : "none";
        if (historySection.style.display === "block") {
            loadAppointments();
        } else {
            document.getElementById("appointment-list").innerHTML = "";
        }
    };

    // Các phần code còn lại giữ nguyên
    cycleForm.addEventListener("submit", function(event) {
        event.preventDefault();
        const lastPeriodDateInput = document.getElementById("lastPeriodDate").value;
        if (!lastPeriodDateInput) {
            alert("Vui lòng nhập ngày bắt đầu kỳ kinh gần nhất.");
            return;
        }

        const lastPeriodDate = new Date(lastPeriodDateInput);
        const cycleLength = parseInt(document.getElementById("cycleLength").value);
        const periodLength = parseInt(document.getElementById("periodLength").value);

        if (isNaN(cycleLength) || isNaN(periodLength) || cycleLength < 20 || cycleLength > 40) {
            alert("Độ dài chu kỳ không hợp lệ! Vui lòng nhập giá trị từ 20 đến 40 ngày.");
            return;
        }

        if (isNaN(lastPeriodDate.getTime())) {
            alert("Ngày bắt đầu kỳ kinh không hợp lệ.");
            return;
        }

        const ovulationDate = new Date(lastPeriodDate);
        ovulationDate.setDate(lastPeriodDate.getDate() + cycleLength - 14);

        const fertileStart = new Date(ovulationDate);
        fertileStart.setDate(ovulationDate.getDate() - 3);

        const fertileEnd = new Date(ovulationDate);
        fertileEnd.setDate(ovulationDate.getDate() + 2);

        const nextMenstruationDate = new Date(lastPeriodDate);
        nextMenstruationDate.setDate(lastPeriodDate.getDate() + cycleLength);

        let fertilityStatus = "Thấp";
        if (cycleLength >= 26 && cycleLength <= 32) {
            fertilityStatus = "Cao";
        } else if (cycleLength > 32) {
            fertilityStatus = "Trung bình";
        }

        let healthScore = 80;
        if (cycleLength >= 26 && cycleLength <= 32) {
            healthScore = 90;
        } else if (cycleLength > 32) {
            healthScore = 85;
        } else if (cycleLength < 26) {
            healthScore = 75;
        }

        localStorage.setItem("cycleLength", cycleLength);
        localStorage.setItem("ovulationDate", formatDate(ovulationDate));
        localStorage.setItem("fertileStart", formatDate(fertileStart));
        localStorage.setItem("fertileEnd", formatDate(fertileEnd));
        localStorage.setItem("nextMenstruationDate", formatDate(nextMenstruationDate));
        localStorage.setItem("fertilityStatus", fertilityStatus);
        localStorage.setItem("healthScore", healthScore);

        document.getElementById("cycle-length-display").textContent = cycleLength + " ngày";
        document.getElementById("ovulation-date-display").textContent = formatDate(ovulationDate);
        document.getElementById("fertile-period-display").textContent = fertilityStatus;
        document.getElementById("health-score-display").textContent = healthScore + "/100";

        document.getElementById("menstruation-date-display").textContent = formatDate(nextMenstruationDate);
        document.getElementById("fertile-start-display").textContent = formatDate(fertileStart);
        document.getElementById("fertile-end-display").textContent = formatDate(fertileEnd);

        generateCalendar(currentMonth, currentYear);

        alert("Chu kỳ đã được lưu thành công!");
    });

    if (localStorage.getItem("cycleLength")) {
        generateCalendar(currentMonth, currentYear);
    }

    prevMonthButton.addEventListener("click", () => {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        generateCalendar(currentMonth, currentYear);
    });

    nextMonthButton.addEventListener("click", () => {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        generateCalendar(currentMonth, currentYear);
    });

    generateCalendar(currentMonth, currentYear);
});

function formatDate(date) {
    if (!date || date === "null") return "--";
    const d = new Date(date);
    return d.toLocaleDateString("vi-VN", { day: "2-digit", month: "2-digit", year: "numeric" });
}

function generateCalendar(month, year) {
    const calendarContainer = document.querySelector(".calendar");
    calendarContainer.innerHTML = "";

    const firstDay = new Date(year, month, 1).getDay();
    const lastDate = new Date(year, month + 1, 0).getDate();

    document.querySelector(".current-month").textContent = `Tháng ${month + 1}, ${year}`;

    const daysOfWeek = ["CN", "T2", "T3", "T4", "T5", "T6", "T7"];
    daysOfWeek.forEach(day => {
        const dayHeader = document.createElement("div");
        dayHeader.classList.add("calendar-header");
        dayHeader.textContent = day;
        calendarContainer.appendChild(dayHeader);
    });

    for (let i = 0; i < firstDay; i++) {
        const emptyDay = document.createElement("div");
        emptyDay.classList.add("calendar-day", "other-month");
        calendarContainer.appendChild(emptyDay);
    }

    const menstruationDate = localStorage.getItem("nextMenstruationDate") ? new Date(localStorage.getItem("nextMenstruationDate")) : null;
    const fertileStart = localStorage.getItem("fertileStart") ? new Date(localStorage.getItem("fertileStart")) : null;
    const fertileEnd = localStorage.getItem("fertileEnd") ? new Date(localStorage.getItem("fertileEnd")) : null;
    const ovulationDate = localStorage.getItem("ovulationDate") ? new Date(localStorage.getItem("ovulationDate")) : null;
    const periodLength = localStorage.getItem("periodLength") ? parseInt(localStorage.getItem("periodLength")) : 5;

    for (let day = 1; day <= lastDate; day++) {
        const dayElement = document.createElement("div");
        dayElement.classList.add("calendar-day");
        dayElement.textContent = day;

        const fullDate = new Date(year, month, day);

        if (fullDate.toDateString() === new Date().toDateString()) {
            dayElement.classList.add("today");
        }

        if (menstruationDate && fullDate >= menstruationDate && fullDate < new Date(menstruationDate.getTime() + periodLength * 86400000)) {
            dayElement.classList.add("menstruation");
        }

        if (fertileStart && fertileEnd && fullDate >= fertileStart && fullDate <= fertileEnd) {
            dayElement.classList.add("fertile");
        }

        if (ovulationDate && fullDate.toDateString() === ovulationDate.toDateString()) {
            dayElement.classList.add("ovulation");
        }

        calendarContainer.appendChild(dayElement);
    }
}

document.addEventListener("DOMContentLoaded", function() {
    const cycleFormContainer = document.getElementById("cycle-form-container");

    window.toggleCycleForm = function() {
        cycleFormContainer.style.display = cycleFormContainer.style.display === "none" ? "block" : "none";
    };

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Bạn cần đăng nhập để truy cập trang này!");
        window.location.href = "/login";
    }
});