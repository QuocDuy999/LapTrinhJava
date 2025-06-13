document.addEventListener('DOMContentLoaded', () => {
    const cycleForm = document.getElementById("cycle-form");
    const calendarContainer = document.querySelector(".calendar");
    const currentMonthDisplay = document.querySelector(".current-month");
    const prevMonthButton = document.querySelector(".month-nav.prev");
    const nextMonthButton = document.querySelector(".month-nav.next");

    let currentDate = new Date();
    let currentMonth = currentDate.getMonth();
    let currentYear = currentDate.getFullYear();

    cycleForm.addEventListener("submit", function(event) {
        event.preventDefault(); // Ngăn form chuyển trang

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

        // Kiểm tra nếu ngày hợp lệ
        if (isNaN(lastPeriodDate.getTime())) {
            alert("Ngày bắt đầu kỳ kinh không hợp lệ.");
            return;
        }

        // Tính toán ngày rụng trứng
        const ovulationDate = new Date(lastPeriodDate);
        ovulationDate.setDate(lastPeriodDate.getDate() + cycleLength - 14);

        // Tính toán thời kỳ màu mỡ
        const fertileStart = new Date(ovulationDate);
        fertileStart.setDate(ovulationDate.getDate() - 3);

        const fertileEnd = new Date(ovulationDate);
        fertileEnd.setDate(ovulationDate.getDate() + 2);

        // Tính toán ngày hành kinh tiếp theo
        const nextMenstruationDate = new Date(lastPeriodDate);
        nextMenstruationDate.setDate(lastPeriodDate.getDate() + cycleLength);

        // Tính toán khả năng thụ thai
        let fertilityStatus = "Thấp";
        if (cycleLength >= 26 && cycleLength <= 32) {
            fertilityStatus = "Cao";
        } else if (cycleLength > 32) {
            fertilityStatus = "Trung bình";
        }

        // Tính toán điểm sức khỏe
        let healthScore = 80;
        if (cycleLength >= 26 && cycleLength <= 32) {
            healthScore = 90;
        } else if (cycleLength > 32) {
            healthScore = 85;
        } else if (cycleLength < 26) {
            healthScore = 75;
        }

        // Lưu dữ liệu vào localStorage
        localStorage.setItem("cycleLength", cycleLength);
        localStorage.setItem("ovulationDate", formatDate(ovulationDate));
        localStorage.setItem("fertileStart", formatDate(fertileStart));
        localStorage.setItem("fertileEnd", formatDate(fertileEnd));
        localStorage.setItem("nextMenstruationDate", formatDate(nextMenstruationDate));
        localStorage.setItem("fertilityStatus", fertilityStatus);
        localStorage.setItem("healthScore", healthScore);

        // Hiển thị dữ liệu trên Stats Cards
        document.getElementById("cycle-length-display").textContent = cycleLength + " ngày";
        document.getElementById("ovulation-date-display").textContent = formatDate(ovulationDate);
        document.getElementById("fertile-period-display").textContent = fertilityStatus;
        document.getElementById("health-score-display").textContent = healthScore + "/100";

        // Hiển thị dữ liệu trên Predictions Panel
        document.getElementById("menstruation-date-display").textContent = formatDate(nextMenstruationDate);
        document.getElementById("fertile-start-display").textContent = formatDate(fertileStart);
        document.getElementById("fertile-end-display").textContent = formatDate(fertileEnd);

        // Hiển thị dữ liệu trên Lịch chu kỳ
        generateCalendar(currentMonth, currentYear);

        alert("Chu kỳ đã được lưu thành công!");
    });

    // Hiển thị dữ liệu đã lưu khi tải lại trang
    if (localStorage.getItem("cycleLength")) {
        generateCalendar(currentMonth, currentYear);
    }

    // Chuyển đổi tháng
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

    // Hiển thị lịch ban đầu
    generateCalendar(currentMonth, currentYear);
});

// Hàm định dạng ngày-tháng-năm
function formatDate(date) {
    if (!date || date === "null") return "--"; // Nếu dữ liệu null, hiển thị "--"
    
    const d = new Date(date);
    return d.toLocaleDateString("vi-VN", { day: "2-digit", month: "2-digit", year: "numeric" });
}

// Hàm tạo lịch động với màu sắc khác nhau
function generateCalendar(month, year) {
    const calendarContainer = document.querySelector(".calendar");
    calendarContainer.innerHTML = ""; // Xóa lịch cũ

    const firstDay = new Date(year, month, 1).getDay();
    const lastDate = new Date(year, month + 1, 0).getDate();

    document.querySelector(".current-month").textContent = `Tháng ${month + 1}, ${year}`;

    // Thêm tiêu đề ngày
    const daysOfWeek = ["CN", "T2", "T3", "T4", "T5", "T6", "T7"];
    daysOfWeek.forEach(day => {
        const dayHeader = document.createElement("div");
        dayHeader.classList.add("calendar-header");
        dayHeader.textContent = day;
        calendarContainer.appendChild(dayHeader);
    });

    // Thêm ngày trống trước ngày đầu tiên của tháng
    for (let i = 0; i < firstDay; i++) {
        const emptyDay = document.createElement("div");
        emptyDay.classList.add("calendar-day", "other-month");
        calendarContainer.appendChild(emptyDay);
    }

    // Lấy dữ liệu từ localStorage
    const menstruationDate = localStorage.getItem("nextMenstruationDate") ? new Date(localStorage.getItem("nextMenstruationDate")) : null;
    const fertileStart = localStorage.getItem("fertileStart") ? new Date(localStorage.getItem("fertileStart")) : null;
    const fertileEnd = localStorage.getItem("fertileEnd") ? new Date(localStorage.getItem("fertileEnd")) : null;
    const ovulationDate = localStorage.getItem("ovulationDate") ? new Date(localStorage.getItem("ovulationDate")) : null;
    const periodLength = localStorage.getItem("periodLength") ? parseInt(localStorage.getItem("periodLength")) : 5;

    // Thêm ngày của tháng
    for (let day = 1; day <= lastDate; day++) {
        const dayElement = document.createElement("div");
        dayElement.classList.add("calendar-day");
        dayElement.textContent = day;

        const fullDate = new Date(year, month, day);

        // Kiểm tra nếu ngày là hôm nay
        if (fullDate.toDateString() === new Date().toDateString()) {
            dayElement.classList.add("today");
        }

        // Kiểm tra nếu ngày thuộc chu kỳ kinh nguyệt
        if (menstruationDate && fullDate >= menstruationDate && fullDate < new Date(menstruationDate.getTime() + periodLength * 86400000)) {
            dayElement.classList.add("menstruation");
        }

        // Kiểm tra nếu ngày thuộc thời kỳ màu mỡ
        if (fertileStart && fertileEnd && fullDate >= fertileStart && fullDate <= fertileEnd) {
            dayElement.classList.add("fertile");
        }

        // Kiểm tra nếu ngày là ngày rụng trứng
        if (ovulationDate && fullDate.toDateString() === ovulationDate.toDateString()) {
            dayElement.classList.add("ovulation");
        }

        calendarContainer.appendChild(dayElement);
    }
}

// Hiển thị form nhập dữ liệu khi nhấn nút
document.addEventListener("DOMContentLoaded", function() {
    const cycleFormContainer = document.getElementById("cycle-form-container");

    window.toggleCycleForm = function() {
        cycleFormContainer.style.display = cycleFormContainer.style.display === "none" ? "block" : "none";
    };
});

// document.addEventListener("DOMContentLoaded", function() {
//     const token = localStorage.getItem("token");

//     if (!token) {
//         alert("Bạn cần đăng nhập để truy cập trang này!");
//         window.location.href = "/login";
//     }
// });
